package com.fundit.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fundit.CreateCampaignTermsActivity;
import com.fundit.HomeActivity;
import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.W;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.campaign.GetAllCampaignDataBean;
import com.fundit.fragmet.FRequestFragment;
import com.fundit.helper.CustomDialog;
import com.fundit.model.AppModel;
import com.fundit.model.CampaignListResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Nivida new on 24-Jul-17.
 */

public class CampaignRequestAdapter extends BaseAdapter {

    // List<GetAllCampaignDataBean> campaignDataBeen = new ArrayList<>();
    List<CampaignListResponse.CampaignList> campaignList = new ArrayList<>();
    Activity activity;
    LayoutInflater inflater;
    AppPreference preference;
    AdminAPI adminAPI;
    CustomDialog dialog;
    int SUCCESS_CODE = 475;
    OnReviewClickListener onReviewClickListener;


    public CampaignRequestAdapter(List<CampaignListResponse.CampaignList> campaignList, Activity activity) {
        this.campaignList = campaignList;
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
        this.preference = new AppPreference(activity);
        this.adminAPI = ServiceGenerator.getAPIClass();
        this.dialog = new CustomDialog(activity);

    }

    public CampaignRequestAdapter(List<CampaignListResponse.CampaignList> campaignList, Activity activity, OnReviewClickListener onReviewClickListener) {
        this.campaignList = campaignList;
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
        this.preference = new AppPreference(activity);
        this.onReviewClickListener = onReviewClickListener;
        this.adminAPI = ServiceGenerator.getAPIClass();
        this.dialog = new CustomDialog(activity);

    }

    /*public CampaignRequestAdapter(List<GetAllCampaignDataBean> campaignDataBeen, Activity activity) {
        this.campaignDataBeen = campaignDataBeen;
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
        this.preference = new AppPreference(activity);
        this.onReviewClickListener = onReviewClickListener;
    }
*/
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
            switch (campaignList.get(position).getCampaign().getAction_status()) {
                case C.PENDING:
                    message = "<b>" + campaignList.get(position).getUserOrganization().getFundspot().getTitle() + "</b> wants to start a campaign with you!" /*+ campaignList.get(position).getCampaign().getTitle()*/;
                    break;
                case C.ORGANIZATION_APPROVED:
                    message = "<b>" + campaignList.get(position).getUserFundspot().getFundspot().getTitle() + "</b> is started by you.";
                    break;
                case C.FUNDSPOT_APPROVED:
                    message = "<b>" + campaignList.get(position).getUserFundspot().getFundspot().getTitle() + "</b> Accepted your campaign Request! <b>" /*+ campaignList.get(position).getCampaign().getTitle() + "</b>."*/;
                    break;
                case C.REJECTED:
                    message = "<b>" + campaignList.get(position).getUserFundspot().getFundspot().getTitle() + "</b> has decliend to start campaign <b>" + campaignList.get(position).getCampaign().getTitle() + "</b> with you!";
                    break;
            }

