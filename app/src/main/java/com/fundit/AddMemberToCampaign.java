package com.fundit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.adapter.CampaignMembersAdapter;
import com.fundit.adapter.MemberListAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.CampaignListResponse;
import com.fundit.model.MemberResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMemberToCampaign extends AppCompatActivity {


    AutoCompleteTextView autoSearchMember;
    TextView textTitle, textCouponTitle;
    ListView listMembers;

    AppPreference preference;
    AdminAPI adminAPI;


    String campaignId = "";

    //CustomDialog dialog;

    CampaignMembersAdapter memberAdapter;

    List<MemberResponse.MemberData> memberData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member_to_campaign);

        preference = new AppPreference(getApplicationContext());
        adminAPI = ServiceGenerator.getAPIClass();
       // dialog = new CustomDialog(getApplicationContext());

        Intent intent = getIntent();
        campaignId = intent.getStringExtra("campaignId");


        setupToolbar();
        fetchIds();
    }

    private void setupToolbar() {
    }

    private void fetchIds() {


        autoSearchMember = (AutoCompleteTextView) findViewById(R.id.auto_searchMember);
        textTitle = (TextView) findViewById(R.id.txt_Titleadd);
        textCouponTitle = (TextView) findViewById(R.id.txt_coupon);
        listMembers = (ListView) findViewById(R.id.list_members);

        memberAdapter = new CampaignMembersAdapter(memberData , getApplicationContext());
        listMembers.setAdapter(memberAdapter);

        autoSearchMember.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


       // dialog.show();
        final Call<MemberResponse> getMemberData = adminAPI.GetMemberForCampaign(preference.getUserID(), preference.getUserRoleID(), campaignId);
        Log.e("parameters" , "--->" + preference.getUserRoleID() + "--->" + preference.getUserID() + "--->" + campaignId);
        getMemberData.enqueue(new Callback<MemberResponse>() {
            @Override
            public void onResponse(Call<MemberResponse> call, Response<MemberResponse> response) {
               // dialog.dismiss();
                MemberResponse memberResponse = response.body();
                if (memberResponse != null) {
                    if (memberResponse.isStatus()) {
                        memberData.addAll(memberResponse.getData());

                        Log.e("data" , "" + memberResponse.getData());
                    }else{

                        C.INSTANCE.showToast(getApplicationContext() , memberResponse.getMessage());

                    }



                }else {

                    C.INSTANCE.defaultError(getApplicationContext());
                }

                memberAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<MemberResponse> call, Throwable t) {

             //   dialog.dismiss();
                C.INSTANCE.errorToast(getApplicationContext() , t);

            }
        });


    }
}
