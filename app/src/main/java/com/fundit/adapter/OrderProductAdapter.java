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


        LinearLayout linear_remaining= (LinearLayout) view.findViewById(R.id.linear_remaining);
        LinearLayout linear_sellingprice= (LinearLayout) view.findViewById(R.id.linear_sellingprice);
        TextView txtItem = (TextView) view.findViewById(R.id.txt_item);
        TextView txt_total_qty= (TextView) view.findViewById(R.id.txt_total_qty);
        TextView txt_totalprice= (TextView) view.findViewById(R.id.txt_totalprice);
        TextView txt_remainig_qty= (TextView) view.findViewById(R.id.txt_remainig_qty);
        TextView txtQty = (TextView) view.findViewById(R.id.txt_qty);
        TextView txtTotalAmt = (TextView) view.findViewById(R.id.txt_price);
        TextView txt_sellingprice= (TextView) view.findViewById(R.id.txt_sellingprice);

        LinearLayout layout_coupon = (LinearLayout) view.findViewById(R.id.layout_coupon);
        LinearLayout layout_main = (LinearLayout) view.findViewById(R.id.layout_main);
        ImageView img_qr = (ImageView) view.findViewById(R.id.img_qrScan) ;

       final String QRSCAN = W.BASE_URL + orderedProductses.get(position).getQr_code_img();

      final String  productName = orderedProductses.get(position).getName() + " $ " + orderedProductses.get(position).getSelling_price();
      final String Name=orderedProductses.get(position).getName();
      final  String type_id=orderedProductses.get(position).getType_id();
      final String Selling_price=orderedProductses.get(position).getSelling_price();
      final String Total_qty=orderedProductses.get(position).getQuantity();
      final String Item_total=orderedProductses.get(position).getItem_total();
      final String Remaining_qty=orderedProductses.get(position).getLeft_qty();
      final String Coupon_no=orderedProductses.get(position).getCoupon_number();



     //   txtQty.setText(orderedProductses.get(position).getQuantity());
//        txtTotalAmt.setText("$" + orderedProductses.get(position).getSelling_price());


        if(isCoupontimes){

            layout_coupon.setVisibility(View.VISIBLE);


            Picasso.with(context)
                    .load(QRSCAN)

                    .into(img_qr);


            layout_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFullImageView(QRSCAN , Name,type_id,Selling_price,Total_qty,Item_total,Remaining_qty,Coupon_no);
                }
            });



            linear_sellingprice.setVisibility(View.VISIBLE);
            linear_remaining.setVisibility(View.VISIBLE);
            txtItem.setText(orderedProductses.get(position).getName());

            txt_sellingprice.setText("$" + String.format("%.2f", Double.parseDouble(orderedProductses.get(position).getSelling_price())));

            txt_total_qty.setText(orderedProductses.get(position).getQuantity());
            txt_totalprice.setText("$" + String.format("%.2f", Double.parseDouble(orderedProductses.get(position).getItem_total())));
            txt_remainig_qty.setText(orderedProductses.get(position).getLeft_qty());



        }
        else {

            linear_sellingprice.setVisibility(View.GONE);
            linear_remaining.setVisibility(View.GONE);
            txtQty.setText("QTY:");
            txtTotalAmt.setText("Total Amount:");
            txt_total_qty.setText(orderedProductses.get(position).getQuantity());
            txtItem.setText(orderedProductses.get(position).getName()+" for "+ "$" + String.format("%.2f", Double.parseDouble(orderedProductses.get(position).getSelling_price())));




            txt_totalprice.setText("$" + String.format("%.2f", Double.parseDouble(orderedProductses.get(position).getItem_total())));
            txt_remainig_qty.setText(orderedProductses.get(position).getLeft_qty());

        }



        return view;
    }


    private void showFullImageView(String qrscan , String productName,String type_id,String Selling_price,String Total_qty,String item_total,String Remaining_qty,String Coupon_no) {
        Intent intent = new Intent(context, FullZoomViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("type_id",type_id);
        intent.putExtra("imagePaths", qrscan);
        intent.putExtra("productName", productName);
        intent.putExtra("price",Selling_price);
        intent.putExtra("qty",Total_qty);
        intent.putExtra("item_total",item_total);
        intent.putExtra("remain_qty",Remaining_qty);
        intent.putExtra("coupon_no",Coupon_no);
       context.startActivity(intent);
    }
}
