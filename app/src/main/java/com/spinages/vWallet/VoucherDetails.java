package com.spinages.vWallet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.widget.LikeView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.spinages.app.AppConstants;
import com.spinages.app.MyApplication;
import com.spinages.app.R;
import com.spinages.app.ReviewDialog;
import com.spinages.app.TermsNdCondDialog;
import com.spinages.utilities.Util;

import java.util.ArrayList;
import java.util.HashMap;


public class VoucherDetails extends Activity implements View.OnClickListener {
    Button next, reviews;
    ImageView image;
    TextView description;
    TextView expiryDate;
    TextView location;
    TextView validityTime;
    TextView voucherId;
    TextView validityDays, validityDays2;
    LinearLayout line;
    // TextView reviews;
    TextView contact_number;
    TextView Location_available;
    TextView termsAndCondition;
    ArrayList<HashMap<String, String>> arl;
    int position;
    String activity;
    CallbackManager callbackManager;
    LikeView likeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.voucher_details);

        init();
        arl = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("arrayList");
        position = getIntent().getIntExtra("position", 0);
        activity = getIntent().getStringExtra("activity");

        Log.e("...serialized data..", arl.toString());
        setViewsData();
        initInstances();
        initCallbackManager();
        refreshButtonsState();

    }

    private void initInstances() {
        String fbPage = arl.get(position).get("fbLikePage");
        likeView = (LikeView) findViewById(R.id.likeView);
//setting button style
        likeView.setLikeViewStyle(LikeView.Style.STANDARD);
        likeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);
