package com.fundit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.W;
import com.fundit.adapter.OrderProductAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.BankCardResponse;
import com.fundit.model.Member;
import com.fundit.model.OrderHistoryResponse;
import com.fundit.model.User;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryDetail extends AppCompatActivity {

    AppPreference preference;
    AdminAPI adminAPI;
    CustomDialog dialog;

    ListView listProducts;
    Button btnCoupon;
    Date date1;

    TextView txt_recipient , txt_campaignName , txt_fundspotName , txt_organizations , txt_date , txt_product,txt_history_address,txt_coupon_exp_date,txt_expired,txt_payment_method,payment_ref_no,payment_code,txt_expiry_date,txy_expired;

    OrderHistoryResponse.OrderList historyResponse;


    List<OrderHistoryResponse.OrderedProducts> products = new ArrayList<>();

    OrderProductAdapter productAdapter;
    boolean pending=false;

    boolean isCouponTimes = false;
    boolean isExpired=false;
    boolean isaccept=false;
    boolean flag=false;
    LinearLayout layout_coupon ,linear_payment,linear_address,linear_resend,linear_ref,linear_code,linear_expiration,linear_expiry,linear_expired,layout_camp,date_sold;
    ImageView qrScan;
    String QRSCAN = "";
    String productName = "";
    Date date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_detail);

        preference = new AppPreference(getApplicationContext());
        adminAPI = ServiceGenerator.getAPIClass();
        dialog = new CustomDialog(OrderHistoryDetail.this);

        Intent intent = getIntent();

        historyResponse = (OrderHistoryResponse.OrderList) intent.getSerializableExtra("details");
        isCouponTimes = intent.getBooleanExtra("couponTimes" , false);
        isaccept=intent.getBooleanExtra("accept",false);
        flag=intent.getBooleanExtra("flag",false);
        pending=intent.getBooleanExtra("pending",false);

       setupToolBar();
        fetchIDs();
    }
    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if(isCouponTimes){
            Log.e("iscoupan1","--->"+isCouponTimes);
            actionTitle.setText("My Coupons");
        }
        else {
            Log.e("iscoupan1","--->"+isCouponTimes);
            actionTitle.setText("Order History");

        }




        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void fetchIDs() {

        txt_recipient = (TextView) findViewById(R.id.txt_recipient);
        txt_campaignName = (TextView) findViewById(R.id.txt_campaignName);
        txt_fundspotName = (TextView) findViewById(R.id.txt_fundspotName);
        txt_organizations = (TextView) findViewById(R.id.txt_organizations);
        txt_date = (TextView) findViewById(R.id.txt_date);
        txt_product = (TextView) findViewById(R.id.txt_product);
        txt_history_address= (TextView) findViewById(R.id.txt_history_address);
        txt_coupon_exp_date= (TextView) findViewById(R.id.txt_coupon_exp_date);
        txt_expired= (TextView) findViewById(R.id.text_expired);
        txt_payment_method= (TextView) findViewById(R.id.txt_payment_method);
        linear_payment= (LinearLayout) findViewById(R.id.linear_payment);
        linear_address= (LinearLayout) findViewById(R.id.linear_address);
        linear_resend= (LinearLayout) findViewById(R.id.linear_resend);
        linear_code= (LinearLayout) findViewById(R.id.linear_payment_code);
        linear_ref= (LinearLayout) findViewById(R.id.linear_payment_refno);
        payment_ref_no= (TextView) findViewById(R.id.txt_payment_refno);
        payment_code= (TextView) findViewById(R.id.txt_payment_code);
        linear_expiration= (LinearLayout) findViewById(R.id.linear_expiration);
        linear_expiry= (LinearLayout) findViewById(R.id.linear_expiry);
        txt_expiry_date= (TextView) findViewById(R.id.txt_coupon_expiry_date);
        txt_expired= (TextView) findViewById(R.id.text_expired);
        linear_expired= (LinearLayout) findViewById(R.id.linear_expired);
        layout_camp= (LinearLayout) findViewById(R.id.layout_camp);
        date_sold= (LinearLayout) findViewById(R.id.datesold);



        btnCoupon = (Button) findViewById(R.id.btn_coupon);

        listProducts = (ListView) findViewById(R.id.list_products);


        if(isCouponTimes){
            Log.e("iscoupan","--->"+isCouponTimes);
            txt_history_address.setVisibility(View.VISIBLE);
            linear_address.setVisibility(View.VISIBLE);
            txt_history_address.setText(historyResponse.getFundspot().getAddress());
            txt_payment_method.setVisibility(View.GONE);
            linear_payment.setVisibility(View.GONE);


            payment_code.setVisibility(View.GONE);
            payment_ref_no.setVisibility(View.GONE);
            linear_ref.setVisibility(View.GONE);
            linear_code.setVisibility(View.GONE);

            txt_coupon_exp_date.setVisibility(View.VISIBLE);



            if(flag == true)
            {

                linear_expiration.setVisibility(View.GONE);
                layout_camp.setVisibility(View.GONE);
                linear_expired.setVisibility(View.GONE);

                if(isaccept == true)
                {
                    linear_resend.setVisibility(View.VISIBLE);
                    btnCoupon.setVisibility(View.VISIBLE);
                    btnCoupon.setText("Confirm");
                    btnCoupon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           /* Intent intent = new Intent(getApplicationContext() , CardPaymentActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("isCouponTimes" , true);
                            intent.putExtra("orderId" , historyResponse.getOrder().getId());
                            startActivity(intent);
*/

                            GoForNext();


                        }
                    });

                }
                else {
                    linear_resend.setVisibility(View.GONE);
                    btnCoupon.setVisibility(View.GONE);
                }
            }
            else {


                String getDate = historyResponse.getOrder().getCoupon_expiry_date();
                String output = getDate.substring(0, 10);

                try {
                    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    date1 = sdf.parse(output);
                } catch (final ParseException e) {
                    e.printStackTrace();
                }

                Calendar c = Calendar.getInstance();

                // set the calendar to start of today
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);

                // and get that as a Date
                Date today = c.getTime();
                Log.e("today_date",today.toString());

                linear_expiration.setVisibility(View.VISIBLE);
                layout_camp.setVisibility(View.VISIBLE);
                if(date1.before(today)) {
                    linear_expired.setVisibility(View.VISIBLE);
                    txt_expired.setVisibility(View.VISIBLE);
                    isExpired=true;
                    //  productAdapter.notifyDataSetChanged();
                    Log.e("isexp1","--->"+isExpired);
                }
                else {
                    linear_expired.setVisibility(View.GONE);
                    txt_expired.setVisibility(View.GONE);
                    isExpired=false;
                    // productAdapter.notifyDataSetChanged();
                    Log.e("isexp2","--->"+isExpired);
                }
            }




            String getExpDate = historyResponse.getOrder().getCoupon_expiry_date();
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd k:mm:ss");
            try {
                date = simpleDateFormat.parse(getExpDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            txt_coupon_exp_date.setText(new SimpleDateFormat("MM/dd/yy").format(date));
          //  txt_coupon_exp_date.setText(historyResponse.getOrder().getCoupon_expiry_date());
            linear_expiry.setVisibility(View.GONE);
            txt_expiry_date.setVisibility(View.GONE);



        }
        else {
            Log.e("iscoupan","--->"+isCouponTimes);
            linear_address.setVisibility(View.GONE);
           txt_history_address.setVisibility(View.GONE);
            txt_payment_method.setVisibility(View.VISIBLE);
            linear_payment.setVisibility(View.VISIBLE);
            linear_resend.setVisibility(View.VISIBLE);
            btnCoupon.setVisibility(View.GONE);
            linear_expired.setVisibility(View.GONE);
            txt_expired.setVisibility(View.GONE);
            String payment_method=historyResponse.getOrder().getPayment_method();

            linear_expiration.setVisibility(View.GONE);
            txt_coupon_exp_date.setVisibility(View.GONE);
            linear_expiry.setVisibility(View.VISIBLE);
            txt_expiry_date.setVisibility(View.VISIBLE);
            String getExpDate = historyResponse.getOrder().getCoupon_expiry_date();
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd k:mm:ss");
            try {
                date = simpleDateFormat.parse(getExpDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            txt_expiry_date.setText(new SimpleDateFormat("MM/dd/yy").format(date));

            if(payment_method.equalsIgnoreCase("1")) {
                txt_payment_method.setText("COD");
                payment_code.setVisibility(View.GONE);
                payment_ref_no.setVisibility(View.GONE);
                linear_ref.setVisibility(View.GONE);
                linear_code.setVisibility(View.GONE);
            }
            else {
                txt_payment_method.setText("CC/DC");
                payment_code.setVisibility(View.VISIBLE);
                payment_ref_no.setVisibility(View.VISIBLE);
                linear_ref.setVisibility(View.VISIBLE);
                linear_code.setVisibility(View.VISIBLE);
                payment_code.setText(historyResponse.getOrder().getPayment_code());
                payment_ref_no.setText(historyResponse.getOrder().getPayment_refno());
            }

        }

        txt_recipient.setText(historyResponse.getOrder().getFirstname() + " " + historyResponse.getOrder().getLastname());
        txt_campaignName.setText(historyResponse.getCampaign().getTitle());
        txt_fundspotName.setText(historyResponse.getFundspot().getTitle());
        txt_organizations.setText(historyResponse.getOrganization().getTitle());

        productAdapter = new OrderProductAdapter(products,getApplicationContext() , isCouponTimes,isaccept);
        listProducts.setAdapter(productAdapter);

        String getCreatedDate = historyResponse.getOrder().getCreated();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd k:mm:ss");
        try {
            date = simpleDateFormat.parse(getCreatedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txt_date.setText(new SimpleDateFormat("MM/dd/yy").format(date));

        layout_coupon = (LinearLayout) findViewById(R.id.layout_coupon);

        qrScan = (ImageView) findViewById(R.id.img_qrScan);

        for(int i=0 ; i < historyResponse.getOrderProduct().size() ; i++){

            products.add(historyResponse.getOrderProduct().get(i));
            productAdapter.notifyDataSetChanged();
        }

        /*if(isCouponTimes){
            listProducts.setVisibility(View.GONE);
            btnCoupon.setVisibility(View.GONE);
            layout_coupon.setVisibility(View.VISIBLE);


            for(int i=0 ; i < historyResponse.getOrderProduct().size() ; i++){

              QRSCAN = W.BASE_URL + historyResponse.getOrderProduct().get(i).getQr_code_img();
                productName = historyResponse.getOrderProduct().get(i).getName() + " $ " + historyResponse.getOrderProduct().get(i).getSelling_price();

            }

            txt_product.setText(productName);
            Picasso.with(getApplicationContext())
                    .load(QRSCAN)
                    .into(qrScan);


            if(!QRSCAN.isEmpty()){

                qrScan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFullImageView(QRSCAN , productName);
                    }
                });

            }


        }*/
    }

    private void GoForNext() {

        dialog.show();
        final Call<BankCardResponse> bankCardResponse = adminAPI.BankCard(preference.getUserID(), preference.getTokenHash());
        Log.e("parameters", "-->" + preference.getUserID() + "-->" + preference.getTokenHash());
        bankCardResponse.enqueue(new Callback<BankCardResponse>() {
            @Override
            public void onResponse(Call<BankCardResponse> call, Response<BankCardResponse> response) {
                dialog.dismiss();
                BankCardResponse cardResponse = response.body();

                Log.e("getData", "-->" + new Gson().toJson(cardResponse));

                if (cardResponse != null) {
                    if (cardResponse.isStatus()) {
                        Intent i = new Intent(getApplicationContext(), CardActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("isCouponTimes" , true);
                        i.putExtra("orderId" , historyResponse.getOrder().getId());
                        startActivity(i);
                    } else {
                        Intent i = new Intent(getApplicationContext(), CreateCardActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("isCouponTimes" , true);
                        i.putExtra("orderId" , historyResponse.getOrder().getId());
                        startActivity(i);

                    }
                } else {
                    C.INSTANCE.defaultError(getApplicationContext());
                }

            }

            @Override
            public void onFailure(Call<BankCardResponse> call, Throwable t) {

                dialog.dismiss();
                C.INSTANCE.errorToast(getApplicationContext(), t);
            }
        });








    }

    private void showFullImageView(String qrscan , String productName) {
        Intent intent = new Intent(getApplicationContext(), FullZoomViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("imagePaths", qrscan);
        intent.putExtra("productName", productName);
        startActivity(intent);
    }
}
