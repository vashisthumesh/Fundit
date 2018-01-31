package com.fundit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fundit.NewsDetailActivity;
import com.fundit.OrderPlaceActivity;
import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.W;
import com.fundit.apis.ServiceHandler;
import com.fundit.model.CampaignListResponse;
import com.fundit.model.MultipleProductResponse;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by NWSPL-17 on 12-Aug-17.
 */

public class ShowCampaignAdapter extends BaseAdapter {

    List<CampaignListResponse.CampaignList> campaignLists = new ArrayList<>();
    Activity activity;
    LayoutInflater inflater;
    AppPreference preference;
    Context context;
    Date startdate, enddate;
    boolean isPastCampaign = false ;

    public ShowCampaignAdapter(List<CampaignListResponse.CampaignList> campaignLists, Activity activity , boolean isPastCampaign) {
        this.campaignLists = campaignLists;
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
        this.context = activity.getApplication();
        this.isPastCampaign = isPastCampaign ;

    }

    @Override
    public int getCount() {
        return campaignLists.size();
    }

    @Override
    public Object getItem(int position) {
        return campaignLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        preference = new AppPreference(context);

        View view = inflater.inflate(R.layout.layout_listitem_past_campaign, parent, false);

        TextView txt_mainTitle = (TextView) view.findViewById(R.id.txt_mainTitle);
        TextView txt_labelPartner = (TextView) view.findViewById(R.id.txt_labelPartner);
        TextView txt_fundPartner = (TextView) view.findViewById(R.id.txt_fundPartner);
        TextView txt_item = (TextView) view.findViewById(R.id.txt_item);
        TextView txt_itemLabel = (TextView) view.findViewById(R.id.txt_itemLabel);
        TextView txt_raised = (TextView) view.findViewById(R.id.txt_raised);
        TextView txt_targetAmt = (TextView) view.findViewById(R.id.txt_targetAmt);
        TextView txt_dates = (TextView) view.findViewById(R.id.txt_dates);
        TextView txt_dateLabel = (TextView) view.findViewById(R.id.txt_dateLabel);
        TextView txt_labelpart = (TextView) view.findViewById(R.id.txt_labelpart);
        TextView txt_orgPartner = (TextView) view.findViewById(R.id.txt_orgPartner);
        LinearLayout org_layout = (LinearLayout) view.findViewById(R.id.org_layout);
        LinearLayout main_layout2 = (LinearLayout) view.findViewById(R.id.main_layout2);
        LinearLayout main_layout = (LinearLayout) view.findViewById(R.id.main_layout);
        LinearLayout outer = (LinearLayout) view.findViewById(R.id.outer);

        ImageView img_forward = (ImageView) view.findViewById(R.id.img_forward);

        Log.e("screen", "---->" + getScreenWidth());

        double halflength = 0;
        halflength = (int) (getScreenWidth() / 1.4);
        main_layout.getLayoutParams().width = (int) halflength;
        main_layout2.getLayoutParams().width = (int) halflength;
        outer.getLayoutParams().width = (int) halflength;
        Log.e("screenhalf", "----->" + halflength);

        double finallength = 0;
        finallength = (halflength / 2.1);
        Log.e("finallength", "--->" + finallength);

        double initiallength = 0;
        initiallength = (halflength / 2.6);
        Log.e("initial", "--->" + initiallength);

        double remaininglength = 0;
        remaininglength = (halflength - (halflength / 2.1) - (halflength / 2.6));


        txt_targetAmt.getLayoutParams().width = (int) finallength;
        Log.e("final width-->", "" + finallength);

        txt_raised.getLayoutParams().width = (int) initiallength;
        Log.e("initial width-->", "" + initiallength);
        Log.e("remaining width-->", "" + remaininglength);

        double initialno = 0, finalno = 0;
        double ibyf = 0.0, multiply = 0.0, toadd = 0.0;


        txt_itemLabel.setText("Product :");
        txt_dateLabel.setText("Dates :");
        txt_mainTitle.setText(campaignLists.get(position).getCampaign().getTitle());

        if (campaignLists.get(position).getCampaign().getTitle().isEmpty()) {
            txt_mainTitle.setVisibility(View.GONE);
        }


        initialno = Double.parseDouble(campaignLists.get(position).getCampaign().getTotal_earning());
        Log.e("initialno", "--->" + initialno);
        finalno = Double.parseDouble(campaignLists.get(position).getCampaign().getTarget_amount());
        Log.e("finalno", "--->" + finalno);
        if (finalno == 0) {

            toadd = initiallength;
            txt_raised.getLayoutParams().width = (int) toadd;
            txt_targetAmt.setText("NoLimit");
        } else {
            ibyf = (double) initialno / (double) finalno;
            Log.e("iv", "-->" + ibyf);
            if (ibyf >= 1) {
                multiply = remaininglength * 1;
            } else {
                multiply = remaininglength * ibyf;
            }

            toadd = initiallength + multiply;
            txt_raised.getLayoutParams().width = (int) toadd;
            txt_targetAmt.setText("$" + String.format("%.2f", Double.parseDouble(campaignLists.get(position).getCampaign().getTarget_amount())));
        }

        // txt_raised.setText("Raised"+"\t"+"$"+1200.00);

        txt_raised.setText("Raised" + "\t" + "$" + campaignLists.get(position).getCampaign().getTotal_earning());

//        txt_dates.setText(campaignLists.get(position).getCampaign().getStart_date() + "  to  " + campaignLists.get(position).getCampaign().getEnd_date());

        if (campaignLists.get(position).getCampaign().getCampaign_duration().equalsIgnoreCase("0")) {
            txt_dates.setVisibility(View.GONE);
            txt_dateLabel.setVisibility(View.GONE);
        } else {
            txt_dates.setVisibility(View.VISIBLE);
            txt_dateLabel.setVisibility(View.VISIBLE);

            String start_date = "";
            String end_date = "";


            if(campaignLists.get(position).getCampaign().getStart_date()==null || campaignLists.get(position).getCampaign().getStart_date().equalsIgnoreCase("null")|| campaignLists.get(position).getCampaign().getStart_date().equalsIgnoreCase(null) || campaignLists.get(position).getCampaign().getStart_date().equalsIgnoreCase("") || campaignLists.get(position).getCampaign().getStart_date().isEmpty()){
                start_date = "1990-01-01";
            }else {
                start_date = campaignLists.get(position).getCampaign().getStart_date();
            }

            if(campaignLists.get(position).getCampaign().getEnd_date()==null || campaignLists.get(position).getCampaign().getEnd_date().equalsIgnoreCase("null")|| campaignLists.get(position).getCampaign().getEnd_date().equalsIgnoreCase(null) || campaignLists.get(position).getCampaign().getEnd_date().equalsIgnoreCase("") || campaignLists.get(position).getCampaign().getEnd_date().isEmpty()){
                end_date = "2050-01-01";
            }else {
                end_date = campaignLists.get(position).getCampaign().getEnd_date();
            }






            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                startdate = simpleDateFormat.parse(start_date);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            final SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            try {
                enddate = simpleDateFormat1.parse(end_date);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            txt_dates.setText(" " + new SimpleDateFormat("MMM dd").format(startdate) + " " + "to" + " " + new SimpleDateFormat("MMM dd").format(enddate));
        }


        if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
            org_layout.setVisibility(View.GONE);
            txt_labelPartner.setText("Organization Partner :");

            if (campaignLists.get(position).getCampaign().getRole_id().equalsIgnoreCase(C.FUNDSPOT)) {
                txt_fundPartner.setText(campaignLists.get(position).getUserFundspot().getTitle());
            } else if (campaignLists.get(position).getCampaign().getRole_id().equalsIgnoreCase(C.ORGANIZATION)) {
                txt_fundPartner.setText(campaignLists.get(position).getUserOrganization().getTitle());
            }
        }

        if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
            org_layout.setVisibility(View.GONE);
            //txt_labelPartner.setText("Organization Partner :");
            txt_labelPartner.setText("Fundspot Partner :");


            if (campaignLists.get(position).getCampaign().getRole_id().equalsIgnoreCase(C.FUNDSPOT)) {
                txt_fundPartner.setText(campaignLists.get(position).getUserOrganization().getTitle());
            } else if (campaignLists.get(position).getCampaign().getRole_id().equalsIgnoreCase(C.ORGANIZATION)) {
                txt_fundPartner.setText(campaignLists.get(position).getUserFundspot().getTitle());
            }


        }

