package com.spinages.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.spinages.utilities.DividerItemDecoration;
import com.spinages.utilities.PrefManager;
import com.spinages.utilities.Util;
import com.spinages.utilities.VerticalSpaceItemDecoration;
import com.spinages.webservices.URLconnectionService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Naxtre on 7/20/15.
 */
public class InterestPage extends Activity implements View.OnClickListener {
    Button nextbut;
    int itemSize;
    String name;
    String birthday;
    String email;
    String id;
    String first_name;
    String last_name;
    String gender;
    String refcode;
    JSONObject response;
    LinearLayoutManager layoutManager;
    RecyclerView mRecyclerView;
    String[] catName;
    PrefManager prefManager;
    // ALL JSON node names
     /*{"country":null,"interests":[{"description":"Food","id":10000001,"logoUrl":"","name":"Food and Drinks",
"status":"DISABLED"},{"description":"Entertainment","id":10000002,"logoUrl":"","name":"Entertainment",
"status":"DISABLED"},{"description":"Shopping","id":10000003,"logoUrl":"","name":"Shopping","status":"DISABLED"}
,{"description":"Fashion and Clothing","id":10000004,"logoUrl":"","name":"Fashion and Clothing","status":"DISABLED"},
{"description":"Arts","id":10000005,"logoUrl":"","name":"Arts","status":"DISABLED"},
{"description":"Business services","id":10000006,"logoUrl":"","name":"Business services","status":"DISABLED"}],
"page":null,"status":null,"voucherList":null}*/

    /*{"interests":[{"description":"Food and Drinks","id":10000001,"logoUrl":"","name":"Food and Drinks"},
    {"description":"Arts","id":10000002,"logoUrl":"","name":"Arts"},
    {"description":"Travel","id":10000003,"logoUrl":"","name":"Travel"},
    {"description":"Food and Drinks","id":10000001,"logoUrl":"","name":"Food and Drinks"},
    {"description":"Arts","id":10000002,"logoUrl":"","name":"Arts"},
    {"description":"Travel","id":10000003,"logoUrl":"","name":"Travel"}]}

*/
    private static final String TAG_INTEREST = "interests";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_LOGO_URL = "logoUrl";
    private static final String TAG_ = "subject";
    private static final String TAG_STATUS = "status";
    TextView  tellus;
    private ProgressDialog pDialog;
    public static ArrayList<String> mArrayList = new ArrayList<String>();
    public static ArrayList<String> cate_mArrayList = new ArrayList<String>();
    public static ArrayList<String> id_mArrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            Log.e("create()", "oncreate is called");
        setContentView(R.layout.user_interest_page);
        mRecyclerView = (RecyclerView) findViewById(R.id.interest_List);
        mRecyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(InterestPage.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(2));
        //or
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this));
        //or
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, R.drawable.divider));

        nextbut = (Button) findViewById(R.id.Interest_next_btn);
        nextbut.setOnClickListener(InterestPage.this);
        nextbut.setTypeface(Util.setTextFontOpenSans(this, "OpenSans-Regular.ttf"));
        prefManager = new PrefManager(this);

