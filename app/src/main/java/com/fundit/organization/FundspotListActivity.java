package com.fundit.organization;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fundit.R;
import com.fundit.VerificationActivity;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.adapter.FundspotListAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.FundspotListResponse;
import com.fundit.model.VerifyResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FundspotListActivity extends AppCompatActivity {

    ListView list_fundspots;
    int REQUEST_PRODUCT = 369;

    AdminAPI adminAPI;
    AppPreference preference;
    List<VerifyResponse.VerifyResponseData> fundSpotList=new ArrayList<>();
    FundspotListAdapter fundspotListAdapter;
    CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fundspot_list);

        adminAPI = ServiceGenerator.getAPIClass();
        preference = new AppPreference(this);
        dialog=new CustomDialog(this);

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

        list_fundspots.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if((fundSpotList.get(i).getFundspot().getFundspot_percent().equalsIgnoreCase("0") && fundSpotList.get(i).getFundspot().getOrganization_percent().equalsIgnoreCase("0"))){
                    AlertDialog.Builder builder=new AlertDialog.Builder(FundspotListActivity.this);
                    builder.setTitle("Sorry, Fundraiser settings not valid");
                    builder.setMessage("You can't start campaign with this fundspot");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog bDialog=builder.create();
                    bDialog.show();
                }else if(fundSpotList.get(i).getFundspot().getProduct_count().equalsIgnoreCase("0")){
                    AlertDialog.Builder builder=new AlertDialog.Builder(FundspotListActivity.this);
                    builder.setTitle("Sorry,No product available");
                    builder.setMessage("You can't start campaign with this fundspot");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog bDialog=builder.create();
                    bDialog.show();
                }else {
                    Intent intent = new Intent(getApplicationContext(), FundspotProductListActivity.class);
                    intent.putExtra("fundspotName", fundSpotList.get(i).getFundspot().getTitle());
                    intent.putExtra("fundspotID", fundSpotList.get(i).getFundspot().getUser_id());
                    startActivityForResult(intent,REQUEST_PRODUCT);
                }



            }
        });

        dialog.show();
        Call<FundspotListResponse> fundSpotCall=adminAPI.browseFundspots(preference.getUserID(),preference.getTokenHash());
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
