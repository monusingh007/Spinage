package com.spinages.app;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spinages.webservices.URLconnectionService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.ResponseCache;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by Naxtre on 7/20/15.
 */
public class AdapterInterestPage extends RecyclerView.Adapter<AdapterInterestPage.ViewHolder> {
    Context mContext;
    JSONObject response;
    ArrayList<String> mList;
    ArrayList<String> idList;
    int ItemCount;
    boolean isColorChanged = false;
    boolean[] mBooleans;


    public AdapterInterestPage(Context contex,ArrayList<String>cat_list,ArrayList<String>id_list,int listSize)
    {  InterestPage.mArrayList.clear();
        idList=id_list;
        mList=cat_list;
        ItemCount=listSize;
        mContext = contex;
        mBooleans= new boolean[ItemCount];
        for(int i =0;i<listSize;i++)
        {
            mBooleans[i]=false;
        }
    }

    public AdapterInterestPage(Context context) {
        mContext = context;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mInterestedCategoryTxt;
        public RelativeLayout mInterestRel;
        public CheckBox mInterestedCategoryRadio;


        public ViewHolder(View view) {
            super(view);

            mInterestedCategoryTxt = (TextView) view.findViewById(R.id.adapter_interest_radiotext);
            mInterestRel = (RelativeLayout) view.findViewById(R.id.adapter_interest_relative);
            mInterestedCategoryRadio = (CheckBox) view.findViewById(R.id.adapter_interest_radio);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        View mView = mInflater.inflate(R.layout.adapter_inerest_page, viewGroup,false);

        ViewHolder mHolder = new ViewHolder(mView);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

try {
    viewHolder.mInterestedCategoryTxt.setText(mList.get(i));

        viewHolder.mInterestRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           if (!mBooleans[i]) {
                    viewHolder.mInterestRel.setBackgroundResource(R.drawable.navigation_bg);
                    viewHolder.mInterestedCategoryTxt.setTextColor(Color.parseColor("#c1392b"));

                    mBooleans[i] = true;
                    viewHolder.mInterestedCategoryRadio.setChecked(true);
                    InterestPage.mArrayList.add(mList.get(i));
                    InterestPage.id_mArrayList.add((idList.get(i)));
                } else {
                    viewHolder.mInterestRel.setBackgroundColor(Color.WHITE);
                    viewHolder.mInterestedCategoryTxt.setTextColor(Color.parseColor("#FF616568"));
                    mBooleans[i] = false;
                    if (InterestPage.mArrayList.contains(mList.get(i))) {

                        viewHolder.mInterestedCategoryRadio.setChecked(false);
                       InterestPage.mArrayList.remove(mList.get(i));
                        InterestPage.id_mArrayList.remove((idList.get(i)));
                    }
                }
            }
        });
}catch (Exception e)
{



    e.printStackTrace();
}
    }

    @Override
    public int getItemCount() {
        return ItemCount;}

}
