package com.fundit.fragmet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    List<CampaignListResponse.CampaignList> campaignArrayList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_present_campaign , container , false);
        preference = new AppPreference(getActivity());
        adminAPI = ServiceGenerator.getAPIClass();
        dialog = new CustomDialog(getActivity());


        fetchIDs();

        return view;
    }

    private void fetchIDs() {

        listCampaign = (ListView) view.findViewById(R.id.listCampaigns);
        campaignAdapter = new ShowCampaignAdapter(campaignArrayList , getActivity());
        listCampaign.setAdapter(campaignAdapter);


        dialog.show();
        Call<CampaignListResponse> campaignResponse = null;

        campaignResponse = adminAPI.ApprovedCampaign(preference.getUserID() , preference.getTokenHash() , preference.getUserRoleID()  , C.PRESENT);
        campaignResponse.enqueue(new Callback<CampaignListResponse>() {
            @Override
            public void onResponse(Call<CampaignListResponse> call, Response<CampaignListResponse> response) {
                dialog.dismiss();
                CampaignListResponse campaignList = response.body();
                if(campaignList!=null){
                    C.INSTANCE.showToast(getActivity() , campaignList.getMessage());
                    if(campaignList.isStatus()){
                        campaignArrayList.addAll(campaignList.getData());
                    }else {

                        // FOR_NOW_ITS_NOTHING
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
