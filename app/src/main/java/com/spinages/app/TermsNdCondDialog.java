package com.spinages.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spinages.webservices.URLconnectionService;

import org.json.JSONObject;

public class TermsNdCondDialog extends Activity {
    ProgressDialog pDialog;
    TextView termCondtions;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_nd_cond_dialog);
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

    class GetTermsAndConditions extends AsyncTask<String, String, String> {
        String termsNdConditions;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(TermsNdCondDialog.this, R.style.ProgerssDialogTheme);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = AppConstants.INSOMNIX_WEBSRVICES_URL+"app/getTermNConditions";
            try {//websrvice to get terms and condition
                JSONObject obj = URLconnectionService.getWebServiceData(url,
                        null, AppConstants.GET_REQUEST, AppConstants.APP_GMAIL, AppConstants.APP_PASSWORD);

                termsNdConditions = obj.opt("termsNCond").toString();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (null != termsNdConditions)
                termCondtions.setText(termsNdConditions);
            else
                termCondtions.setText("Error!, try again.");


            pDialog.dismiss();
        }
    }

}
