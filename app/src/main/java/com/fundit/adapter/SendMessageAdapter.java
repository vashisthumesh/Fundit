package com.fundit.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fundit.AddMembersActivity;
import com.fundit.FinalSendMessage;
import com.fundit.R;
import com.fundit.model.Member;

import java.util.ArrayList;
import java.util.List;



public class SendMessageAdapter extends BaseAdapter {

    List<Member> memberList = new ArrayList<>();
    Context context;

    LayoutInflater inflater = null;

    boolean memberMode = false;

    public SendMessageAdapter(List<Member> memberList, Context context , boolean memberMode) {
        this.memberList = memberList;
        this.context = context;
        this.memberMode = memberMode;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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


        View view = inflater.inflate(R.layout.layout_user_list, parent, false);


        final TextView txtUserName = (TextView) view.findViewById(R.id.txt_userName);
        ImageView imgChat = (ImageView) view.findViewById(R.id.img_chat);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.layout_main);

        txtUserName.setText(memberList.get(position).getFirst_name() + " " + memberList.get(position).getLast_name());



        imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtUserName.getText().toString().trim();
                String id = memberList.get(position).getUser_id();
                Intent intent = new Intent(context, FinalSendMessage.class);
                intent.putExtra("name", name);
                intent.putExtra("id", id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.e("name", "-->" + name + "-->" + id);
                context.startActivity(intent);
            }
        });


        if (memberMode){
            imgChat.setVisibility(View.INVISIBLE);


            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.e("getSelectedItemId" , "-->" + memberList.get(position).getId());

                    String getSelectedMemberId = memberList.get(position).getId();

                    Intent intent = new Intent(context , AddMembersActivity.class);
                    intent.putExtra("memberId" , getSelectedMemberId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });











        }









        return view;
    }
}
