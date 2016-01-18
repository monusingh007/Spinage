package com.spinages.Adapters;

import android.content.Context;
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
import com.spinages.utilities.Util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by monu on 10/22/2015.
 */
public class CustomListAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> listData;
    private LayoutInflater layoutInflater;
    int activityValue;
    Context  context;

    public CustomListAdapter(Context context, ArrayList<HashMap<String, String>> listData, int ActivityDifferntiator) {
        this.listData = listData;
        this.context=context;
        activityValue = ActivityDifferntiator;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.voucher_list_row, null);

            holder = new ViewHolder();
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.date.setTypeface(Util.setTextFontRoboto(context, "Roboto-Light.ttf"));
            holder.description = (TextView) convertView.findViewById(R.id.description);
            holder.description.setTypeface(Util.setTextFontRoboto(context, "Roboto-Regular.ttf"));
            holder.location = (TextView) convertView.findViewById(R.id.offer_region);
            holder.location.setTypeface(Util.setTextFontRoboto(context, "Roboto-LightItalic.ttf"));
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            holder.imageView0 = (ImageView) convertView.findViewById(R.id.image0);
            holder.offerPrice = (TextView) convertView.findViewById(R.id.offer_price);
            holder.offerPrice.setTypeface(Util.setTextFontRoboto(context, "Roboto-LightItalic.ttf"));
            holder.layout =(RelativeLayout)convertView.findViewById(R.id.layout);
            holder.layout0 =(RelativeLayout)convertView.findViewById(R.id.layout0);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



     //   holder.compnayName.setText(listData.get(position).get("companyName").toString());
        if (holder.imageView != null) {
            ImageLoader imageLoader = ImageLoader.getInstance();

            if (listData.get(position).get("voucherType").equals("VIRTUAL")) {
                holder.layout.setVisibility(View.VISIBLE);
                holder.layout0.setVisibility(View.INVISIBLE);
                holder.date.setText(listData.get(position).get("validPeriodEnd").toString());
                holder.description.setText(listData.get(position).get("description").toString());
                holder.location.setText(listData.get(position).get("locationAvail").toString());

               imageLoader.displayImage(AppConstants.INSOMNIX_RESOURCES_URL+listData.get(position).get("companyLogo")
                       , holder.imageView,MyApplication.options);

            }
            if( listData.get(position).get("voucherType").equals("PHYSICAL"))
            {    holder.layout.setVisibility(View.INVISIBLE);
                holder.layout0.setVisibility(View.VISIBLE);
                String url = listData.get(position).get("companyLogo").toString();
                Log.e("url", url);

              imageLoader.displayImage(url, holder.imageView0, MyApplication.options);
               
            }
        }
        return convertView;
    }

    static class ViewHolder {
        RelativeLayout layout0,layout;
        TextView date;
        TextView description;
        TextView location;
        TextView compnayName;
        TextView offerPrice;
        ImageView imageView,imageView0;
    }
}