//setting the url of page to like on click
        likeView.setObjectIdAndType(
                fbPage,
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

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_btn:

                openAlertDialog();


                break;
            case R.id.terms:
                Intent intent = new Intent();
                intent.putExtra("terms", arl.get(position).get("termsConditions").toString());
                intent.setClass(getBaseContext(), TermsNdCondDialog.class);
                startActivity(intent);
                break;
            case R.id.review_btn:

                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(arl.get(position).get("reviews").toString()));
                    startActivity(browserIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    void init() {
        image = (ImageView) findViewById(R.id.image);
        description = (TextView) findViewById(R.id.description);
        expiryDate = (TextView) findViewById(R.id.date);
        location = (TextView) findViewById(R.id.offer_region);
        validityTime = (TextView) findViewById(R.id.validity_text);
        voucherId = (TextView) findViewById(R.id.id_text);
        validityDays = (TextView) findViewById(R.id.days_text);
        validityDays2 = (TextView) findViewById(R.id.days2_text);
        line = (LinearLayout) findViewById(R.id.line);
        reviews = (Button) findViewById(R.id.review_btn);
        contact_number = (TextView) findViewById(R.id.number_text);
        Location_available = (TextView) findViewById(R.id.location_text);
        termsAndCondition = (TextView) findViewById(R.id.terms);
        termsAndCondition.setOnClickListener(this);
        next = (Button) findViewById(R.id.next_btn);
        next.setOnClickListener(this);
        reviews.setOnClickListener(this);

    }

    void setViewsData() {
        ImageLoader imageLoader = ImageLoader.getInstance();
/*{"pVouchers":[{"backImage":"/spinages/images/iYHfUoC20x9TVin8_15Oct2015042651GMT_1444883211304.jpg",
"description":"Everyday Voucher","expiryDate":"22-12-2015","frontImage":"/spinages/images/ZcQErK6bTMGe8vcB_15Oct2015041459GMT
_1444882499648.jpg","id":6,"received_on":"15-10-2015","status":"EXPIRED"}],"vVouchers":[{"claimedOn":null,
"commContact":"9417833754","companyAddr":"MBI Sec 34","companyLogo":"http://test.com/partner3.png","companyName":
"Godary Company Pvt Ltd","contactNumber":"9419632225","description":"Upto 60% Off on Movie","fbLikePage":
"http://facebook.com/partner3","id":"7","locationAvail":"Loc 7 $ Loc 8 $ Loc 2","receivedOn":"15-10-2015","reviews":
"http://partner3.com/voucherrev3.html","status":"EXPIRED","termsConditions":"http://partner3.com/vouchertnc3.html",
"validDays":"YYYYYYY","validPeriodEnd":"22-10-2015","validPeriodStart":"29-09-2015","validTimeFrom":"1030","validTimeTo":
"2040","voucherType":"VIRTUAL"}]}*/
        if (arl.get(position).get("voucherType").equals("VIRTUAL")) {
            if (arl.get(position).get("claim").equals("yes")) {
                next.setVisibility(View.INVISIBLE);
                line.setVisibility(View.INVISIBLE);


            }
            description.setText(arl.get(position).get("description").toString());
            location.setText(arl.get(position).get("locationAvail").toString());
            expiryDate.setText(arl.get(position).get("validPeriodEnd").toString());
            voucherId.setText(arl.get(position).get("id").toString());
            contact_number.setText(arl.get(position).get("commContact").toString());
            //  reviews.setText(arl.get(position).get("reviews").toString());
            Location_available.setText(arl.get(position).get("locationAvail").toString());
            validityTime.setText(arl.get(position).get("validTimeFrom").toString() + " to " + arl.get(position).get("validTimeFrom").toString());
            validityDays.setText(arl.get(position).get("validPeriodStart").toString() + " to " + arl.get(position).get("validPeriodEnd").toString());
            validityDays2.setText(arl.get(position).get("validDays"));
            //imageLoader.displayImage("http://staging.spinages.com/resources/" + arl.get(position).get("companyLogo").toString(), image, MyApplication.options);
            ImageLoader.getInstance().displayImage(AppConstants.INSOMNIX_RESOURCES_URL + arl.get(position).get("companyLogo")

                    , image, MyApplication.options);
        }

        if (arl.get(position).get("voucherType").equals("PHYSICAL")) {
            String url = arl.get(position).get("companyLogo").toString();
            description.setText(arl.get(position).get("description").toString());
            location.setText(arl.get(position).get("locationAvail").toString());
            expiryDate.setText(arl.get(position).get("validPeriodEnd").toString());
            voucherId.setText(arl.get(position).get("id").toString());
            // imageLoader.displayImage(url, image, MyApplication.options);
            ImageLoader.getInstance().displayImage(AppConstants.INSOMNIX_RESOURCES_URL + arl.get(position).get("companyLogo")
                    , image, MyApplication.options);

        }

    }

    public void openAlertDialog() {
        String message = "Are you sure you want to claim this? This process is " +
                "irreversible, only press this when it's verified by the staff.";
        new CustomDialogYesNo(this, message, "black").show();
    }

    void nextActivity() {
        Intent intent = new Intent();
        intent.putExtra("id", Integer.parseInt(arl.get(position).get("id").toString()));
        intent.setClass(this, ClaimVoucher.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // finish();
    }

    public class CustomDialogYesNo extends Dialog implements
            View.OnClickListener {

        public Activity c;
        public Dialog d;
        public Button yes, no;
        String messsage;
        TextView textView;
        RelativeLayout relativeLayout;
        String background;

        public CustomDialogYesNo(Activity a, String message, String background) {
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
            setContentView(R.layout.custom_dialog_yes_no);
            no = (Button) findViewById(R.id.no_but);
            yes = (Button) findViewById(R.id.yes_but);
            yes.setTypeface(Util.setTextFontOpenSans(getContext(), "OpenSans-Light.ttf"));
            relativeLayout = (RelativeLayout) findViewById(R.id.layout);
            textView = (TextView) findViewById(R.id.dialog_text);
            textView.setTypeface(Util.setTextFontOpenSans(getContext(), "OpenSans-Light.ttf"));
            textView.setText(messsage);
            no.setOnClickListener(this);

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
                case R.id.yes_but:
                    nextActivity();
                    break;
                case R.id.no_but:
                    // dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }
}
