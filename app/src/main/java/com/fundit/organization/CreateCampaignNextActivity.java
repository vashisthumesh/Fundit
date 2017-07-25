package com.fundit.organization;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;

public class CreateCampaignNextActivity extends AppCompatActivity {

    AdminAPI adminAPI;
    AppPreference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_campaign_next);

        preference = new AppPreference(this);
        adminAPI = ServiceGenerator.getAPIClass();

        fetchIDs();
    }

    private void fetchIDs() {

    }
}
