package com.fundit.fragmet;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fundit.MyCardDetailsActivity;
import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.adapter.FundspotListAdapter;
import com.fundit.adapter.GetDataAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.AppModel;
import com.fundit.model.Fundspot;
import com.fundit.model.GetDataResponses;
import com.fundit.model.Member;
import com.fundit.model.Organization;
import com.fundit.model.User;
import com.fundit.model.VerifyResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NWSPL-17 on 07-Sep-17.
 */

public class MyBrowseCampaign extends Fragment {


    View view ;

    AppPreference preferences;
    AdminAPI adminAPI;
    CustomDialog dialog;

    EditText auto_searchUser , txt_city , txt_zip ;
    RadioGroup rg_type ;
    RadioButton rb_fundspot , rb_org ;

    Button btn_search;
    ListView list;

    GetDataAdapter fundspotListAdapter;

    List<GetDataResponses.Data> verifyResponseDatas = new ArrayList<>();

    Fundspot fundspot = new Fundspot();
    Organization organization = new Organization();
    User user = new User();
    Member member = new Member();

    boolean fundspotSelected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_browse , container , false);

        preferences = new AppPreference(getActivity());
        adminAPI = ServiceGenerator.getAPIClass();
        dialog = new CustomDialog(getActivity());


        try{

            fundspot = new Gson().fromJson(preferences.getMemberData() , Fundspot.class);
            organization = new Gson().fromJson(preferences.getMemberData() , Organization.class);
            user = new Gson().fromJson(preferences.getMemberData() , User.class);
            member = new Gson().fromJson(preferences.getMemberData() , Member.class);



        }catch (Exception e) {

            e.printStackTrace();

        }




        fetchIds();

        return view;
    }

    private void fetchIds() {


        auto_searchUser = (EditText) view.findViewById(R.id.auto_searchUser);
        txt_city = (EditText) view.findViewById(R.id.txt_city);
        txt_zip = (EditText) view.findViewById(R.id.txt_zip);


        rg_type = (RadioGroup) view.findViewById(R.id.rg_type);

        rb_fundspot = (RadioButton) view.findViewById(R.id.rb_fundspot);
        rb_org = (RadioButton) view.findViewById(R.id.rb_org);

        btn_search = (Button) view.findViewById(R.id.btn_search);
        list = (ListView) view.findViewById(R.id.list);

        if(preferences.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)){
            txt_city.setText(organization.getCity().getName());
            txt_zip.setText(organization.getZip_code());


        }

        if(preferences.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)){

            txt_city.setText(fundspot.getCity().getName());
            txt_zip.setText(fundspot.getZip_code());

        }

        if(preferences.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)){

            txt_city.setText(member.getCity().getName());
            txt_zip.setText(member.getZip_code());


        }




        rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {


                if(checkedId == R.id.rb_fundspot){

                    fundspotSelected = true;
                    getAllFundspot();

                }


                if(checkedId == R.id.rb_org){

                    fundspotSelected = false;
                    getAllOrganization();

                }




            }
        });







    }

    private void getAllFundspot() {

        dialog.show();
        final Call<GetDataResponses> verifyResponse = adminAPI.GET_ALL_FUNDSPOT(preferences.getUserID() , fundspot.getCity_id() , fundspot.getZip_code());
        verifyResponse.enqueue(new Callback<GetDataResponses>() {
            @Override
            public void onResponse(Call<GetDataResponses> call, Response<GetDataResponses> response) {
                verifyResponseDatas.clear();
                dialog.dismiss();

                GetDataResponses responses = response.body();
                Log.e("response", new Gson().toJson(response));
                if (response != null) {
                    if (responses.isStatus()) {

                        Log.e("datas" , "-->");
                        verifyResponseDatas.addAll(responses.getData());
                    }
                } else {
                    C.INSTANCE.defaultError(getActivity());
                }

                fundspotListAdapter = new GetDataAdapter(verifyResponseDatas , getActivity() , fundspotSelected);
                list.setAdapter(fundspotListAdapter);

                fundspotListAdapter.notifyDataSetChanged();
            }


            @Override
            public void onFailure(Call<GetDataResponses> call, Throwable t) {

                dialog.dismiss();
                C.INSTANCE.errorToast(getActivity(), t);

            }

        });







    }


    private void getAllOrganization() {

        dialog.show();
        Call<GetDataResponses> verifyResponse = adminAPI.GET_ALL_ORGANIZATION(preferences.getUserID() , fundspot.getCity_id() , fundspot.getZip_code());
        verifyResponse.enqueue(new Callback<GetDataResponses>() {
            @Override
            public void onResponse(Call<GetDataResponses> call, Response<GetDataResponses> response) {
                verifyResponseDatas.clear();
                dialog.dismiss();

                GetDataResponses responses = response.body();
                Log.e("response", new Gson().toJson(response));
                if (response != null) {
                    if (responses.isStatus()) {

                        Log.e("datas" , "-->");
                        verifyResponseDatas.addAll(responses.getData());
                    }
                } else {
                    C.INSTANCE.defaultError(getActivity());
                }
                fundspotListAdapter = new GetDataAdapter(verifyResponseDatas , getActivity() , fundspotSelected);
                list.setAdapter(fundspotListAdapter);
                fundspotListAdapter.notifyDataSetChanged();
            }


            @Override
            public void onFailure(Call<GetDataResponses> call, Throwable t) {

                dialog.dismiss();
                C.INSTANCE.errorToast(getActivity(), t);

            }

        });







    }

}
