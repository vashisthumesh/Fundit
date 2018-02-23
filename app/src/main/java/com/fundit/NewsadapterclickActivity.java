package com.fundit;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.adapter.NewsDetailAdapter;
import com.fundit.adapter.Newsadapterclick;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.AdjustableListView;
import com.fundit.helper.CustomDialog;
import com.fundit.model.CampaignListResponse;
import com.fundit.model.MultipleProductResponse;
import com.fundit.model.News_model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewsadapterclickActivity extends AppCompatActivity {
    LinearLayout linear_layout,layout_raisedPrice,fund_partner,org_partner;
    AppPreference preference;
    AdminAPI adminAPI;
    CustomDialog dialog;
    AdjustableListView list_products;
    TextView txt_description,txt_partnerName,txt_goal,txt_date,txt_campaignName,txt_organizationname,date_label;
    News_model.NewsData newslist;
    List<News_model.Product> productList = new ArrayList<>();
    Newsadapterclick newsadapterclick;
    Date start_date,end_date;
    Button btn_place_order,btn_share;
    Double width=60.0;
    Date today_date,todaydate2,end_date1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsadapterclick);
        preference = new AppPreference(getApplicationContext());
        adminAPI = ServiceGenerator.getAPIClass();

        Intent intent = getIntent();

        newslist = (News_model.NewsData) intent.getSerializableExtra("details");


        setupToolbar();
        fetchIds();
    }



    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        FrameLayout toolbar_add_to_cart = (FrameLayout) findViewById(R.id.toolbar_add_to_cart);
        TextView cartTotal = (TextView) findViewById(R.id.cartTotal);
        ImageView imgNotofication = (ImageView) findViewById(R.id.img_notification);

        toolbar_add_to_cart.setVisibility(View.VISIBLE);

        int unReadMessage = 0;
        unReadMessage = preference.getUnReadCount();

        if(unReadMessage==0){
            cartTotal.setVisibility(View.GONE);
        }else {
            cartTotal.setVisibility(View.VISIBLE);
            cartTotal.setText(String.valueOf(unReadMessage));
        }


        imgNotofication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext() , NotificationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });




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
    public void fetchIds()
    {
        layout_raisedPrice= (LinearLayout) findViewById(R.id.layout_raisedPrice);
        txt_date= (TextView) findViewById(R.id.txt_date);
        txt_goal= (TextView) findViewById(R.id.txt_goal);
        txt_partnerName= (TextView) findViewById(R.id.txt_partnerName);
        txt_description= (TextView) findViewById(R.id.txt_description);
        txt_campaignName= (TextView) findViewById(R.id.txt_campaignName);
        txt_organizationname= (TextView) findViewById(R.id.txt_organizationname);
        TextView txt_raised = (TextView) findViewById(R.id.txt_raised);
        TextView txt_targetAmt = (TextView) findViewById(R.id.txt_targetAmt);
        list_products = (AdjustableListView) findViewById(R.id.list_products);
        btn_place_order= (Button) findViewById(R.id.btn_placeOrder);
        btn_share= (Button) findViewById(R.id.btn_share);
        date_label= (TextView) findViewById(R.id.date_label);
        org_partner= (LinearLayout) findViewById(R.id.org_partner);
        fund_partner= (LinearLayout) findViewById(R.id.fund_partner);

        txt_campaignName.setText(newslist.getCampaignDetails().getNews_Campaign().getTitle());

        txt_description.setText(newslist.getCampaignDetails().getNews_Campaign().getDescription());

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


        String target_amount=newslist.getCampaignDetails().getNews_Campaign().getTarget_amount();
        if(target_amount.equalsIgnoreCase("0"))
        {
            txt_goal.setText("No Limit");
        }
        else
        {
            txt_goal.setText(" $" +String.format("%.2f",  Double.parseDouble(newslist.getCampaignDetails().getNews_Campaign().getTarget_amount())));
        }




        String roleid=newslist.getCampaignDetails().getNews_Campaign().getRole_id();



        if (roleid.equalsIgnoreCase("2")) {
/*
            txt_organizationname.setText(campaignList.getUserOrganization().getTitle());
            txt_partnerName.setText(campaignList.getUserFundspot().getTitle());
*/

            txt_organizationname.setText(newslist.getCampaignDetails().getCreateUser().getTitle());
            txt_partnerName.setText(newslist.getCampaignDetails().getReceiveUser().getTitle());
        }
        if (roleid.equalsIgnoreCase("3")) {
/*

            txt_organizationname.setText(campaignList.getUserFundspot().getTitle());
            txt_partnerName.setText(campaignList.getUserOrganization().getTitle());
*/


            txt_organizationname.setText(newslist.getCampaignDetails().getReceiveUser().getTitle());
            txt_partnerName.setText(newslist.getCampaignDetails().getCreateUser().getTitle());
        }









        String Campain_duration=newslist.getCampaignDetails().getNews_Campaign().getCampaign_duration();
        if(Campain_duration.equalsIgnoreCase("0"))
        {
            txt_date.setVisibility(View.GONE);
            date_label.setVisibility(View.GONE);
        }
        else {
            txt_date.setVisibility(View.VISIBLE);
            date_label.setVisibility(View.VISIBLE);

            String startdate=newslist.getCampaignDetails().getNews_Campaign().getStart_date();
            String enddate=newslist.getCampaignDetails().getNews_Campaign().getEnd_date();
            Log.e("start",startdate);
            Log.e("end",enddate);

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

        initialno= Double.parseDouble(newslist.getCampaignDetails().getNews_Campaign().getTotal_earning());
        finalno= Double.parseDouble(newslist.getCampaignDetails().getNews_Campaign().getTarget_amount());


        String end_date=newslist.getCampaignDetails().getNews_Campaign().getEnd_date();
        String end_date2=end_date.substring(0, 10);
        Log.e("end_date", "---->" + end_date);

        try {
            final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

            end_date1 = sdf1.parse(end_date2);
        } catch (final ParseException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();

        // set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Date today = c.getTime();
        Log.e("today_date",today.toString());
        Log.e("end_date1", "--->" + end_date1);
        if(preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER))
        {

           if(finalno > initialno)
           {
            if(end_date1.after(today) || end_date1.equals(today))
            {
                btn_place_order.setVisibility(View.VISIBLE);
                btn_share.setVisibility(View.VISIBLE);
            }else {
                btn_place_order.setVisibility(View.GONE);
                btn_share.setVisibility(View.GONE);
            }

           }
           else
             {
            btn_place_order.setVisibility(View.GONE);
            btn_share.setVisibility(View.GONE);
             }
        }
        else {
            btn_place_order.setVisibility(View.GONE);
            btn_share.setVisibility(View.GONE);
        }



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





        txt_raised.setText("Raised"+"\t"+"$"+String.format("%.2f",Double.parseDouble(newslist.getCampaignDetails().getNews_Campaign().getTotal_earning())));

        if(target_amount.equalsIgnoreCase("0"))
        {
            txt_targetAmt.setText("No Limit");
        }
        else {
            txt_targetAmt.setText("$" +String.format("%.2f", Double.parseDouble(newslist.getCampaignDetails().getNews_Campaign().getTarget_amount())));
        }

        newsadapterclick = new Newsadapterclick(productList, getApplicationContext());
        list_products.setAdapter(newsadapterclick);


        for (int i = 0; i < newslist.getCampaignDetails().getCampaignProduct().size(); i++) {

            productList.add(newslist.getCampaignDetails().getCampaignProduct().get(i));
            newsadapterclick.notifyDataSetChanged();

        }

        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext() , FinalOrderPlacenewActivity.class);
                intent.putExtra("details" , newslist);
                intent.putExtra("flag","true");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        fund_partner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsadapterclickActivity.this , AddMemberFudActivity.class);
                if(newslist.getCampaignDetails().getNews_Campaign().getRole_id().equalsIgnoreCase("2"))
                {
                    intent.putExtra("memberId",newslist.getCampaignDetails().getReceiveUser().getId());
                }
                else if(newslist.getCampaignDetails().getNews_Campaign().getRole_id().equalsIgnoreCase("3"))
                {
                    intent.putExtra("memberId",newslist.getCampaignDetails().getCreateUser().getId());
                }
                intent.putExtra("Flag","fund");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });


        org_partner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsadapterclickActivity.this , AddMemberFudActivity.class);
                if(newslist.getCampaignDetails().getNews_Campaign().getRole_id().equalsIgnoreCase("2"))
                {
                    intent.putExtra("memberId",newslist.getCampaignDetails().getCreateUser().getId());
                }
                else if(newslist.getCampaignDetails().getNews_Campaign().getRole_id().equalsIgnoreCase("3"))
                {
                    intent.putExtra("memberId",newslist.getCampaignDetails().getReceiveUser().getId());
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

    @Override
    protected void onResume() {
        super.onResume();
        System.gc();
    }
}
