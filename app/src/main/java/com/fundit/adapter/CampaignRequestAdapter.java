package com.fundit.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.fundit.CreateCampaignTermsActivity;
import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.W;
import com.fundit.model.CampaignListResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Nivida new on 24-Jul-17.
 */

public class CampaignRequestAdapter extends BaseAdapter {

    List<CampaignListResponse.CampaignList> campaignList = new ArrayList<>();
    Activity activity;
    LayoutInflater inflater;
    AppPreference preference;

    public CampaignRequestAdapter(List<CampaignListResponse.CampaignList> campaignList, Activity activity) {
        this.campaignList = campaignList;
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
        this.preference = new AppPreference(activity);
    }

    @Override
    public int getCount() {
        return campaignList.size();
    }

    @Override
    public Object getItem(int position) {
        return campaignList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.layout_request_campaign_item, parent, false);

        TextView txt_partnerName = (TextView) view.findViewById(R.id.txt_partnerName);
        TextView txt_location = (TextView) view.findViewById(R.id.txt_location);
        TextView txt_partnerLabel = (TextView) view.findViewById(R.id.txt_partnerLabel);
        TextView txt_partnerTitle = (TextView) view.findViewById(R.id.txt_partnerTitle);
        Button btn_reviewTerms = (Button) view.findViewById(R.id.btn_reviewTerms);
        CircleImageView img_partnerPic = (CircleImageView) view.findViewById(R.id.img_partnerPic);

        if (preference.getUserRoleID().equals(C.ORGANIZATION)) {
            String message = "<b>" + campaignList.get(position).getUserFundspot().getFundspot().getTitle() + "</b> wants to start a campaign with you!";
            txt_partnerName.setText(Html.fromHtml(message));
            txt_location.setText(campaignList.get(position).getUserFundspot().getFundspot().getLocation());
            txt_partnerLabel.setText("Organization :");
            txt_partnerTitle.setText(campaignList.get(position).getUserFundspot().getTitle());
            Picasso.with(activity)
                    .load(W.FILE_URL + campaignList.get(position).getUserFundspot().getFundspot().getImage())
                    .into(img_partnerPic);
        } else if (preference.getUserRoleID().equals(C.FUNDSPOT)) {
            String message = "<b>" + campaignList.get(position).getUserOrganization().getOrganization().getTitle() + "</b> wants to start a campaign with you!";
            txt_partnerName.setText(Html.fromHtml(message));
            txt_location.setText(campaignList.get(position).getUserOrganization().getOrganization().getLocation());
            txt_partnerLabel.setText("Organization :");
            txt_partnerTitle.setText(campaignList.get(position).getUserOrganization().getTitle());
            Picasso.with(activity)
                    .load(W.FILE_URL + campaignList.get(position).getUserOrganization().getOrganization().getImage())
                    .into(img_partnerPic);
        }

        btn_reviewTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CreateCampaignTermsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("campaignItem", campaignList.get(position));
                activity.startActivity(intent);
            }
        });

        return view;
    }
}
