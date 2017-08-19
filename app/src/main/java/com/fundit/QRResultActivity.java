package com.fundit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QRResultActivity extends AppCompatActivity {

    TextView txtProduct , txtCampign , txtPrice;

    String getText = "";

    String campaignName = "" , customer_name = "" , organization_name = "" , fundspot_name = "" , name = "" , quantity = "" , selling_price = "" , item_total = "" , expiry_date = ""  ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrresult);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);


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
        getText = intent.getStringExtra("result");

        txtProduct = (TextView) findViewById(R.id.productName);
        txtCampign = (TextView) findViewById(R.id.campaignName);
        txtPrice = (TextView) findViewById(R.id.productPrice);

        Log.e("getText" , "" + getText);

        /*{"campaign_name":"New Sport Club","customer_name":"Nivida","organization_name":"Vt Bhangra","fundspot_name":"Pizza","name":"DEMO PIZZA","quantity":"1","selling_price":"8.00","item_total":"8.00","expiry_date":"08-25-2017"}
*/

        try {
            JSONObject mainObject = new JSONObject(getText);


            campaignName = mainObject.getString("campaign_name");
            customer_name = mainObject.getString("customer_name");
            organization_name = mainObject.getString("organization_name");
            fundspot_name = mainObject.getString("fundspot_name");
            name = mainObject.getString("name");
            quantity = mainObject.getString("quantity");
            selling_price = mainObject.getString("selling_price");
            item_total = mainObject.getString("item_total");
            expiry_date = mainObject.getString("expiry_date");


            txtCampign.setText(campaignName);
            txtProduct.setText( name + "$ " + item_total);
            txtPrice.setText(quantity);



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext() , HomeActivity.class);
        startActivity(intent);
    }
}
