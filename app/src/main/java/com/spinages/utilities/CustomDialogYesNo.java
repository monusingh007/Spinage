package com.spinages.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spinages.app.R;

/**
 * Created by monu on 11/7/2015.
 */
public class CustomDialogYesNo extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    String messsage;
    TextView textView;
    RelativeLayout relativeLayout;
    String background;

    public CustomDialogYesNo(Activity a, String message, String background) {
        super(a, R.style.AlertDialogTheme);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.messsage = message;
        this.background = background;
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
        textView.setText(messsage);
        no.setOnClickListener(this);

        // no = (Button) findViewById(R.id.btn_no);
        yes.setOnClickListener(this);

        //no.setOnClickListener(this);
        if (background.equals("black")) {
            relativeLayout.setBackgroundResource(R.drawable.slideraalertback);
        } else if (background.equals("white")) {
            relativeLayout.setBackgroundResource(R.drawable.alertwhitbackground);
        } else {
            relativeLayout.setBackgroundResource(R.drawable.termsndconditionbackground);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yes_but:

                break;
            case R.id.no_but:
             dismiss();
            // break;
            default:
                break;
        }
      //  dismiss();
    }

    @Override
    public void onBackPressed() {
        //
    }
}