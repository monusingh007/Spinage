package com.spinages.app;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.spinages.SettingsActivity;
import com.spinages.animation.AnimatorPath;
import com.spinages.animation.PathEvaluator;
import com.spinages.animation.PathPoint;
import com.spinages.rewards.RewardsActivity;
import com.spinages.shakeMotion.AccelerometerListener;
import com.spinages.shakeMotion.AccelerometerManager;
import com.spinages.slider.FragmentDrawer;
import com.spinages.utilities.CustomDialogOk;
import com.spinages.utilities.PrefManager;
import com.spinages.utilities.Util;
import com.spinages.vWallet.VwalletMainActivity;
import com.spinages.webservices.URLconnectionService;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class Slider extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener, AccelerometerListener, View.OnClickListener {
DrawerLayout drawerLayout ;
    ImageView FloatingImage;
    ImageView animatedImage, shake_it;
    PathEvaluator mEvaluator = new PathEvaluator();
    ObjectAnimator anim;
    FloatingActionButton fab;
    ImageView img1, img2, img3, img4, img5;
    Animation anim_but_ok, anim_share, anim_layout1, anim_layout2, animation1, animation2, animation3, animation4, animation5, animlinear, animlinear0;
    RelativeLayout layou1, background, layout2;
    Button shareButton;
    Button okay_button;
    String id;
    ImageView slider_profile_image;
    ProgressDialog progressDialog;
    LinearLayout linear, linear0;
    LikeView likeView;
    CallbackManager callbackManager;
    Bitmap voucherBitmap;
    String standardText;
    String totalSpindo;

    //getUserVoucher variables
    JSONObject voucherWin;
    String companyLogo;
    // voucher details
    String description;
    String expiryDate;
    String locationAvailability;
    String partnerLikePage;
    String termsAndConditions;
    TextView profile_name;
    TextView spindoCount;
    TextView voucherDescription;
    TextView voucherExpiryDate, dateText, textView5;
    TextView voucherLocationAvailaibility;
    RelativeLayout voucherLayout;
    ImageView voucherImageView;
    AccelerometerListener accelerometerListener;
    PrefManager prefManager;
    ShareDialog shareDialog;
    String alert="null";
    ShareLinkContent content;
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
FrameLayout frameLayout ;
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AccelerometerManager.stopListening();
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_slider);
        callbackManager = CallbackManager.Factory.create();
        shareButton = (Button) findViewById(R.id.fb_share_button);
        drawerLayout =(DrawerLayout)findViewById(R.id.drawer_layout);
        frameLayout = (FrameLayout)findViewById(R.id.frame_layout);


        classObjectsInitialization();
        getAndsetSighinJson();
        AnimInitialization();
        totalSpindo=prefManager.getPreviousMessage(AppConstants.USER_SPINDO_COUNT);
        if("null"!=totalSpindo)
        {
            spindoCount.setText(totalSpindo+" spindoes");
        }else {totalSpindo="0";
            spindoCount.setText(totalSpindo+" spindoes");}
        userPofileData();
        if (!alert.equals("null")) {
            new CustomDialogOkVoucherserrvice(Slider.this, alert, "black").show();
            alert= "123@#!23";
            AccelerometerManager.stopListening();
            shake_it.setEnabled(false);
        }




    }

    void userPofileData() {
        try {
            String url = prefManager.getPreviousMessage(AppConstants.PROFILE_PIC_URL);
            String name = prefManager.getPreviousMessage(AppConstants.PROFILE_NAME);
            DisplayImageOptions userimgoptions = new DisplayImageOptions.Builder()
                    .displayer(new RoundedBitmapDisplayer(1000))
                    .showImageOnLoading(R.drawable.image_)
                    .showImageForEmptyUri(R.drawable.image_)
                    .showImageOnFail(R.drawable.image_)
                    .cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
            ImageLoader.getInstance().displayImage(url, slider_profile_image, userimgoptions);
            profile_name.setText(name);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

     /*   if(id == R.id.action_search){
            Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                // startActivity(new Intent(this, History.class));
                //
                // startAnimation();
                break;
            case 1:
                startActivity(new Intent(this, VwalletMainActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, RewardsActivity.class));
                break;
            case 3:
                startActivity(new Intent(this, SettingsActivity.class));
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

//set the toolbar title


        }
    }


//following method using accelerometerManger class and accelemeterListener Interface

    // alert popup method
    public void openAlertDialog(String message) {
      /* AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setNeutralButton("!Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

//to create alertdialog
        AlertDialog alertDialog = alertDialogBuilder.create();
//to show dialog
        alertDialog.show();*/
        CustomDialogOkVoucherserrvice customDialog = new CustomDialogOkVoucherserrvice(this, message, "black");
        customDialog.show();

    }

    public void onAccelerationChanged(float x, float y, float z) {
        // TODO Auto-generated method stub

    }

    public void onShake(float force) {
        if (AccelerometerManager.isListening()) {
            shake_it.setEnabled(false);
            startPendulamAmnimation();

//Start Accelerometer Listening
            // Toast.makeText(getBaseContext(), "voucher service got hit",
            //   Toast.LENGTH_LONG).show();
            AccelerometerManager.stopListening();


        }


        //   Toast.makeText(getBaseContext(), "Motion detected  service got a hit",
        //         Toast.LENGTH_LONG).show();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (AccelerometerManager.isSupported(this)&& alert.equals("123@#!23") ){
            alert="null";
            AccelerometerManager.stopListening();
            shake_it.setEnabled(true);
        }else if( AccelerometerManager.isSupported(this))
        {
            AccelerometerManager.startListening(this);
            shake_it.setEnabled(true);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

//Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isListening()) {

//Start Accelerometer Listening
            AccelerometerManager.stopListening();

            // Toast.makeText(getBaseContext(), "onStop Accelerometer Stoped",
            //       Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Sensor", "Service  distroy");

//Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isListening()) {

//Start Accelerometer Listening
            AccelerometerManager.stopListening();

            // Toast.makeText(getBaseContext(), "onDestroy Accelerometer Stoped",
            //       Toast.LENGTH_LONG).show();
        }

    }

    public void setButtonLoc(PathPoint newLoc) {
        img3.setTranslationX(newLoc.mX);
        img3.setTranslationY(newLoc.mY);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                //startActivity(new Intent(this, RewardsActivity.class));
                break;

            case R.id.fb_share_button:
                share();

                break;

            case R.id.ok:
                okay_button.setEnabled(false);
                animAfterOk();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        invalidateVoucherData();
                        mToolbar.setBackgroundColor(Color.TRANSPARENT);
                        frameLayout.setVisibility(View.VISIBLE);
                        drawerLayout.setBackgroundResource(R.drawable.bg_);
                        setlayoutInVisible();
                        shake_it.setEnabled(true);
                        AccelerometerManager.startListening(Slider.this);
                        okay_button.setEnabled(true);
                    }
                }, 2000);


                break;
            case R.id.shakeit:
                startPendulamAmnimation();
                AccelerometerManager.stopListening();
                shake_it.setEnabled(false);
        }

    }

    void animAfterOk() {
        anim_but_ok = AnimationUtils.loadAnimation(this, R.anim.moveup_fast);
        anim_layout1 = AnimationUtils.loadAnimation(this, R.anim.move);
        anim_layout2 = AnimationUtils.loadAnimation(this, R.anim.move_up);
        layout2.startAnimation(anim_layout2);
        okay_button.startAnimation(anim_but_ok);
        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                //add your code here
                okay_button.setVisibility(View.INVISIBLE);

            }
        }, 600);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                layou1.startAnimation(anim_layout1);
            }
        }, 1000);

    }

    void invalidateVoucherData() {
        try {
            voucherDescription.setText("");
            voucherExpiryDate.setText("");
            voucherLocationAvailaibility.setText("");
            voucherImageView.setImageBitmap(null);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void setlayoutInVisible() {

        img3.setImageResource(R.drawable.spindo_hanging);
        background.setVisibility(View.INVISIBLE);
        layou1.setVisibility(View.INVISIBLE);
        linear0.setVisibility(View.INVISIBLE);
        okay_button.setVisibility(View.INVISIBLE);
        textView5.setVisibility(View.INVISIBLE);
        dateText.setText("");
        dateText.setVisibility(View.INVISIBLE);
        layout2.setVisibility(View.INVISIBLE);


    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

        }
    }

    public void setJsonData() {
        try {
            voucherDescription.setText(description);
            voucherExpiryDate.setText(expiryDate);
            dateText.setText("Exp. Date");
            textView5.setVisibility(View.VISIBLE);
            dateText.setVisibility(View.VISIBLE);
            voucherLocationAvailaibility.setText(locationAvailability);
            voucherImageView.setImageBitmap(voucherBitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    void classObjectsInitialization() {
        callbackManager = CallbackManager.Factory.create();
        progressDialog = new ProgressDialog(this, R.style.ProgerssDialogTheme);
        profile_name = (TextView) findViewById(R.id.profile_name);
        slider_profile_image = (ImageView) findViewById(R.id.profile_image);
        prefManager = new PrefManager(this);
        textView5 = (TextView) findViewById(R.id.textView5);
        spindoCount = (TextView) findViewById(R.id.info_text);
        voucherDescription = (TextView) findViewById(R.id.description);
        voucherExpiryDate = (TextView) findViewById(R.id.date);
        dateText = (TextView) findViewById(R.id.expirydate);
        voucherLocationAvailaibility = (TextView) findViewById(R.id.offer_region);
        voucherLayout = (RelativeLayout) findViewById(R.id.layout);

        background = (RelativeLayout) findViewById(R.id.layout0);
        voucherImageView = (ImageView) findViewById(R.id.image);
        shake_it = (ImageView) findViewById(R.id.shakeit);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        FloatingImage = (ImageView) findViewById(R.id.spindo);
        animatedImage = (ImageView) findViewById(R.id.animspindo);
        linear0 = (LinearLayout) findViewById(R.id.linear0);
        linear = (LinearLayout) findViewById(R.id.linear);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);
        img5 = (ImageView) findViewById(R.id.img5);
        // mToolbar

        //animated voucher layout
        layou1 = (RelativeLayout) findViewById(R.id.layout1);
        layout2 = (RelativeLayout) findViewById(R.id.layout2);
        shake_it.setOnClickListener(this);


        shareButton.setOnClickListener(this);
        okay_button = (Button) findViewById(R.id.ok);
        okay_button.setOnClickListener(this);
        afterShakeAnimLoading();
        // set ui of the linear layout


        // pendulam initialization

//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.spindofruit);
        setSupportActionBar(mToolbar);
        //
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);


    }

    void afterShakeAnimLoading() {
        anim_but_ok = AnimationUtils.loadAnimation(this, R.anim.sliding_down2);
        anim_layout1 = AnimationUtils.loadAnimation(this, R.anim.swing_up_right);
        anim_layout2 = AnimationUtils.loadAnimation(this, R.anim.slide_down);
    }

    public void startAnimation() {
        //mToolbar.setBackgroundResource(R.drawable.aftershake_bg);
        img3.setImageResource(R.drawable.spinage_hanging);
        img3.setVisibility(View.VISIBLE);
        anim.start();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                img3.setVisibility(View.INVISIBLE);
            }
        }, 1000);
        afterShakeAnimLoading();
        stopPendulamAnimation();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //add your code here
                        setlayoutVisible();
                    }
                }, 1000);


            }
        });
    }

    public void setlayoutVisible() {
        drawerLayout.setBackgroundResource(R.drawable.aftershake_bg);
        //mToolbar.setBackgroundColor(Color.parseColor("#99000000"));
        frameLayout.setVisibility(View.INVISIBLE);
        layou1.startAnimation(anim_layout1);
        background.setVisibility(View.VISIBLE);
        layou1.setVisibility(View.VISIBLE);
        linear0.setVisibility(View.VISIBLE);
        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                //add your code here
                layout2.setVisibility(View.VISIBLE);
                layout2.startAnimation(anim_layout2);
            }
        }, 1000);
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                //add your code here
                okay_button.setVisibility(View.VISIBLE);
                okay_button.startAnimation(anim_but_ok);
            }
        }, 2000);


        // shareButton.startAnimation(anim_share);
        //linear0.setVisibility(View.VISIBLE);
        // linear0.startAnimation(animlinear);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //add your code heresetlayoutVisible();setJsonData();
                setJsonData();
            }
        }, 700);


    }

    public void AnimInitialization() {
        //display the first navigation drawer view on app launch
// displayView(0);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y;
        Log.v("width", String.valueOf(width) + "/n " + "height" + String.valueOf(height));
        AnimatorPath path = new AnimatorPath();

        path.moveTo(0, height / 6);

        path.curveTo(width / 2, height / 4, 0, height / 3, -width / 4, height / 2);
        float x2 = convertPixelsToDp((width), this);
        float y2 = convertPixelsToDp((height), this);
        path.lineTo(0, height);


        // Set up the animation


        anim = ObjectAnimator.ofObject(this, "buttonLoc",
                new PathEvaluator(), path.getPoints().toArray());
        anim.setDuration(3000);


    }

    void getAndsetSighinJson() {

        PrefManager prefManager = new PrefManager(this);
        String json = prefManager.getPreviousMessage(AppConstants.SIGHNIN_JSON);
        prefManager.clearKeyMessage(AppConstants.SIGHNIN_JSON);
    /* {"code":101,"errors":null,"message":"SUCCESS",
    "spindoWin":null,"standardText":null,"totalSpindo":1,"voucherWin":null}*/
        try {
            JSONObject jsonObject = new JSONObject(json);
            totalSpindo = jsonObject.opt("totalSpindo").toString();
            prefManager.createMessageBuffer(AppConstants.USER_SPINDO_COUNT,totalSpindo);
            spindoCount.setText(totalSpindo + " Spindo");
            alert =jsonObject.opt("standardText").toString();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startPendulamAmnimation() {


        animation1 = AnimationUtils.loadAnimation(this, R.anim.swinging);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.swinging2);
        animation3 = AnimationUtils.loadAnimation(this, R.anim.swinging2);
        animation4 = AnimationUtils.loadAnimation(this, R.anim.swinging);
        animation5 = AnimationUtils.loadAnimation(this, R.anim.swinging);
        img1.startAnimation(animation1);
        img2.startAnimation(animation2);
        img3.startAnimation(animation3);
        img4.startAnimation(animation4);
        img5.startAnimation(animation5);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //add your code here
                new GetUserVoucher().execute();
            }
        }, 1000);

    }

    void stopPendulamAnimation() {
        img1.clearAnimation();
        img2.clearAnimation();
        img3.clearAnimation();
        img4.clearAnimation();
        img5.clearAnimation();



    }

    private void initInstances() {
        likeView = (LikeView) findViewById(R.id.likeView);
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

    public Bitmap screenShot(View view) {
        view.setBackgroundColor(Color.WHITE);
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);


        return bitmap;


    }

    protected void share() {

        ShareDialog shareDialog = new ShareDialog(this);
        if (ShareDialog.canShow(SharePhotoContent.class)) {
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    //  Toast.makeText(Slider.this, "Share Success" + result.toString(), Toast.LENGTH_SHORT).show();
                    Log.d("DEBUG", "SHARE SUCCESS");
                    // new ShareWonVoucherOnFacebookService().execute();
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
            Uri uri = Uri.parse("http://www.google.com");
            ShareLinkContent content1 = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://developers.facebook.com"))
                    .build();
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(screenShot(voucherLayout))
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();

            shareDialog.show(content);
        }

    }

    public class GetUserVoucher extends AsyncTask<String, String, String> {
        JSONObject outputJson;
        String url = AppConstants.INSOMNIX_WEBSRVICES_URL + "user/shakeSpinagesTree";
        String code="";
        String errors="";
        String message="";


        @Override
        protected void onPreExecute() {

            //  progressDialog.setMessage("spindoes are loading");
            progressDialog.setCancelable(false);
            progressDialog.show();
//Called when Motion Detected
            // hit the service
        /*7. Service Name :     shakeSpinagesTree
        Athentication: USER_ROLE
        Relative URL:  user/shakeSpinagesTree
        HTTP Method :  GET
        Input Params:  None
        Example Response:


        {"code":101,"errors":null,"message":"SUCCESS","spindoWin":null,
        "standardText":"GETS YOU One Free","totalSpindo":24,"voucherWin":
        {"id":10000008",companyLogo":"http://test.com/partner1.png","description":
        "Diet Coke Free with a large chicken burger","expiryDate":"22-10-2015","locationAvailability":
        "Loc 3 $ Loc 4 $ Loc 5","partnerLikeAvailable":"Y","partnerLikePage":"http://facebook.com/partner1",
        "termsAndConditions":"http://partner1.com/vouchertnc2.html"}}
        or
         {"code":102,"errors":null,"message":"DAY_SHAKE_LIMIT_REACHED"}
  {"code":103,"errors":null,"message":"SORRY_TRY_AGAIN"}
        */
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String gmail = prefManager.getPreviousMessage(AppConstants.USER_GMAIL);
                String password = prefManager.getPreviousMessage(AppConstants.USER_PASSWORD);
                try {
                    outputJson = URLconnectionService.getWebServiceData(url, null, AppConstants.GET_REQUEST, gmail, password);
                    code = outputJson.opt("code").toString();
                    errors = outputJson.opt("errors").toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (code.equals("101") ) {
                    standardText = outputJson.opt("standardText").toString();
                    totalSpindo = outputJson.opt("totalSpindo").toString();
                    prefManager.createMessageBuffer(AppConstants.USER_SPINDO_COUNT,totalSpindo);

                    // voucher details

                    JSONObject voucherWin = outputJson.optJSONObject("voucherWin");

                    companyLogo = voucherWin.opt("companyLogo").toString();
                    description = voucherWin.opt("description").toString();
                    expiryDate = voucherWin.opt("expiryDate").toString();
                    locationAvailability = voucherWin.opt("locationAvailability").toString();
                    partnerLikePage = voucherWin.opt("partnerLikePage").toString();

                    termsAndConditions = voucherWin.opt("termsAndConditions").toString();
                    id = voucherWin.opt("id").toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initInstances();
                            initCallbackManager();
                            refreshButtonsState();
                            spindoCount.setText(totalSpindo + "spindo");
//ImageLoader.getInstance().displayImage("http://staging.spinages.com/resources"+companyLogo,voucherImageView, MyApplication.options);
                            new GetBitmapFromURL().execute(AppConstants.INSOMNIX_RESOURCES_URL + companyLogo);
                        }
                    });

                }
               else if (code.equals("102")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            message = outputJson.opt("message").toString();
                            progressDialog.dismiss();
                           // Toast.makeText(Slider.this,"Toaday's shake limit is reached.",Toast.LENGTH_SHORT).show();

                            stopPendulamAnimation();
                            shake_it.setEnabled(true);

                            startActivity(new Intent(Slider.this, RewardsActivity.class));

                        }
                    });

                }
             else   if (code.equals("103")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            message = outputJson.opt("message").toString();
                            progressDialog.dismiss();
                            openAlertDialog("Sorry, please try again.");
                            stopPendulamAnimation();



                        }
                    });
                }else
                {  runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        stopPendulamAnimation();
                        shake_it.setEnabled(true);
                        AccelerometerManager.startListening(Slider.this);
                        Toast.makeText(Slider.this,"error",Toast.LENGTH_SHORT).show();
                    }
                });

                }

            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        stopPendulamAnimation();
                        shake_it.setEnabled(true);
                        AccelerometerManager.startListening(Slider.this);
                      //  Toast.makeText(Slider.this,"error",Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    public class GetBitmapFromURL extends AsyncTask<String, String, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {

            try {


                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;


                //return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            voucherBitmap = result;

            progressDialog.dismiss();
            startAnimation();
            stopPendulamAnimation();

        }
    }

    class ShareWonVoucherOnFacebookService extends AsyncTask<String, String, String> {
        String gmail = prefManager.getPreviousMessage(AppConstants.USER_GMAIL);
        String password = prefManager.getPreviousMessage(AppConstants.USER_PASSWORD);
        JSONObject outputJson;
        String code;
        String url = AppConstants.INSOMNIX_WEBSRVICES_URL + "user/shareWonVoucherOnFb/" + id;
        String message;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Slider.this, R.style.ProgerssDialogTheme);
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
                    Toast.makeText(getBaseContext(), "success", Toast.LENGTH_LONG).show();

                if (code.equals("102")) {
                    // Toast.makeText(getBaseContext(), "unable to share voucher", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //    void shareOnfacbookwithPublicshAction(final String text){
//        List<String> permissionNeeds = Arrays.asList("publish_actions");
//
//        //this loginManager helps you eliminate adding a LoginButton to your UI
//        LoginManager manager = LoginManager.getInstance();
//
//        manager.logInWithPublishPermissions(this, permissionNeeds);
//
//        manager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                sharePhotoToFacebook(text);
//            }
//
//            @Override
//            public void onCancel() {
//                System.out.println("onCancel");
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                System.out.println("onError");
//            }
//        });
//    }
//    public class CustomFacebookshareDialogYesNo extends Dialog implements
//            android.view.View.OnClickListener {
//
//        public Activity c;
//        public Dialog d;
//        public Button yes, no;
//        String messsage;
//        TextView textView;
//        EditText caption;
//        ImageView voucher;
//        RelativeLayout relativeLayout;
//        String background;
//
//        public CustomFacebookshareDialogYesNo(Activity a, String background) {
//            super(a, R.style.AlertDialogTheme);
//            // TODO Auto-generated constructor stub
//            this.c = a;
//            this.background = background;
//        }
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            requestWindowFeature(Window.FEATURE_NO_TITLE);
//            setContentView(R.layout.custom_facebook_share_dialog);
//            no = (Button) findViewById(R.id.no_but);
//            yes = (Button) findViewById(R.id.yes_but);
//            yes.setTypeface(Util.setTextFontOpenSans(getContext(), "OpenSans-Light.ttf"));
//            relativeLayout = (RelativeLayout) findViewById(R.id.layout);
//            textView = (TextView) findViewById(R.id.dialog_text);
//            caption =(EditText)findViewById(R.id.caption);
//            voucher = (ImageView)findViewById(R.id.imagefb);
//            textView.setTypeface(Util.setTextFontOpenSans(getContext(), "OpenSans-Light.ttf"));
//            no.setOnClickListener(this);
//            voucher.setImageBitmap(screenShot(voucherLayout));
//            // no = (Button) findViewById(R.id.btn_no);
//            yes.setOnClickListener(this);
//
//            //no.setOnClickListener(this);
//            if (background.equals("black")) {
//                relativeLayout.setBackgroundResource(R.drawable.slideraalertback);
//            } else if (background.equals("white")) {
//                relativeLayout.setBackgroundResource(R.drawable.alertwhitbackground);
//            } else {
//                relativeLayout.setBackgroundResource(R.drawable.termsndconditionbackground);
//            }
//
//        }
//
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.yes_but:
//                    String captiontext="";
//                    Bitmap bmp;
//                    if(caption.getText().toString().trim()!="")
//                    {
//                        captiontext=caption.getText().toString().trim();
//
//                    }
//                       shareOnfacbookwithPublicshAction(captiontext);
//                    break;
//                case R.id.no_but:
//
//                    // break;
//                default:
//                    break;
//            }
//              dismiss();
//        }
//    }
    private class CustomDialogOkVoucherserrvice extends Dialog implements
            View.OnClickListener {

        public Activity c;
        public Dialog d;
        public Button yes, no;
        String messsage;
        TextView textView;
        RelativeLayout relativeLayout;
        String background;

        public CustomDialogOkVoucherserrvice(Activity a, String message, String background) {
            super(a, R.style.AlertDialogTheme);
            // TODO Auto-generated constructor stub
            this.c = a;
            this.messsage = message;
            this.background = background;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_dialog);
            yes = (Button) findViewById(R.id.okay_but);
            yes.setTypeface(Util.setTextFontOpenSans(getContext(), "OpenSans-Light.ttf"));
            relativeLayout = (RelativeLayout) findViewById(R.id.layout);
            textView = (TextView) findViewById(R.id.dialog_text);
            textView.setTypeface(Util.setTextFontOpenSans(getContext(), "OpenSans-Light.ttf"));
            textView.setText(messsage);

            // no = (Button) findViewById(R.id.btn_no);
            yes.setOnClickListener(this);

            //no.setOnClickListener(this);
            if (background.equals("black")) {
                relativeLayout.setBackgroundResource(R.drawable.slideraalertback);
            } else if (background.equals("white")) {
                relativeLayout.setBackgroundResource(R.drawable.alertwhitbackground);
            } else {
                relativeLayout.setBackgroundResource(R.drawable.termsndconditionbackground);
            }

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.okay_but:
                    //c.finish();
                    break;
                //case R.id.btn_no:
                //  dismiss();
                // break;
                default:
                    break;
            }
            AccelerometerManager.startListening(Slider.this);
            shake_it.setEnabled(true);
            dismiss();

        }

        @Override
        public void onBackPressed() {

        }
    }

    @Override
    public void onBackPressed() {

    }
}
