package com.spinages.vWallet;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.spinages.app.AppConstants;
import com.spinages.app.MyApplication;
import com.spinages.app.R;
import com.spinages.utilities.PrefManager;
import com.spinages.utilities.Util;
import com.spinages.webservices.URLconnectionService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DeletePhysicalVoucher extends Activity  {
    ArrayList<HashMap<String, String>> arl;
    int position;
    String activity;
    Button upload;
    ImageView front_image, back_image;
    EditText description;
    EditText expiryDate;
    String VoucherId;
    String expdate;
    PrefManager prefManager;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    TextView title;
    RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_physical_voucher);
        prefManager = new PrefManager(this);
        title = (TextView) findViewById(R.id.title);
        title.setTypeface(Util.setTextFontOpenSans(this, "OpenSans-Light.ttf"));
        front_image = (ImageView) findViewById(R.id.front);
        back_image = (ImageView) findViewById(R.id.back);
       // front_image.setOnClickListener(this);
       // back_image.setOnClickListener(this);
        arl = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("arrayList");
        position = getIntent().getIntExtra("position", 0);
        activity = getIntent().getStringExtra("activity");
        upload = (Button) findViewById(R.id.upload_voucher);
        upload.setTypeface(Util.setTextFontOpenSans(this, "OpenSans-Light.ttf"));
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteVoucherWithId(VoucherId,position).execute();
            }
        });
        mainLayout = (RelativeLayout) findViewById(R.id.mainlayout);
        description = (EditText) findViewById(R.id.descriptionData);
        expiryDate = (EditText) findViewById(R.id.date_Data);
        setData();
    }

void setData(){
    ImageLoader imageLoader = ImageLoader.getInstance();
   String  frontImageurl = arl.get(position).get("companyLogo").toString();
    description.setText(arl.get(position).get("description").toString());
    expiryDate.setText(arl.get(position).get("validPeriodEnd").toString());
  String  backImageurl = arl.get(position).get("backImage").toString();
    VoucherId =arl.get(position).get("id").toString();
   imageLoader.displayImage(frontImageurl, front_image, MyApplication.options);
    imageLoader.displayImage(backImageurl, back_image, MyApplication.options);
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
            if (code.equals("101")) {
                //voucher_list.remove(pos);
                // ((BaseAdapter) getListView().getAdapter()).notifyDataSetChanged();
                // getListView().invalidate();
              //  openToast("SUCCESS");
                Intent intent = new Intent();
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setClass(DeletePhysicalVoucher.this, VwalletMainActivity.class);
                startActivity(intent);


            }else {
                openToast("error");
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setClass(DeletePhysicalVoucher.this, VwalletMainActivity.class);
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
