package com.fundit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.J;
import com.fundit.adapter.SendMessageAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.Member;
import com.fundit.model.MemberListResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMemberActivity extends AppCompatActivity {


    EditText searchUsers ;
    ImageView search ;
    ListView listView;

    AppPreference preference ;
    CustomDialog dialog;
    AdminAPI adminAPI;

    List<Member> searchedMembers = new ArrayList<>();

    String campaignId = "";


    SendMessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        Intent intent = getIntent();
        campaignId = intent.getStringExtra("campaignId");




        preference = new AppPreference(getApplicationContext());
        dialog = new CustomDialog(this);
        adminAPI = ServiceGenerator.getAPIClass();


        setupToolBar();
        fetchIds();





    }

    private void setupToolBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionTitle.setText("My Members");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void fetchIds() {

        searchUsers = (EditText) findViewById(R.id.auto_searchUser);
        search = (ImageView) findViewById(R.id.img_search);
        listView = (ListView) findViewById(R.id.list_users);

        messageAdapter = new SendMessageAdapter(searchedMembers , getApplicationContext() , true);
        listView.setAdapter(messageAdapter);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getText = searchUsers.getText().toString();

                SearchMembers(getText);


            }
        });


        searchUsers.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                String getText = searchUsers.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    SearchMembers(getText);
                    return true;
                }
                return false;
            }
        });


    }

    private void SearchMembers(String getText) {


        dialog.show();
        Call<MemberListResponse> memberListResponseCall = adminAPI.getMemberList(preference.getUserID(), preference.getTokenHash(), getText);

        Log.e("parameters" , "-->" + preference.getUserID() + "--->" + preference.getTokenHash());
        memberListResponseCall.enqueue(new Callback<MemberListResponse>() {
            @Override
            public void onResponse(Call<MemberListResponse> call, Response<MemberListResponse> response) {
                dialog.dismiss();
                MemberListResponse memberListResponse = response.body();

                Log.e("AllNames" , "--->" + new Gson().toJson(memberListResponse));

                if (memberListResponse != null) {
                    if (memberListResponse.isStatus()) {
                        searchedMembers.addAll(memberListResponse.getData());
                    }
                } else {
                    C.INSTANCE.defaultError(getApplicationContext());
                }


                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MemberListResponse> call, Throwable t) {
                dialog.dismiss();
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
