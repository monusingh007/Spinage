package com.spinages.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spinages.app.R;
import com.spinages.shakeMotion.AccelerometerListener;
import com.spinages.shakeMotion.AccelerometerManager;

/**
 * Created by monu on 11/7/2015.
 */
public class CustomDialogOk extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    String messsage;
    TextView textView;
    RelativeLayout relativeLayout;
    String background;

    public CustomDialogOk(Activity a, String message, String background) {
        super(a, R.style.AlertDialogTheme);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.messsage =message;
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
                //c.finish();
                break;
         //case R.id.btn_no:
              //  dismiss();
               // break;
            default:
                break;
        }
        dismiss();

    }

    @Override
    public void onBackPressed() {
        //
    }
}