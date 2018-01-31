package com.fundit.fragmet;


import android.os.Bundle;
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
 * A simple {@link Fragment} subclass.
 */
public class UpcomingCampaignFragment extends Fragment {

    View view;
    AppPreference preference;
    AdminAPI adminAPI;
    ListView listCampaign;
    ShowCampaignAdapter campaignAdapter;
    CustomDialog dialog;

    List<CampaignListResponse.CampaignList> campaignArrayList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_upcoming_campaign , container , false);
        preference = new AppPreference(getActivity());
        adminAPI = ServiceGenerator.getAPIClass();
        dialog = new CustomDialog(getActivity());


        fetchIDs();

        return view;
    }

    private void fetchIDs() {

        listCampaign = (ListView) view.findViewById(R.id.listCampaigns);
        campaignAdapter = new ShowCampaignAdapter(campaignArrayList , getActivity() , false);
        listCampaign.setAdapter(campaignAdapter);


        dialog.show();
        Call<CampaignListResponse> campaignResponse = null;

        campaignResponse = adminAPI.ApprovedCampaign(preference.getUserID() , preference.getTokenHash() , preference.getUserRoleID()  , C.UPCOMING);
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
