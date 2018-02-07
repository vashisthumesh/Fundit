package com.fundit;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinalQRScanActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private QRCodeReaderView qrCodeReaderView;

    String campaignName = "", customer_name = "", organization_name = "", fundspot_name = "", name = "", quantity = "", selling_price = "", item_total = "", expiry_date = "", fundspot_id = "", organization_id = "", campaign_id = "", order_id = "", product_id = "";


    CustomDialog customDialog;
    AppPreference preference;
    AdminAPI adminAPI;

    String user = "";
    String role = "";

    Member member = new Member();
    Fundspot fundspot = new Fundspot();

    boolean isEditTime = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_qrscan);

        preference = new AppPreference(getApplicationContext());
        adminAPI = ServiceGenerator.getAPIClass();
        customDialog = new CustomDialog(FinalQRScanActivity.this);
        customDialog.setCancelable(false);

        try {
            member = new Gson().fromJson(preference.getMemberData(), Member.class);
            fundspot = new Gson().fromJson(preference.getMemberData(), Fundspot.class);
        } catch (Exception e) {
            e.printStackTrace();
        }


        setupToolbar();
        fetchIds();
    }

    private void fetchIds() {

        if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
            user = fundspot.getUser_id();
            role = C.FUNDSPOT;
        }
        if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
            user = member.getId();
            role = C.GENERAL_MEMBER;
        }

        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setQRDecodingEnabled(true);

        qrCodeReaderView.setAutofocusInterval(2000L);

        qrCodeReaderView.setTorchEnabled(true);

        // qrCodeReaderView.setFrontCamera();

        qrCodeReaderView.setBackCamera();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        ImageView img_edit = (ImageView) findViewById(R.id.img_edit);
        img_edit.setImageResource((R.drawable.scanedit));
        img_edit.setVisibility(View.VISIBLE);
        ImageView img_notification = (ImageView) findViewById(R.id.img_notification);
        img_notification.setVisibility(View.GONE);
        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrCodeReaderView.stopCamera();
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
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
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
                fundspot_id = mainObject.getString("fundspot_id");
                organization_id = mainObject.getString("organization_id");
                campaign_id = mainObject.getString("campaign_id");
                order_id = mainObject.getString("order_id");
                product_id = mainObject.getString("product_id");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            CheckValidateCoupon(text);
        }
    }

    private void OpenPopupDialog() {
        final Dialog dialog = new Dialog(FinalQRScanActivity.this);
        dialog.setContentView(R.layout.layout_popup);
        dialog.setCancelable(false);

        final EditText edt_couponnumber = (EditText) dialog.findViewById(R.id.edt_couponnumber);
        Button btn_apply = (Button) dialog.findViewById(R.id.btn_apply);
        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getCouponNumber = "";
                getCouponNumber = edt_couponnumber.getText().toString().trim();
                if (getCouponNumber.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please Enter Coupon Number");
                } else {
                    isEditTime = true;
                    CheckValidateCoupon(getCouponNumber);
                }
            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                qrCodeReaderView.startCamera();
            }
        });

        dialog.show();
    }

    private void showSimpleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        AlertDialog bDialog = builder.create();
        bDialog.show();
    }

    private void CheckValidateCoupon(String getCouponNumber) {

        customDialog.show();
        Call<QRScanModel> qrScanModelCall = null;

        if (isEditTime == false) {
            qrScanModelCall = adminAPI.CheckQRValidCoupon(user, role, getCouponNumber);
        } else {
            qrScanModelCall = adminAPI.CheckValidCoupon(user, role, getCouponNumber);
        }

        qrScanModelCall.enqueue(new Callback<QRScanModel>() {
            @Override
            public void onResponse(Call<QRScanModel> call, Response<QRScanModel> response) {
                customDialog.dismiss();
                QRScanModel qrScanModel = response.body();
                if (qrScanModel != null) {
                    if (qrScanModel.isStatus()) {

                        String productTypeId = qrScanModel.getProduct_type_id();

                        if (productTypeId.equalsIgnoreCase("1")) {

                            openQuantityTypeDialog(qrScanModel);

                        } else if (productTypeId.equalsIgnoreCase("2")) {

                            openLeftMoneyDialog(qrScanModel);
                        }

                /*        Intent intent = new Intent(QRScannerActivity.this , RedeemActivity.class);
                        intent.putExtra("quantity" , qrScanModel.getLeft_qty());
                        intent.putExtra("order_product_id" , qrScanModel.getOrder_product_id());
                        intent.putExtra("product_type_id" , qrScanModel.getProduct_type_id());
                        intent.putExtra("product_name" , qrScanModel.getProduct_name());
                        intent.putExtra("left_money" , qrScanModel.getLeft_money());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
*/
                    } else {
                        C.INSTANCE.showToast(getApplicationContext(), qrScanModel.getMessage());
                    }
                } else {
                    C.INSTANCE.defaultError(getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<QRScanModel> call, Throwable t) {
                customDialog.dismiss();
                C.INSTANCE.errorToast(getApplicationContext(), t);

            }
        });
    }

    private void openLeftMoneyDialog(final QRScanModel qrScanModel) {

        final Dialog dialog = new Dialog(FinalQRScanActivity.this);
        dialog.setContentView(R.layout.layout_leftmoney_dialog);
        dialog.setCancelable(false);

        TextView txt_productname = (TextView) dialog.findViewById(R.id.txt_productname);
        TextView txt_quantity = (TextView) dialog.findViewById(R.id.txt_quantity);
        final TextView txt_remain = (TextView) dialog.findViewById(R.id.txt_remain);

        final EditText edt_quantity = (EditText) dialog.findViewById(R.id.edt_quantity);
        Button btn_redeem = (Button) dialog.findViewById(R.id.btn_redeem);

        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);

        String remaining = "";
        remaining = String.format("%.2f", Double.parseDouble(qrScanModel.getLeft_money()));

        edt_quantity.setText("0");
        txt_productname.setText("Product Name: " + qrScanModel.getProduct_name());
        txt_quantity.setText(String.format("%.2f", Double.parseDouble(qrScanModel.getLeft_money())));

        final String finalRemaining = remaining;
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
                float getQty = 0;
                float remaingQty = Float.parseFloat(finalRemaining);

                float finalQty = 0;


                if (getEditQuantity.isEmpty()) {
                    Log.e("empty", "empty");
                    edt_quantity.setHint("0");
                    txt_remain.setText(String.format("%.2f", Double.parseDouble(finalRemaining)));
                    getQty = 0;
                } else {
                    getQty = Float.parseFloat(getEditQuantity);
                    if (getQty > remaingQty) {
                        Log.e("greater", "greater");
                        getQty = Float.parseFloat(getEditQuantity);
                        edt_quantity.setText(String.format("%.2f", Double.parseDouble(String.valueOf(remaingQty))));
                        finalQty = remaingQty - Float.parseFloat(edt_quantity.getText().toString());
                        txt_remain.setText(String.format("%.2f", Double.parseDouble(String.valueOf(finalQty))));
                    } else {
                        finalQty = remaingQty - getQty;
                        txt_remain.setText(String.format("%.2f", Double.parseDouble(String.valueOf(finalQty))));
                    }
                }
            }
        });

        btn_redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getQty = edt_quantity.getText().toString();
                String remainingQty = txt_remain.getText().toString();

                float finalQuantities = 0;

                if (!getQty.isEmpty()) {
                    finalQuantities = Integer.parseInt(getQty);
                }

                if (getQty.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter buying amount");
                } else if (finalQuantities == 0) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter valid buying amount");
                } else {
                    new RedeemQuantity(qrScanModel.getOrder_product_id(), remainingQty, qrScanModel.getProduct_type_id()).execute();
                }
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                qrCodeReaderView.startCamera();
            }
        });

        dialog.show();


    }

    private void openQuantityTypeDialog(final QRScanModel qrScanModel) {

        final Dialog dialog = new Dialog(FinalQRScanActivity.this);
        dialog.setContentView(R.layout.layout_quantity_reddem);
        dialog.setCancelable(false);

        TextView txt_productname = (TextView) dialog.findViewById(R.id.txt_productname);
        TextView txt_quantity = (TextView) dialog.findViewById(R.id.txt_quantity);
        final TextView txt_remain = (TextView) dialog.findViewById(R.id.txt_remain);

        final EditText edt_quantity = (EditText) dialog.findViewById(R.id.edt_quantity);
        Button btn_redeem = (Button) dialog.findViewById(R.id.btn_redeem);

        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);

        String remaining = "";
        remaining = qrScanModel.getLeft_qty();

        edt_quantity.setText("0");
        txt_productname.setText("Product Name: " + qrScanModel.getProduct_name());
        txt_quantity.setText(qrScanModel.getLeft_qty());

        final String finalRemaining = remaining;
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
                int remaingQty = Integer.parseInt(finalRemaining);

                int finalQty = 0;


                if (getEditQuantity.isEmpty()) {
                    Log.e("empty", "empty");
                    edt_quantity.setHint("0");
                    txt_remain.setText(finalRemaining);
                    getQty = 0;
                } else {
                    getQty = Integer.parseInt(getEditQuantity);
                    if (getQty > remaingQty) {
                        Log.e("greater", "greater");
                        getQty = Integer.parseInt(getEditQuantity);
                        edt_quantity.setText(String.valueOf(remaingQty));
                        finalQty = remaingQty - Integer.parseInt(edt_quantity.getText().toString());
                        txt_remain.setText(String.valueOf(finalQty));
                    } else {
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

                int finalQuantities = 0;

                if (!getQty.isEmpty()) {
                    finalQuantities = Integer.parseInt(getQty);
                }

                if (getQty.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter QTY");
                } else if (finalQuantities == 0) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter Valid buying QTY");
                } else {
                    new RedeemQuantity(qrScanModel.getOrder_product_id(), remainingQty, qrScanModel.getProduct_type_id()).execute();
                }
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                qrCodeReaderView.startCamera();
            }
        });

        dialog.show();
    }

    public class RedeemQuantity extends AsyncTask<Void, Void, String> {

        String productId = "";
        String qty = "";
        String productTypeId = "";

        public RedeemQuantity(String productId, String qty, String product_type_id) {
            this.productId = productId;
            this.qty = qty;
            this.productTypeId = product_type_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {

                customDialog.show();

            } catch (Exception e) {
                e.printStackTrace();

            }
        }


        @Override
        protected String doInBackground(Void... params) {

            List<NameValuePair> pairs = new ArrayList<>();

            pairs.add(new BasicNameValuePair("order_product_id", productId));
            if (productTypeId.equalsIgnoreCase("2")) {
                pairs.add(new BasicNameValuePair("left_money", qty));
            } else {
                pairs.add(new BasicNameValuePair("left_qty", qty));
            }

            String json = new ServiceHandler().makeServiceCall(W.BASE_URL + "Order/App_Update_Qty", ServiceHandler.POST, pairs);

            Log.e("parameters", "-->" + pairs);
            Log.e("json", "-->" + json);


            return json;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.isEmpty()) {
                C.INSTANCE.defaultError(getApplicationContext());
            } else {
                try {
                    JSONObject mainObject = new JSONObject(s);
                    boolean status = false;
                    String message = "";
                    status = mainObject.getBoolean("status");
                    message = mainObject.getString("message");
                    if (status) {
                        C.INSTANCE.showToast(getApplicationContext(), "This coupon was successfully redeemed");
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
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
