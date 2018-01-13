package com.fundit.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fundit.R;
import com.fundit.SerchPeopleActivity;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.W;
import com.fundit.apis.ServiceHandler;
import com.fundit.helper.CustomDialog;
import com.fundit.model.Member;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NWSPL-17 on 01-Sep-17.
 */

public class AccountMemberAdapter extends BaseAdapter {

    boolean flag=false;
    List<Member> memberList = new ArrayList<>();
    Activity activity;
    LayoutInflater inflater = null;

    public AccountMemberAdapter(List<Member> memberList, Activity activity) {
        this.memberList = memberList;
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
                new RemoveFromMyList(memberList.get(position).getId() , position).execute();

                Log.e("check123" , "--->");
            }
        });

        txt_memberName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=true;
                Intent intent = new Intent(activity , SerchPeopleActivity.class);
                intent.putExtra("id" , memberList.get(position).getId());
                intent.putExtra("flag",flag);
                Log.e("id",memberList.get(position).getId());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        });

        txt_memberName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=true;
                Intent intent = new Intent(activity , SerchPeopleActivity.class);
                intent.putExtra("id" , memberList.get(position).getId());
                intent.putExtra("flag",flag);
                Log.e("id",memberList.get(position).getId());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
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



    public class RemoveFromMyList extends AsyncTask<Void , Void , String>{

        AppPreference preference = new AppPreference(activity);

        CustomDialog dialog ;
        String memberId = "";
        int position = 0;

        public RemoveFromMyList(String memberId , int position) {
            this.memberId = memberId;
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {

                dialog = new CustomDialog(activity , "");
                dialog.setCancelable(false);
                dialog.show();

            }catch (Exception e){
                e.printStackTrace();

            }
        }

        @Override
        protected String doInBackground(Void... params) {


            List<NameValuePair> pairs = new ArrayList<>();

            pairs.add(new BasicNameValuePair(W.KEY_USERID , preference.getUserID()));
            pairs.add(new BasicNameValuePair(W.KEY_ROLEID , preference.getUserRoleID()));
            pairs.add(new BasicNameValuePair("member_id" , memberId));


            String json = new ServiceHandler().makeServiceCall(W.BASE_URL + "Member/app_delete_member_for_account" , ServiceHandler.POST , pairs);

            Log.e("parameters" , "-->" + pairs);
            Log.e("json" , json);


            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();


            if(s.isEmpty()){

                C.INSTANCE.defaultError(activity);
            }else {

                try {
                    JSONObject mainObject = new JSONObject(s);

                    boolean status = mainObject.getBoolean("status");
                    String message = mainObject.getString("message");

                    C.INSTANCE.showToast(activity , message);

                    if(status){


                        memberList.remove(position);
                        notifyDataSetChanged();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }




        }
    }



}
