package com.fundit.fragmet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fundit.Bean.MemberRequestBean;
import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.W;
import com.fundit.adapter.MemberRequestAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.apis.ServiceHandler;
import com.fundit.helper.CustomDialog;
import com.fundit.model.Member;
import com.fundit.model.Organization;
import com.fundit.model.User;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NWSPL-17 on 03-Aug-17.
 */

public class MemberRequestFragment extends Fragment {

    View view;
    AppPreference preference;
    AdminAPI adminAPI;

    ListView list_mrequest;

    MemberRequestAdapter memberRequestAdapter;

    List<MemberRequestBean> requestBeen = new ArrayList<>();

    CustomDialog dialog;

    String json = "";
    User user = new User();
    Member member = new Member();
    Organization organization = new Organization();

    LinearLayout layout_request;
    TextView txt_count;




    public MemberRequestFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_member_request, container, false);
        preference = new AppPreference(getActivity());
        adminAPI = ServiceGenerator.getAPIClass();
        dialog = new CustomDialog(getActivity());
        try {
            user = new Gson().fromJson(preference.getUserData(), User.class);
            member = new Gson().fromJson(preference.getMemberData(), Member.class);
            organization = new Gson().fromJson(preference.getMemberData(), Organization.class);
            Log.e("userData", preference.getUserData());
            Log.e("memberData" , preference.getMemberData());

            Log.e("orgid" , "1" + member.getOrganization().getId());
            Log.e("orgid" , "2" + member.getOrganization().getOrganization_id());
            Log.e("orgid" , "3" + member.getId());

        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        fetchIDs();


        return view;
    }

    private void fetchIDs() {

        layout_request = (LinearLayout) view.findViewById(R.id.layout_request) ;
        txt_count = (TextView) view.findViewById(R.id.txt_count) ;


        list_mrequest = (ListView) view.findViewById(R.id.list_mrequest);
        memberRequestAdapter = new MemberRequestAdapter(requestBeen, getActivity());
        list_mrequest.setAdapter(memberRequestAdapter);

        int getCount = preference.getMemberCount();

        if(getCount <=0){
            layout_request.setVisibility(View.GONE);
        }else {
            txt_count.setText(String.valueOf(getCount));
        }




        new GetMemberRequests().execute();


    }


    public class GetMemberRequests extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {

                dialog.show();
                dialog.setCancelable(false);

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... params) {


            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("user_id", preference.getUserID()));
            pairs.add(new BasicNameValuePair("tokenhash", preference.getTokenHash()));
            pairs.add(new BasicNameValuePair("status", "0"));

            if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
                pairs.add(new BasicNameValuePair("organization_id", preference.getUserID()));


            }

            if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
                pairs.add(new BasicNameValuePair("fundspot_id", preference.getUserID()));
            }


            json = new ServiceHandler().makeServiceCall(W.BASE_URL + W.MemberRequest, ServiceHandler.POST, pairs);


            Log.e("paramters" ,"" + pairs);
            Log.e("json" , json);


            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();


            try {

                if (s.equalsIgnoreCase("") || s.isEmpty()) {
                    C.INSTANCE.noInternet(getActivity());
                } else {

                    JSONObject mainObject = new JSONObject(s);

                    boolean status = true;
                    String message = "";

                    status = mainObject.getBoolean("status");
                    message = mainObject.getString("message");
                    Log.e("Letssee" , "-->" + mainObject.getBoolean("status"));
                    if (status==true) {

                        JSONArray dataAraay = mainObject.getJSONArray("data");
                        Log.e("Letssee" , "-->" + mainObject.getJSONArray("data"));
                        for (int i = 0; i < dataAraay.length(); i++) {


                            JSONObject object = dataAraay.getJSONObject(i);

                            JSONObject memberObject = object.getJSONObject("Member");

                            MemberRequestBean memberRequestBean = new MemberRequestBean();

                            memberRequestBean.setMemberId(memberObject.getString("id"));
                            memberRequestBean.setLocation(memberObject.getString("location"));
                            memberRequestBean.setZip_code(memberObject.getString("zip_code"));
                            memberRequestBean.setContact_info(memberObject.getString("contact_info"));
                            memberRequestBean.setImage(memberObject.getString("image"));
                            memberRequestBean.setFirst_name(memberObject.getString("first_name"));
                            memberRequestBean.setLast_name(memberObject.getString("last_name"));

                            JSONObject userObject = object.getJSONObject("User");

                            memberRequestBean.setUserId(userObject.getString("id"));
                            memberRequestBean.setRole_id(userObject.getString("role_id"));
                            memberRequestBean.setTitle(userObject.getString("title"));
                            memberRequestBean.setEmail_id(userObject.getString("email_id"));

                            JSONObject stateObject = object.getJSONObject("State");

                            memberRequestBean.setState_name(stateObject.getString("name"));


                            JSONObject cityObject = object.getJSONObject("City");

                            memberRequestBean.setCity_name(cityObject.getString("name"));



                            requestBeen.add(memberRequestBean);

                        }
                        memberRequestAdapter.notifyDataSetChanged();
                    }else {

                        C.INSTANCE.showToast(getContext() , message);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}
