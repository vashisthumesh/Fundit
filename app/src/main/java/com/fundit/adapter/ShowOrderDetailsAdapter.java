package com.fundit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fundit.HomeActivity;
import com.fundit.OrderHistoryDetail;
import com.fundit.OrderPlaceActivity;
import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.model.CampaignListResponse;
import com.fundit.model.OrderHistoryResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by NWSPL-17 on 12-Aug-17.
 */

public class ShowOrderDetailsAdapter extends BaseAdapter {

    List<OrderHistoryResponse.OrderList> orderLists = new ArrayList<>();
    Activity activity;
    LayoutInflater inflater;
    AppPreference preference;
    Context context;
    Date date;


    public ShowOrderDetailsAdapter(List<OrderHistoryResponse.OrderList> campaignLists, Activity activity) {
        this.orderLists = campaignLists;
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
        this.context = activity.getApplication();

    }

    @Override
    public int getCount() {
        return orderLists.size();
    }

    @Override
    public Object getItem(int position) {
        return orderLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        preference = new AppPreference(context);

        View view = inflater.inflate(R.layout.layout_order_list, parent, false);

        TextView txt_orderId= (TextView) view.findViewById(R.id.txt_orderId);
        TextView txt_totalAmt= (TextView) view.findViewById(R.id.txt_totalAmt);
        TextView txt_date= (TextView) view.findViewById(R.id.txt_date);
        TextView txt_coupon= (TextView) view.findViewById(R.id.txt_coupon);

        ImageView img_arrow = (ImageView) view.findViewById(R.id.img_arrow);

        LinearLayout layout_coupon = (LinearLayout) view.findViewById(R.id.layout_coupon);
        layout_coupon.setVisibility(View.VISIBLE);


        txt_orderId.setText(orderLists.get(position).getOrder().getId());
        txt_totalAmt.setText(orderLists.get(position).getOrder().getTotal());

        String getCreatedDate = orderLists.get(position).getOrder().getCreated();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd k:mm:ss");
        try {
            date = simpleDateFormat.parse(getCreatedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        txt_date.setText(new SimpleDateFormat("dd-MMM-yyyy").format(date));
        txt_coupon.setText(orderLists.get(position).getOrder().getTotal_coupon_count());


        img_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , OrderHistoryDetail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("details" , orderLists.get(position));
                intent.putExtra("couponTimes" , true);


                context.startActivity(intent);
            }
        });





        return view;
    }



}
