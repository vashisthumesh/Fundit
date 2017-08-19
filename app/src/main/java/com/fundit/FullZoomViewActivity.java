package com.fundit;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class FullZoomViewActivity extends AppCompatActivity {

    String getImagePath = "";
    String Name = "";

    ImageView fullImagePager ;
    TextView productName ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_zoom_view);

        Intent intent = getIntent();
        getImagePath = intent.getStringExtra("imagePaths");
        Name = intent.getStringExtra("productName");


        fullImagePager = (ImageView) findViewById(R.id.fullImagePager);
        productName = (TextView) findViewById(R.id.productName);
        productName.setText(Name);

        Picasso.with(getApplicationContext())
                .load(getImagePath)
                .into(fullImagePager);

    }
}
