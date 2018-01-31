package com.fundit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    boolean newsFeedTimes = false ;
    boolean isotherTimes = false;
    boolean isRequestTimes = false;
    String name = "";
    String userId = "";
    String email = "";

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
        newsFeedTimes = i.getBooleanExtra("newsFeedTimes" , false);
        isotherTimes=i.getBooleanExtra("isOtherTimes" , false);
        isRequestTimes=i.getBooleanExtra("requestTimes" , false);

        Log.e("isothertimes" , "-->" + isotherTimes);






        txt1= (TextView) findViewById(R.id.txt1);
        txt2= (TextView) findViewById(R.id.txt2);
        txt3= (TextView) findViewById(R.id.txt3);
        txt4= (TextView) findViewById(R.id.txt4);
        txt_coupon= (TextView) findViewById(R.id.txt_coupon);
        home= (Button) findViewById(R.id.home);

        txt1.setText("Thank you for supporting"+"\t"+org+"!");
        if(isOtherUser){
            Log.e("test1" , "test1");
            txt2.setText("Your order has been completed");
            txt4.setVisibility(View.VISIBLE);
        }else if(isFundTimes){
            Log.e("test2" , "test2");
            txt2.setText("Your order has been completed on Behalf of " + name);
            txt4.setVisibility(View.GONE);
            txt_coupon.setVisibility(View.GONE);
        }else if(isCouponTimes) {
            Log.e("test3" , "test3");
            txt1.setVisibility(View.GONE);
            txt2.setText("Your order has been completed");
            txt4.setVisibility(View.VISIBLE);
        }else if(newsFeedTimes){
            Log.e("test4" , "test4");
            txt2.setText("Your order has been completed");
            txt3.setVisibility(View.VISIBLE);
            txt3.setText("You can use your coupon @ "+fundspot+"!");
            txt4.setText("View your coupon in");
            txt4.setVisibility(View.VISIBLE);
            txt_coupon.setText("My Coupons");
            txt_coupon.setVisibility(View.VISIBLE);
        }else if(isotherTimes){
            Log.e("test5" , "test5");
            email= i.getStringExtra("email");
            txt2.setVisibility(View.VISIBLE);
            txt2.setText("You have successfully placed an order for " + name);
            txt3.setVisibility(View.VISIBLE);
            txt3.setText("The coupon has been sent to "+email);
            txt4.setVisibility(View.VISIBLE);

        }else if(isRequestTimes){
            Log.e("test6" , "test6");
            txt2.setText("Your request has been sent to " + name);
            txt4.setVisibility(View.GONE);
            txt_coupon.setVisibility(View.GONE);
        }
        else{
            Log.e("test7" , "test7");
            txt2.setText("Your order has been completed");
            txt4.setVisibility(View.VISIBLE);
        }


        /*txt3.setText("You can use your coupon @"+fundspot+"!");*/
       // txt3.setVisibility(View.GONE);

        txt_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txt_coupon.getText().toString().equalsIgnoreCase("My Coupons")){

                    Intent i=new Intent(getApplicationContext(),HomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("flag",true);
                    i.putExtra("couponTimes" , true);
                    startActivity(i);
                    finish();
                }else {
                    Intent i=new Intent(getApplicationContext(),HomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("flag",true);
                    startActivity(i);
                    finish();
                }


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
        startActivity(i);
    }
}
