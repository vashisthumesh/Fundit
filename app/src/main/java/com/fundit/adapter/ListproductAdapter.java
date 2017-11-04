package com.fundit.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fundit.ProductActivity;
import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.W;
import com.fundit.apis.AdminAPI;
import com.fundit.helper.CustomDialog;
import com.fundit.model.CampaignListResponse;
import com.fundit.model.VerifyResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by NWSPL6 on 10/27/2017.
 */

public class ListproductAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Activity activity;
    List<CampaignListResponse.CampaignList> campaignList1=new ArrayList<>();
    AppPreference preference;
    AdminAPI adminAPI;
    CustomDialog dialog;


    public ListproductAdapter(List<CampaignListResponse.CampaignList> campaignList1, Activity activity) {
       this.campaignList1=campaignList1;
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
        preference = new AppPreference(activity.getApplicationContext());
    }
    @Override
    public int getCount() {
        return campaignList1.size();
    }
    @Override
    public Object getItem(int position) {
        return campaignList1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final  int position, View convertview, ViewGroup parent) {
        View view = inflater.inflate(R.layout.layout_list_product, parent, false);

        TextView txt_title = (TextView) view.findViewById(R.id.txttitle);
        TextView txt_sellprice = (TextView) view.findViewById(R.id.txt_sellprice);
        TextView txt_desc = (TextView) view.findViewById(R.id.txt_desc);
        CircleImageView img_Pic = (CircleImageView) view.findViewById(R.id.img_Pic);
        LinearLayout layout_click= (LinearLayout) view.findViewById(R.id.layout_click);
        txt_title.setText(campaignList1.get(0).getCampaignProduct().get(position).getName());
        Log.e("name",campaignList1.get(0).getCampaignProduct().get(position).getName());
        txt_sellprice.setText("$" +String.format("%.2f",Double.parseDouble( campaignList1.get(0).getCampaignProduct().get(position).getPrice())));


        Log.e("price",campaignList1.get(0).getCampaignProduct().get(position).getPrice());
        txt_desc.setText(campaignList1.get(0).getCampaignProduct().get(position).getDescription());
        Log.e("desc",campaignList1.get(0).getCampaignProduct().get(position).getDescription());

        String getImage = "";

        getImage = W.FILE_URL +  campaignList1.get(0).getCampaignProduct().get(position).getImage();

        Picasso.with(activity)
                .load(getImage)
                .into(img_Pic);

        layout_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(activity, ProductActivity.class);
                i.putExtra("name", campaignList1.get(0).getCampaignProduct().get(position).getName());
                i.putExtra("price",campaignList1.get(0).getCampaignProduct().get(position).getPrice());
                i.putExtra("Desc",campaignList1.get(0).getCampaignProduct().get(position).getDescription());
                i.putExtra("fine",campaignList1.get(0).getCampaignProduct().get(position).getFine_print());
                i.putExtra("image",campaignList1.get(0).getCampaignProduct().get(position).getImage());
                i.putExtra("myproduct",false);
                i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(i);
            }
        });

        return view;
    }
}
