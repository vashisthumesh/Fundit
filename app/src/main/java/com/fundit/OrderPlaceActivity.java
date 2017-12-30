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
import com.fundit.adapter.ListproductAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.model.CampaignListResponse;
import com.fundit.model.VerifyResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderPlaceActivity extends AppCompatActivity {

    CampaignListResponse.CampaignList campaignList;
    List<CampaignListResponse.CampaignList> campaignList1=new ArrayList<>();
    ListView list_product;
    TextView txt_campaignName, txt_description, txt_partnerLabel, txt_partnerName, txt_item, txt_goal, txt_split, txt_date, txt_members, txt_addMember, txt_message, txt_back, txt_raised, txt_targetAmt  ,txt_statistic,txt_labeldate,txt_msgtype;

    Button btn_placeOrder,btn_placeOrder1;
    LinearLayout layot_date,place_layout,share_layout;

    AppPreference preference;
    AdminAPI adminAPI;
    ListproductAdapter listproductadapter;
    Date startdate,enddate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_place);


        Intent intent = getIntent();
        campaignList = (CampaignListResponse.CampaignList) intent.getSerializableExtra("Details");
       campaignList1.add(campaignList);

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
        list_product = (ListView) findViewById(R.id.listproduct);


        txt_campaignName = (TextView) findViewById(R.id.txt_campaignName);
        txt_description = (TextView) findViewById(R.id.txt_description);
        txt_partnerLabel = (TextView) findViewById(R.id.txt_partnerLabel);
        txt_partnerName = (TextView) findViewById(R.id.txt_partnerName);
     //   txt_item = (TextView) findViewById(R.id.txt_item);
        txt_goal = (TextView) findViewById(R.id.txt_goal);
        txt_split = (TextView) findViewById(R.id.txt_split);
        txt_date = (TextView) findViewById(R.id.txt_date);
        txt_members = (TextView) findViewById(R.id.txt_members);
        txt_addMember = (TextView) findViewById(R.id.txt_addMember);
        txt_message = (TextView) findViewById(R.id.txt_message);
        txt_back = (TextView) findViewById(R.id.txt_back);
        txt_raised = (TextView) findViewById(R.id.txt_raised);
        txt_targetAmt = (TextView) findViewById(R.id.txt_targetAmt);
        txt_statistic = (TextView) findViewById(R.id.txt_statistic);
        txt_labeldate= (TextView) findViewById(R.id.txt_labeldate);
        txt_msgtype= (TextView) findViewById(R.id.txt_msgtype);
        layot_date= (LinearLayout) findViewById(R.id.layot_date);

        btn_placeOrder = (Button) findViewById(R.id.btn_placeOrder);
        btn_placeOrder1= (Button) findViewById(R.id.btn_placeOrder1);
        place_layout= (LinearLayout) findViewById(R.id.place_layout);
        share_layout= (LinearLayout) findViewById(R.id.share_layout);

        /*if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
            btn_placeOrder.setText("Share");
        }
*/

        if(preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION))
        {
            place_layout.setVisibility(View.VISIBLE);
        }
        else {
            place_layout.setVisibility(View.GONE);

        }
        listproductadapter = new ListproductAdapter(campaignList1,this);
        list_product.setAdapter(listproductadapter);
        listproductadapter.notifyDataSetChanged();



        txt_campaignName.setText(campaignList.getCampaign().getTitle());
        txt_description.setText(campaignList.getCampaign().getDescription());

        if(campaignList.getCampaign().getTarget_amount().equalsIgnoreCase("0"))
        {
            txt_goal.setText("No Limit");
        }
        else
        {
            txt_goal.setText("$" +String.format("%.2f",Double.parseDouble(campaignList.getCampaign().getTarget_amount())));
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
        initialno= Double.parseDouble(campaignList.getCampaign().getTotal_earning());
        finalno= Double.parseDouble(campaignList.getCampaign().getTarget_amount());
        if(finalno == 0)
        {

            toadd=initiallength ;
            txt_raised.getLayoutParams().width= (int) toadd;
            txt_targetAmt.setText("NoLimit");
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
            txt_targetAmt.setText("$" +String.format("%.2f", Double.parseDouble(campaignList.getCampaign().getTarget_amount())));
        }

        txt_raised.setText("Raised"+"\t"+"$"+campaignList.getCampaign().getTotal_earning());
        txt_split.setText(campaignList.getCampaign().getFundspot_percent() + " / " + campaignList.getCampaign().getOrganization_percent());


       // txt_date.setText(campaignList.getCampaign().getStart_date() + " to " + campaignList.getCampaign().getEnd_date());


        if(campaignList.getCampaign().getCampaign_duration().equalsIgnoreCase("0"))
        {
            layot_date.setVisibility(View.GONE);
            //txt_date.setVisibility(View.GONE);
         //  txt_labeldate.setVisibility(View.GONE);
        }
        else {
           layot_date.setVisibility(View.VISIBLE);
          // txt_date.setVisibility(View.VISIBLE);
            // txt_labeldate.setVisibility(View.VISIBLE);
            String start_date=campaignList.getCampaign().getStart_date();
            String end_date=campaignList.getCampaign().getEnd_date();


            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                startdate = simpleDateFormat.parse(start_date);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            final SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            try {
                enddate=simpleDateFormat1.parse(end_date);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            txt_date.setText(" "+new SimpleDateFormat("MMM dd").format(startdate)+" "+"to"+" "+new SimpleDateFormat("MMM dd").format(enddate));
       }








        if(preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {

            txt_msgtype.setText("Message To Redeemer :");
            txt_message.setText(campaignList.getCampaign().getMessage());

        }
       if(preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION))
       {
           txt_msgtype.setText("Message To Sellers :");
           txt_message.setText(campaignList.getCampaign().getMsg_seller());
       }
        if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
            txt_partnerLabel.setText("Organization Partner :");
            if (campaignList.getCampaign().getReview_status().equals(1)) {
                txt_partnerName.setText(campaignList.getUserFundspot().getTitle());
            } else {
                txt_partnerName.setText(campaignList.getUserOrganization().getTitle());
            }
        }

        if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
            txt_partnerLabel.setText("Fundspot Partner :");
            if (campaignList.getCampaign().getReview_status().equals(1)) {
                txt_partnerName.setText(campaignList.getUserOrganization().getTitle());
            } else {
                txt_partnerName.setText(campaignList.getUserFundspot().getTitle());
            }
        }




        txt_addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddMemberToCampaign.class);
                intent.putExtra("campaignId", campaignList.getCampaign().getId());
                startActivity(intent);
            }
        });

        btn_placeOrder1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FinalOrderPlace.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("details", campaignList);
                startActivity(intent);


            }
        });
        btn_placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                C.INSTANCE.showToast(getApplicationContext() , "Feature Coming Soon");
            }
        });

        txt_statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext() , StatisticActivity.class);
                intent.putExtra("campaignId", campaignList.getCampaign().getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });





    }
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
}
