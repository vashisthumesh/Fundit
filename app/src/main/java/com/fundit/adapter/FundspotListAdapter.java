package com.fundit.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fundit.R;
import com.fundit.a.W;
import com.fundit.model.VerifyResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Nivida new on 21-Jul-17.
 */

public class FundspotListAdapter extends BaseAdapter {

    List<VerifyResponse.VerifyResponseData> fundSpotList=new ArrayList<>();
    Activity activity;
    LayoutInflater inflater;

    public FundspotListAdapter(List<VerifyResponse.VerifyResponseData> fundSpotList, Activity activity) {
        this.fundSpotList = fundSpotList;
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return fundSpotList.size();
    }

    @Override
    public Object getItem(int position) {
        return fundSpotList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.layout_fundspot_list_item,parent,false);

        TextView txt_Name = (TextView) view.findViewById(R.id.txt_Name);
        TextView txt_location = (TextView) view.findViewById(R.id.txt_location);

        CircleImageView img_profileImage = (CircleImageView) view.findViewById(R.id.img_profileImage);

        Picasso.with(activity)
                .load(W.FILE_URL+fundSpotList.get(position).getFundspot().getImage())
                .into(img_profileImage);

        txt_Name.setText(fundSpotList.get(position).getFundspot().getTitle());
        txt_location.setText(fundSpotList.get(position).getFundspot().getLocation());

        return view;
    }
}
