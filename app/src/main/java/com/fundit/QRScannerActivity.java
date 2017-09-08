package com.fundit;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.W;
import com.fundit.apis.ServiceHandler;
import com.fundit.helper.CustomDialog;
import com.fundit.model.Fundspot;
import com.fundit.model.Member;
import com.google.gson.Gson;
import com.google.zxing.qrcode.QRCodeReader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QRScannerActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private TextView resultTextView;
    private QRCodeReaderView qrCodeReaderView;

    TextView txtProduct, txtCampign, txtPrice, customerName, sellingPrice, date;


    String campaignName = "", customer_name = "", organization_name = "", fundspot_name = "", name = "", quantity = "", selling_price = "", item_total = "", expiry_date = "";

    CustomDialog dialog;
    AppPreference preference;

    String user = "";
    String role = "";

    Member member = new Member();
    Fundspot fundspot = new Fundspot();

    JSONArray dataArray = new JSONArray();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);



        dialog = new CustomDialog(getApplicationContext());
        preference = new AppPreference(getApplicationContext());


        try{

            member = new Gson().fromJson(preference.getMemberData() , Member.class);
            fundspot = new Gson().fromJson(preference.getMemberData() , Fundspot.class);

            Log.e("getDatas" , preference.getMemberData());


        }catch (Exception e){

            e.printStackTrace();
        }


        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);

        qrCodeReaderView.setQRDecodingEnabled(true);

        qrCodeReaderView.setAutofocusInterval(2000L);

        qrCodeReaderView.setTorchEnabled(true);

        // qrCodeReaderView.setFrontCamera();

        qrCodeReaderView.setBackCamera();


        if(preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)){

            user = fundspot.getUser_id();
            role = C.FUNDSPOT;

            Log.e("id" , "-->" + fundspot.getId());
            Log.e("userId" , "--->" + fundspot.getUser_id());
        }

        if(preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)){

            user = member.getId();
            role = C.GENERAL_MEMBER;
        }
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {

        //resultTextView.setText("DEMO PIZZA");
        qrCodeReaderView.stopCamera();
        Log.e("text", text);
        if (!text.contains("organization_name")) {


            showSimpleDialog();
        } else {

            try {
                JSONObject mainObject = new JSONObject(text);

                campaignName = mainObject.getString("campaign_name");
                customer_name = mainObject.getString("customer_name");
                organization_name = mainObject.getString("organization_name");
                fundspot_name = mainObject.getString("fundspot_name");
                name = mainObject.getString("name");
                quantity = mainObject.getString("quantity");
                selling_price = mainObject.getString("selling_price");
                item_total = mainObject.getString("item_total");
                expiry_date = mainObject.getString("expiry_date");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(QRScannerActivity.this , RedeemActivity.class);
            intent.putExtra("text" , text);
            intent.putExtra("userId" , user);
            intent.putExtra("roleId" , role);
            intent.putExtra("quantity" , quantity);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);



        }
    }

    private void showSimpleDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Can't Read QR Data");
        builder.setMessage("Sorry QR Code Invalid");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                qrCodeReaderView.startCamera();
            }
        });
        AlertDialog bDialog=builder.create();
        bDialog.show();

    }

    private void showQRData(String text, PointF[] points) {


        final Dialog dialog = new Dialog(QRScannerActivity.this);
        dialog.setContentView(R.layout.activity_qrresult);

        txtProduct = (TextView) dialog.findViewById(R.id.productName);
        txtCampign = (TextView) dialog.findViewById(R.id.campaignName);
        txtPrice = (TextView) dialog.findViewById(R.id.productPrice);
        customerName = (TextView) dialog.findViewById(R.id.customerName);
        sellingPrice = (TextView) dialog.findViewById(R.id.sellingPrice);
        date = (TextView) dialog.findViewById(R.id.date);


        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);


        txtProduct.setVisibility(View.VISIBLE);
        txtCampign.setVisibility(View.VISIBLE);
        txtPrice.setVisibility(View.VISIBLE);
        customerName.setVisibility(View.VISIBLE);
        sellingPrice.setVisibility(View.VISIBLE);


        try {
            JSONObject mainObject = new JSONObject(text);

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
            txtProduct.setText(name + "$ " + item_total);
            txtPrice.setText(quantity);
            customerName.setText(customer_name);
            sellingPrice.setText(selling_price);
            date.setText(expiry_date);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
                qrCodeReaderView.startCamera();

            }
        });

        dialog.show();

    }


    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }


    public class CheckQRDetails extends AsyncTask<Void , Void , String>{

        String userId = "";
        String dataFromQR = "";
        String roleId = "";

        public CheckQRDetails(String userId, String dataFromQR , String roleId) {
            this.userId = userId;
            this.dataFromQR = dataFromQR;
            this.roleId = roleId;
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

            C.INSTANCE.showToast(getApplicationContext() , "success");




        }
    }


}
