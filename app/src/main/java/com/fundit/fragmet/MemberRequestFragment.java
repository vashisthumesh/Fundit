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
import com.fundit.a.J;
import com.fundit.a.W;
import com.fundit.adapter.MemberRequestAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.apis.ServiceHandler;
import com.fundit.helper.CustomDialog;
import com.fundit.model.Member;
import com.fundit.model.MemberRequestModel;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NWSPL-17 on 03-Aug-17.
 */

public class MemberRequestFragment extends Fragment implements MemberRequestAdapter.RespondRequest {

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

    boolean isRefereshTimes = false;


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
            Log.e("memberData", preference.getMemberData());

            Log.e("orgid", "1" + member.getOrganization().getId());
            Log.e("orgid", "2" + member.getOrganization().getOrganization_id());
            Log.e("orgid", "3" + member.getId());

        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        preference.setMemberTimes(false);
        fetchIDs();


        return view;
    }

    private void fetchIDs() {

        layout_request = (LinearLayout) view.findViewById(R.id.layout_request);
        txt_count = (TextView) view.findViewById(R.id.txt_count);


        list_mrequest = (ListView) view.findViewById(R.id.list_mrequest);
        memberRequestAdapter = new MemberRequestAdapter(requestBeen, getActivity(), this);
        list_mrequest.setAdapter(memberRequestAdapter);

        int getCount = preference.getMemberCount();

        Log.e("count" , "-->" + getCount);

        if (getCount <= 0) {
            layout_request.setVisibility(View.GONE);
        } else {
            txt_count.setText(String.valueOf(getCount));
        }


       // new GetMemberRequests().execute();

        GetAllMemberRequest();


    }

    @Override
    public void onClick() {

        Log.e("letsCheckClick", "-->");
        isRefereshTimes = true;
       // new GetMemberRequests().execute();

        GetAllMemberRequest();

    }





    private void GetAllMemberRequest() {
        String organizationsId = null, fundspotsId = null;
        if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
            organizationsId = preference.getUserID();
        }
        if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
            fundspotsId = preference.getUserID();
        }
        dialog.show();
        final Call<MemberRequestModel> requestModelCall = adminAPI.MEMBER_REQUEST_MODEL_CALL(preference.getUserID(), preference.getTokenHash(), "0", organizationsId, fundspotsId);
        requestModelCall.enqueue(new Callback<MemberRequestModel>() {
            @Override
            public void onResponse(Call<MemberRequestModel> call, Response<MemberRequestModel> response) {
                dialog.dismiss();
                requestBeen.clear();
                MemberRequestModel requestModel = response.body();
                if (requestModel != null) {
                    if (requestModel.isStatus()) {

                        for (int i = 0; i < requestModel.getData().size(); i++) {

                            MemberRequestBean memberRequestBean = new MemberRequestBean();

                            memberRequestBean.setMemberId(requestModel.getData().get(i).getMember().getId());
                            memberRequestBean.setLocation(requestModel.getData().get(i).getMember().getLocation());
                            memberRequestBean.setZip_code(requestModel.getData().get(i).getMember().getZip_code());
                            memberRequestBean.setContact_info(requestModel.getData().get(i).getMember().getContact_info());
                            memberRequestBean.setImage(requestModel.getData().get(i).getMember().getImage());
                            memberRequestBean.setFirst_name(requestModel.getData().get(i).getMember().getFirst_name());
                            memberRequestBean.setLast_name(requestModel.getData().get(i).getMember().getLast_name());


                            memberRequestBean.setUserId(requestModel.getData().get(i).getUser().getId());
                            memberRequestBean.setRole_id(requestModel.getData().get(i).getUser().getRole_id());
                            memberRequestBean.setTitle(requestModel.getData().get(i).getUser().getTitle());
                            memberRequestBean.setEmail_id(requestModel.getData().get(i).getUser().getEmail_id());


                            memberRequestBean.setState_name(requestModel.getData().get(i).getState().getName());

                            memberRequestBean.setCity_name(requestModel.getData().get(i).getCity().getName());


                            requestBeen.add(memberRequestBean);

                        }
                    }

                    memberRequestAdapter.notifyDataSetChanged();

                    if (isRefereshTimes == true) {
                        J.GetNotificationCountGlobal(preference.getUserID(), preference.getTokenHash(), preference, getContext(), getActivity());
                    }

                    isRefereshTimes = false;

                    RefreshLayout();


                } else {
                    C.INSTANCE.defaultError(getActivity());
                }


            }

            @Override
            public void onFailure(Call<MemberRequestModel> call, Throwable t) {
                dialog.dismiss();
                C.INSTANCE.errorToast(getActivity(), t);
            }
        });


    }

    private void RefreshLayout() {
        Log.e("seeChecking", "--->" + preference.getMemberCount());
        if (requestBeen.size() == 0 || preference.getMemberCount()<=0) {
            layout_request.setVisibility(View.GONE);
        } else {
            layout_request.setVisibility(View.VISIBLE);
        }
    }

// Following are the ASYNCTASK Services that are already converted to retrofit . If you found any issues in Retrofit API please refer the following.


    /*public class GetMemberRequests extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (isRefereshTimes == false) {
                try {


                    dialog.show();
                    dialog.setCancelable(false);

                } catch (Exception e) {

                    e.printStackTrace();
                }
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


            json = new ServiceHandler(getContext()).makeServiceCall(W.ASYNC_BASE_URL + W.MemberRequest, ServiceHandler.POST, pairs);


            Log.e("paramtersMember", "" + pairs);
            Log.e("json", json);


            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            requestBeen.clear();
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
                    Log.e("Letssee", "-->" + mainObject.getBoolean("status"));
                    if (status == true) {


                        JSONArray dataAraay = mainObject.getJSONArray("data");
                        Log.e("Letssee", "-->" + mainObject.getJSONArray("data"));
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

                    } else {

                    }


                    memberRequestAdapter.notifyDataSetChanged();

                    if (isRefereshTimes == true) {
                        J.GetNotificationCountGlobal(preference.getUserID(), preference.getTokenHash(), preference, getContext(), getActivity());
                    }

                    isRefereshTimes = false;

                    RefreshLayout();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }*/


}
