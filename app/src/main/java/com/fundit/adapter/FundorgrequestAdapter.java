package com.fundit.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.W;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.AppModel;
import com.fundit.model.FundRequest;
import com.fundit.model.Member;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NWSPL6 on 1/31/2018.
 */

public class FundorgrequestAdapter extends BaseAdapter {

    List<FundRequest.FundRequest_Data> fundRequestDataList = new ArrayList<>();
    Activity activity;
    LayoutInflater inflater;
    Context context;
    CustomDialog dialog;
    AdminAPI adminAPI;
    AppPreference preference;
    String member_id =  "";

    Member member = new Member();


    public FundorgrequestAdapter(List<FundRequest.FundRequest_Data> fundRequestDataList, Activity activity) {
        this.fundRequestDataList = fundRequestDataList;
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
        preference = new AppPreference(activity.getApplicationContext());
        try {

            member = new Gson().fromJson(preference.getMemberData(), Member.class);
            Log.e("userData", preference.getUserData());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

    }

    @Override
    public int getCount() {
        return fundRequestDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return fundRequestDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(R.layout.fundrequestlayout,parent,false);



        TextView txt_Name= (TextView) view.findViewById(R.id.txt_Name);
        TextView txt_location= (TextView) view.findViewById(R.id.txt_location);
        CircleImageView img_profileImage = (CircleImageView) view.findViewById(R.id.img_profileImage);

        Button btn_decline= (Button) view.findViewById(R.id.btn_decline);
        Button btn_accept= (Button) view.findViewById(R.id.btn_accept);



        String getImage = "";

        getImage = W.FILE_URL +  fundRequestDataList.get(position).getImage();

        Picasso.with(activity)
                .load(getImage)
                .into(img_profileImage);

        txt_Name.setText(fundRequestDataList.get(position).getTitle());
        txt_location.setText(fundRequestDataList.get(position).getLocation()+"\n"+fundRequestDataList.get(position).getCity_name()+","+fundRequestDataList.get(position).getState()+" "+fundRequestDataList.get(position).getZip_code());




        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accpt_decline(fundRequestDataList.get(position).getUser_id(),fundRequestDataList.get(position).getRole_id(),"1",position);
            }
        });

        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                accpt_decline(fundRequestDataList.get(position).getUser_id(),fundRequestDataList.get(position).getRole_id(),"2",position);

            }
        });


        return view;
    }


    public void  accpt_decline(String user_id, String role_id, String status, final int position)
    {
        adminAPI = ServiceGenerator.getAPIClass();
        member_id = member.getId();
        dialog = new CustomDialog(activity);
        dialog.show();
        Call<AppModel> Decline=adminAPI.Acc_Decline_Request(user_id,member_id,status,role_id);
        Decline.enqueue(new Callback<AppModel>() {
            @Override
            public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                dialog.dismiss();
                AppModel model = response.body();

                if(model!=null){
                    if(model.isStatus()){

                        fundRequestDataList.remove(position);

                    }else {
                        C.INSTANCE.showToast(activity , model.getMessage());
                    }

                    notifyDataSetChanged();

                }else{
                    C.INSTANCE.defaultError(activity);
                }



            }

            @Override
            public void onFailure(Call<AppModel> call, Throwable t) {
                dialog.dismiss();



            }
        });





    }

}
