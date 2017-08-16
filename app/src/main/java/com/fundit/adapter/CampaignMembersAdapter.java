package com.fundit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fundit.R;
import com.fundit.model.MemberResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NWSPL-17 on 16-Aug-17.
 */

public class CampaignMembersAdapter extends BaseAdapter {

    List<MemberResponse.MemberData> memberResponses = new ArrayList<>();
    Context context;
    LayoutInflater inflater = null;

    public CampaignMembersAdapter(List<MemberResponse.MemberData> memberResponses, Context context) {
        this.memberResponses = memberResponses;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return memberResponses.size();
    }

    @Override
    public Object getItem(int position) {
        return memberResponses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.layout_member_list , parent , false);

        TextView textMemberName =(TextView) view.findViewById(R.id.text_name);
        ImageView imgDelete = (ImageView) view.findViewById(R.id.img_delete);


        textMemberName.setText(memberResponses.get(position).getFirst_name() + " " + memberResponses.get(position).getLast_name());



        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });






        return view;
    }
}
