package com.fundit.fragmet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.adapter.ShowCampaignAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.CampaignListResponse;
import com.fundit.model.Fundspot;
import com.fundit.model.Member;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NWSPL-17 on 12-Aug-17.
 */

public class PresentCampaignFragment extends Fragment {


    View view;
    AppPreference preference;
    AdminAPI adminAPI;
    ListView listCampaign;
    ShowCampaignAdapter campaignAdapter;
    CustomDialog dialog;
    Member member=new Member();

    List<CampaignListResponse.CampaignList> campaignArrayList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_present_campaign , container , false);
        preference = new AppPreference(getActivity());
        adminAPI = ServiceGenerator.getAPIClass();
        dialog = new CustomDialog(getActivity());
        try {
            member = new Gson().fromJson(preference.getMemberData() , Member.class);
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }


        fetchIDs();

        return view;
    }

    private void fetchIDs() {

        listCampaign = (ListView) view.findViewById(R.id.listCampaigns);
        campaignAdapter = new ShowCampaignAdapter(campaignArrayList , getActivity() , false);
        listCampaign.setAdapter(campaignAdapter);


        dialog.show();
        Call<CampaignListResponse> campaignResponse = null;

        if(preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER))
        {
            campaignResponse = adminAPI.SellerCampaign(member.getId(), C.PRESENT);
        }
        else {
            campaignResponse = adminAPI.ApprovedCampaign(preference.getUserID(), preference.getTokenHash(), preference.getUserRoleID(), C.PRESENT);
        }

        Log.e("parameters" , "-->" + preference.getUserID() + "-->" + preference.getTokenHash() + "-->" + preference.getUserRoleID() + "-->" + C.PRESENT);
        campaignResponse.enqueue(new Callback<CampaignListResponse>() {
            @Override
            public void onResponse(Call<CampaignListResponse> call, Response<CampaignListResponse> response) {
                dialog.dismiss();
                CampaignListResponse campaignList = response.body();
                if(campaignList!=null){

                    if(campaignList.isStatus()){
                        campaignArrayList.addAll(campaignList.getData());
                    }else {
//                        C.INSTANCE.showToast(getContext(), "No Campaign Found");
                    }

                    campaignAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<CampaignListResponse> call, Throwable t) {
                dialog.dismiss();
                C.INSTANCE.errorToast(getActivity() , t);

            }
        });













    }
}
