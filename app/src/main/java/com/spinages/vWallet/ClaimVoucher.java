package com.spinages.vWallet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.spinages.app.AppConstants;
import com.spinages.app.R;
import com.spinages.utilities.PrefManager;
import com.spinages.webservices.URLconnectionService;

import org.json.JSONObject;

public class ClaimVoucher extends ActionBarActivity {
    ProgressDialog progressDialog;
    PrefManager prefManager;
    EditText redeemCodeEdittext;
    Button claimButton;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_voucher);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 1.00);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.50);

        getWindow().setLayout(width, height);
        prefManager = new PrefManager(this);
        progressDialog = new ProgressDialog(this, R.style.ProgerssDialogTheme);
        redeemCodeEdittext = (EditText) findViewById(R.id.code);
        claimButton = (Button) findViewById(R.id.apply_but);
        id = getIntent().getIntExtra("id", 0);

        claimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new VoucherService().execute();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


    }

    void nextActivity() {


    }



    class VoucherService extends AsyncTask<String, String, String> {
        String gmail = prefManager.getPreviousMessage(AppConstants.USER_GMAIL);
        String password = prefManager.getPreviousMessage(AppConstants.USER_PASSWORD);
        String Redeemcode = redeemCodeEdittext.getText().toString().trim();
        JSONObject outputJson;
        String code="";
        String url = AppConstants.INSOMNIX_WEBSRVICES_URL+"user/claimVoucher";
        String message="";
        JSONObject inputJson;

        /*Input Params:  {"claimCode":"1234","voucherId":7}*/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inputJson = new JSONObject();
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                inputJson.accumulate("claimCode", Redeemcode);
                inputJson.accumulate("voucherId", String.valueOf(id));


                outputJson = URLconnectionService.getWebServiceData(url, inputJson.toString(), AppConstants.POST_JSON_REQUEST, gmail, password);
                // prefManager.createMessageBuffer(AppConstants.SIGHNIN_JSON,outputJson.toString());
                message = outputJson.opt("message").toString();
                code = outputJson.opt("code").toString();


            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ClaimVoucher.this, "error", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();
/* {"code":102,"errors":null,"message":"SPINAGES_FB_ALREADY_LIKED"}*/
            if (code.equals("101")) {
              //  Toast.makeText(getBaseContext(), "success", Toast.LENGTH_LONG).show();

                Intent intent = new Intent();
                intent.putExtra(AppConstants.VOUCHER_WON_ONCLAIM, outputJson.toString());
                intent.setClass(ClaimVoucher.this, CongratulationActivity.class);
                startActivity(intent);
                finish();
            }


            if (code.equals("102")) {
                if ("UNABLE_TO_CLAIM_VOUCHER".equals(message)) {
                    Toast.makeText(getBaseContext(), "Unable to claim. Try again", Toast.LENGTH_LONG).show();
                }
              else  if ("CLAIM_CODE_NOT_VAID".equals(message)) {
                    Toast.makeText(getBaseContext(), "Claim code not valid", Toast.LENGTH_LONG).show();
                   // nextActivity();
                } else {
                    Toast.makeText(getBaseContext(), "error", Toast.LENGTH_LONG).show();
                }


            }
        }
    }

}
