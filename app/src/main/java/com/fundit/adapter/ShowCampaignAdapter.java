package com.fundit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fundit.OrderPlaceActivity;
import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.W;
import com.fundit.apis.ServiceHandler;
import com.fundit.model.CampaignListResponse;
import com.fundit.model.MultipleProductResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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


    public ShowCampaignAdapter(List<CampaignListResponse.CampaignList> campaignLists, Activity activity) {
        this.campaignLists = campaignLists;
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
        this.context = activity.getApplication();

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

        ImageView img_forward = (ImageView) view.findViewById(R.id.img_forward);

        txt_itemLabel.setText("Item :");
        txt_dateLabel.setText("Dates :");
        txt_mainTitle.setText(campaignLists.get(position).getCampaign().getTitle());
        txt_targetAmt.setText("$" + campaignLists.get(position).getCampaign().getTarget_amount());
        txt_dates.setText(campaignLists.get(position).getCampaign().getStart_date() + "  to  " + campaignLists.get(position).getCampaign().getEnd_date());


        if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
            txt_labelPartner.setText("Fundspot Partner :");

            Log.e("reviewStatus" , ""+campaignLists.get(position).getCampaign().getReview_status());

            if (campaignLists.get(position).getCampaign().getReview_status().equals(1)) {
                txt_fundPartner.setText(campaignLists.get(position).getUserFundspot().getTitle());

            } else {

                txt_fundPartner.setText(campaignLists.get(position).getUserOrganization().getTitle());



            }
        }

        if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
            txt_labelPartner.setText("Organization Partner :");

            if (campaignLists.get(position).getCampaign().getReview_status().equalsIgnoreCase("1")) {
                txt_fundPartner.setText(campaignLists.get(position).getUserFundspot().getTitle());


            } else {
                txt_fundPartner.setText(campaignLists.get(position).getUserOrganization().getTitle());
            }


        }

        Log.e("ProductSize" , "" + campaignLists.get(position).getCampaignProduct().size());
        for(int i=0 ; i<campaignLists.get(position).getCampaignProduct().size();i++){

            String itemNames = campaignLists.get(position).getCampaignProduct().get(i).getName();

            Log.e("Name" , "" +campaignLists.get(position).getCampaignProduct().get(i).getName());
            txt_item.setText(itemNames);

        }


        /*img_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , OrderPlaceActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Details" , campaignLists.get(position));


                context.startActivity(intent);
            }
        });
*/




        return view;
    }



}
