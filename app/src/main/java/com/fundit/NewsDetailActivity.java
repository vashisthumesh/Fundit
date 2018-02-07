package com.fundit;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.adapter.NewsDetailAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.CampaignListResponse;
import com.fundit.model.MultipleProductResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewsDetailActivity extends AppCompatActivity {

    LinearLayout linear_layout,layout_raisedPrice,fund_partner,org_partner;
    AppPreference preference;
    AdminAPI adminAPI;
    CustomDialog dialog;
    ListView list_products;
    TextView txt_description,txt_partnerName,txt_goal,txt_date,txt_campaignName,txt_organizationname,date_label;
    CampaignListResponse.CampaignList campaignList;
    List<MultipleProductResponse> productList = new ArrayList<>();
    NewsDetailAdapter productAdapter;
    Date start_date,end_date;
    Button btn_place_order;
    Double width=60.0;

    boolean isPastCampaign = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Intent intent = getIntent();

        campaignList = (CampaignListResponse.CampaignList) intent.getSerializableExtra("details");
        isPastCampaign = intent.getBooleanExtra("pastCampaign" , false);
        preference = new AppPreference(getApplicationContext());
        adminAPI = ServiceGenerator.getAPIClass();
        dialog = new CustomDialog(NewsDetailActivity.this);
        setupToolbar();
        fetchIDs();
    }


    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        actionTitle.setText("");
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

    private void fetchIDs() {
      //  linear_layout= (LinearLayout) findViewById(R.id.linear_layout);
        layout_raisedPrice= (LinearLayout) findViewById(R.id.layout_raisedPrice);
        txt_date= (TextView) findViewById(R.id.txt_date);
        txt_goal= (TextView) findViewById(R.id.txt_goal);
        txt_partnerName= (TextView) findViewById(R.id.txt_partnerName);
        txt_description= (TextView) findViewById(R.id.txt_description);
        txt_campaignName= (TextView) findViewById(R.id.txt_campaignName);
        txt_organizationname= (TextView) findViewById(R.id.txt_organizationname);
        TextView txt_raised = (TextView) findViewById(R.id.txt_raised);
        TextView txt_targetAmt = (TextView) findViewById(R.id.txt_targetAmt);
        list_products = (ListView) findViewById(R.id.list_products);
        btn_place_order= (Button) findViewById(R.id.btn_placeOrder);
        date_label= (TextView) findViewById(R.id.date_label);
        org_partner= (LinearLayout) findViewById(R.id.org_partner);
        fund_partner= (LinearLayout) findViewById(R.id.fund_partner);

        txt_campaignName.setText(campaignList.getCampaign().getTitle());

        txt_description.setText(campaignList.getCampaign().getDescription());

        if(isPastCampaign){
            btn_place_order.setVisibility(View.GONE);
        }




        double finallength=0;
        finallength=(int) (getScreenWidth()/3);

        double initiallength=0;
        initiallength= (getScreenWidth()/3.5);

        double remaininglength=0;
        remaininglength= (getScreenWidth()-(int)(getScreenWidth()/3)-(getScreenWidth()/3.5));


        txt_targetAmt.getLayoutParams().width= (int) finallength;
        Log.e("final width-->",""+finallength);

        txt_raised.getLayoutParams().width= (int) initiallength;
        Log.e("initial width-->",""+initiallength);
        Log.e("remaining width-->",""+remaininglength);

        double initialno=0,finalno=0;
        double ibyf=0.0,multiply=0.0,toadd=0.0;


        String target_amount=campaignList.getCampaign().getTarget_amount();
        if(target_amount.equalsIgnoreCase("0"))
        {
            txt_goal.setText("No Limit");
        }
        else
        {
            txt_goal.setText(" $" +String.format("%.2f",  Double.parseDouble(campaignList.getCampaign().getTarget_amount())));
        }



        String roleid=campaignList.getCampaign().getRole_id();



        if (roleid.equalsIgnoreCase("2")) {
            txt_organizationname.setText(campaignList.getUserOrganization().getTitle());
            txt_partnerName.setText(campaignList.getUserFundspot().getTitle());
        }
        if (roleid.equalsIgnoreCase("3")) {

            txt_organizationname.setText(campaignList.getUserFundspot().getTitle());
            txt_partnerName.setText(campaignList.getUserOrganization().getTitle());
        }

        String Campain_duration=campaignList.getCampaign().getCampaign_duration();
        if(Campain_duration.equalsIgnoreCase("0"))
        {
            txt_date.setVisibility(View.GONE);
            date_label.setVisibility(View.GONE);
        }
        else {
            txt_date.setVisibility(View.VISIBLE);
            date_label.setVisibility(View.VISIBLE);

            String startdate="";
            String enddate="";

            if(campaignList.getCampaign().getStart_date()==null || campaignList.getCampaign().getStart_date().equalsIgnoreCase("null")|| campaignList.getCampaign().getStart_date().equalsIgnoreCase(null) || campaignList.getCampaign().getStart_date().equalsIgnoreCase("") || campaignList.getCampaign().getStart_date().isEmpty()){
                startdate = "1990-01-01";
            }else {
                startdate = campaignList.getCampaign().getStart_date();
            }

            if(campaignList.getCampaign().getEnd_date()==null || campaignList.getCampaign().getEnd_date().equalsIgnoreCase("null")|| campaignList.getCampaign().getEnd_date().equalsIgnoreCase(null) || campaignList.getCampaign().getEnd_date().equalsIgnoreCase("") || campaignList.getCampaign().getEnd_date().isEmpty()){
                enddate = "2050-01-01";
            }else {
                enddate = campaignList.getCampaign().getEnd_date();
            }

            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                start_date = simpleDateFormat.parse(startdate);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            final SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            try {
                end_date=simpleDateFormat1.parse(enddate);

            } catch (ParseException e) {
                e.printStackTrace();
            }


            txt_date.setText(" "+new SimpleDateFormat("MMM dd").format(start_date)+" "+"to"+" "+new SimpleDateFormat("MMM dd").format(end_date));


        }

        initialno= Double.parseDouble(campaignList.getCampaign().getTotal_earning());
        finalno= Double.parseDouble(campaignList.getCampaign().getTarget_amount());
        if(finalno == 0)
        {

            toadd=initiallength ;
            txt_raised.getLayoutParams().width= (int) toadd;

        }
        else {
            ibyf=(double) initialno/(double)finalno;
            if(ibyf >=1)
            {
                multiply=remaininglength*1;
            }
            else {
                multiply = remaininglength * ibyf;
            }

            toadd=initiallength+multiply;
            txt_raised.getLayoutParams().width= (int) toadd;
        }





        txt_raised.setText("Raised"+"\t"+"$"+String.format("%.2f",Double.parseDouble(campaignList.getCampaign().getTotal_earning())));

        if(target_amount.equalsIgnoreCase("0"))
        {
            txt_targetAmt.setText("No Limit");
        }
        else {
            txt_targetAmt.setText("$" +String.format("%.2f", Double.parseDouble(campaignList.getCampaign().getTarget_amount())));
        }

        productAdapter = new NewsDetailAdapter(productList, getApplicationContext());
        list_products.setAdapter(productAdapter);


        for (int i = 0; i < campaignList.getCampaignProduct().size(); i++) {

            productList.add(campaignList.getCampaignProduct().get(i));
            productAdapter.notifyDataSetChanged();

        }

        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext() , FinalOrderPlace.class);
                intent.putExtra("details" , campaignList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        fund_partner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsDetailActivity.this , AddMemberFudActivity.class);
                if(campaignList.getCampaign().getRole_id().equalsIgnoreCase("2"))
                {
                    intent.putExtra("memberId",campaignList.getUserFundspot().getId());
                }
                else if(campaignList.getCampaign().getRole_id().equalsIgnoreCase("3"))
                {
                    intent.putExtra("memberId",campaignList.getUserOrganization().getId());
                }
                intent.putExtra("Flag","fund");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });


        org_partner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsDetailActivity.this , AddMemberFudActivity.class);
                if(campaignList.getCampaign().getRole_id().equalsIgnoreCase("2"))
                {
                    intent.putExtra("memberId",campaignList.getUserOrganization().getId());
                }
                else if(campaignList.getCampaign().getRole_id().equalsIgnoreCase("3"))
                {
                    intent.putExtra("memberId",campaignList.getUserFundspot().getId());
                }
                intent.putExtra("Flag","org");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

    }
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
}
