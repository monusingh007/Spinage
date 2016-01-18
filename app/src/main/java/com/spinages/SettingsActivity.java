package com.spinages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.spinages.app.LoginPage;
import com.spinages.app.R;
import com.spinages.app.ReferalActivity;
import com.spinages.utilities.PrefManager;

public class SettingsActivity extends Activity implements View.OnClickListener {
    TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_settings);
        logout = (TextView) findViewById(R.id.Setting_logout_text);
        logout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Setting_logout_text:
                setLogout();
                break;

        }
    }

    void setLogout() {
        PrefManager prefManager = new PrefManager(this);
        if (AccessToken.getCurrentAccessToken() != null && com.facebook.Profile.getCurrentProfile() != null) {

            LoginManager.getInstance().logOut();

            prefManager.clearAllData();
            nextActivity();
        } else {
            prefManager.clearAllData();
            nextActivity();

        }
    }
    void nextActivity() {

        Intent mSignUpIntent = new Intent(SettingsActivity.this, LoginPage.class);
        mSignUpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mSignUpIntent);
    }

}
