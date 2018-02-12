package com.fundit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.J;
import com.fundit.adapter.CampaignMembersAdapter;
import com.fundit.adapter.MemberListAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.CampaignListResponse;
import com.fundit.model.Member;
import com.fundit.model.MemberListResponse;
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

    CustomDialog dialog;

    CampaignMembersAdapter memberAdapter;

    List<MemberResponse.MemberData> memberData = new ArrayList<>();

    List<Member> memberList = new ArrayList<>();
    ArrayList<String> memberNames = new ArrayList<>();
    ArrayAdapter<String> memberArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member_to_campaign);

        preference = new AppPreference(getApplicationContext());
        adminAPI = ServiceGenerator.getAPIClass();
        dialog = new CustomDialog(AddMemberToCampaign.this);

        Intent intent = getIntent();
        campaignId = intent.getStringExtra("campaignId");


        setupToolbar();
        fetchIds();
    }

    private void setupToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionTitle.setText("New Message");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void fetchIds() {


        autoSearchMember = (AutoCompleteTextView) findViewById(R.id.auto_searchMember);
        textTitle = (TextView) findViewById(R.id.txt_Titleadd);
        textCouponTitle = (TextView) findViewById(R.id.txt_coupon);
        listMembers = (ListView) findViewById(R.id.list_members);

        memberAdapter = new CampaignMembersAdapter(memberData, getApplicationContext(), campaignId, AddMemberToCampaign.this);
        listMembers.setAdapter(memberAdapter);

        autoSearchMember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text = autoSearchMember.getText().toString().trim();
                Log.e("getId", "---->" + memberList.get(i).getId());

                Intent intent = new Intent(getApplicationContext(), AddMembersActivity.class);
                intent.putExtra("memberId", memberList.get(i).getId());
                intent.putExtra("campaignId", campaignId);
                startActivity(intent);
            }
        });


        dialog.show();
        final Call<MemberResponse> getMemberData = adminAPI.GetMemberForCampaign(preference.getUserID(), preference.getUserRoleID(), campaignId);
        Log.e("parameters", "--->" + preference.getUserRoleID() + "--->" + preference.getUserID() + "--->" + campaignId);
        getMemberData.enqueue(new Callback<MemberResponse>() {
            @Override
            public void onResponse(Call<MemberResponse> call, Response<MemberResponse> response) {
                dialog.dismiss();
                MemberResponse memberResponse = response.body();
                if (memberResponse != null) {
                    if (memberResponse.isStatus()) {
                        memberData.addAll(memberResponse.getData());

                        SearchMembers();
                        Log.e("data", "" + memberResponse.getData());
                    } else {
                        C.INSTANCE.showToast(getApplicationContext(), memberResponse.getMessage());
                    }
                } else {

                    C.INSTANCE.defaultError(getApplicationContext());
                }

                memberAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<MemberResponse> call, Throwable t) {

                dialog.dismiss();
                C.INSTANCE.errorToast(getApplicationContext(), t);

            }
        });


    }

    private void SearchMembers() {
        Call<MemberListResponse> memberListResponseCall = null;


        if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
            memberListResponseCall = adminAPI.getAllMemberList(preference.getUserID(), preference.getTokenHash(), preference.getUserRoleID(), preference.getUserID(), null);
        }

        if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
            memberListResponseCall = adminAPI.getAllMemberList(preference.getUserID(), preference.getTokenHash(), preference.getUserRoleID(), null, preference.getUserID());
        }

        memberListResponseCall.enqueue(new Callback<MemberListResponse>() {
            @Override
            public void onResponse(Call<MemberListResponse> call, Response<MemberListResponse> response) {

                MemberListResponse memberListResponse = response.body();
                if (memberListResponse != null) {
                    if (memberListResponse.isStatus()) {
                        memberList.addAll(memberListResponse.getData());
                        memberNames.addAll(memberListResponse.getNames());
                    }
                } else {
                    C.INSTANCE.defaultError(getApplicationContext());
                }

                Log.e("Size", "--" + memberNames.size());
                memberArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_textview, memberNames);
                autoSearchMember.setAdapter(memberArrayAdapter);
            }

            @Override
            public void onFailure(Call<MemberListResponse> call, Throwable t) {

                C.INSTANCE.errorToast(getApplicationContext(), t);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        J.GetNotificationCountGlobal(preference.getUserID() , preference.getTokenHash() , preference , getApplicationContext() , this);
    }
}
