package com.fundit;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.W;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.apis.ServiceHandler;
import com.fundit.helper.CustomDialog;
import com.fundit.model.Fundspot;
import com.fundit.model.Member;
import com.fundit.model.QRScanModel;
import com.google.gson.Gson;
import com.google.zxing.qrcode.QRCodeReader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    AdminAPI adminAPI ;
    CustomDialog customDialog ;
    boolean isEditTime = false ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);

        adminAPI = ServiceGenerator.getAPIClass();
        customDialog = new CustomDialog(QRScannerActivity.this);
        customDialog.setCancelable(false);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);

        ImageView img_edit = (ImageView) findViewById(R.id.img_edit);
        img_edit.setImageResource((R.drawable.scanedit));
        img_edit.setVisibility(View.VISIBLE);

        ImageView img_notification = (ImageView) findViewById(R.id.img_notification);
        img_notification.setVisibility(View.GONE);


        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   C.INSTANCE.showToast(getApplicationContext() , "Coming Soon");
                OpenPopupDialog();
            }
        });


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



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

    private void OpenPopupDialog() {


        final Dialog dialog = new Dialog(QRScannerActivity.this);
        dialog.setContentView(R.layout.layout_popup);
        dialog.setCancelable(true);



        final EditText edt_couponnumber = (EditText) dialog.findViewById(R.id.edt_couponnumber);
        Button btn_apply = (Button) dialog.findViewById(R.id.btn_apply);


        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getCouponNumber = "";
                getCouponNumber = edt_couponnumber.getText().toString().trim();

                if(getCouponNumber.isEmpty()){
                    C.INSTANCE.showToast(getApplicationContext() , "Please Enter Coupon Number");
                }else {
                    isEditTime = true ;
                    CheckValidateCoupon(getCouponNumber);
                }
            }
        });



        dialog.setCancelable(true);


        dialog.show();

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

            CheckValidateCoupon(text);

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

    @Override
    protected void onResume() {
        super.onResume();
        System.gc();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }


    private void CheckValidateCoupon(String getCouponNumber) {

        customDialog.show();
        Call<QRScanModel> qrScanModelCall = null ;

        if(isEditTime==false){
            qrScanModelCall = adminAPI.CheckQRValidCoupon(user , role ,getCouponNumber );
        }else {
            qrScanModelCall = adminAPI.CheckValidCoupon(user , role , getCouponNumber);
        }




        qrScanModelCall.enqueue(new Callback<QRScanModel>() {
            @Override
            public void onResponse(Call<QRScanModel> call, Response<QRScanModel> response) {
                customDialog.dismiss();
                QRScanModel qrScanModel = response.body();
                if(qrScanModel!=null){
                    if(qrScanModel.isStatus()){
                        Intent intent = new Intent(QRScannerActivity.this , RedeemActivity.class);
                        intent.putExtra("quantity" , qrScanModel.getLeft_qty());
                        intent.putExtra("order_product_id" , qrScanModel.getOrder_product_id());
                        intent.putExtra("product_type_id" , qrScanModel.getProduct_type_id());
                        intent.putExtra("product_name" , qrScanModel.getProduct_name());
                        intent.putExtra("left_money" , qrScanModel.getLeft_money());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }else {
                        C.INSTANCE.showToast(getApplicationContext() , qrScanModel.getMessage());


                        showInvalidDialog(qrScanModel.getMessage());

                    }
                }else {
                    C.INSTANCE.defaultError(getApplicationContext());
                }
            }
            @Override
            public void onFailure(Call<QRScanModel> call, Throwable t) {
                customDialog.dismiss();
                C.INSTANCE.errorToast(getApplicationContext() , t);

            }
        });
    }


    private void showInvalidDialog(String message) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Invalid Coupon");
        builder.setMessage(message);
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




}
