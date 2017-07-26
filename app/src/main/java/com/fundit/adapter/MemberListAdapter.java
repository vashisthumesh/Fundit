package com.fundit.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fundit.R;
import com.fundit.model.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nivida new on 25-Jul-17.
 */

public class MemberListAdapter extends BaseAdapter {

    List<Member> memberList = new ArrayList<>();
    Activity activity;
    LayoutInflater inflater;

    public MemberListAdapter(List<Member> memberList, Activity activity) {
        this.memberList = memberList;
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
    }

    public MemberListAdapter(Activity activity) {
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return memberList.size();
    }

    @Override
    public Object getItem(int position) {
        return memberList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.layout_member_item, parent, false);

        TextView txt_memberName = (TextView) view.findViewById(R.id.txt_memberName);
        txt_memberName.setText(memberList.get(position).getFirst_name() + " " + memberList.get(position).getLast_name());
        ImageView img_remove = (ImageView) view.findViewById(R.id.img_remove);

        img_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                memberList.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    public void addMember(Member member) {
        boolean isExist = false;

        for (int i = 0; i < memberList.size(); i++) {
            if (memberList.get(i).getUser_id().equals(member.getUser_id())) {
                isExist = true;
                break;
            }
        }

        if (!isExist) {
            this.memberList.add(member);
            notifyDataSetChanged();
        }

    }

    public List<Member> getMemberList() {
        return memberList;
    }


}
