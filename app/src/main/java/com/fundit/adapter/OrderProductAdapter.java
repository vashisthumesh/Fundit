package com.fundit.adapter;

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
import android.widget.Toast;

import com.fundit.FullZoomViewActivity;
import com.fundit.R;
import com.fundit.a.C;
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
    boolean isExpired=false;

    public OrderProductAdapter(List<OrderHistoryResponse.OrderedProducts> orderedProductses, Context context , boolean isCoupontimes,boolean isExpired) {
        this.orderedProductses = orderedProductses;
        this.context = context;
        this.isCoupontimes = isCoupontimes;
        this.isExpired=isExpired;
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

        Log.e("isexp_adap","---"+isExpired);

        TextView txtItem = (TextView) view.findViewById(R.id.txt_item);
        TextView txt_selling_price= (TextView) view.findViewById(R.id.txt_sellingprice);
        TextView txtQty = (TextView) view.findViewById(R.id.txt_qty);
        TextView txtTotalAmt = (TextView) view.findViewById(R.id.txt_totalAmt);
        TextView txt_remainig_qty= (TextView) view.findViewById(R.id.txt_remainig_qty);
        TextView txtTotalprice= (TextView) view.findViewById(R.id.txt_totalprice);
        TextView txt_total_qty= (TextView) view.findViewById(R.id.txt_total_qty);
        LinearLayout linear_amount= (LinearLayout) view.findViewById(R.id.linear_amount);
        LinearLayout linear_total_price= (LinearLayout) view.findViewById(R.id.linear_total_price);
        LinearLayout linear_sellingprice= (LinearLayout) view.findViewById(R.id.linear_sellingprice);
        LinearLayout linear_total_qty= (LinearLayout) view.findViewById(R.id.linear_total_qty);
        LinearLayout linear_qty= (LinearLayout) view.findViewById(R.id.linear_qty);
        LinearLayout linear_remaining= (LinearLayout) view.findViewById(R.id.linear_remaining);
        LinearLayout linear_reedem= (LinearLayout) view.findViewById(R.id.linear_redeem);
        TextView txt_reedem= (TextView) view.findViewById(R.id.txt_reedem);
        LinearLayout linear_item= (LinearLayout) view.findViewById(R.id.linear_item);
        LinearLayout linear_gift_card= (LinearLayout) view.findViewById(R.id.linear_gift_card);
        TextView txt_gift_card= (TextView) view.findViewById(R.id.txt_gift_card);
        LinearLayout linear_gift_card_value= (LinearLayout) view.findViewById(R.id.linear_gift_card_value);
        TextView txt_gift_card_value= (TextView) view.findViewById(R.id.txt_gift_card_value);
      LinearLayout  linear_gift_card_qty= (LinearLayout) view.findViewById(R.id.linear_gift_card_qty);
        TextView txt_gift_card_qty= (TextView) view.findViewById(R.id.txt_gift_card_qty);

        LinearLayout linear_total_gift_value= (LinearLayout) view.findViewById(R.id.linear_total_gift_value);
        TextView txt_totalgift_value= (TextView) view.findViewById(R.id.txt_totalgift_value);

        LinearLayout linear_remaining_money= (LinearLayout) view.findViewById(R.id.linear_remaining_money);
        TextView txt_remainig_money= (TextView) view.findViewById(R.id.txt_remainig_money);




        LinearLayout layout_coupon = (LinearLayout) view.findViewById(R.id.layout_coupon);
        LinearLayout layout_main = (LinearLayout) view.findViewById(R.id.layout_main);
        ImageView img_qr = (ImageView) view.findViewById(R.id.img_qrScan) ;

       final String QRSCAN = W.BASE_URL + orderedProductses.get(position).getQr_code_img();

      final String  productName = orderedProductses.get(position).getName();
        Double x1=Double.parseDouble(orderedProductses.get(position).getItem_total());
        Double x2=Double.parseDouble(orderedProductses.get(position).getSelling_price());
        Double x3=Double.parseDouble(orderedProductses.get(position).getLeft_money());

       final String price=" $ " + String.format("%.2f", x2);
        //orderedProductses.get(position).getSelling_price();
        final String qty=orderedProductses.get(position).getQuantity();
        final  String item_total=String.format("%.2f", x1);
        //orderedProductses.get(position).getItem_total();
        final  String remain_qty=orderedProductses.get(position).getLeft_qty();

        final  String remain_money=String.format("%.2f",x3);




        final String type_id=orderedProductses.get(position).getType_id();







        if(isCoupontimes){

            layout_coupon.setVisibility(View.VISIBLE);

            linear_qty.setVisibility(View.GONE);
            txtQty.setVisibility(View.GONE);
            linear_amount.setVisibility(View.GONE);
            txtTotalAmt.setVisibility(View.GONE);



            String coupon_status=orderedProductses.get(position).getCoupon_status();

            if(isExpired)
            {
                layout_main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        C.INSTANCE.showToast(context , "Sorry,Coupon Expired");
                    }
                });

            }
            else {

                if(coupon_status.equalsIgnoreCase("0"))
                {
                    Picasso.with(context)
                            .load(QRSCAN)

                            .into(img_qr);


                    layout_main.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            showFullImageView(QRSCAN , productName,price,qty,item_total,remain_qty,type_id,remain_money);
                        }
                    });

                }
                else {
                    layout_main.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            C.INSTANCE.showToast(context , "Sorry,Coupon Expired");
                        }
                    });

                }

            }


            if(type_id.equalsIgnoreCase("1"))
            {
                linear_item.setVisibility(View.VISIBLE);
                txtItem.setVisibility(View.VISIBLE);
                txtItem.setText(orderedProductses.get(position).getName());
                linear_gift_card.setVisibility(View.GONE);
                txt_gift_card.setVisibility(View.GONE);
                linear_sellingprice.setVisibility(View.VISIBLE);
                txt_selling_price.setVisibility(View.VISIBLE);
                Double y=Double.parseDouble(orderedProductses.get(position).getSelling_price());
                txt_selling_price.setText("$" +String.format("%.2f", y));
                linear_gift_card_value.setVisibility(View.GONE);
                txt_gift_card_value.setVisibility(View.GONE);
                linear_total_qty.setVisibility(View.VISIBLE);
                txt_total_qty.setVisibility(View.VISIBLE);
                txt_total_qty.setText(orderedProductses.get(position).getQuantity());
                linear_gift_card_qty.setVisibility(View.GONE);
                txt_gift_card_qty.setVisibility(View.GONE);

                linear_total_price.setVisibility(View.VISIBLE);
                txtTotalprice.setVisibility(View.VISIBLE);
                Double x=Double.parseDouble(orderedProductses.get(position).getItem_total());
                txtTotalprice.setText("$" +String.format("%.2f", x));
                linear_total_gift_value.setVisibility(View.GONE);
                txt_totalgift_value.setVisibility(View.GONE);

                linear_remaining_money.setVisibility(View.GONE);
                txt_remainig_money.setVisibility(View.GONE);




                if(coupon_status.equalsIgnoreCase("1"))
                {
                    linear_reedem.setVisibility(View.VISIBLE);
                    txt_reedem.setVisibility(View.VISIBLE);
                    linear_remaining.setVisibility(View.GONE);
                    txt_remainig_qty.setVisibility(View.GONE);

                }
                else {
                    linear_reedem.setVisibility(View.GONE);
                    txt_reedem.setVisibility(View.GONE);

                    linear_remaining.setVisibility(View.VISIBLE);
                    txt_remainig_qty.setVisibility(View.VISIBLE);
                    txt_remainig_qty.setText(orderedProductses.get(position).getLeft_qty());


                }

            }
            else if(type_id.equalsIgnoreCase("2"))
            {
                linear_item.setVisibility(View.GONE);
                txtItem.setVisibility(View.GONE);
                linear_gift_card.setVisibility(View.VISIBLE);
                txt_gift_card.setVisibility(View.VISIBLE);
                txt_gift_card.setText(orderedProductses.get(position).getName());
                linear_sellingprice.setVisibility(View.GONE);
                txt_selling_price.setVisibility(View.GONE);
                linear_gift_card.setVisibility(View.VISIBLE);
                linear_gift_card_value.setVisibility(View.VISIBLE);
                txt_gift_card_value.setVisibility(View.VISIBLE);
                Double x=Double.parseDouble(orderedProductses.get(position).getSelling_price());
                txt_gift_card_value.setText("$" +String.format("%.2f", x));
                linear_total_qty.setVisibility(View.GONE);
                txt_total_qty.setVisibility(View.GONE);
                linear_gift_card_qty.setVisibility(View.VISIBLE);
                txt_gift_card_qty.setVisibility(View.VISIBLE);
                txt_gift_card_qty.setText(orderedProductses.get(position).getQuantity());

                linear_total_price.setVisibility(View.GONE);
                txtTotalprice.setVisibility(View.GONE);

                linear_total_gift_value.setVisibility(View.VISIBLE);
                txt_totalgift_value.setVisibility(View.VISIBLE);
                Double y=Double.parseDouble(orderedProductses.get(position).getItem_total());
                txt_totalgift_value.setText("$" +String.format("%.2f", y));

                linear_remaining.setVisibility(View.GONE);
                txt_remainig_qty.setVisibility(View.GONE);

                if(coupon_status.equalsIgnoreCase("1"))
                {
                    linear_reedem.setVisibility(View.VISIBLE);
                    txt_reedem.setVisibility(View.VISIBLE);
                    linear_remaining_money.setVisibility(View.GONE);
                    txt_remainig_money.setVisibility(View.GONE);


                }
                else {
                    linear_reedem.setVisibility(View.GONE);
                    txt_reedem.setVisibility(View.GONE);

                    linear_remaining_money.setVisibility(View.VISIBLE);
                    txt_remainig_money.setVisibility(View.VISIBLE);
                    Double d=Double.parseDouble(orderedProductses.get(position).getLeft_money());
                    txt_remainig_money.setText("$" +String.format("%.2f", d));





                }


            }





        }
        else {
            linear_sellingprice.setVisibility(View.GONE);
            txt_selling_price.setVisibility(View.GONE);
            linear_total_qty.setVisibility(View.GONE);
            txt_total_qty.setVisibility(View.GONE);


            linear_total_price.setVisibility(View.GONE);
            txtTotalprice.setVisibility(View.GONE);
            linear_remaining.setVisibility(View.GONE);

            linear_remaining_money.setVisibility(View.GONE);
            txt_remainig_money.setVisibility(View.GONE);
            txt_remainig_qty.setVisibility(View.GONE);
            linear_reedem.setVisibility(View.GONE);
            txt_reedem.setVisibility(View.GONE);

            linear_gift_card_value.setVisibility(View.GONE);
            txt_gift_card_value.setVisibility(View.GONE);

            if(type_id.equalsIgnoreCase("1"))
            {
                linear_item.setVisibility(View.VISIBLE);
                txtItem.setVisibility(View.VISIBLE);
                txtItem.setText(orderedProductses.get(position).getName());
                linear_gift_card.setVisibility(View.GONE);
                txt_gift_card.setVisibility(View.GONE);
                linear_qty.setVisibility(View.VISIBLE);
                txtQty.setVisibility(View.VISIBLE);
                txtQty.setText(orderedProductses.get(position).getQuantity());
                linear_gift_card_qty.setVisibility(View.GONE);
                txt_gift_card_qty.setVisibility(View.GONE);

                linear_amount.setVisibility(View.VISIBLE);
                txtTotalAmt.setVisibility(View.VISIBLE);

                Double x=Double.parseDouble(orderedProductses.get(position).getItem_total());

                txtTotalAmt.setText("$" +String.format("%.2f", x));
                linear_total_gift_value.setVisibility(View.GONE);
                txt_totalgift_value.setVisibility(View.GONE);



            }
            else if(type_id.equalsIgnoreCase("2"))
            {
                linear_item.setVisibility(View.GONE);
                txtItem.setVisibility(View.GONE);
                linear_gift_card.setVisibility(View.VISIBLE);
                txt_gift_card.setVisibility(View.VISIBLE);
                txt_gift_card.setText(orderedProductses.get(position).getName());
                linear_qty.setVisibility(View.GONE);
                txtQty.setVisibility(View.GONE);
                linear_gift_card_qty.setVisibility(View.VISIBLE);
                txt_gift_card_qty.setVisibility(View.VISIBLE);
                txt_gift_card_qty.setText(orderedProductses.get(position).getQuantity());

                linear_amount.setVisibility(View.GONE);
                txtTotalAmt.setVisibility(View.GONE);

                linear_total_gift_value.setVisibility(View.VISIBLE);
                txt_totalgift_value.setVisibility(View.VISIBLE);
                Double y=Double.parseDouble(orderedProductses.get(position).getItem_total());
                txt_totalgift_value.setText("$"  +String.format("%.2f", y));





            }
        }



        return view;
    }


    private void showFullImageView(String qrscan , String productName,String price,String qty,String item_total,String remain_qty, String type_id,String remaining_money ) {
        Intent intent = new Intent(context, FullZoomViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("type_id",type_id);
        intent.putExtra("imagePaths", qrscan);
        intent.putExtra("productName", productName);
        intent.putExtra("price",price);
        intent.putExtra("qty",qty);
        intent.putExtra("item_total",item_total);
        intent.putExtra("remain_qty",remain_qty);
        intent.putExtra("remain_money",remaining_money);
       context.startActivity(intent);
    }
}
