package com.fundit.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fundit.FullZoomViewActivity;
import com.fundit.R;
import com.fundit.a.W;
import com.fundit.model.OrderHistoryResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NWSPL-17 on 18-Aug-17.
 */

public class OrderProductAdapter extends BaseAdapter {

    List<OrderHistoryResponse.OrderedProducts> orderedProductses = new ArrayList<>();
    Context context;
    LayoutInflater inflater = null;
    boolean isCoupontimes = false;

    public OrderProductAdapter(List<OrderHistoryResponse.OrderedProducts> orderedProductses, Context context , boolean isCoupontimes) {
        this.orderedProductses = orderedProductses;
        this.context = context;
        this.isCoupontimes = isCoupontimes;
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

        LinearLayout layout_coupon = (LinearLayout) view.findViewById(R.id.layout_coupon);
        LinearLayout layout_main = (LinearLayout) view.findViewById(R.id.layout_main);
        ImageView img_qr = (ImageView) view.findViewById(R.id.img_qrScan) ;

       final String QRSCAN = W.BASE_URL + orderedProductses.get(position).getQr_code_img();

      final String  productName = orderedProductses.get(position).getName() + " $ " + orderedProductses.get(position).getSelling_price();


        txtItem.setText(orderedProductses.get(position).getName());
        txtQty.setText(orderedProductses.get(position).getQuantity());
        txtTotalAmt.setText("$" + orderedProductses.get(position).getSelling_price());


        if(isCoupontimes){

            layout_coupon.setVisibility(View.VISIBLE);


            Picasso.with(context)
                    .load(QRSCAN)

                    .into(img_qr);


            layout_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFullImageView(QRSCAN , productName);
                }
            });




        }



        return view;
    }


    private void showFullImageView(String qrscan , String productName) {
        Intent intent = new Intent(context, FullZoomViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("imagePaths", qrscan);
        intent.putExtra("productName", productName);
       context.startActivity(intent);
    }
}
