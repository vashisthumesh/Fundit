package com.fundit.organization;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.fundit.R;

public class CreateCampaignActivity extends AppCompatActivity {

    TextView txt_browseFundspot;
    AutoCompleteTextView auto_searchFundspot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_campaign);


    }
}