tellus = (TextView)findViewById(R.id.interest_tellus_txt);
        tellus.setTypeface(Util.setTextFontOpenSans(this, "OpenSans-Light.ttf"));
        if (isConnected() == true) {
            new GetJsonDtata().execute();

        } else {
            Toast.makeText(this, "please check your internet connection", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Interest_next_btn:
                if (mArrayList.size() >2 ) {
                    new RegisterUser().execute();

                } else {
                    Toast.makeText(InterestPage.this, "Please Choose minimum 3 Categories", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    /*************************/
    public class GetJsonDtata extends AsyncTask<URL, Long, Integer> {
        String targetURL = "http://staging.spinages.com/spinages-webservices/app/listInterests";
        String app_user = "appuser";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(InterestPage.this, R.style.ProgerssDialogTheme);
            pDialog.setMessage("Loading Intrests ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);

            Log.d("intesret page: ", "preexecute  is runing");
            pDialog.show();
        }

        @Override
        protected Integer doInBackground(URL... params) {

            try {
                response = URLconnectionService.getWebServiceData(targetURL, null, "getRequest", AppConstants.APP_GMAIL, AppConstants.APP_PASSWORD);
                try {
                    Log.d("interest JSON: ", response.toString());
                    Log.d("interest page: ", "do in background is runing");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray interest_array = response.getJSONArray(TAG_INTEREST);
                    itemSize = interest_array.length();
                    for (int j = 0; j <itemSize; j++) {
                        JSONObject jobject = interest_array.getJSONObject(j);

                        String desciption = jobject.getString(TAG_NAME);
                        cate_mArrayList.add(desciption);
                        String id = jobject.getString(TAG_ID);
                        id_mArrayList.add(id);


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*{"country":null,"interests":[{"description":"Food","id":10000001,"logoUrl":"","name":"Food and Drinks",
"status":"DISABLED"},{"description":"Entertainment","id":10000002,"logoUrl":"","name":"Entertainment",
"status":"DISABLED"},{"description":"Shopping","id":10000003,"logoUrl":"","name":"Shopping","status":"DISABLED"}
,{"description":"Fashion and Clothing","id":10000004,"logoUrl":"","name":"Fashion and Clothing","status":"DISABLED"},
{"description":"Arts","id":10000005,"logoUrl":"","name":"Arts","status":"DISABLED"},
{"description":"Business services","id":10000006,"logoUrl":"","name":"Business services","status":"DISABLED"}],
"page":null,"status":null,"voucherList":null}*/

            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Log.d("post execute:", "post execute is runing");
            try {
                pDialog.dismiss();

                if (id_mArrayList.size() == 0) {
                    String message = "Due to some error we are unable to process request. Please try again.";
                   Toast.makeText(getBaseContext(),message,Toast.LENGTH_LONG).show();
                }

                AdapterInterestPage mInterestPage = new AdapterInterestPage(InterestPage.this, cate_mArrayList, id_mArrayList, itemSize);
                //mInterestPage.notifyDataSetChanged();

                mRecyclerView.setAdapter(mInterestPage);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.e("on start ", "on start is called");


    }

    @Override
    protected void onResume() {

        super.onResume();
        Log.e("on resume ", "onresume is called");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("on restart", "restart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("stop", "onstop is called");

    }


    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public class RegisterUser extends AsyncTask<URL, Long, Integer> {

        String targetURL = "http://staging.spinages.com/spinages-webservices/app/registerUser";
        String app_user = "appuser";
        JSONObject input_json;
        JSONObject output_json;
        String user_name;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(InterestPage.this, R.style.ProgerssDialogTheme);
            pDialog.setMessage("Registering User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            input_json = getUserData();
            user_name = input_json.optString("emailId").toString();


        }


        @Override
        protected Integer doInBackground(URL... urls) {
            try {
                output_json = URLconnectionService.getWebServiceData(targetURL, input_json.toString(), AppConstants.POST_JSON_REQUEST, AppConstants.APP_GMAIL, AppConstants.APP_PASSWORD);
                Log.e("register user", output_json.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Integer integer) {
            pDialog.dismiss();

            try {
                //{"code":101,"errors":null,"message":"#gBzh#xn"}

                String code = output_json.opt("code").toString();
                ArrayList<Integer> arrayList = new ArrayList<>();

                String message = output_json.opt("message").toString();
                if (code.equals("101")) {

                        prefManager.clearKeyMessage(AppConstants.USER_GMAIL);
                        prefManager.clearKeyMessage(AppConstants.USER_PASSWORD);



                    prefManager.createMessageBuffer(AppConstants.USER_GMAIL, user_name);
                    prefManager.createMessageBuffer(AppConstants.USER_PASSWORD, message);
                    prefManager.clearKeyMessage(AppConstants.USER_IS_LOGGED_IN);
                    prefManager.createMessageBuffer(AppConstants.USER_IS_LOGGED_IN,"true");
                    message = "You are registered sucessfully";
                    alertDialogOnRegister(101,message);
                } else if (code.equals("102")) {
                    JSONArray jsonArray = output_json.getJSONArray("errors");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        arrayList.add(jsonArray.optInt(i));
                    }
                    for (int i = 0; i < arrayList.size(); i++) {
                        int errorCode = arrayList.get(i);
                        if(errorCode==102)
                        {
                            //alertDialogOnRegister("last name not valid");
                        }
                        if(errorCode==103)
                        {
                           // alertDialogOnRegister("dob not valid");
                        }
                        if(errorCode==104)
                        {
                           // alertDialogOnRegister("gender not valid");
                        }
                        if(errorCode==105)
                        {
                           // alertDialogOnRegister(105,"Email id not valid");
                        }
                        if(errorCode==106)
                        {
                            alertDialogOnRegister(106,"Email id already registered.Please login with different account");


                        }
                        if(errorCode==107)
                        {
                           // alertDialogOnRegister("willing to travel not valid.");
                        }
                        if(errorCode==108)
                        {
                           // alertDialogOnRegister("Fb token not valid");
                        }
                        if(errorCode==109)
                        {
                            //alertDialogOnRegister("FB user name not valid");
                        }
                        if(errorCode==110)
                        {
                           // alertDialogOnRegister("user Intersts not valid");
                        }
                        /*101 – First Name not valid
102 – Last Name not valid
103 – DOB not valid (ex: dd-MM-yyyy)
104 – Gender not valid (ex:M or F)
105 – Email Id not valid
106 – Email already registered
107 – Willing to travel option is not valid
108 – FB User Id not valid
109 – FB Token not valid
110 – User interests are not valid (upper and lower bound are 1(min) and 3(max))

*/
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public JSONObject getUserData() {

        JSONObject input_json = new JSONObject();
        JSONArray interests = new JSONArray();
        JSONObject user = new JSONObject();
        String name = "";
        String birthday = "";
        String email = "";
        String id = "";
        String first_name = "";
        String last_name = "";
        String gender = "";
        String address_hometown = "";
        String address_current = "";
        String user_token = "";
        String willingToTravel = "Y";

        String token = prefManager.getPreviousMessage(AppConstants.FACCEBOOK_TOKEN_RESPONSE);
        String fbresponse = prefManager.getPreviousMessage(AppConstants.FACEBOOK_USER_INFO_RESPONSE);
        try {
/*fb response = {"id":"1615027628762295","name":"Monu Singh Gurjar","first_name":
"Monu Singh","last_name":"Gurjar","email":"monusingh.singh868@gmail.com",
"gender":"male","birthday":"05\/24\/1992","hometown":{"id":"108078335886887","name":"Roorkee"}}*/
            JSONObject object = new JSONObject(fbresponse);
            try {
                JSONObject addresObj = object.getJSONObject("hometown");
                address_hometown = addresObj.optString("name").toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                JSONObject currentAddressObj = object.getJSONObject("location");
                address_current = currentAddressObj.optString("name");
            } catch (Exception e) {
                e.printStackTrace();
            }
            id = object.optString("id").toString();
            birthday = object.optString("birthday").toString();

            email = object.optString("email").toString();
            if (email.equals("")) {
               email = id+"@gmail.com";
                Log.e("email", id + "@gmail.com");
            }
            gender = object.optString("gender").toString();
            first_name = object.optString("first_name").toString();
            last_name = object.optString("last_name").toString();

            address_current = object.optString("current_location").toString();


            for (int i = 0; i < InterestPage.mArrayList.size(); i++) {
                JSONObject interests_id = new JSONObject();
                interests_id.accumulate("id", InterestPage.id_mArrayList.get(i));
                if (InterestPage.id_mArrayList.get(i).equals("10003")) {
                    willingToTravel = "Y";
                }
                interests.put(interests_id);
                Log.e("marraylist values", InterestPage.id_mArrayList.get(i).toString());


            }
/*{"address":"Kotakpura Bye Pass, Dharam Singh Nagar",
            "addressArea":"Moga","dob":"17-09-1985","emailId":"harkomal.dhk@gmail.com",
            "fbUserId":"harkomal53","fname":"Harkomal","gender":"M","interests":[{"id":10000001},
            {"id":10000002}],"lname":"Singh","mobileNo":"9417525774","postCode":"142001","token":"TC100NF","willingToTravel":"Y"}*/
            if (address_current.trim() != "") {
                input_json.accumulate("address", address_current);
            } else if (address_hometown.trim() != "") {
                input_json.accumulate("address", address_hometown);
            } else {
                input_json.accumulate("address", "");
            }
            input_json.accumulate("addressArea", "");
            if (birthday != "")
                birthday = birthday.replaceAll("\\/", "-");
            else
                birthday = "00-00-0000";
            input_json.accumulate("dob", birthday);
            input_json.accumulate("emailId", email);
            input_json.accumulate("fbUserId", id);
            input_json.accumulate("fname", first_name);
            try {
                if (gender.equals("male")) {
                    input_json.accumulate("gender", "M");
                } else {
                    input_json.accumulate("gender", "F");
                }
            } catch (Exception e) {
                e.printStackTrace();
                input_json.accumulate("gender", "");
            }
            input_json.accumulate("interests", interests);
            input_json.accumulate("lname", last_name);
            input_json.accumulate("mobileno", "");
            input_json.accumulate("postCode", "");
            input_json.accumulate("token", token);
            input_json.accumulate("willingToTravel", willingToTravel);

            // input_json.accumulate("refcode", ref_code);

            // input_json.accumulate("address",address_hometown );

            Log.e("inputjson register user", input_json.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input_json;
    }

    public void alertDialogOnRegister(int code,String message) {
        new InterestCustomDialogOk(code,message,"black").show();
    }

    void nextActivity() {

        Intent mSignUpIntent = new Intent(InterestPage.this, ReferalActivity.class);
        mSignUpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mSignUpIntent);
    }

    public void openAlertDialog(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setNeutralButton("!Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                new GetJsonDtata().execute();
            }
        });

//to create alertdialog
        AlertDialog alertDialog = alertDialogBuilder.create();
//to show dialog
        alertDialog.show();
    }
    public class InterestCustomDialogOk extends Dialog implements
            View.OnClickListener {

        public Activity c;
        public Dialog d;
        public Button yes, no;
        String messsage;
        TextView textView;
        RelativeLayout relativeLayout;
        String background;
        int code;

        public InterestCustomDialogOk(int code, String message, String background) {
            super(InterestPage.this, R.style.AlertDialogTheme);
            // TODO Auto-generated constructor stub
            this.messsage =message;
            this.code=code;
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
            textView = (TextView)findViewById(R.id.dialog_text);
            textView.setTypeface(Util.setTextFontOpenSans(getContext(), "OpenSans-Light.ttf"));
            textView.setText(messsage);

            // no = (Button) findViewById(R.id.btn_no);
            yes.setOnClickListener(this);

            //no.setOnClickListener(this);
            if(background.equals("black"))
            {
                relativeLayout.setBackgroundResource(R.drawable.slideraalertback);
            }else if(background.equals("white")) {
                relativeLayout.setBackgroundResource(R.drawable.alertwhitbackground);
            }else{
                relativeLayout.setBackgroundResource(R.drawable.termsndconditionbackground);
            }

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.okay_but:
                    if(code==101){
                    Intent mSignUpIntent = new Intent(InterestPage.this, ReferalActivity.class);
                    mSignUpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mSignUpIntent);
                }
                    if(code==106){
                        Intent mSignUpIntent = new Intent(InterestPage.this, LoginPage.class);
                        mSignUpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mSignUpIntent);
                    }

                    break;
                //case R.id.btn_no:
                //  dismiss();
                // break;
                default:
                    break;
            }
            dismiss();
        }
    }
}
