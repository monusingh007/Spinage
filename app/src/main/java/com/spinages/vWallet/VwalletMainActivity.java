package com.spinages.vWallet;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.spinages.app.R;
import com.spinages.utilities.Util;

public class VwalletMainActivity extends TabActivity {
	// TabSpec Names
	private static final String ALL_SPEC = "All";
	private static final String EV_SPEC = "Virtual";
	private static final String PV_SPEC = "Physical";
    private static final String HISTORY_SPEC = "History";
    private static final String ADD_PV_SPEC = "Add P-V";
    TextView vwallet ;
    ImageView addpv,history;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        vwallet = (TextView)findViewById(R.id.wallet);
        addpv = (ImageView)findViewById(R.id.addpv);
        history = (ImageView)findViewById(R.id.history);
        vwallet.setTypeface(Util.setTextFontOpenSans(VwalletMainActivity.this, "OpenSans-Light.ttf"));
        addpv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VwalletMainActivity.this, AddPhysicalVoucher.class));
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VwalletMainActivity.this, History.class));
            }
        });
        
      final  TabHost tabHost = getTabHost();

        // Inbox Tab
        TabSpec allSpec = tabHost.newTabSpec(ALL_SPEC);
        // Tab Icon
        allSpec.setIndicator(ALL_SPEC,null);
        Intent inboxIntent = new Intent(this, AllVouchers.class);

        // Tab Content
        allSpec.setContent(inboxIntent);
        
        // Outbox Tab
        TabSpec evSpec = tabHost.newTabSpec(EV_SPEC);
        evSpec.setIndicator(EV_SPEC, null);
        Intent outboxIntent = new Intent(this, EVouchers.class);
        evSpec.setContent(outboxIntent);
        
        // Profile Tab
        TabSpec pvSpec = tabHost.newTabSpec(PV_SPEC);
        pvSpec.setIndicator(PV_SPEC, null);
        Intent profileIntent = new Intent(this, PVouchers.class);
        pvSpec.setContent(profileIntent);


        TabSpec historySpec = tabHost.newTabSpec(HISTORY_SPEC);
        historySpec.setIndicator(null, getResources().getDrawable(R.drawable.history));
        Intent historyIntent = new Intent(this, History.class);
         historySpec.setContent(historyIntent);

        TabSpec AddPvSpec = tabHost.newTabSpec(ADD_PV_SPEC);
        AddPvSpec.setIndicator(null, getResources().getDrawable(R.drawable.add));
        Intent AddPvIntent = new Intent(this, AddPhysicalVoucher.class);
        AddPvSpec.setContent(AddPvIntent);


        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            public void onTabChanged(String arg0) {
                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    tabHost.getTabWidget().getChildAt(i)
                            .setBackgroundColor(Color.parseColor("#c1392b")); // unselected
                  //  tabHost.getTabWidget().getChildTabViewAt(i).text)
                }
                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())
                        .setBackgroundColor(Color.parseColor("#ffffff")); // unselected

            }
        });
        
        // Adding all TabSpec to TabHost
        tabHost.addTab(allSpec); // Adding Inbox tab
        tabHost.addTab(evSpec); // Adding Outbox tab
        tabHost.addTab(pvSpec); // Adding Profile tab
       // tabHost.addTab(AddPvSpec);//add physical voucher
       // tabHost.addTab(historySpec);//show history of vouchers
      //  tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tabhost);
        tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.shape_left_selected);
        tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.color.button_background);
        tabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.shape_right);
        TextView tv = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title); //Unselected Tabs
        TextView tv2 = (TextView) tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        tv2.setAllCaps(false);
        tv2.setTypeface(Util.setTextFontOpenSans(VwalletMainActivity.this, "OpenSans-Regular.ttf"));
        TextView tv3 = (TextView) tabHost.getTabWidget().getChildAt(2).findViewById(android.R.id.title);
        tv3.setAllCaps(false);
        tv3.setTypeface(Util.setTextFontOpenSans(VwalletMainActivity.this, "OpenSans-Regular.ttf"));
        tv.setTextColor(Color.parseColor("#000000"));
        tv.setAllCaps(false);
        tv.setTypeface(Util.setTextFontOpenSans(VwalletMainActivity.this, "OpenSans-Regular.ttf"));
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            public void onTabChanged(String arg0) {

                switch (tabHost.getCurrentTab())
                {
                    case 0:
                        tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.shape_left_selected);
                        tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.color.button_background);
                        tabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.shape_right);


                        break;
                    case 1:
                        tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.shape_left);
                        tabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.WHITE);
                        tabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.shape_right);

                        break;
                    case 2:
                        tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.shape_left);
                        tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.color.button_background);
                        tabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.shape_right_selected);

                        break;

                }

                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {


                    TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
                    tv.setTextColor(Color.parseColor("#ffffff"));
                }
                TextView tv = (TextView) tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).findViewById(android.R.id.title); //Unselected Tabs
                tv.setTextColor(Color.parseColor("#000000"));



            }
        });

    }
    public  void addVoucher (View v)
    {

    }
}