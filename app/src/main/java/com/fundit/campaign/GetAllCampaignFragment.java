package com.fundit.campaign;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.W;
import com.fundit.adapter.CampaignRequestAdapter;
import com.fundit.apis.ServiceHandler;
import com.fundit.helper.CustomDialog;
import com.fundit.model.CampaignListResponse;
import com.fundit.model.Fundspot;
import com.fundit.model.Member;
import com.fundit.model.Organization;
import com.fundit.model.User;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

/**
 * Created by NWSPL-17 on 11-Aug-17.
 */

public class GetAllCampaignFragment extends Fragment {

    ListView listCampaign;
    AppPreference preference;
    CampaignRequestAdapter requestAdapter;
    CustomDialog dialog;


    User user = new User();
    Member member = new Member();
    Organization organization = new Organization();
    Fundspot fundspot = new Fundspot();

    View view;

    List<GetAllCampaignDataBean> campaignData = new ArrayList<>();

    String json = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_campaign_request, container, false);

        preference = new AppPreference(getActivity());


        try {
            user = new Gson().fromJson(preference.getUserData(), User.class);
            member = new Gson().fromJson(preference.getMemberData(), Member.class);
            fundspot = new Gson().fromJson(preference.getMemberData(), Fundspot.class);
            organization = new Gson().fromJson(preference.getMemberData(), Organization.class);
            Log.e("userData", preference.getUserData());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }


        fetchIDs();


        return view;
    }

    private void fetchIDs() {

        dialog = new CustomDialog(getActivity());

        listCampaign = (ListView) view.findViewById(R.id.listRequests);
  //      requestAdapter = new CampaignRequestAdapter(campaignData, getActivity());
        listCampaign.setAdapter(requestAdapter);


    }


   /* public class GetAllCampaign extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                dialog = new CustomDialog(getActivity());
                dialog.show();
                dialog.setCancelable(false);

            } catch (Exception e) {

            }
        }

        @Override
        protected String doInBackground(Void... params) {


            List<NameValuePair> parameters = new ArrayList<>();

            parameters.add(new BasicNameValuePair("user_id" , preference.getUserID()));
            parameters.add(new BasicNameValuePair("tokenhash" , preference.getTokenHash()));
            parameters.add(new BasicNameValuePair("role_id" , preference.getUserRoleID()));
           // parameters.add(new BasicNameValuePair("status"));
           // parameters.add(new BasicNameValuePair("action_status"));

            if(preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)){

                parameters.add(new BasicNameValuePair("organization_id" , preference.getUserID()));
            }

            if(preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)){


                parameters.add(new BasicNameValuePair("fundspot_id" , preference.getUserID()));
            }


            json = new ServiceHandler(getContext()).makeServiceCall(W.ASYNC_BASE_URL + W.CAMPAIGN_LIST , ServiceHandler.POST , parameters);


            Log.e("parameters" , "" + parameters);
            Log.e("json" , json);



            return json;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            if(s.isEmpty() || s.equalsIgnoreCase("")){

                C.INSTANCE.noInternet(getActivity());
            }else {


















            }







        }
    }*/




}
