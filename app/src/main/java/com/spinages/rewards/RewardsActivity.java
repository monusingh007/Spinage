package com.spinages.rewards;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.LikeView;
import com.spinages.app.AppConstants;
import com.spinages.app.R;
import com.spinages.utilities.PrefManager;
import com.spinages.webservices.URLconnectionService;

import org.json.JSONObject;

public class RewardsActivity extends Activity implements View.OnClickListener {
    TextView redeemCode, referFriends;
    PrefManager prefManager;
    int x = 0;
    LikeView likeView;
    CallbackManager callbackManager;
    private CallbackManager sCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getApplicationContext());
        sCallbackManager = CallbackManager.Factory.create();

        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(this);
        setContentView(R.layout.activity_rewards);
        redeemCode = (TextView) findViewById(R.id.code);
        referFriends = (TextView) findViewById(R.id.refer);
        redeemCode.setOnClickListener(this);
        referFriends.setOnClickListener(this);


        initInstances();
        initCallbackManager();
        refreshButtonsState();

    }

    private void initInstances() {
        likeView = (LikeView) findViewById(R.id.likeView1);
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
                //  new LikeSpinages().execute();
                // refreshButtonsState();
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


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.code) {
         //   startActivity(new Intent(this, RedeemCodeActivity.class));
        }
        if (view.getId() == R.id.refer) {
         //   openDialogInvite(RewardsActivity.this);
        }

    }

    public void openDialogInvite(final Activity activity) {
        String AppURl = "https://fb.me/659724450828700";  //Generated from //fb developers

        //String previewImageUrl = "   ";

        sCallbackManager = CallbackManager.Factory.create();

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(AppURl)
                            //.setPreviewImageUrl(previewImageUrl)
                    .build();

            final AppInviteDialog appInviteDialog = new AppInviteDialog(activity);

            appInviteDialog.registerCallback(sCallbackManager,
                    new FacebookCallback<AppInviteDialog.Result>() {
                        @Override
                        public void onSuccess(AppInviteDialog.Result result) {

                            try {
                                Log.d("monu", "Request id: " + result.getData());
                                Log.d("monu", "Recipients:" + result.getData().getString("request_ids"));
                                Log.v("invitation", result.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
// setting parameters for request execution for both graph api request

                        }

                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onError(FacebookException e) {
                            Log.d("Invitation", "Error Occured");
                        }
                    });

            appInviteDialog.show(content);
        }
    }


    class LikeSpinages extends AsyncTask<String, String, String> {

        String gmail = prefManager.getPreviousMessage(AppConstants.USER_GMAIL);
        String password = prefManager.getPreviousMessage(AppConstants.USER_PASSWORD);
        JSONObject outputJson;
        String code="";
        String url = AppConstants.INSOMNIX_WEBSRVICES_URL+"user/likeSpinagesFB";
        String message="";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RewardsActivity.this, R.style.ProgerssDialogTheme);
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


                if (code.equals("102"))
                    Toast.makeText(getBaseContext(), "spinages is already liked", Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }
}