        if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
            org_layout.setVisibility(View.VISIBLE);
            txt_labelPartner.setText("Fundspot Partner :");
            txt_labelpart.setText("Organization Partner:");
            if (campaignLists.get(position).getCampaign().getRole_id().equalsIgnoreCase(C.FUNDSPOT)) {
                txt_fundPartner.setText(campaignLists.get(position).getUserOrganization().getTitle());
                txt_orgPartner.setText(campaignLists.get(position).getUserFundspot().getTitle());
            } else if (campaignLists.get(position).getCampaign().getRole_id().equalsIgnoreCase(C.ORGANIZATION)) {
                txt_fundPartner.setText(campaignLists.get(position).getUserFundspot().getTitle());
                txt_orgPartner.setText(campaignLists.get(position).getUserOrganization().getTitle());
            }
        }

        for (int i = 0; i < campaignLists.get(position).getCampaignProduct().size(); i++) {
            String itemNames = campaignLists.get(position).getCampaignProduct().get(i).getName();
            txt_item.setText(itemNames);
        }

        img_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                    Intent intent = new Intent(context, NewsDetailActivity.class);
                    intent.putExtra("details", campaignLists.get(position));
                    intent.putExtra("pastCampaign" , isPastCampaign);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, OrderPlaceActivity.class);
                    intent.putExtra("Details", campaignLists.get(position));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });

        main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                    Intent intent = new Intent(context, NewsDetailActivity.class);
                    intent.putExtra("details", campaignLists.get(position));
                    intent.putExtra("pastCampaign" , isPastCampaign);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, OrderPlaceActivity.class);
                    intent.putExtra("Details", campaignLists.get(position));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });


        return view;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

}
