package com.fundit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.model.CampaignListResponse;

public class OrderPlaceActivity extends AppCompatActivity {

    CampaignListResponse.CampaignList campaignList;


    TextView txt_campaignName, txt_description, txt_partnerLabel ,txt_partnerName, txt_item, txt_goal, txt_split, txt_date, txt_members, txt_addMember, txt_message, txt_back, txt_raised, txt_targetAmt;

    Button btn_placeOrder;

    AppPreference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_place);


        Intent intent = getIntent();
        campaignList = (CampaignListResponse.CampaignList) intent.getSerializableExtra("Details");

        preference = new AppPreference(getApplicationContext());


        setUpToolbar();
        fetchIds();

    }

    private void setUpToolbar() {

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


    }

    private void fetchIds() {

        txt_campaignName = (TextView) findViewById(R.id.txt_campaignName);
        txt_description = (TextView) findViewById(R.id.txt_description);
        txt_partnerLabel = (TextView) findViewById(R.id.txt_partnerLabel);
        txt_partnerName = (TextView) findViewById(R.id.txt_partnerName);
        txt_item = (TextView) findViewById(R.id.txt_item);
        txt_goal = (TextView) findViewById(R.id.txt_goal);
        txt_split = (TextView) findViewById(R.id.txt_split);
        txt_date = (TextView) findViewById(R.id.txt_date);
        txt_members = (TextView) findViewById(R.id.txt_members);
        txt_addMember = (TextView) findViewById(R.id.txt_addMember);
        txt_message = (TextView) findViewById(R.id.txt_message);
        txt_back = (TextView) findViewById(R.id.txt_back);
        txt_raised = (TextView) findViewById(R.id.txt_raised);
        txt_targetAmt = (TextView) findViewById(R.id.txt_targetAmt);

        btn_placeOrder = (Button) findViewById(R.id.btn_placeOrder);


        txt_campaignName.setText(campaignList.getCampaign().getTitle());
        txt_description.setText(campaignList.getCampaign().getDescription());
        txt_goal.setText("$" + campaignList.getCampaign().getTarget_amount());
        txt_targetAmt.setText("$" + campaignList.getCampaign().getTarget_amount());
        txt_split.setText(campaignList.getCampaign().getFundspot_percent() + " / " + campaignList.getCampaign().getOrganization_percent());
        txt_date.setText(campaignList.getCampaign().getStart_date() + " to " + campaignList.getCampaign().getEnd_date());
        txt_message.setText(campaignList.getCampaign().getMessage());


        if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {

            txt_partnerLabel.setText("Fundspot Partner :");


            if (campaignList.getCampaign().getReview_status().equals(1)) {

                txt_partnerName.setText(campaignList.getUserFundspot().getTitle());
            } else {

                txt_partnerName.setText(campaignList.getUserFundspot().getTitle());
            }


        }

        if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {

            txt_partnerLabel.setText("Organization Partner :");

            if (campaignList.getCampaign().getReview_status().equals(1)) {

                txt_partnerName.setText(campaignList.getUserFundspot().getTitle());

            } else {


                txt_partnerName.setText(campaignList.getUserFundspot().getTitle());
            }


        }


        for (int i = 0; i < campaignList.getCampaignProduct().size(); i++) {


            String productsName = campaignList.getCampaignProduct().get(i).getName();

            txt_item.setText(productsName);


        }


        txt_addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getApplicationContext() , AddMemberToCampaign.class);
                intent.putExtra("campaignId" , campaignList.getCampaign().getId());
                startActivity(intent);






            }
        });


    }
}
