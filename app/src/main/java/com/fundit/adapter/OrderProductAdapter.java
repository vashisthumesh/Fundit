package com.fundit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fundit.R;
import com.fundit.model.OrderHistoryResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NWSPL-17 on 18-Aug-17.
 */

public class OrderProductAdapter extends BaseAdapter {

    List<OrderHistoryResponse.OrderedProducts> orderedProductses = new ArrayList<>();
    Context context;
    LayoutInflater inflater = null;

    public OrderProductAdapter(List<OrderHistoryResponse.OrderedProducts> orderedProductses, Context context) {
        this.orderedProductses = orderedProductses;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return orderedProductses.size();
    }

    @Override
    public Object getItem(int position) {
        return orderedProductses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = inflater.inflate(R.layout.layout_order_products , parent , false);

        TextView txtItem = (TextView) view.findViewById(R.id.txt_item);
        TextView txtQty = (TextView) view.findViewById(R.id.txt_qty);
        TextView txtTotalAmt = (TextView) view.findViewById(R.id.txt_totalAmt);


        txtItem.setText(orderedProductses.get(position).getName());
        txtQty.setText(orderedProductses.get(position).getQuantity());
        txtTotalAmt.setText(orderedProductses.get(position).getSelling_price());



        return view;
    }
}
