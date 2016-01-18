package com.spinages.vWallet;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.spinages.Adapters.CustomListAdapter;
import com.spinages.app.AppConstants;
import com.spinages.app.R;
import com.spinages.utilities.PrefManager;
import com.spinages.utilities.Util;
import com.spinages.webservices.URLconnectionService;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class PVouchers extends ListActivity
{
    // Inbox JSON url
    private static final String All_Vocher_Url = AppConstants.INSOMNIX_WEBSRVICES_URL+"user/listAllVouchers";
    PrefManager prefManager;
    ArrayList<HashMap<String, String>> voucher_list;
    // products JSONArray
    JSONArray vVouchers = null;
    JSONArray pVouchers = null;
    // Progress Dialog
    private ProgressDialog pDialog;
    private JSONObject json;
    Calendar myCalendar;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voucher_list);
        voucher_list = new ArrayList<HashMap<String, String>>();
      prefManager = new PrefManager(this);

        new LoadVouchers().execute();
    }

    private class LoadVouchers extends AsyncTask<String, String, String> {
        String gmail = prefManager.getPreviousMessage(AppConstants.USER_GMAIL);
        String password = prefManager.getPreviousMessage(AppConstants.USER_PASSWORD);

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(PVouchers.this, R.style.ProgerssDialogTheme);
            pDialog.setMessage("Loading Vouchers  ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Inbox JSON
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();


            // Check your log cat for JSON reponse


            try {
                json = URLconnectionService.getWebServiceData(All_Vocher_Url, null, AppConstants.GET_REQUEST, gmail, password);

                Log.e("Inbox JSON: ", json.toString());
                vVouchers = json.getJSONArray("vVouchers");
                pVouchers = json.getJSONArray("pVouchers");

                for (int i = 0; i < pVouchers.length(); i++) {
                    JSONObject c = pVouchers.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    // Storing each json item in variable
                    String commContact = c.getString("backImage");
                    map.put("commContact", commContact);

                    String description = c.getString("description");
                    map.put("description", description);

/*{"backImage":"\/spinages\/images\/taS09U68mSUfX8u7_2Nov2015071141GMT_1446448301902.jpg","description":"wedwsdas",
"expiryDate":"12-09-8999","frontImage":"\/spinages\/images\/s4ZpU9kbyTx3tIYd_2Nov2015071142GMT_1446448302552.jpg","id":35,
"received_on":"02-11-2015","status":"EXPIRED"}
*/
                    String expiryDate= c.getString("expiryDate");
                    map.put("validPeriodEnd",expiryDate);
                    String frontImage = c.getString("frontImage");
                    map.put("companyLogo", AppConstants.INSOMNIX_RESOURCES_URL+frontImage);
                    map.put("backImage", AppConstants.INSOMNIX_RESOURCES_URL+c.getString("backImage"));

                    String id = c.getString("id");
                    map.put("id",id);
                    String received_on = c.getString("received_on");
                    map.put("received_on",received_on);
                    String status = c.getString("status");
                    map.put("status", status);

                    map.put("companyName", "");
                    map.put("locationAvail", "");
                    map.put("voucherType", "PHYSICAL");

                    // creating new HashMap


                    // adding each child node to HashMap key => value


                    voucher_list.add(map);

                    // adding HashList to ArrayList

                }
                // looping through All messages


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */

                    CustomListAdapter adapter = new CustomListAdapter(PVouchers.this, voucher_list,2);
                    // updating listview
                    setListAdapter(adapter);
                }
            });
            getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (voucher_list.get(i).get("voucherType").toString().equals("PHYSICAL")) {
                        Intent intent = new Intent();
                        intent.putExtra("activity", " ");
                        intent.putExtra("position", i);
                        intent.putExtra("arrayList", voucher_list);
                        intent.setClass(getBaseContext(), DeletePhysicalVoucher.class);
                        startActivity(intent);

                    }
                }
            });
            getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                   // openDeleteDialog(position);
                    return true;
                }
            });


        }
    }
    class DeleteVoucher extends AsyncTask<String, String, String> {
        String voucherId;
        int pos;
        String code="";
        String message="";
        String errors="";
        String targetUrl;
        String gmail = prefManager.getPreviousMessage(AppConstants.USER_GMAIL);
        String password = prefManager.getPreviousMessage(AppConstants.USER_PASSWORD);

        DeleteVoucher(String id,int position) {
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
                voucher_list.remove(pos);
                ((BaseAdapter) getListView().getAdapter()).notifyDataSetChanged();
                // getListView().invalidate();
                openToast("SUCCESS");


            }else {
                openToast("error");
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
    public void openDeleteDialog(final int position) {

        CustomDialogYesNo customDialogOk =  new CustomDialogYesNo(position);
        customDialogOk.show();
    }
    public class CustomDialogYesNo extends Dialog implements
            View.OnClickListener {

        public Activity c;
        public Button yes, no;
        TextView textView;
        RelativeLayout relativeLayout;
        String background;
        int position;

        public CustomDialogYesNo( int position ) {
            super(PVouchers.this , R.style.AlertDialogTheme);


            this.position = position;
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
            textView.setText("You want to delete this voucher");
            no.setOnClickListener(this);

            // no = (Button) findViewById(R.id.btn_no);
            yes.setOnClickListener(this);

            //no.setOnClickListener(this);

            relativeLayout.setBackgroundResource(R.drawable.slideraalertback);


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.yes_but:
                    dismiss();
                    Intent intent = new Intent();
                    intent.putExtra("activity"," ");
                    intent.putExtra("position",position);
                    intent.putExtra("arrayList", voucher_list);
                    intent.setClass(getBaseContext(), DeleteVoucherActivity.class);
                    startActivity(intent);
                    break;
                case R.id.no_but:
                    dismiss();
                    // break;
                default:
                    break;
            }
            //  dismiss();
        }
    }
}
