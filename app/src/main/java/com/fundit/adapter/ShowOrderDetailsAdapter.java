package com.fundit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fundit.CardPaymentActivity;
import com.fundit.HomeActivity;
import com.fundit.OrderHistoryDetail;
import com.fundit.OrderPlaceActivity;
import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.AppModel;
import com.fundit.model.CampaignListResponse;
import com.fundit.model.OrderHistoryResponse;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NWSPL-17 on 12-Aug-17.
 */

public class ShowOrderDetailsAdapter extends BaseAdapter {

    List<OrderHistoryResponse.OrderList> orderLists = new ArrayList<>();
    Activity activity;
    LayoutInflater inflater;
    AppPreference preference;
    CustomDialog dialog;
    Context context;
    AdminAPI adminAPI;
    Date date;
    Date startdate, enddate;
    String end_date;
    Date today;

    public ShowOrderDetailsAdapter(List<OrderHistoryResponse.OrderList> campaignLists, Activity activity) {
        this.orderLists = campaignLists;
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
        this.context = activity.getApplication();
        this.adminAPI=ServiceGenerator.getAPIClass();

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


        final View view = inflater.inflate(R.layout.layout_order_coupan, parent, false);
        ArrayList<String> productsNames = new ArrayList<>();

        LinearLayout layout_from= (LinearLayout) view.findViewById(R.id.layout_from);
        LinearLayout layout_date= (LinearLayout) view.findViewById(R.id.layout_date);
        LinearLayout layout_camp= (LinearLayout) view.findViewById(R.id.layout_camp);
        LinearLayout layout_maincoupon= (LinearLayout) view.findViewById(R.id.layout_maincoupon);
        TextView txt_organization= (TextView) view.findViewById(R.id.txt_organization);
        TextView txt_fundspot = (TextView) view.findViewById(R.id.txt_fundspot);
        TextView txt_campaign = (TextView) view.findViewById(R.id.txt_campaign);
        TextView txt_address = (TextView) view.findViewById(R.id.txt_address);
        TextView txt_exp_date = (TextView) view.findViewById(R.id.txt_expdate);
        TextView txt_item = (TextView) view.findViewById(R.id.txt_item);
        TextView txt_coupons = (TextView) view.findViewById(R.id.txt_coupons);
        TextView txt_amt= (TextView) view.findViewById(R.id.txt_amt);
        TextView txt_sendfrom= (TextView) view.findViewById(R.id.txt_sendfrom);

        ImageView img_arrow = (ImageView) view.findViewById(R.id.img_arrow);
        ImageView img_delete= (ImageView) view.findViewById(R.id.img_delete);

        LinearLayout layout_coupon = (LinearLayout) view.findViewById(R.id.layout_coupon);
        final LinearLayout layout_request = (LinearLayout) view.findViewById(R.id.layout_request);
        final LinearLayout layout_couponmain = (LinearLayout) view.findViewById(R.id.layout_main);
        layout_coupon.setVisibility(View.VISIBLE);

        Button btn_accept = (Button) view.findViewById(R.id.btn_accept);
        Button btn_decline = (Button) view.findViewById(R.id.btn_decline);




        Calendar c = Calendar.getInstance();

        // set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        today = c.getTime();



        txt_amt.setText("$" + String.format("%.2f",Double.parseDouble(orderLists.get(position).getOrder().getTotal())));
        txt_fundspot.setText(orderLists.get(position).getFundspot().getTitle());
        txt_campaign.setText(orderLists.get(position).getCampaign().getTitle());
        txt_address.setText(orderLists.get(position).getFundspot().getAddress());
        txt_organization.setText(orderLists.get(position).getOrganization().getTitle());


        String getExpDate = orderLists.get(position).getOrder().getCoupon_expiry_date();
        Log.e("expdate",getExpDate);
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd k:mm:ss");
        try {
            date = simpleDateFormat.parse(getExpDate);
            Log.e("date","---->"+date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        txt_exp_date.setText(new SimpleDateFormat("MM/dd/yyyy").format(date));
        Log.e("date1","---->"+new SimpleDateFormat("MM/dd/yyyy").format(date));
        txt_coupons.setText(orderLists.get(position).getOrder().getTotal_coupon_count());

        if (orderLists.get(position).getOrder().getOrder_request().equalsIgnoreCase("1")) {
            layout_from.setVisibility(View.VISIBLE);
            layout_date.setVisibility(View.GONE);
            layout_camp.setVisibility(View.GONE);
            txt_sendfrom.setText("Pending request from "+orderLists.get(position).getOrder().getRequested_user_name());
            layout_request.setVisibility(View.VISIBLE);
            layout_maincoupon.setBackground(activity.getResources().getDrawable(R.drawable.listviewbackcolor));
            img_delete.setVisibility(View.GONE);



        }


        for (int i = 0; i < orderLists.get(position).getOrderProduct().size(); i++) {
            productsNames.add(orderLists.get(position).getOrderProduct().get(i).getName());
        }

        String getProductName = productsNames.toString().trim();
        getProductName = getProductName.replaceAll("\\[", "").replaceAll("\\(", "").replaceAll("\\]", "").replaceAll("\\)", "");
        txt_item.setText(getProductName);
//        img_arrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, OrderHistoryDetail.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("details", orderLists.get(position));
//                intent.putExtra("couponTimes", true);
//
//
//                context.startActivity(intent);
//            }
//        });



         end_date = orderLists.get(position).getOrder().getCoupon_expiry_date();


        final SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            enddate = simpleDateFormat1.parse(end_date);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (enddate.before(today)) {

                    dialog = new CustomDialog(activity, "");
                    dialog.setCancelable(false);
                    dialog.show();

                    Call<AppModel> appModelCall = adminAPI.DeleteCoupon(orderLists.get(position).getOrder().getId());
                    Log.e("parameters", "--?" + orderLists.get(position).getOrder().getId());
                    appModelCall.enqueue(new Callback<AppModel>() {
                        @Override
                        public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                            dialog.dismiss();
                            AppModel model = response.body();
                            Log.e("response", "-->" + new Gson().toJson(response.body()));
                            if (model != null) {
                                C.INSTANCE.showToast(context, model.getMessage());
                                if (model.isStatus()) {
                                    orderLists.remove(position);
                                    Log.e("check", "-->" + position);

                                }
                            } else {
                                C.INSTANCE.defaultError(context);

                            }

                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<AppModel> call, Throwable t) {
                            dialog.dismiss();
                            C.INSTANCE.errorToast(context, t);
                        }
                    });

                }
                else {
                    C.INSTANCE.showToast(activity, "Sorry, You can't delete this coupon till expired");
                }







            }
        });

        layout_maincoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, OrderHistoryDetail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("details", orderLists.get(position));
                intent.putExtra("couponTimes", true);
                if (orderLists.get(position).getOrder().getOrder_request().equalsIgnoreCase("1")) {
                    intent.putExtra("accept",true);
                    intent.putExtra("flag" , true);
                }
                else {
                    intent.putExtra("accept",true);
                }
                context.startActivity(intent);
            }
        });


        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderHistoryDetail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("details", orderLists.get(position));
                intent.putExtra("accept",true);
                intent.putExtra("flag" , true);
                intent.putExtra("couponTimes", true);
                context.startActivity(intent);



            }
        });


        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderLists.remove(position);
                DeclineCouponRequest(position, view);


            }

            private void DeclineCouponRequest(final int position, final View view) {
                preference = new AppPreference(activity);
                dialog = new CustomDialog(activity, "");
                dialog.setCancelable(false);
                dialog.show();
                Call<AppModel> appModelCall = adminAPI.DeclineCoupon(orderLists.get(position).getOrder().getId(), preference.getUserID(), "0");
                Log.e("parameters", "--?" + orderLists.get(position).getOrder().getId() + "-->" + preference.getUserID());
                appModelCall.enqueue(new Callback<AppModel>() {
                    @Override
                    public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                        dialog.dismiss();
                        AppModel model = response.body();
                        Log.e("response", "-->" + new Gson().toJson(response.body()));
                        if (model != null) {
                            C.INSTANCE.showToast(context, model.getMessage());
                            if (model.isStatus()) {

                                Log.e("check", "-->" + position);

                            }
                        } else {
                            C.INSTANCE.defaultError(context);

                        }

                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<AppModel> call, Throwable t) {
                        dialog.dismiss();
                        C.INSTANCE.errorToast(context, t);
                    }
                });


            }
        });


        return view;
    }


}
