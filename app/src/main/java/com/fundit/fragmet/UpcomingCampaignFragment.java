package com.fundit.fragmet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingCampaignFragment extends Fragment {

    View view;
    AppPreference preference;
    AdminAPI adminAPI;
    ListView listCampaigns;

    public UpcomingCampaignFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upcoming_campaign, container, false);
        adminAPI = ServiceGenerator.getAPIClass();
        preference = new AppPreference(getActivity());

        fetchIDs();

        return view;
    }

    private void fetchIDs() {
        listCampaigns = (ListView) view.findViewById(R.id.listCampaigns);

    }

}
