package com.fundit.fragmet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fundit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyCampaignsFragment extends Fragment {


    public MyCampaignsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_campaigns, container, false);
    }

}
