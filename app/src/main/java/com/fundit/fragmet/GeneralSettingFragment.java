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
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;

/**
 * Created by NWSPL-17 on 05-Aug-17.
 */

public class GeneralSettingFragment extends Fragment {


    View view;
    AppPreference preference;
    AdminAPI adminAPI;

    TextView txtNotification, txtCampaign, txtChangePwd, txtTouch, txtLegal, txtFeedback;


    public GeneralSettingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_general_setting, container, false);

        preference = new AppPreference(getActivity());
        adminAPI = ServiceGenerator.getAPIClass();

        fetchIDs();


        return view;
    }

    private void fetchIDs() {


        txtNotification = (TextView) view.findViewById(R.id.txt_notification);
        txtCampaign = (TextView) view.findViewById(R.id.txt_fundraising);
        txtChangePwd = (TextView) view.findViewById(R.id.txt_change_pwd);
        txtTouch = (TextView) view.findViewById(R.id.txt_touch);
        txtLegal = (TextView) view.findViewById(R.id.txt_legal);
        txtFeedback = (TextView) view.findViewById(R.id.txt_feedback);


        if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {

            txtCampaign.setVisibility(View.VISIBLE);

        } else {

            txtCampaign.setVisibility(View.GONE);
        }

        txtCampaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CampaignSetting.class);
                intent.putExtra("editMode" , true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        txtChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


    }
}
