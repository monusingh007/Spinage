package com.spinages.vWallet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.spinages.app.AppConstants;
import com.spinages.app.MyApplication;
import com.spinages.app.R;
import com.spinages.app.Slider;
import com.spinages.utilities.PrefManager;
import com.spinages.webservices.URLconnectionService;

import org.json.JSONObject;

public class CongratulationActivity extends Activity implements View.OnClickListener {
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    LinearLayout btnLoginToLike;
    LikeView likeView, likeView2;
    Button shareButton;
    PrefManager prefManager;
    String json;
    TextView description, expiryDate, location;
    ImageView image;
    ShareLinkContent content;
    RelativeLayout wonVoucher, background,layout2;
    String id;
    String partnerLikePage;
    ScaleAnimation scaleAnimation;
    ImageView logo;
    Animation animation_up,animation_down;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_congratulation);

        prefManager = new PrefManager(this);
        wonVoucher = (RelativeLayout) findViewById(R.id.won_voucher);
        background = (RelativeLayout) findViewById(R.id.backgound);
        layout2 = (RelativeLayout) findViewById(R.id.layout2);
        background.setOnClickListener(this);

        shareButton = (Button) findViewById(R.id.fb_share_button);
        logo = (ImageView) findViewById(R.id.logo);
        logo.setOnClickListener(this);


        shareButton.setOnClickListener(this);
        json = getIntent().getStringExtra(AppConstants.VOUCHER_WON_ONCLAIM);
        /*
        * {"code":101,"errors":null,"message":"SUCCESS","spindoWin":2,
        * "standardText":"GETS YOU","totalSpindo":101,
        * "voucherWin":{"companyLogo":"\/spinages\/images\/McDonalds-logo-415.png","companyName":
        * "Godary Company Pvt Ltd","description":"Free Delivery On Every Set Meal Purchased",
        * "expiryDate":"31-12-2015","id":10000007,"locationAvailability":"Loc 1 $ Loc 5 $ Loc 7",
        * "partnerLikeAvailable":"Y","partnerLikePage":"http:\/\/facebook.com\/partner3",
        * "termsAndConditions":"http:\/\/partner3.com\/vouchertnc1.html"}}
        *
        * */

        // creating new HashMap


        // adding each child node to HashMap key => value

        content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://fb.me/659724450828700"))
                .build();
//fb like and share button implementation methods
//shareOnFacebook();
        getAndSetjson();
        initInstances();
        initCallbackManager();
        refreshButtonsState();


    }

    void scaleUp() {
        animation_up = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        background.setVisibility(View.VISIBLE);

        wonVoucher.setVisibility(View.VISIBLE);
        wonVoucher.startAnimation(animation_up);
    }
    void scaleDown(){
        animation_down = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        wonVoucher.startAnimation(animation_down);


    }

    private void initInstances() {
        likeView = (LikeView) findViewById(R.id.likeView1);
//setting button style
        likeView.setLikeViewStyle(LikeView.Style.STANDARD);
        likeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);
//setting the url of page to like on click
        likeView.setObjectIdAndType(
                partnerLikePage,
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
            // likeView.setVisibility(View.VISIBLE);
        } else {
            //  btnLoginToLike.setVisibility(View.GONE);
            // likeView.setVisibility(View.VISIBLE);
        }
    }


    //method to check whether user is loggen in or not
    public boolean isLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.fb_share_button:
                share();
                break;

            case R.id.logo:
                scaleUp();
               // Log.e("moniu","logo is runing");
               final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scaleDown();
                    }
                }, 1750);
                final Handler handler1= new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        wonVoucher.setVisibility(View.INVISIBLE);
                        background.setVisibility(View.INVISIBLE);
                    }
                }, 2500);



                break;
            case R.id.backgound:

                break;
            case R.id.won_voucher:

                break;
        }


    }
    public Bitmap screenShot(View view) {
        view.setBackgroundColor(Color.WHITE);
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);


        return bitmap;


    }

