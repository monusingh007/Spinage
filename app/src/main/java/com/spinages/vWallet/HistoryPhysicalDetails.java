package com.spinages.vWallet;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.spinages.app.AppConstants;
import com.spinages.app.MyApplication;
import com.spinages.app.R;
import com.spinages.utilities.PrefManager;
import com.spinages.utilities.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by monu on 12/10/2015.
 */
public class HistoryPhysicalDetails extends Activity {
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
        setContentView(R.layout.historyphysicalvoucherdetails);
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

        mainLayout = (RelativeLayout) findViewById(R.id.mainlayout);
        description = (EditText) findViewById(R.id.descriptionData);
        expiryDate = (EditText) findViewById(R.id.date_Data);
        setData();
    }

       void setData(){
        ImageLoader imageLoader = ImageLoader.getInstance();
        String  frontImageurl = AppConstants.INSOMNIX_RESOURCES_URL + arl.get(position).get("companyLogo");
        description.setText(arl.get(position).get("description").toString());
        expiryDate.setText(arl.get(position).get("validPeriodEnd").toString());
        String  backImageurl = AppConstants.INSOMNIX_RESOURCES_URL+ arl.get(position).get("backImage");
        VoucherId =arl.get(position).get("id").toString();
        imageLoader.displayImage(frontImageurl, front_image, MyApplication.options);
           imageLoader.displayImage(backImageurl, back_image, MyApplication.options);
    }

}
