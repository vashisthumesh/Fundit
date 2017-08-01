package com.fundit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CampaignSetting extends AppCompatActivity {


    String userid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_setting);

        Intent in=getIntent();
        userid=in.getExtras().getString("user_id");
    }
}
