package com.fundit;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class FullZoomViewActivity extends AppCompatActivity {

    String getImagePath = "";
    String Name = "";
    String price="";
    String qty="";
    String item_total="";
    String remain_qty="";
    String type_id="";
    String remain_money="";
    String  coupon_no="";

    ImageView fullImagePager ;
    TextView productName ,txt_coupon_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_zoom_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        getImagePath = intent.getStringExtra("imagePaths");
        Name = intent.getStringExtra("productName");
        price=intent.getStringExtra("price");
        qty=intent.getStringExtra("qty");
        item_total=intent.getStringExtra("item_total");
        remain_qty=intent.getStringExtra("remain_qty");
        type_id=intent.getStringExtra("type_id");
        remain_money=intent.getStringExtra("remain_money");
        coupon_no=intent.getStringExtra("coupon_no");

        Log.e("name","---->"+Name);






        fullImagePager = (ImageView) findViewById(R.id.fullImagePager);
        productName = (TextView) findViewById(R.id.productName);
        txt_coupon_number= (TextView) findViewById(R.id.coupon_number);

        if(type_id.equalsIgnoreCase("1"))
        {
            productName.setText("Item : " + Name +"\n"+"Selling Price :"+"$"+String.format("%.2f",Double.parseDouble(price))+"\n"+"Total QTY:"+qty+"\n"+"Item Total:"+"$"+String.format("%.2f",Double.parseDouble(item_total))+"\n"+"Remaining QTY:"+remain_qty);

            txt_coupon_number.setText("Coupon Number:"+coupon_no);
        }
        else if(type_id.equalsIgnoreCase("2"))
        {
            productName.setText("Gift Card : " + Name +"\n"+"Gift Card Value:" +"$"+String.format("%.2f",Double.parseDouble(price))+"\n"+"Total Gift Card:"+qty+"\n"+"Gift Card Value Total:"+"$"+String.format("%.2f",Double.parseDouble(item_total))+"\n"+"Remaining Money:"+"$"+
                    remain_money);

            txt_coupon_number.setText("Coupon Number:"+coupon_no);
        }


        Picasso.with(getApplicationContext())
                .load(getImagePath)
                .into(fullImagePager);

    }
}
