package com.spinages.vWallet;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.spinages.app.AppConstants;
import com.spinages.app.MyApplication;
import com.spinages.app.R;
import com.spinages.utilities.PrefManager;
import com.spinages.webservices.URLconnectionService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DeleteVoucherActivity extends Activity implements View.OnClickListener{
    ArrayList<HashMap<String, String>> arl;
    int position;
    String activity;
    Button next,delete,next_arrow,previous_arrow ;
    ImageView image;
    TextView description;
    TextView expiryDate;
    TextView location;
    TextView validityTime;
   String voucherId;
    TextView validityDays;
    // TextView reviews;
    TextView contact_number;
    TextView Location_available;
    TextView termsAndCondition;
    PrefManager prefManager;
    String backImageurl , frontImageurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_voucher);
        arl = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("arrayList");
        position = getIntent().getIntExtra("position", 0);
        activity = getIntent().getStringExtra("activity");
        init();
        setViewsData();

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
            description.setText(arl.get(position).get("description").toString());
            String  url = arl.get(position).get("companyLogo").toString();
            location.setText(arl.get(position).get("locationAvail").toString());
            expiryDate.setText(arl.get(position).get("validPeriodEnd").toString());
            imageLoader.displayImage(AppConstants.INSOMNIX_RESOURCES_URL+arl.get(position).get("companyLogo"), image, MyApplication.options);

        }
        if (arl.get(position).get("voucherType").equals("PHYSICAL")) {
            frontImageurl = arl.get(position).get("companyLogo").toString();
            description.setText(arl.get(position).get("description").toString());
            location.setText(arl.get(position).get("locationAvail").toString());
            expiryDate.setText(arl.get(position).get("validPeriodEnd").toString());
            backImageurl = arl.get(position).get("backImage").toString();
            imageLoader.displayImage(frontImageurl, image, MyApplication.options);

        }

    }

    void init() {
prefManager = new PrefManager(this);
        image = (ImageView) findViewById(R.id.image);
        description = (TextView) findViewById(R.id.description);
        expiryDate = (TextView) findViewById(R.id.date);
        location = (TextView) findViewById(R.id.offer_region);
        next = (Button) findViewById(R.id.done);
        delete = (Button) findViewById(R.id.delete);
        voucherId = arl.get(position).get("id").toString();
        next_arrow = (Button) findViewById(R.id.next);
        previous_arrow = (Button) findViewById(R.id.previous);
        next_arrow.setOnClickListener(this);
        previous_arrow.setOnClickListener(this);
        next.setOnClickListener(this);
        delete.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.done:
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setClass(DeleteVoucherActivity.this, VwalletMainActivity.class);
                startActivity(intent);
                break;
            case R.id.delete:
                new DeleteVoucherWithId(voucherId,position).execute();
                break;
            case R.id.next:
              //  if (arl.get(position).get("voucherType").equals("PHYSICAL"))
              //  ImageLoader.getInstance().displayImage(backImageurl, image, MyApplication.options);
                break;
            case R.id.previous:
               // if (arl.get(position).get("voucherType").equals("PHYSICAL"))
               // ImageLoader.getInstance().displayImage(frontImageurl, image, MyApplication.options);
                break;
        }
    }
    class DeleteVoucherWithId extends AsyncTask<String, String, String> {
        String voucherId;
        int pos;
        String code="";
        String message="";
        String errors="";
        String targetUrl;
        String gmail = prefManager.getPreviousMessage(AppConstants.USER_GMAIL);
        String password = prefManager.getPreviousMessage(AppConstants.USER_PASSWORD);

        DeleteVoucherWithId(String id,int position) {
            voucherId = id;
            pos = position;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            targetUrl  = AppConstants.INSOMNIX_WEBSRVICES_URL+"user/deleteVoucher/" + voucherId;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                JSONObject jsonResponse = URLconnectionService.getWebServiceData(targetUrl, null, AppConstants.GET_REQUEST, gmail, password);
                code = jsonResponse.optString("code").toString();
                errors = jsonResponse.optString("errors").toString();
                message = jsonResponse.optString("message").toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (code.equals("101") ) {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setClass(DeleteVoucherActivity.this,VwalletMainActivity.class);
                startActivity(intent);



            }else {
                openToast("error");
                Intent intent = new Intent();
              intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setClass(DeleteVoucherActivity.this,VwalletMainActivity.class);
                startActivity(intent);
            }
			/*{"code":101,"errors":null,"message":"SUCCESS"}

or

{"code":102,"errors":null,"message":"VOUCHER_NOT_FOUND"}

*/
        }
    }
    public void openToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
