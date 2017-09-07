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
import com.fundit.adapter.NewsFeedAdapter;
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
 * Created by NWSPL-17 on 17-Aug-17.
 */

public class NewsFeedFragment extends Fragment {



    View view;
    ListView listNewsfeed ;
    AppPreference preference;
    AdminAPI adminAPI;
    CustomDialog dialog;


    List<CampaignListResponse.CampaignList> listResponse = new ArrayList<>();
    NewsFeedAdapter newsfeedAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_newsfeed , container , false);

        preference = new AppPreference(getActivity());
        adminAPI = ServiceGenerator.getAPIClass();
        dialog = new CustomDialog(getActivity());


        fetchIDs();


        return view;
    }

    private void fetchIDs() {

        listNewsfeed = (ListView) view.findViewById(R.id.listNewsfeed);
        newsfeedAdapter = new NewsFeedAdapter(listResponse , getActivity());
        listNewsfeed.setAdapter(newsfeedAdapter);


        dialog.show();
        Call<CampaignListResponse> campaignResponse = null;

        campaignResponse = adminAPI.ApprovedCampaign(preference.getUserID() , preference.getTokenHash() , preference.getUserRoleID()  , C.PRESENT);
        campaignResponse.enqueue(new Callback<CampaignListResponse>() {
            @Override
            public void onResponse(Call<CampaignListResponse> call, Response<CampaignListResponse> response) {
                dialog.dismiss();
                CampaignListResponse campaignList = response.body();
                if(campaignList!=null){

                    if(campaignList.isStatus()){
                        listResponse.addAll(campaignList.getData());
                    }else {

                        C.INSTANCE.showToast(getActivity() , campaignList.getMessage());
                        // FOR_NOW_ITS_NOTHING
                    }

                    newsfeedAdapter.notifyDataSetChanged();
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
