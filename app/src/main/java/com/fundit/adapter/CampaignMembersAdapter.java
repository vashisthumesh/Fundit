package com.fundit.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.AppModel;
import com.fundit.model.MemberResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NWSPL-17 on 16-Aug-17.
 */

public class CampaignMembersAdapter extends BaseAdapter {

    List<MemberResponse.MemberData> memberResponses = new ArrayList<>();
    Context context;
    LayoutInflater inflater = null;
    CustomDialog dialog;
    AdminAPI adminAPI;
    AppPreference preference;
    String campaignId = "";
    Activity activity;

    public CampaignMembersAdapter(List<MemberResponse.MemberData> memberResponses, Context context , String campaignId , Activity activity) {
        this.memberResponses = memberResponses;
        this.context = context;
        this.campaignId = campaignId;
        this.activity = activity;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.layout_member_list , parent , false);

        TextView textMemberName =(TextView) view.findViewById(R.id.text_name);
        ImageView imgDelete = (ImageView) view.findViewById(R.id.img_delete);


        textMemberName.setText(memberResponses.get(position).getFirst_name() + " " + memberResponses.get(position).getLast_name());



        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String memberId = memberResponses.get(position).getId();


                Log.e("memberId" , memberId);


                RemoveUserFromCampaign(memberId);
            }
        });






        return view;
    }

    private void RemoveUserFromCampaign(String memberId) {

        Log.e("campaignId" , campaignId);


        adminAPI = ServiceGenerator.getAPIClass();
        preference = new AppPreference(context);
        dialog = new CustomDialog(activity);
        dialog.show();
        final Call<AppModel> removeUser = adminAPI.RemoveUserFromCampaign(preference.getUserID() , preference.getUserRoleID() ,campaignId ,memberId);
        removeUser.enqueue(new Callback<AppModel>() {
            @Override
            public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                dialog.dismiss();
                AppModel appModel = response.body();
                if(appModel!=null){

                    C.INSTANCE.showToast(context , appModel.getMessage());

                }else {
                    C.INSTANCE.defaultError(context);
                }
            }

            @Override
            public void onFailure(Call<AppModel> call, Throwable t) {
                dialog.dismiss();
                C.INSTANCE.errorToast(context , t);
            }
        });






    }
}
