package com.fundit.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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
    int SUCCESS_CODE=475;
    OnReviewClickListener onReviewClickListener;

    public CampaignRequestAdapter(List<CampaignListResponse.CampaignList> campaignList, Activity activity) {
        this.campaignList = campaignList;
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
        this.preference = new AppPreference(activity);
    }

    public CampaignRequestAdapter(List<CampaignListResponse.CampaignList> campaignList, Activity activity,OnReviewClickListener onReviewClickListener) {
        this.campaignList = campaignList;
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
        this.preference = new AppPreference(activity);
        this.onReviewClickListener = onReviewClickListener;
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

        LinearLayout layout_review = (LinearLayout) view.findViewById(R.id.layout_review);

        Button btn_reviewTerms = (Button) view.findViewById(R.id.btn_reviewTerms);
        Button btn_accept = (Button) view.findViewById(R.id.btn_accept);
        Button btn_decline = (Button) view.findViewById(R.id.btn_decline);

        CircleImageView img_partnerPic = (CircleImageView) view.findViewById(R.id.img_partnerPic);

        if (preference.getUserRoleID().equals(C.ORGANIZATION)) {

            String message = "";

            //When Fundspot will create campaign , below logic will not be applied, it will be same as Fundspot review
            switch (campaignList.get(position).getCampaign().getAction_status()){
                case C.PENDING:
                    message = "<b>" + campaignList.get(position).getUserFundspot().getFundspot().getTitle() + "</b> is not approved your campaign <b>"+campaignList.get(position).getCampaign().getTitle() +"</b>yet!";
                    break;
                case C.ORGANIZATION_APPROVED:
                    message = "<b>" + campaignList.get(position).getUserFundspot().getFundspot().getTitle() + "</b> is started by you.";
                    break;
                case C.FUNDSPOT_APPROVED:
                    message = "<b>" + campaignList.get(position).getUserFundspot().getFundspot().getTitle() + "</b> has approved your campaign <b>"+campaignList.get(position).getCampaign().getTitle() +"</b>.";
                    break;
                case C.REJECTED:
                    message = "<b>" + campaignList.get(position).getUserFundspot().getFundspot().getTitle() + "</b> has decliend to start campaign <b>"+campaignList.get(position).getCampaign().getTitle() +"</b> with you!";
                    break;
            }


            txt_partnerName.setText(Html.fromHtml(message));
            txt_location.setText(campaignList.get(position).getUserFundspot().getFundspot().getLocation());
            txt_partnerLabel.setText("Fundspot :");
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

        //Change this when above logic get changed
        if(preference.getUserRoleID().equals(C.ORGANIZATION)){
            btn_reviewTerms.setVisibility(View.GONE);
            layout_review.setVisibility(View.VISIBLE);
        }else{
            btn_reviewTerms.setVisibility(View.VISIBLE);
        }

        btn_reviewTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(activity, CreateCampaignTermsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("campaignItem", campaignList.get(position));
                activity.startActivityForResult(intent,SUCCESS_CODE);*/

                if(onReviewClickListener!=null){
                    onReviewClickListener.onReviewButtonClick(position);
                }

            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onReviewClickListener!=null){
                    onReviewClickListener.onReviewButtonClick(position);
                }



            }
        });

        return view;
    }

    public interface OnReviewClickListener{
        void onReviewButtonClick(int position);
    }
}
