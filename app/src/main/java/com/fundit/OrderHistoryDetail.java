package com.fundit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fundit.a.AppPreference;
import com.fundit.a.W;
import com.fundit.adapter.OrderProductAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.Member;
import com.fundit.model.OrderHistoryResponse;
import com.fundit.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryDetail extends AppCompatActivity {

    AppPreference preference;
    AdminAPI adminAPI;
    CustomDialog dialog;

    ListView listProducts;
    Button btnCoupon;

    TextView txt_recipient , txt_campaignName , txt_fundspotName , txt_organizations , txt_date , txt_product;

    OrderHistoryResponse.OrderList historyResponse;


    List<OrderHistoryResponse.OrderedProducts> products = new ArrayList<>();

    OrderProductAdapter productAdapter;

    boolean isCouponTimes = false;

    LinearLayout layout_coupon ;
    ImageView qrScan;
    String QRSCAN = "";
    String productName = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_detail);

        preference = new AppPreference(getApplicationContext());
        adminAPI = ServiceGenerator.getAPIClass();
        dialog = new CustomDialog(getApplicationContext());

        Intent intent = getIntent();

        historyResponse = (OrderHistoryResponse.OrderList) intent.getSerializableExtra("details");
        isCouponTimes = intent.getBooleanExtra("couponTimes" , false);

       setupToolBar();
        fetchIDs();
    }
    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionTitle.setText("Order History");

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

        btnCoupon = (Button) findViewById(R.id.btn_coupon);

        listProducts = (ListView) findViewById(R.id.list_products);
        productAdapter = new OrderProductAdapter(products,getApplicationContext());
        listProducts.setAdapter(productAdapter);

        txt_recipient.setText(historyResponse.getOrder().getFirstname() + " " + historyResponse.getOrder().getLastname());
        txt_campaignName.setText(historyResponse.getCampaign().getTitle());
        txt_fundspotName.setText(historyResponse.getFundspot().getTitle());
        txt_organizations.setText(historyResponse.getOrganization().getTitle());


        layout_coupon = (LinearLayout) findViewById(R.id.layout_coupon);
        qrScan = (ImageView) findViewById(R.id.img_qrScan);

        for(int i=0 ; i < historyResponse.getOrderProduct().size() ; i++){

            products.add(historyResponse.getOrderProduct().get(i));
            productAdapter.notifyDataSetChanged();
        }

        if(isCouponTimes){
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


        }



    }

    private void showFullImageView(String qrscan , String productName) {



        Intent intent = new Intent(getApplicationContext(), FullZoomViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("imagePaths", qrscan);
        intent.putExtra("productName", productName);
        startActivity(intent);




    }
}
