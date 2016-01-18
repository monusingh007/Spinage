package com.spinages.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by monu on 12/11/2015.
 */
public class ReviewDialog  extends Activity {
    ProgressDialog pDialog;
    TextView termCondtions;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_dialog);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.80);

        getWindow().setLayout(width, height);
        String terms = getIntent().getStringExtra("terms");
        termCondtions = (TextView) findViewById(R.id.termsAndCond);
        try {
            termCondtions.setText(terms);
        } catch (Exception e) {
            e.printStackTrace();
        }
        button = (Button) findViewById(R.id.okay_but);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //  new GetTermsAndConditions().execute();

    }


}
