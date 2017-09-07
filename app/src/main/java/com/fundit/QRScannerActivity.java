package com.fundit;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.fundit.a.C;
import com.google.zxing.qrcode.QRCodeReader;

import org.json.JSONException;
import org.json.JSONObject;

public class QRScannerActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private TextView resultTextView;
    private QRCodeReaderView qrCodeReaderView;

    TextView txtProduct, txtCampign, txtPrice, customerName, sellingPrice, date;


    String campaignName = "", customer_name = "", organization_name = "", fundspot_name = "", name = "", quantity = "", selling_price = "", item_total = "", expiry_date = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);


        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);

        qrCodeReaderView.setQRDecodingEnabled(true);

        qrCodeReaderView.setAutofocusInterval(2000L);

        qrCodeReaderView.setTorchEnabled(true);

        // qrCodeReaderView.setFrontCamera();

        qrCodeReaderView.setBackCamera();

    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {

        //resultTextView.setText("DEMO PIZZA");
        Log.e("text", text);
        if (!text.contains("organization_name")) {

            qrCodeReaderView.stopCamera();
            showSimpleDialog();
        } else {
            qrCodeReaderView.stopCamera();
            showQRData(text, points);
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
}
