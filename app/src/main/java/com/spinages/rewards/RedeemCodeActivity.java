package com.spinages.rewards;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.spinages.app.AppConstants;
import com.spinages.app.R;
import com.spinages.utilities.PrefManager;
import com.spinages.webservices.URLconnectionService;

import org.json.JSONObject;

public class RedeemCodeActivity extends Activity {
    ProgressDialog progressDialog ;
    PrefManager prefManager;
    EditText redeemCodeEdittext;
    Button claimButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_code);
        progressDialog = new  ProgressDialog(this, R.style.ProgerssDialogTheme);
        redeemCodeEdittext = (EditText)findViewById(R.id.code);
        claimButton = (Button)findViewById(R.id.apply_but);
        claimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         new ClaimVoucher();
            }
        });
        prefManager = new PrefManager(this);

    }
    class ClaimVoucher extends AsyncTask<String,String,String >
    {
        String  gmail = prefManager.getPreviousMessage(AppConstants.USER_GMAIL);
        String password = prefManager.getPreviousMessage(AppConstants.USER_PASSWORD);
        String Redeemcode = redeemCodeEdittext.getText().toString().trim();
        JSONObject outputJson;
        String code="";
        String url = "http://insonix.noip.me:8081/spinages-webservices/user/claimVoucher";
        String message="";
        JSONObject inputJson ;
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
                inputJson.accumulate("claimCode",Redeemcode);
               // inputJson.accumulate("voucherId",);



                outputJson = URLconnectionService.getWebServiceData(url, null, AppConstants.GET_REQUEST, gmail, password);
                // prefManager.createMessageBuffer(AppConstants.SIGHNIN_JSON,outputJson.toString());
                message =outputJson.opt("message").toString();
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
            if (code.equals("101"))
              //  Toast.makeText(getBaseContext(), "success, you have won two spindo and a voucher", Toast.LENGTH_LONG).show();

            if (code.equals("102"));
              //  Toast.makeText(getBaseContext(),"spinages is already liked", Toast.LENGTH_LONG).show();


        }
    }
}
