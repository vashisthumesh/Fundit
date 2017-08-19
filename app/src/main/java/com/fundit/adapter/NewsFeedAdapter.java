package com.fundit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fundit.FinalOrderPlace;
import com.fundit.OrderPlaceActivity;
import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.model.CampaignListResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NWSPL-17 on 17-Aug-17.
 */

public class NewsFeedAdapter extends BaseAdapter {


    List<CampaignListResponse.CampaignList> campaignLists = new ArrayList<>();
    Activity activity;
    LayoutInflater inflater;
    AppPreference preference;
    Context context;





    public NewsFeedAdapter(List<CampaignListResponse.CampaignList> campaignLists, Activity activity) {
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
    public View getView(final int position, final View convertView, ViewGroup parent) {

        preference = new AppPreference(context);

        View view = inflater.inflate(R.layout.list_newsfeed, parent, false);
        ArrayList<String> productsNames = new ArrayList<>();

        TextView txt_mainTitle = (TextView) view.findViewById(R.id.txt_mainTitle);
        TextView txt_raised = (TextView) view.findViewById(R.id.txt_raised);
        TextView txt_targetAmt = (TextView) view.findViewById(R.id.txt_targetAmt);
        Button btnPlaceOrder = (Button) view.findViewById(R.id.btn_placeOrder);


        String createdPerson = campaignLists.get(position).getUserOrganization().getTitle();
        String recievedPerson = campaignLists.get(position).getUserFundspot().getTitle();


        for(int i=0 ; i<campaignLists.get(position).getCampaignProduct().size();i++){

            productsNames.add(campaignLists.get(position).getCampaignProduct().get(i).getName() + " for " + campaignLists.get(position).getCampaignProduct().get(i).getPrice());
            // productPrice.add(campaignLists.get(position).getCampaignProduct().get(i).getPrice());
        }

        String getNameandPrice = productsNames.toString().trim();
        getNameandPrice = getNameandPrice.replaceAll("\\[", "").replaceAll("\\(", "").replaceAll("\\]", "").replaceAll("\\)", "");
        String message = "<b>" + createdPerson + "</b>" +" just kicked off a campaign with " + "<b>" + recievedPerson + "</b>" +" Support them and buy " + getNameandPrice /*+ " for " + productPrice*/;





        txt_raised.setText("Raised $0.00");
        txt_targetAmt.setText("$" + campaignLists.get(position).getCampaign().getTarget_amount());


        txt_mainTitle.setText(Html.fromHtml(message));



        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context , FinalOrderPlace.class);
                intent.putExtra("details" , campaignLists.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });





        return view;
    }


}
