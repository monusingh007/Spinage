package com.spinages.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.widget.LikeView;
import com.spinages.utilities.PrefManager;
import com.spinages.utilities.Util;
import com.spinages.vWallet.VwalletMainActivity;
import com.spinages.webservices.URLconnectionService;

import org.json.JSONObject;

/**
 * Created by monu on 7/19/2015.
 */
public class ReferalActivity extends Activity {
    LinearLayout btnLoginToLike;
    LikeView likeView;
    EditText referalCode;
    CallbackManager callbackManager;
    Button skip;
    PrefManager prefManager;
    ProgressDialog progressDialog;
    String gmail;
    String password;
TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.referalcode_page);
        prefManager = new PrefManager(this);
        title= (TextView)findViewById(R.id.title);
        title.setTypeface(Util.setTextFontOpenSans(this, "OpenSans-Light.ttf"));
        gmail = prefManager.getPreviousMessage(AppConstants.USER_GMAIL);
        password = prefManager.getPreviousMessage(AppConstants.USER_PASSWORD);
        skip = (Button) findViewById(R.id.skip_btn);
        skip.setTypeface(Util.setTextFontOpenSans(this, "OpenSans-Regular.ttf"));
        referalCode = (EditText) findViewById(R.id.refrl_code_edttxt);
        progressDialog = new ProgressDialog(this, R.style.ProgerssDialogTheme);
//edittext of referal code textchange listener method

        referalCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//before text change code
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//on text change of edittext method called to change button text
                checkCode();

            }

            @Override
            public void afterTextChanged(Editable editable) {
//after text change code
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = referalCode.getText().toString().trim();
                if (code.length() == 0)
                    new UserLogin().execute();
                else
                    new SendReferalCode().execute();
            }
        });

//methods called for facebook like button implementation
        initInstances();
        initCallbackManager();
        refreshButtonsState();

    }

    private void initInstances() {
        likeView = (LikeView) findViewById(R.id.likeView);
//setting button style
        likeView.setLikeViewStyle(LikeView.Style.STANDARD);
        likeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);
//setting the url of page to like on click
        likeView.setObjectIdAndType(
                "https://www.facebook.com/spinages?_rdr=p",
                LikeView.ObjectType.PAGE);
    }


    private void initCallbackManager() {
//method called after click on like button
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//funtion called to change like button state
                refreshButtonsState();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });
    }

    private void refreshButtonsState() {
//funtion to change visibilty if like button
        if (!isLoggedIn()) {
//            btnLoginToLike.setVisibility(View.VISIBLE);
            likeView.setVisibility(View.VISIBLE);
        } else {
            //  btnLoginToLike.setVisibility(View.GONE);
            likeView.setVisibility(View.VISIBLE);
        }
    }

    //method to check whether user is loggen in or not
    public boolean isLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//Handle Facebook Login Result
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK)
            new LikeSpinages().execute();

    }


    public void nextActivity() {
//method to start slider activity on click on next button

        Intent intent = new Intent(this, VwalletMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    void checkCode() {
//method to check the refral code text
        String code = referalCode.getText().toString().trim();
        if (code.length() == 0) {
            skip.setText("Skip");
        } else {
            skip.setText("Next");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    class SendReferalCode extends AsyncTask<String, String, String> {
        String ref_code = referalCode.getText().toString().trim();
        JSONObject outputJson;
        String code="";
        String url = AppConstants.INSOMNIX_WEBSRVICES_URL+"user/registerReferralCode/" + ref_code;
        String message;

        @Override
        protected void onPreExecute() {

            progressDialog.setMessage("Loading...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                outputJson = URLconnectionService.getWebServiceData(url, null, AppConstants.GET_REQUEST, gmail, password);
                message = outputJson.opt("message").toString();
                code = outputJson.opt("code").toString();


            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            if (code.equals("101")) {
              //  Toast.makeText(getBaseContext(), "success", Toast.LENGTH_LONG).show();
                new UserLogin().execute();

            }

            if (code.equals("102")) {
                referalCode.setText("");
                Toast.makeText(getBaseContext(), "referal code not valid, try again or skip", Toast.LENGTH_LONG).show();
            }
          /*final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override

                public void run() {
                    //add your code here

                }
            }, 1500);*/

        }
    }

    class LikeSpinages extends AsyncTask<String, String, String> {

        String referalcode = referalCode.getText().toString().trim();
        JSONObject outputJson;
        String code="";
        String url = AppConstants.INSOMNIX_WEBSRVICES_URL+"user/likeSpinagesFB";
        String message="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                outputJson = URLconnectionService.getWebServiceData(url, null, AppConstants.GET_REQUEST, gmail, password);
                // prefManager.createMessageBuffer(AppConstants.SIGHNIN_JSON,outputJson.toString());
                message = outputJson.opt("message").toString();
                code = outputJson.opt("code").toString();


            } catch (Exception e) {
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();
/* {"code":102,"errors":null,"message":"SPINAGES_FB_ALREADY_LIKED"}*/
            try {
                if (code.equals("101"))
                    Toast.makeText(getBaseContext(), "success, you have won two spindo and a voucher", Toast.LENGTH_LONG).show();


                if (code.equals("102")) {
                    //  Toast.makeText(getBaseContext(), "spinages is already liked", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    public class UserLogin extends AsyncTask<String, String, String> {
        JSONObject outputJson;
        String url = AppConstants.INSOMNIX_WEBSRVICES_URL+"user/login";
        String message;
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ReferalActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String gmail = prefManager.getPreviousMessage(AppConstants.USER_GMAIL);
            String password = prefManager.getPreviousMessage(AppConstants.USER_PASSWORD);
            try {

                outputJson = URLconnectionService.getWebServiceData(url, null, AppConstants.GET_REQUEST, gmail, password);
                try {
                    prefManager.clearKeyMessage(AppConstants.SIGHNIN_JSON);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                prefManager.createMessageBuffer(AppConstants.SIGHNIN_JSON, outputJson.toString());

            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            nextActivity();
        }
    }
    @Override
    public void onBackPressed() {
        return;
    }
}
