package com.fundit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fundit.a.AppPreference;
import com.fundit.fragmet.CouponFragment;

import static android.R.attr.fragment;

public class Thankyou extends AppCompatActivity {
    AppPreference preference;
    TextView txt1,txt2,txt3,txt4,txt_coupon;
    Button home;
    String org="";
    String fundspot="";

    boolean isOtherUser = false;
    boolean isFundTimes = false;
    boolean isCouponTimes = false;
    String name = "";
    String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);
         preference=new AppPreference(getApplicationContext());
        Intent i=getIntent();
       org= i.getStringExtra("org");
       fundspot= i.getStringExtra("fundspot");
       isOtherUser = i.getBooleanExtra("isOtherUser" , false);
       name = i.getStringExtra("name");
       userId = i.getStringExtra("selectedUserId");
        isFundTimes = i.getBooleanExtra("fundTimes" , false);
        isCouponTimes = i.getBooleanExtra("isCouponTimes" , false);






        txt1= (TextView) findViewById(R.id.txt1);
        txt2= (TextView) findViewById(R.id.txt2);
        txt3= (TextView) findViewById(R.id.txt3);
        txt4= (TextView) findViewById(R.id.txt4);
        txt_coupon= (TextView) findViewById(R.id.txt_coupon);
        home= (Button) findViewById(R.id.home);

        txt1.setText("Thank you for supporting"+"\t"+org+"!");
        if(isOtherUser){
            txt2.setText("Your order has been completed");
            txt4.setVisibility(View.VISIBLE);
        }else if(isFundTimes){
            txt2.setText("Your order has been completed on Behalf of " + name);
            txt4.setVisibility(View.GONE);
            txt_coupon.setVisibility(View.GONE);
        }else if(isCouponTimes) {
            txt1.setVisibility(View.GONE);
            txt2.setText("Your order has been completed");
            txt4.setVisibility(View.VISIBLE);
        }else{
            txt2.setText("Your order has been completed");
            txt4.setVisibility(View.VISIBLE);
        }


        /*txt3.setText("You can use your coupon @"+fundspot+"!");*/
        txt3.setVisibility(View.GONE);

        txt_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("flag",true);
                startActivity(i);
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        setupToolbar();
    }


    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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


    }

    @Override
    public void onBackPressed() {
       /* super.onBackPressed();*/
        Intent i=new Intent(getApplicationContext(),HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("flag",true);
        startActivity(i);
        finish();
    }
}