//method to share link on fb

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
// new LikeSpinagesPartner().execute();
        }
    }

    protected void share() {

        ShareDialog shareDialog = new ShareDialog(this);
        if (ShareDialog.canShow(SharePhotoContent.class)) {
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    //  Toast.makeText(Slider.this, "Share Success" + result.toString(), Toast.LENGTH_SHORT).show();
                    Log.d("DEBUG", "SHARE SUCCESS");
                    new ShareClaimVoucherOnFacebookService().execute();
                }

                @Override
                public void onCancel() {
                    // Toast.makeText(Slider.this, "Share Cancelled", Toast.LENGTH_SHORT).show();
                    Log.d("DEBUG", "SHARE CACELLED");
                }

                @Override
                public void onError(FacebookException exception) {
                    //  Toast.makeText(Slider.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("DEBUG", "Share: " + exception.getMessage());
                    exception.printStackTrace();
                }
            });

//            SharePhoto photo = new SharePhoto.Builder()
//                    .setBitmap(screenShot(layout2))
//                    .setCaption("I have won voucher on spinages app")
//                    .build();
//            SharePhotoContent content = new SharePhotoContent.Builder()
//                    .addPhoto(photo)
//                    .build();
            content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://www.spinages.com"))
                    .build();

            shareDialog.show(content);
        }

    }

    void getAndSetjson() {


        image = (ImageView) findViewById(R.id.image);
        description = (TextView) findViewById(R.id.description);
        expiryDate = (TextView) findViewById(R.id.date);
        location = (TextView) findViewById(R.id.offer_region);

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject c = jsonObject.getJSONObject("voucherWin");
            String idString = c.getString("id");
            id=idString;
            String companyName = c.getString("companyName");
            String companyLogo = c.getString("companyLogo");

            String descriptionString = c.getString("description");


            String locationAvailability = c.getString("locationAvailability");
            String expiryDatestring = c.getString("expiryDate");

            String partnerLikeAvailable = c.getString("partnerLikeAvailable");

            partnerLikePage = c.getString("partnerLikePage");

            String termsConditions = c.getString("termsAndConditions");

            ImageLoader.getInstance().displayImage(AppConstants.INSOMNIX_RESOURCES_URL + companyLogo
                    , image, MyApplication.options);
            description.setText(descriptionString);
            expiryDate.setText(expiryDatestring);
            location.setText(locationAvailability);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class ShareClaimVoucherOnFacebookService extends AsyncTask<String, String, String> {
        String gmail = prefManager.getPreviousMessage(AppConstants.USER_GMAIL);
        String password = prefManager.getPreviousMessage(AppConstants.USER_PASSWORD);
        JSONObject outputJson;
        String code="";
        String url = AppConstants.INSOMNIX_WEBSRVICES_URL+"user/shareClaimedVoucherOnFb/" + id;
        String message;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CongratulationActivity.this, R.style.ProgerssDialogTheme);
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
                if (code.equals("101")) {
                    // String spindowin =     outputJson.opt("spindoWin").toString();
                  //  Toast.makeText(getBaseContext(), "You won spindo for sharing on sharing on facebook", Toast.LENGTH_LONG).show();
                }
/*{"code":101,"errors":null,"message":"SUCCESS","spindoWin":3,"standardText":"GETS YOU","totalSpindo":256,
"voucherWin":{"companyLogo":"/spinages/images/nike-logo.jpg","companyName":"Demo Ptv Ltd","description":
"Upto 70% Off on Cloths","expiryDate":"31-12-2015","id":10000006,"locationAvailability":"Loc 2 $ Loc 9 $ Loc 1",
"partnerLikeAvailable":"Y","partnerLikePage":"http://facebook.com/partner2","termsAndConditions":
"http://partner2.com/vouchertnc3.html"}}*/
                if (code.equals("102")){
                  //  Toast.makeText(getBaseContext(), "unable to share voucher", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class LikeSpinagesPartner extends AsyncTask<String, String, String> {

        String referalcode;
        ProgressDialog progressDialog;
        JSONObject outputJson;
        String code;
        String partnerid ="22";
        String gmail = prefManager.getPreviousMessage(AppConstants.USER_GMAIL);
        String password = prefManager.getPreviousMessage(AppConstants.USER_PASSWORD);
        String url = AppConstants.INSOMNIX_WEBSRVICES_URL+"user/likePartnerFB/"+partnerid;
        String message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CongratulationActivity.this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(this,VwalletMainActivity.class);
        startActivity(intent);
    }
}

