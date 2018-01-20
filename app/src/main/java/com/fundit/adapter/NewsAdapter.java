package com.fundit.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.ArrayRes;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.model.News_model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    AppPreference preference;

    List<News_model.NewsData> newsDataList = new ArrayList<>();
    Context context;

    Date today_date,todaydate2,end_date1;

    public NewsAdapter(List<News_model.NewsData> newsDataList, Context context) {
        this.newsDataList = newsDataList;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_newsfeed, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        ArrayList<String> products = new ArrayList<>();
        preference = new AppPreference(context);

        double finallength = 0;
        finallength = (int) (getScreenWidth() / 3);

        double initiallength = 0;
        initiallength = (getScreenWidth() / 3.5);

        double remaininglength = 0;
        remaininglength = (getScreenWidth() - (int) (getScreenWidth() / 3) - (getScreenWidth() / 3.5));


        holder.txt_targetAmt.getLayoutParams().width = (int) finallength;

        holder.txt_raised.getLayoutParams().width = (int) initiallength;

        double initialno = 0, finalno = 0;
        double ibyf = 0.0, multiply = 0.0, toadd = 0.0;

        if (newsDataList.get(position).getNewsFeed().getType().equalsIgnoreCase("1")) {
            String main_title="<b>"+newsDataList.get(position).getNewsFeed().getTitle()+"</b>"+" "+newsDataList.get(position).getNewsFeed().getMsg();
            holder.txt_mainTitle.setText(Html.fromHtml(main_title));


            holder.btnPlaceOrder.setVisibility(View.GONE);
            holder.raised_layout.setVisibility(View.GONE);
        }  if (newsDataList.get(position).getNewsFeed().getType().equalsIgnoreCase("2")) {
            String main_title="<b>"+newsDataList.get(position).getNewsFeed().getTitle()+"</b>"+" "+newsDataList.get(position).getNewsFeed().getMsg();
            holder.txt_mainTitle.setText(Html.fromHtml(main_title));

            holder.btnPlaceOrder.setVisibility(View.GONE);
            holder.raised_layout.setVisibility(View.GONE);
        }  if ((newsDataList.get(position).getNewsFeed().getType().equalsIgnoreCase("3"))) {

            holder.raised_layout.setVisibility(View.VISIBLE);


            initialno = Double.parseDouble(newsDataList.get(position).getCampaignDetails().getNews_Campaign().getTotal_earning());
            finalno = Double.parseDouble(newsDataList.get(position).getCampaignDetails().getNews_Campaign().getTarget_amount());
            if (finalno == 0) {

                toadd = initiallength;
                holder.txt_raised.getLayoutParams().width = (int) toadd;

            } else {
                ibyf = (double) initialno / (double) finalno;

                if (ibyf >= 1) {
                    multiply = remaininglength * 1;
                } else {
                    multiply = remaininglength * ibyf;
                }
                toadd = initiallength + multiply;
                holder.txt_raised.getLayoutParams().width = (int) toadd;
            }

            String target_amount = newsDataList.get(position).getCampaignDetails().getNews_Campaign().getTarget_amount();


            holder.txt_raised.setText("Raised" + "\t" + "$" +String.format("%.2f", Double.parseDouble(newsDataList.get(position).getCampaignDetails().getNews_Campaign().getTotal_earning())));



            if (target_amount.equalsIgnoreCase("0")) {
                holder.txt_targetAmt.setText("No Limit");
            } else {
                holder.txt_targetAmt.setText("$" + String.format("%.2f", Double.parseDouble(newsDataList.get(position).getCampaignDetails().getNews_Campaign().getTarget_amount())));
            }


          //  holder.txt_raised.setText(newsDataList.get(position).getCampaignDetails().getNews_Campaign().getTotal_earning());
            //holder.txt_targetAmt.setText(newsDataList.get(position).getCampaignDetails().getNews_Campaign().getTarget_amount());


            for (int i = 0; i < newsDataList.get(position).getCampaignDetails().getCampaignProduct().size(); i++) {
                String productName = newsDataList.get(position).getCampaignDetails().getCampaignProduct().get(i).getName() + " for $" + String.format("%.2f", Double.parseDouble(newsDataList.get(position).getCampaignDetails().getCampaignProduct().get(i).getPrice()));
                products.add(productName);
            }

            String mainString = "";
            mainString = products.toString();
            String replaceString = mainString.replace("[" , "");
            String finalString = replaceString.replace("]" , "");

            String main_titlestring = "<b>" + newsDataList.get(position).getCampaignDetails().getCreateUser().getTitle()  + "</b> " + "just kicked off a campaign with "+"<b>"+newsDataList.get(position).getCampaignDetails().getReceiveUser().getTitle()+"</b>" +" Support them and buy "+ finalString;

            holder.txt_mainTitle.setText(Html.fromHtml(main_titlestring));

        }  if (newsDataList.get(position).getNewsFeed().getType().equalsIgnoreCase("4")) {

            holder.btnPlaceOrder.setVisibility(View.VISIBLE);
            holder.raised_layout.setVisibility(View.VISIBLE);

            initialno = Double.parseDouble(newsDataList.get(position).getCampaignDetails().getNews_Campaign().getTotal_earning());
            finalno = Double.parseDouble(newsDataList.get(position).getCampaignDetails().getNews_Campaign().getTarget_amount());


            String end_date=newsDataList.get(position).getCampaignDetails().getNews_Campaign().getEnd_date();
            Log.e("end_date", "---->" + end_date);
            today_date = new Date();
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            String todaydate1 = df1.format(today_date);

            try {
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                todaydate2 = sdf.parse(todaydate1);
            } catch (final ParseException e) {
                e.printStackTrace();
            }

            try {
                final SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");

                end_date1 = sdf1.parse(end_date);
            } catch (final ParseException e) {
                e.printStackTrace();
            }
            Log.e("end_date1", "--->" + end_date1);
            Log.e("todaydate2", "--->" + todaydate2);

            if(preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER))
            {

                if(finalno >= initialno)
                {
                    if(end_date1.before(todaydate2) || end_date1.equals(todaydate2))
                    {
                        holder.btnPlaceOrder.setVisibility(View.VISIBLE);
                    }else {
                        holder.btnPlaceOrder.setVisibility(View.GONE);
                    }

                }


            }




            if (finalno == 0) {

                toadd = initiallength;
                holder.txt_raised.getLayoutParams().width = (int) toadd;

            } else {
                ibyf = (double) initialno / (double) finalno;

                if (ibyf >= 1) {
                    multiply = remaininglength * 1;
                } else {
                    multiply = remaininglength * ibyf;
                }
                toadd = initiallength + multiply;
                holder.txt_raised.getLayoutParams().width = (int) toadd;
            }

            String target_amount = newsDataList.get(position).getCampaignDetails().getNews_Campaign().getTarget_amount();


            holder.txt_raised.setText("Raised" + "\t" + "$" +String.format("%.2f", Double.parseDouble(newsDataList.get(position).getCampaignDetails().getNews_Campaign().getTotal_earning())));



            if (target_amount.equalsIgnoreCase("0")) {
                holder.txt_targetAmt.setText("No Limit");
            } else {
                holder.txt_targetAmt.setText("$" + String.format("%.2f", Double.parseDouble(newsDataList.get(position).getCampaignDetails().getNews_Campaign().getTarget_amount())));
            }






            for (int i = 0; i < newsDataList.get(position).getCampaignDetails().getCampaignProduct().size(); i++) {
                String productName = newsDataList.get(position).getCampaignDetails().getCampaignProduct().get(i).getName() + " for $" + String.format("%.2f",Double.parseDouble(newsDataList.get(position).getCampaignDetails().getCampaignProduct().get(i).getPrice()));
                products.add(productName);
            }

            String mainString = "";
            mainString = products.toString();
            String replaceString = mainString.replace("[" , "");
            String finalString = replaceString.replace("]" , "");

            String main_titlestring = "<b>" +newsDataList.get(position).getCampaignDetails().getNews_Campaign().getTitle() + "</b> "
            + "<br/>" + "Fundspot Partner:" +"<b>"+ newsDataList.get(position).getCampaignDetails().getReceiveUser().getTitle() +"</b>"+ "<br/>"  +"Products:"+ finalString;
           // String main_titlestring1="Fundspot Partner:" +"<b>"+ newsDataList.get(position).getCampaignDetails().getReceiveUser().getTitle() +"</b>";
          //  String main_titlestring2="Products:"+ finalString;

          //  String final_string=main_titlestring+"\n"+main_titlestring1+"\n"+main_titlestring2;




          //  holder.txt_mainTitle.setText(newsDataList.get(position).getCampaignDetails().getNews_Campaign().getTitle() + "\n" + "Fundspot Partner:" + newsDataList.get(position).getCampaignDetails().getReceiveUser().getTitle() + "\n" + "Products:" + finalString);

            holder.txt_mainTitle.setText(Html.fromHtml(main_titlestring));
           // holder.txt_raised.setText(newsDataList.get(position).getCampaignDetails().getNews_Campaign().getTotal_earning());
          //  holder.txt_targetAmt.setText(newsDataList.get(position).getCampaignDetails().getNews_Campaign().getTarget_amount());

            if(preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER))
            {

            }
            else {
                holder.btnPlaceOrder.setVisibility(View.GONE);
            }


        }

        holder.txt_mainTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("position" , "--->" + position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return newsDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView txt_mainTitle, txt_raised, txt_targetAmt;
        Button btnPlaceOrder;
        LinearLayout main_layout, longlayout, smalllayout, raised_layout;


        public MyViewHolder(View itemView) {
            super(itemView);
            txt_mainTitle = (TextView) itemView.findViewById(R.id.txt_mainTitle);
            txt_raised = (TextView) itemView.findViewById(R.id.txt_raised);
            txt_targetAmt = (TextView) itemView.findViewById(R.id.txt_targetAmt);
            btnPlaceOrder = (Button) itemView.findViewById(R.id.btn_placeOrder);
            main_layout = (LinearLayout) itemView.findViewById(R.id.main_layout);
            longlayout = (LinearLayout) itemView.findViewById(R.id.outer);
            smalllayout = (LinearLayout) itemView.findViewById(R.id.inner);
            raised_layout = (LinearLayout) itemView.findViewById(R.id.raised_layout);
        }
    }


    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }


}
