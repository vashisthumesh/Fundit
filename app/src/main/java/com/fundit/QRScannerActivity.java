package com.fundit;

import android.content.Intent;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.fundit.a.C;

public class QRScannerActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private TextView resultTextView;
    private QRCodeReaderView qrCodeReaderView;




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
        Intent intent = new Intent(getApplicationContext() , QRResultActivity.class);
        intent.putExtra("result" , text);
        startActivity(intent);

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
