package com.fundit.fundspot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.adapter.FundspotListAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.Fundspot;
import com.fundit.model.FundspotListResponse;
import com.fundit.model.Member;
import com.fundit.model.User;
import com.fundit.model.VerifyResponse;
import com.fundit.organization.FundspotProductListActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NWSPL-17 on 10-Aug-17.
 */

public class OrganizationListActivity extends AppCompatActivity {

    ListView list_fundspots;
    int REQUEST_PRODUCT = 369;

    AdminAPI adminAPI;
    AppPreference preference;
    List<VerifyResponse.VerifyResponseData> fundSpotList=new ArrayList<>();
    FundspotListAdapter fundspotListAdapter;
    CustomDialog dialog;

    TextView text_selection ;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fundspot_list);

        adminAPI = ServiceGenerator.getAPIClass();
        preference = new AppPreference(this);
        dialog=new CustomDialog(this);

        try {
            user = new Gson().fromJson(preference.getUserData(), User.class);
/*
            member = new Gson().fromJson(preference.getMemberData(), Member.class);
            fundspot = new Gson().fromJson(preference.getMemberData(), Fundspot.class);
*/
            Log.e("userData", preference.getUserData());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }


        fetchIDs();
        setupToolbar();

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionTitle.setText("Create Campaign Request");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void fetchIDs() {
        list_fundspots = (ListView) findViewById(R.id.list_fundspots);
        fundspotListAdapter = new FundspotListAdapter(fundSpotList,this);
        list_fundspots.setAdapter(fundspotListAdapter);
        text_selection = (TextView) findViewById(R.id.text_selection);

        text_selection.setText("Select Organization");




        list_fundspots.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), FundspotProductListActivity.class);
                intent.putExtra("fundspotName", fundSpotList.get(i).getOrganization().getTitle());
                intent.putExtra("fundspotID", preference.getUserID());
                intent.putExtra("organizationID" , fundSpotList.get(i).getOrganization().getUser_id());

                Log.e("OrganizationID" , "-->"  +fundSpotList.get(i).getOrganization().getUser_id());

                startActivityForResult(intent,REQUEST_PRODUCT);
            }
        });

        dialog.show();
        Call<FundspotListResponse> fundSpotCall=adminAPI.browseOrganization(preference.getUserID(),preference.getTokenHash());
        Log.e("parameters" , "" +preference.getUserID() + "--->" + preference.getTokenHash());
        fundSpotCall.enqueue(new Callback<FundspotListResponse>() {
            @Override
            public void onResponse(Call<FundspotListResponse> call, Response<FundspotListResponse> response) {
                dialog.dismiss();
                FundspotListResponse listResponse=response.body();
                if(listResponse!=null){
                    if(listResponse.isStatus()){
                        fundSpotList.addAll(listResponse.getData());
                    }
                    else {
                        C.INSTANCE.showToast(getApplicationContext(),listResponse.getMessage());
                    }
                }
                else {
                    C.INSTANCE.defaultError(getApplicationContext());
                }
                fundspotListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<FundspotListResponse> call, Throwable t) {
                dialog.dismiss();
                C.INSTANCE.errorToast(getApplicationContext(),t);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_PRODUCT && resultCode == RESULT_OK && data!=null){
            setResult(RESULT_OK,data);
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }






}
