package com.fundit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fundit.OrderHistoryDetail;
import com.fundit.R;
import com.fundit.model.OrderHistoryResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NWSPL-17 on 18-Aug-17.
 */

public class OrderHistoryAdapter extends BaseAdapter{

    List<OrderHistoryResponse.OrderList> orderLists = new ArrayList<>();
    Activity activity;
    LayoutInflater inflater = null;
    Context context;


    public OrderHistoryAdapter(List<OrderHistoryResponse.OrderList> orderLists, Activity activity) {
        this.orderLists = orderLists;
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


        View view = inflater.inflate(R.layout.layout_order_list , parent , false);

        TextView txt_orderId= (TextView) view.findViewById(R.id.txt_orderId);
        TextView txt_totalAmt= (TextView) view.findViewById(R.id.txt_totalAmt);
        TextView txt_date= (TextView) view.findViewById(R.id.txt_date);

        ImageView img_arrow = (ImageView) view.findViewById(R.id.img_arrow);

        txt_orderId.setText(orderLists.get(position).getOrder().getId());
        txt_totalAmt.setText(orderLists.get(position).getOrder().getTotal());
        txt_date.setText(orderLists.get(position).getOrder().getCreated());

        img_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , OrderHistoryDetail.class);
                intent.putExtra("details" , orderLists.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
