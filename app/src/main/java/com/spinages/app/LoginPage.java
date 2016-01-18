package com.spinages.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.spinages.utilities.PrefManager;
import com.spinages.utilities.Util;
import com.spinages.webservices.URLconnectionService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class LoginPage extends Activity implements View.OnClickListener {
    CallbackManager callbackManager;
    Button sighin, sighnup;
    TextView partner;
    String name;
    String birthday;
    String email;
    String id;
    String first_name;
    String last_name;
    String gender;
    Intent intent = new Intent();
    Bundle bundle = new Bundle();
    PrefManager prefManager;
    TextView termConditions;
    ProgressDialog progressDialog;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //fb sdk initialization
        FacebookSdk.sdkInitialize(getApplicationContext());
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.login_page);

        prefManager = new PrefManager(this);
        partner = (TextView) findViewById(R.id.login_for_partner_clickhere_text);
        partner.setOnClickListener(this);
        termConditions = (TextView) findViewById(R.id.login_fb_term_cond_text);
        termConditions.setOnClickListener(this);

        if (isLoggedIn()&& prefManager.getPreviousMessage(AppConstants.USER_IS_LOGGED_IN).equals("true"))
        {

            Intent intent = new Intent(this, Slider.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }

        final LoginButton loginButton = (LoginButton) findViewById(R.id.Login_button);
        loginButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        loginButton.setCompoundDrawablePadding(LoginPage.this.getResources().getDimensionPixelSize(R.dimen._10ROR));


        loginButton.setTypeface(Util.setTextFontOpenSans(this, "OpenSans-Regular.ttf"));
//setting permissions
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends, user_location,user_hometown"));
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {


            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                //displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        if (isConnected() == true) {

            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(final LoginResult loginResult) {
                    progressDialog = new ProgressDialog(LoginPage.this, R.style.ProgerssDialogTheme);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
//after succesful login

                    FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {
//getting prfile info response in jsonobject
                                    String token = null;
                                    try {
                                        token = AccessToken.getCurrentAccessToken().getToken();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                    if (token != null) {
                                        Log.e("faceb response", object.toString());
                                        try {
                                            prefManager.clearKeyMessage(AppConstants.FACCEBOOK_TOKEN_RESPONSE);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        prefManager.createMessageBuffer(AppConstants.FACCEBOOK_TOKEN_RESPONSE, token);
                                        name = object.optString("name").toString();
                                        prefManager.createMessageBuffer(AppConstants.PROFILE_NAME, name);
                                    }
                                    try {
                                        prefManager.clearKeyMessage(AppConstants.FACEBOOK_USER_INFO_RESPONSE);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    prefManager.createMessageBuffer(AppConstants.FACEBOOK_USER_INFO_RESPONSE, object.toString());
                                    new IsUserRegistered().execute();

                                }
                            });
// setting parameters for request execution for both graph api request
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,first_name,last_name,email,gender, birthday, location, hometown");
                    request.setParameters(parameters);
                    request.executeAsync();


                    GraphRequest picGraphRequest = GraphRequest.newGraphPathRequest(loginResult.getAccessToken(), "/me/picture", new GraphRequest.Callback() {

                        public void onCompleted(GraphResponse graphResponse) {

                            try {
                                JSONObject main = graphResponse.getJSONObject();
                                JSONObject object = main.getJSONObject("data");
                                String picUrl = object.optString("url").toString();
                                // Nepathya.picUrl = picUrl;
                                try {
                                    prefManager.clearKeyMessage(AppConstants.PROFILE_PIC_URL);
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                                prefManager.createMessageBuffer(AppConstants.PROFILE_PIC_URL, picUrl);

                            } catch (Exception e)

                            {
                                e.printStackTrace();
                            }

                        }
                    });
                    Bundle picParameters = new Bundle();
                    picParameters.putString("width", "9999");
                    picParameters.putString("redirect", "false");
                    picGraphRequest.setParameters(picParameters);
                    picGraphRequest.executeAsync();

                }

                @Override
                public void onCancel() {
//on fb login cancel
                    Log.v("LoginActivity", "cancel");
                }

                @Override
                public void onError(FacebookException exception) {
//on fb login error
                    Log.v("LoginActivity", exception.getCause().toString());
                    // Toast.makeText(LoginPage.this, "facebook error login again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "check ur internet connection", Toast.LENGTH_LONG).show();
        }


    }


    //after fb login result obtained by callbackmanager in login activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            // interestActivity();
            //  LoginManager.getInstance().logOut();
            // user is now logged out

        }

        // String name = data.getExtras().getString("name");
        //Log.e("name", name);

    }

    //method to check internet connectivity
    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    @Override
    public void onClick(View view) {
        /*
        if(view.getId()==R.id.login_login_text)
            sighnInActivity();
        if(view.getId()==R.id.login_signup_text)
            sighnUpActivity();
            */
        switch (view.getId()) {
            case R.id.login_for_partner_clickhere_text:

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://partners.spinages.com"));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.login_fb_term_cond_text:
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.spinages.com/legality/"));
                    startActivity(browserIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


        }

    }


    private void displayMessage(Profile profile) {
        if (profile != null) {

            Toast.makeText(this, String.valueOf(profile.getName()), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();

        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
    }

    public boolean isLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    void nextActivity() {
        Intent intent = new Intent(LoginPage.this, Slider.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public class IsUserRegistered extends AsyncTask<String, String, String> {
        String url = AppConstants.INSOMNIX_WEBSRVICES_URL + "user/fbLogin";
        String fbresponse;

        JSONObject jsonObject;
        JSONObject outputJson;
       /*[11/17/15, 1:16:22 PM] Harkomal Singh: service name
[11/17/15, 1:16:29 PM] Harkomal Singh: user/fbLogin
[11/17/15, 1:16:33 PM] Harkomal Singh: method post
[11/17/15, 1:16:39 PM] Harkomal Singh: input json
[11/17/15, 1:17:05 PM] Harkomal Singh: {"fbToken":"CAAJVai51sZCMBAJxWQFiQb2ORURuI6s90ZBZAMoBR7JdBpAB2m8f2T3qVPgO8Xq9VJOIY2POWZBxJt69eHfERZATfsxPZBmQ2hlPZC3cC1HI0rxNb3hcIFIZBNtFVQaaS77TCv2gehsYR9qTVXxQTo7bfqxlrXQT3F7DhPZBynsH5FsmdSmC3yJHWBG8QwuwDgIYZD","fbUserId":"15851835750804621"}
[11/17/15, 1:17:11 PM] Harkomal Singh: out put
[11/17/15, 1:17:57 PM] Harkomal Singh: {"code":"102","message":"INVALID_CREDENTIALS"}
{"code":"101","message":"SUCCESS","username":"amitc5455@gmail.com","password":"#gBzh#xn"}
[11/17/15, 1:18:17 PM] Harkomal Singh: no autherization header required*/

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            fbresponse = prefManager.getPreviousMessage(AppConstants.FACEBOOK_USER_INFO_RESPONSE);


            jsonObject = new JSONObject();
            try {
                JSONObject jsonfbObject = new JSONObject(fbresponse);
                id = jsonfbObject.opt("id").toString();
                jsonObject.accumulate("fbUserId", id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                outputJson = URLconnectionService.getWebServiceData(url, jsonObject.toString(), AppConstants.POST_JSON_REQUEST, "", "");


            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            String code = "";
            super.onPostExecute(s);
            try {
                code = outputJson.opt("code").toString();
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(LoginPage.this, "Error", Toast.LENGTH_SHORT).show();

            }
            if (code.equals("101")) {
                String username = outputJson.opt("username").toString();
                String password = outputJson.opt("password").toString();

                    prefManager.clearKeyMessage(AppConstants.USER_GMAIL);
                    prefManager.clearKeyMessage(AppConstants.USER_PASSWORD);

                prefManager.createMessageBuffer(AppConstants.USER_GMAIL, username);
                prefManager.createMessageBuffer(AppConstants.USER_PASSWORD, password);
                //   Toast.makeText(LoginPage.this, outputJson.toString(), Toast.LENGTH_SHORT).show();

                new UserLogin().execute();


            }
            if (code.equals("102")) {
                //Toast.makeText(LoginPage.this, outputJson.toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setClass(LoginPage.this, InterestPage.class);
                startActivity(intent);
                progressDialog.dismiss();
            }

        }
    }

    public class UserLogin extends AsyncTask<String, String, String> {
        JSONObject outputJson;
        String url = AppConstants.INSOMNIX_WEBSRVICES_URL + "user/login";
        String message;
        String gmail;
        String password;
        String standard_text = "null";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            gmail = prefManager.getPreviousMessage(AppConstants.USER_GMAIL);
            password = prefManager.getPreviousMessage(AppConstants.USER_PASSWORD);
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                outputJson = URLconnectionService.getWebServiceData(url, null, AppConstants.GET_REQUEST, gmail, password);

                prefManager.clearKeyMessage(AppConstants.SIGHNIN_JSON);

                prefManager.createMessageBuffer(AppConstants.SIGHNIN_JSON, outputJson.toString());
                message = outputJson.opt("message").toString();
                standard_text = outputJson.opt("standardText").toString();
                if (message.equals("SUCCESS")) {

                    prefManager.clearKeyMessage(AppConstants.USER_GMAIL);
                    prefManager.clearKeyMessage(AppConstants.USER_PASSWORD);

                    prefManager.createMessageBuffer(AppConstants.USER_GMAIL, gmail);
                    prefManager.createMessageBuffer(AppConstants.USER_PASSWORD, password);
                }
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            try {
                if (message.equals("SUCCESS")) {
                    //Toast.makeText(getBaseContext(), "Login successfull", Toast.LENGTH_LONG).show();
                    prefManager.createMessageBuffer(AppConstants.USER_IS_LOGGED_IN,"true");
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //add your code here
                            nextActivity();
                        }
                    }

                            , 10);
                } else {
                    //   Toast.makeText(getBaseContext(), "login error", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}

