package com.fundit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.adapter.FundorgrequestAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.FundRequest;
import com.fundit.model.Member;
import com.fundit.model.Organization;
import com.fundit.model.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FundOrganizationRequestActivity extends AppCompatActivity {

    ListView   list_fundrequest;
    CustomDialog dialog;
    AdminAPI adminAPI;
    FundorgrequestAdapter fundorgrequestAdapter;

    Member member = new Member();
    AppPreference preference;
    String member_id =  "";

    List<FundRequest.FundRequest_Data> fundRequestDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_organization_request);
        dialog=new CustomDialog(FundOrganizationRequestActivity.this);
        adminAPI = ServiceGenerator.getAPIClass();
        preference = new AppPreference(this);
        try {

            member = new Gson().fromJson(preference.getMemberData(), Member.class);
            Log.e("userData", preference.getUserData());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }


        fetchIds();

    }

    public void fetchIds()
    {
        list_fundrequest= (ListView) findViewById(R.id.list_fundrequest);
        fundorgrequestAdapter=new FundorgrequestAdapter(fundRequestDataList,FundOrganizationRequestActivity.this);
        list_fundrequest.setAdapter(fundorgrequestAdapter);


        member_id = member.getId();

        Log.e("memberId" , "-->" + member_id);

        dialog.show();
        Call<FundRequest> fundResponse=adminAPI.Fund_Org_Request(member_id);
        fundResponse.enqueue(new Callback<FundRequest>() {
            @Override
            public void onResponse(Call<FundRequest> call, Response<FundRequest> response) {
                dialog.dismiss();
                fundRequestDataList.clear();
                FundRequest fundRequest = response.body();

                if(fundRequest!=null){
                    if(fundRequest.isStatus()){

                        fundRequestDataList.addAll(fundRequest.getData());



                    }else {
                        C.INSTANCE.showToast(getApplicationContext() , fundRequest.getMessage());
                    }

                }else{
                    C.INSTANCE.defaultError(getApplicationContext());
                }

                fundorgrequestAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<FundRequest> call, Throwable t) {
                dialog.dismiss();



            }
        });


    }






}