            if (campaignList.get(position).getCampaign().getReview_status().equals(C.ACTIVE)) {


                txt_partnerName.setText(Html.fromHtml(message));
                txt_location.setText(campaignList.get(position).getUserOrganization().getFundspot().getLocation());
                txt_partnerLabel.setText("Fundspot:");
                txt_partnerTitle.setText(campaignList.get(position).getUserOrganization().getTitle());
                Picasso.with(activity)
                        .load(W.FILE_URL + campaignList.get(position).getUserOrganization().getFundspot().getImage())
                        .into(img_partnerPic);

                btn_reviewTerms.setVisibility(View.VISIBLE);
                layout_review.setVisibility(View.GONE);


            } else if (campaignList.get(position).getCampaign().getReview_status().equals(C.INACTIVE)) {


                txt_partnerName.setText(Html.fromHtml(message));
                txt_location.setText(campaignList.get(position).getUserFundspot().getFundspot().getLocation());
                txt_partnerLabel.setText("Fundspot:");
                txt_partnerTitle.setText(campaignList.get(position).getUserFundspot().getTitle());
                Picasso.with(activity)
                        .load(W.FILE_URL + campaignList.get(position).getUserFundspot().getFundspot().getImage())
                        .into(img_partnerPic);

                btn_reviewTerms.setVisibility(View.GONE);
                layout_review.setVisibility(View.VISIBLE);


            }

        } else if (preference.getUserRoleID().equals(C.FUNDSPOT)) {


            String message = "";

            //When Fundspot will create campaign , below logic will not be applied, it will be same as Fundspot review
            switch (campaignList.get(position).getCampaign().getAction_status()) {
                case C.PENDING:
                    message = "<b>" + campaignList.get(position).getUserOrganization().getOrganization().getTitle() + "</b> wants to start a campaign with you!" /*+ campaignList.get(position).getCampaign().getTitle()*/;
                    break;
                case C.ORGANIZATION_APPROVED:
                    message = "<b>" + campaignList.get(position).getUserFundspot().getFundspot().getTitle() + "</b> is started by you.";
                    break;
                case C.FUNDSPOT_APPROVED:
                    message = "<b>" + campaignList.get(position).getUserFundspot().getOrganization().getTitle() + "</b> Accepted your campaign Request! <b>" /*+ campaignList.get(position).getCampaign().getTitle() + "</b>."*/;
                    break;
                case C.REJECTED:
                    message = "<b>" + campaignList.get(position).getUserFundspot().getFundspot().getTitle() + "</b> has decliend to start campaign <b>" + campaignList.get(position).getCampaign().getTitle() + "</b> with you!";
                    break;
            }

            if (campaignList.get(position).getCampaign().getReview_status().equals(C.ACTIVE)) {

                if (campaignList.get(position).getCampaign().getAction_status().equalsIgnoreCase(C.INACTIVE)) {
                    txt_partnerName.setText(Html.fromHtml(message));
                    txt_location.setText(campaignList.get(position).getUserOrganization().getOrganization().getLocation());
                    txt_partnerLabel.setText("Organization :");
                    txt_partnerTitle.setText(campaignList.get(position).getUserOrganization().getTitle());
                    Picasso.with(activity)
                            .load(W.FILE_URL + campaignList.get(position).getUserOrganization().getOrganization().getImage())
                            .into(img_partnerPic);

                    btn_reviewTerms.setVisibility(View.VISIBLE);
                    layout_review.setVisibility(View.GONE);
                } else {

                    txt_partnerName.setText(Html.fromHtml(message));
                    txt_location.setText(campaignList.get(position).getUserFundspot().getOrganization().getLocation());
                    txt_partnerLabel.setText("Organization :");
                    txt_partnerTitle.setText(campaignList.get(position).getUserFundspot().getTitle());
                    Picasso.with(activity)
                            .load(W.FILE_URL + campaignList.get(position).getUserFundspot().getOrganization().getImage())
                            .into(img_partnerPic);

                    btn_reviewTerms.setVisibility(View.VISIBLE);
                    layout_review.setVisibility(View.GONE);

                }

            } else if (campaignList.get(position).getCampaign().getReview_status().equals(C.INACTIVE)) {

                if (campaignList.get(position).getCampaign().getAction_status().equalsIgnoreCase(C.ACTIVE)) {

                    txt_partnerName.setText(Html.fromHtml(message));
                    txt_location.setText(campaignList.get(position).getUserFundspot().getOrganization().getLocation());
                    txt_partnerLabel.setText("Organization :");
                    txt_partnerTitle.setText(campaignList.get(position).getUserFundspot().getTitle());
                    Picasso.with(activity)
                            .load(W.FILE_URL + campaignList.get(position).getUserFundspot().getOrganization().getImage())
                            .into(img_partnerPic);

                    btn_reviewTerms.setVisibility(View.GONE);

                    layout_review.setVisibility(View.VISIBLE);


                } else {


                    txt_partnerName.setText(Html.fromHtml(message));
                    txt_location.setText(campaignList.get(position).getUserOrganization().getOrganization().getLocation());
                    txt_partnerLabel.setText("Organization :");
                    txt_partnerTitle.setText(campaignList.get(position).getUserOrganization().getTitle());
                    Picasso.with(activity)
                            .load(W.FILE_URL + campaignList.get(position).getUserOrganization().getOrganization().getImage())
                            .into(img_partnerPic);

                    btn_reviewTerms.setVisibility(View.GONE);
                    layout_review.setVisibility(View.VISIBLE);


                }


            }
        }
        btn_reviewTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (onReviewClickListener != null) {
                    onReviewClickListener.onReviewButtonClick(position);
                }

            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<AppModel> acceptCampaign = null;
                adminAPI = ServiceGenerator.getAPIClass();

                dialog = new CustomDialog(activity);
                dialog.show();

                if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {

                    acceptCampaign = adminAPI.cancelCampaign(preference.getUserID(), preference.getTokenHash(), campaignList.get(position).getCampaign().getId(), preference.getUserRoleID(), C.ORGANIZATION);


                }
                if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {

                    acceptCampaign = adminAPI.cancelCampaign(preference.getUserID(), preference.getTokenHash(), campaignList.get(position).getCampaign().getId(), preference.getUserRoleID(), C.ORGANIZATION);


                }


                Log.e("Parameters", "" + preference.getUserID() + "--" + preference.getTokenHash() + "--" + preference.getUserRoleID() + "--" + campaignList.get(position).getCampaign().getId());

                acceptCampaign.enqueue(new Callback<AppModel>() {
                    @Override
                    public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                        dialog.dismiss();
                        AppModel appModel = response.body();
                        if (appModel != null) {
                            if (appModel.isStatus()) {
                                C.INSTANCE.showToast(activity, appModel.getMessage());
                                Intent intent = new Intent(activity , HomeActivity.class);
                                activity.startActivity(intent);
                                //setResult(RESULT_OK);


                            } else {
                                C.INSTANCE.showToast(activity, appModel.getMessage());
                            }
                        } else {
                            C.INSTANCE.defaultError(activity);
                        }
                    }

                    @Override
                    public void onFailure(Call<AppModel> call, Throwable t) {
                        dialog.dismiss();
                        C.INSTANCE.errorToast(activity, t);
                    }
                });


            }
        });


        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new CustomDialog(activity);
                dialog.show();
                adminAPI = ServiceGenerator.getAPIClass();

                Call<AppModel> cancelCampaign = adminAPI.cancelCampaign(preference.getUserID(), preference.getTokenHash(), campaignList.get(position).getCampaign().getId(), preference.getUserRoleID(), "3");


                Log.e("Parameters", "" + preference.getUserID() + "--" + preference.getTokenHash() + "--" + preference.getUserRoleID() + "--" + campaignList.get(position).getCampaign().getId());

                cancelCampaign.enqueue(new Callback<AppModel>() {
                    @Override
                    public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                        dialog.dismiss();
                        AppModel appModel = response.body();
                        if (appModel != null) {
                            if (appModel.isStatus()) {
                                C.INSTANCE.showToast(activity, appModel.getMessage());
                                /*Intent intent = new Intent(activity , HomeActivity.class);
                                activity.startActivity(intent);*/
                                campaignList.remove(position);
                                //setResult(RESULT_OK);

                            } else {
                                C.INSTANCE.showToast(activity, appModel.getMessage());
                            }
                        } else {
                            C.INSTANCE.defaultError(activity);
                        }

                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<AppModel> call, Throwable t) {
                        dialog.dismiss();
                        C.INSTANCE.errorToast(activity, t);
                    }
                });
            }
        });


        return view;
    }

    public interface OnReviewClickListener {
        void onReviewButtonClick(int position);
    }



}
