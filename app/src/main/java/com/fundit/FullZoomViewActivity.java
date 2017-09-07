package com.fundit;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
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

        Intent intent = getIntent();
        getImagePath = intent.getStringExtra("imagePaths");
        Name = intent.getStringExtra("productName");


        fullImagePager = (ImageView) findViewById(R.id.fullImagePager);
        productName = (TextView) findViewById(R.id.productName);
        productName.setText("Item with Price: " + Name);

        Picasso.with(getApplicationContext())
                .load(getImagePath)
                .into(fullImagePager);

    }
}
