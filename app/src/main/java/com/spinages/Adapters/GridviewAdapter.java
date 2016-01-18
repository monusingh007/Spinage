package com.spinages.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.spinages.app.AppConstants;
import com.spinages.app.MyApplication;
import com.spinages.app.R;
import com.spinages.vWallet.HistoryPhysicalDetails;
import com.spinages.vWallet.HistoryVoucherDetails;
import com.spinages.vWallet.VoucherDetails;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by monu on 11/25/2015.
 */
public class GridviewAdapter extends BaseAdapter {

    private Context mContext;
    private int listsize;
    ArrayList<HashMap<String, String>> listData;
    public GridviewAdapter(Context context, ArrayList<HashMap<String, String>> voucher_list) {
        mContext = context;
        this.listData = voucher_list;
        listsize=voucher_list.size();
    }

    @Override
    public int getCount() {
        return listsize ;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // Convert DP to PX
    // Source: http://stackoverflow.com/a/8490361
   /* public int dpToPx(int dps) {
        final float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);

        return pixels;
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos= position;
        ImageView imageView;
        TextView time , date;
        RelativeLayout layout;
        int imageID = 0;

        // Want the width/height of the items
        // to be 120dp




        if (convertView == null) {
            // If convertView is null then inflate the appropriate layout file
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_layout_file, null);
        }
        else {

        }
layout=(RelativeLayout)convertView.findViewById(R.id.layout);
        imageView = (ImageView) convertView.findViewById(R.id.grid_image);
        date = (TextView)convertView.findViewById(R.id.date);
       time = (TextView)convertView.findViewById(R.id.time);


        // Set height and width constraints for the image view

layout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(listData.get(pos).get("voucherType").equals("VIRTUAL"))
        {  Intent intent = new Intent();
        intent.putExtra("activity","History");
        intent.putExtra("position",pos);
        intent.putExtra("arrayList",listData);
        intent.setClass(mContext, HistoryVoucherDetails.class);
        mContext.startActivity(intent);
        }
        if(listData.get(pos).get("voucherType").equals("PHYSICAL"))
        { Intent intent = new Intent();
            intent.putExtra("activity","History");
            intent.putExtra("position",pos);
            intent.putExtra("arrayList",listData);
            intent.setClass(mContext, HistoryPhysicalDetails.class);
            mContext.startActivity(intent);

        }
    }
});
        // Image should be cropped towards the center
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // Set Padding for images
        imageView.setPadding(4, 4, 4, 4);

        // Crop the image to fit within its padding
       // imageView.setCropToPadding(true);
        if (listData.get(position).get("voucherType").equals("VIRTUAL")) {
            date.setText(listData.get(position).get("validPeriodEnd").toString());
            time.setText(listData.get(position).get("validTimeTo").toString());
           // holder.description.setText(listData.get(position).get("description").toString());
          //  holder.location.setText(listData.get(position).get("locationAvail").toString());

            ImageLoader.getInstance().displayImage(AppConstants.INSOMNIX_RESOURCES_URL + listData.get(position).get("companyLogo")
                    , imageView, MyApplication.options);

        } if( listData.get(position).get("voucherType").equals("PHYSICAL"))
        {
            String url = AppConstants.INSOMNIX_RESOURCES_URL+listData.get(position).get("companyLogo").toString();
            Log.e("url", url);

           ImageLoader.getInstance().displayImage(url, imageView, MyApplication.options);


        }

        return convertView;
    }
}
