package com.fundit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.fundit.Bean.MemberRequestBean;
import com.fundit.HomeActivity;
import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.W;
import com.fundit.apis.ServiceHandler;
import com.fundit.helper.CustomDialog;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by NWSPL-17 on 03-Aug-17.
 */

public class MemberRequestAdapter extends BaseAdapter {

    List<MemberRequestBean> memberRequestBeen = new ArrayList<>();
    Activity activity;
    LayoutInflater inflater;
    AppPreference preference;
    Context context;

    String memberId = "";
    String json = "";

    CustomDialog dialog;


    public MemberRequestAdapter(List<MemberRequestBean> memberRequestBeen, Activity activity) {
        this.memberRequestBeen = memberRequestBeen;
        this.activity = activity;

        this.inflater = activity.getLayoutInflater();
        this.preference = new AppPreference(activity);

    }

    @Override
    public int getCount() {
        return memberRequestBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return memberRequestBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.custom_member_request_layout , parent , false);

        CircleImageView imageView =(CircleImageView) view.findViewById(R.id.img_profileImage);
        TextView txtTitle = (TextView) view.findViewById(R.id.txt_Name);
        TextView txtLocation = (TextView) view.findViewById(R.id.txt_location);
        Button btnAccept = (Button) view.findViewById(R.id.btn_accept);
        Button btnDecline = (Button) view.findViewById(R.id.btn_decline);


        Log.e("log" , "---;" + memberRequestBeen.get(position).getTitle());


        String imagePath = W.FILE_URL + memberRequestBeen.get(position).getImage();

        Picasso.with(context)
                .load(imagePath)
                .into(imageView);


        txtTitle.setText(memberRequestBeen.get(position).getFirst_name() + " " + memberRequestBeen.get(position).getLast_name());
        txtLocation.setText(memberRequestBeen.get(position).getLocation());

        memberId = memberRequestBeen.get(position).getMemberId();

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = "1";

                new RespondMemberRequest(status , memberId).execute();


            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = "2";

                new RespondMemberRequest(status , memberId).execute();
            }
        });



        return view;
    }


    public class RespondMemberRequest extends AsyncTask<Void , Void , String>{

        String getStatus = "";
        String getMemberId = "";

        public RespondMemberRequest(String getStatus , String getMemberId) {
            this.getStatus = getStatus;
            this.getMemberId = getMemberId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{

                dialog = new CustomDialog(activity);
                dialog.show();
                dialog.setCancelable(false);


            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... params) {


            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("user_id" , preference.getUserID()));
            pairs.add(new BasicNameValuePair("tokenhash" , preference.getTokenHash()));
            pairs.add(new BasicNameValuePair("member_id" , getMemberId));
            pairs.add(new BasicNameValuePair("status" , getStatus));

            json = new ServiceHandler().makeServiceCall(W.BASE_URL + "Member/app_respond_member_request" , ServiceHandler.POST , pairs);

            Log.e("parameters" , "" + pairs);


            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            try{

                if(s.equalsIgnoreCase("") || s.isEmpty()){

                    C.INSTANCE.noInternet(context);
                }else {

                    JSONObject mainObject = new JSONObject(s);

                    boolean status = false;
                    String message = "";

                    status = mainObject.getBoolean("status");
                    message = mainObject.getString("message");

                    C.INSTANCE.showToast(activity , message);
                    if(status==true){
                        Intent intent = new Intent(activity , HomeActivity.class);
                        activity.startActivity(intent);
                    }
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
