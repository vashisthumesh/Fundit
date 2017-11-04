package com.fundit.fragmet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fundit.CampaignSetting;
import com.fundit.ChangePasswordActivity;
import com.fundit.R;
import com.fundit.SendFeedbackActivity;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;

/**
 * Created by NWSPL-17 on 05-Aug-17.
 */

public class HelpFragment extends Fragment {

    View view;

    public HelpFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_help, container, false);

        return view;
    }


}
