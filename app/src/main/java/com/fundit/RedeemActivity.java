package com.fundit;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.W;
import com.fundit.apis.ServiceHandler;
import com.fundit.helper.CustomDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RedeemActivity extends AppCompatActivity {

    AppPreference preference;
    CustomDialog dialog;

    String text = "";
    String userId = "";
    String roleId = "";
    String quantity = "";

    TextView txt_quantity , txt_remain ;
    EditText edt_quantity ;
    Button btn_redeem ;

    String remaining = "";
    String productId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);

        preference = new AppPreference(getApplicationContext());
        dialog = new CustomDialog(RedeemActivity.this , "");

        Intent intent = getIntent();
        text = intent.getStringExtra("text");
        userId = intent.getStringExtra("userId");
        roleId = intent.getStringExtra("roleId");
        quantity = intent.getStringExtra("quantity");







        setupToolBar();
        fetchIds();



    }

    private void fetchIds() {

        txt_quantity = (TextView) findViewById(R.id.txt_quantity);
        txt_remain = (TextView) findViewById(R.id.txt_remain);

        edt_quantity = (EditText) findViewById(R.id.edt_quantity);
        edt_quantity.setText("0");

        btn_redeem = (Button) findViewById(R.id.btn_redeem);

        txt_quantity.setText(quantity);

        edt_quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                String getEditQuantity = edt_quantity.getText().toString().trim();
                int getQty = 0;
                int remaingQty = Integer.parseInt(remaining);

                int finalQty = 0;


                if(getEditQuantity.isEmpty()){

                    Log.e("empty" , "empty");

                    edt_quantity.setHint("0");
                    txt_remain.setText(remaining);
                    getQty = 0;

                }
                else {
                    getQty = Integer.parseInt(getEditQuantity);

                    if(getQty > remaingQty){
                        Log.e("greater" , "greater");
                        getQty = Integer.parseInt(getEditQuantity);

                        edt_quantity.setText(String.valueOf(remaingQty));
                        finalQty = remaingQty - Integer.parseInt(edt_quantity.getText().toString());
                        txt_remain.setText(String.valueOf(finalQty));
                    }
                    else {


                        finalQty = remaingQty - getQty;
                        txt_remain.setText(String.valueOf(finalQty));

                    }
                }

            }
        });





        btn_redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getQty = edt_quantity.getText().toString();
                String remainingQty = txt_remain.getText().toString();

                Log.e("remainingQTY" , remainingQty);

                int finalQuantities = 0;

                if(!getQty.isEmpty()){
                    finalQuantities = Integer.parseInt(getQty);
                }

                if(getQty.isEmpty()){
                    C.INSTANCE.showToast(getApplicationContext() , "Please Enter Quantity First");
                }
                else if(finalQuantities==0){
                    C.INSTANCE.showToast(getApplicationContext() , "Please Enter Quantity Atleast 1 Quantity");
                }
                else {
                    new RedeemQuantity(productId , remainingQty).execute();
                }





            }
        });




        new CheckQRDetails(userId , text , roleId).execute();

    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);
        actionTitle.setText("Redeem Coupon");
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

    public class CheckQRDetails extends AsyncTask<Void , Void , String> {

        String userId = "";
        String dataFromQR = "";
        String roleId = "";

        public CheckQRDetails(String userId, String dataFromQR , String roleId) {
            this.userId = userId;
            this.dataFromQR = dataFromQR;
            this.roleId = roleId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{

                dialog.show();

            }catch (Exception e){
                e.printStackTrace();

            }
        }

        @Override
        protected String doInBackground(Void... params) {

            List<NameValuePair> pairs = new ArrayList<>();



            pairs.add(new BasicNameValuePair(W.KEY_USERID , userId));
            pairs.add(new BasicNameValuePair(W.KEY_ROLEID , roleId));
            pairs.add(new BasicNameValuePair("coupon_data" , dataFromQR ));

            String json = new ServiceHandler().makeServiceCall(W.BASE_URL + "Order/app_validate_coupon" , ServiceHandler.POST , pairs);

            Log.e("parameters" , "-->" + pairs);
            Log.e("json" , "-->" + json);



            return json;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
dialog.dismiss();

            if(s.isEmpty()){


                C.INSTANCE.defaultError(getApplicationContext());



            }else {

                try {
                    JSONObject mainObject = new JSONObject(s);

                    boolean status = false;
                    String message = "";

                    status = mainObject.getBoolean("status");
                    message = mainObject.getString("message");

                    if(status){

                        remaining = mainObject.getString("left_qty");
                        productId = mainObject.getString("order_product_id");

                        txt_remain.setText(remaining);

                    }

                    else {
                        C.INSTANCE.showToast(getApplicationContext() , message);
                        Intent intent = new Intent(getApplicationContext() , HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }





                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }




        }
    }

    public class RedeemQuantity extends AsyncTask<Void , Void , String> {

        String productId = "";
        String qty = "";

        public RedeemQuantity(String productId, String qty) {
            this.productId = productId;
            this.qty = qty;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{

                dialog.show();

            }catch (Exception e){
                e.printStackTrace();

            }
        }


        @Override
        protected String doInBackground(Void... params) {

            List<NameValuePair> pairs = new ArrayList<>();



            pairs.add(new BasicNameValuePair("order_product_id" , productId));
            pairs.add(new BasicNameValuePair("left_qty" , qty ));

            String json = new ServiceHandler().makeServiceCall(W.BASE_URL + "Order/App_Update_Qty" , ServiceHandler.POST , pairs);

            Log.e("parameters" , "-->" + pairs);
            Log.e("json" , "-->" + json);



            return json;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if(s.isEmpty()){


                C.INSTANCE.defaultError(getApplicationContext());



            }else {

                try {
                    JSONObject mainObject = new JSONObject(s);

                    boolean status = false;
                    String message = "";


                    status = mainObject.getBoolean("status");
                    message = mainObject.getString("message");

                    if(status){
                     C.INSTANCE.showToast(getApplicationContext() , message);

                        Intent intent = new Intent(getApplicationContext() , HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);


                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }




        }
    }
}
