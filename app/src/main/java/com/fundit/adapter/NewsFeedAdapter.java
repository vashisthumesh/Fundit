package com.fundit.adapter;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fundit.FinalOrderPlace;
import com.fundit.NewsDetailActivity;
import com.fundit.OrderPlaceActivity;
import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.model.CampaignListResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    Date date1;


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
        final Button btnPlaceOrder = (Button) view.findViewById(R.id.btn_placeOrder);
        LinearLayout main_layout = (LinearLayout) view.findViewById(R.id.main_layout);
        LinearLayout longlayout = (LinearLayout) view.findViewById(R.id.outer);
        LinearLayout smalllayout = (LinearLayout) view.findViewById(R.id.inner);
        // TextView txt_initial=(TextView)view.findViewById(R.id.initial);
        // TextView txt_final=(TextView)view.findViewById(R.id.finall);


        double finallength = 0;
        finallength = (int) (getScreenWidth() / 3);

        double initiallength = 0;
        initiallength = (getScreenWidth() / 3.5);

        double remaininglength = 0;
        remaininglength = (getScreenWidth() - (int) (getScreenWidth() / 3) - (getScreenWidth() / 3.5));


        txt_targetAmt.getLayoutParams().width = (int) finallength;
        Log.e("final width-->", "" + finallength);

        txt_raised.getLayoutParams().width = (int) initiallength;
        Log.e("initial width-->", "" + initiallength);
        Log.e("remaining width-->", "" + remaininglength);

        double initialno = 0, finalno = 0;
        double ibyf = 0.0, multiply = 0.0, toadd = 0.0;


        initialno = Double.parseDouble(campaignLists.get(position).getCampaign().getTotal_earning());
        finalno = Double.parseDouble(campaignLists.get(position).getCampaign().getTarget_amount());
        if (finalno == 0) {

            if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT) || preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
                btnPlaceOrder.setVisibility(View.GONE);
            }

            String date = campaignLists.get(position).getCampaign().getEnd_date();

            String output = date.substring(0, 10);
            try {
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                date1 = sdf.parse(output);
            } catch (final ParseException e) {
                e.printStackTrace();
            }

            Calendar c = Calendar.getInstance();

            // set the calendar to start of today
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

            // and get that as a Date
            Date today = c.getTime();
            Log.e("today_date", today.toString());

            if (date1.before(today)) {

                btnPlaceOrder.setVisibility(View.GONE);
            }

            if (Double.parseDouble(campaignLists.get(position).getCampaign().getTotal_earning()) >= Double.parseDouble(campaignLists.get(position).getCampaign().getTarget_amount())) {
                btnPlaceOrder.setVisibility(View.GONE);
            }

            String createdPerson = campaignLists.get(position).getUserOrganization().getTitle();
            String recievedPerson = campaignLists.get(position).getUserFundspot().getTitle();


            for (int i = 0; i < campaignLists.get(position).getCampaignProduct().size(); i++) {

                productsNames.add(campaignLists.get(position).getCampaignProduct().get(i).getName() + " for $ " + String.format("%.2f", Double.parseDouble(campaignLists.get(position).getCampaignProduct().get(i).getPrice())));
                // productPrice.add(campaignLists.get(position).getCampaignProduct().get(i).getPrice());
            }

            String getNameandPrice = productsNames.toString().trim();
            getNameandPrice = getNameandPrice.replaceAll("\\[", "").replaceAll("\\(", "").replaceAll("\\]", "").replaceAll("\\)", "");
            String message = "<b>" + createdPerson + "</b>" + " just kicked off a campaign with " + "<b>" + recievedPerson + "</b>" + " Support them and buy " + getNameandPrice /*+ " for " + productPrice*/;


            initialno = Double.parseDouble(campaignLists.get(position).getCampaign().getTotal_earning());
            finalno = Double.parseDouble(campaignLists.get(position).getCampaign().getTarget_amount());
            if (finalno == 0) {

                toadd = initiallength;
                txt_raised.getLayoutParams().width = (int) toadd;

            } else {
                ibyf = (double) initialno / (double) finalno;

                if (ibyf >= 1) {
                    multiply = remaininglength * 1;
                } else {
                    multiply = remaininglength * ibyf;
                }
                toadd = initiallength + multiply;
                txt_raised.getLayoutParams().width = (int) toadd;
            }


            String target_amount = campaignLists.get(position).getCampaign().getTarget_amount();
            txt_raised.setText("Raised" + "\t" + "$" + String.format("%.2f", Double.parseDouble(campaignLists.get(position).getCampaign().getTotal_earning())));

            if (target_amount.equalsIgnoreCase("0")) {
                txt_targetAmt.setText("No Limit");
            } else {
                txt_targetAmt.setText("$" + String.format("%.2f", Double.parseDouble(campaignLists.get(position).getCampaign().getTarget_amount())));
            }


            txt_mainTitle.setText(Html.fromHtml(message));


            btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, FinalOrderPlace.class);
                    intent.putExtra("details", campaignLists.get(position));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            });


            main_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                        if (btnPlaceOrder.getVisibility() == View.VISIBLE) {
                            Intent intent = new Intent(context, NewsDetailActivity.class);
                            intent.putExtra("details", campaignLists.get(position));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
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
                        if (btnPlaceOrder.getVisibility() == View.VISIBLE) {
                            Intent intent = new Intent(context, NewsDetailActivity.class);
                            intent.putExtra("details", campaignLists.get(position));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    } else {

                        Intent intent = new Intent(context, OrderPlaceActivity.class);
                        intent.putExtra("Details", campaignLists.get(position));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    }
                }
            });

        }
        return view;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }


}
