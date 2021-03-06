package com.fundit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.W;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.apis.ServiceHandler;
import com.fundit.helper.CustomDialog;
import com.fundit.model.AppModel;
import com.fundit.model.Fundspot;
import com.fundit.model.GeneralMemberProfileResponse;
import com.fundit.model.GetDataResponses;
import com.fundit.model.GetSearchPeople;
import com.fundit.model.JoinMemberModel;
import com.fundit.model.Member;
import com.fundit.model.Organization;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SerchPeopleActivity extends AppCompatActivity {

    boolean flag = false;
    AppPreference preference;
    CustomDialog dialog;
    AdminAPI adminAPI;

    CircleImageView circleImageView;

    TextView txt_name, txt_address, txt_emailID, txt_organizations, txt_fundspots, txt_currentCampaigns, txt_pastCampaigns, txt_email, txt_mobile;

    Button btnAdd, btnJoin, btnFollow, btnMessage;

    LinearLayout layout_contact, current, past, layout_buttons, layout_mail, layout_fundspot, layput_contact_mobile, layput_contact_email, layout_org;


    String Id = "";
    String campaignId = "";
    String roleIdss = "";
    boolean profileMode = false;
    boolean status = false;

    GetSearchPeople.People getResponse;


    Fundspot fundspot = new Fundspot();
    Organization organization = new Organization();
    Member member = new Member();
    int isMemberJoined = 0;
    int isDialogOpen = 0;
    String json = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search_people);

        Log.e("searchPeople", "-->");


        preference = new AppPreference(getApplicationContext());
        dialog = new CustomDialog(this);
        adminAPI = ServiceGenerator.getAPIClass();

        Log.e("notiSearch", "-->" + preference.getUserRoleID());
        Intent intent = getIntent();
        Id = intent.getStringExtra("id");
        flag = intent.getBooleanExtra("flag", false);
        getResponse = (GetSearchPeople.People) intent.getSerializableExtra("details");


        try {
            fundspot = new Gson().fromJson(preference.getMemberData(), Fundspot.class);
            organization = new Gson().fromJson(preference.getMemberData(), Organization.class);
            member = new Gson().fromJson(preference.getMemberData(), Member.class);
            Log.e("userData", preference.getUserData());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }


        setupToolbar();
        fetchIds();


    }

    private void setupToolbar() {


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        toolbar.setVisibility(View.VISIBLE);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        // actionTitle.setText("Profile");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void fetchIds() {

        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_address = (TextView) findViewById(R.id.txt_address);
        txt_emailID = (TextView) findViewById(R.id.txt_emailID);
        txt_organizations = (TextView) findViewById(R.id.txt_organizations);
        circleImageView = (CircleImageView) findViewById(R.id.img_profilePic);
        txt_fundspots = (TextView) findViewById(R.id.txt_fundspot);
        layout_fundspot = (LinearLayout) findViewById(R.id.layout_fundspot);
        layout_org = (LinearLayout) findViewById(R.id.layout_org);
        layput_contact_email = (LinearLayout) findViewById(R.id.layput_contact_email);
        layput_contact_mobile = (LinearLayout) findViewById(R.id.layput_contact_mobile);
        layout_mail = (LinearLayout) findViewById(R.id.layout_mail);
        txt_email = (TextView) findViewById(R.id.txt_email);
        txt_mobile = (TextView) findViewById(R.id.txt_mobile);


        btnAdd = (Button) findViewById(R.id.btnAdd);

        btnMessage = (Button) findViewById(R.id.btnmessage);


        if (preference.getUserRoleID().equalsIgnoreCase("4")) {
            btnAdd.setVisibility(View.GONE);

        } else if (preference.getUserRoleID().equalsIgnoreCase("2")) {
            if (flag == true) {
                btnAdd.setVisibility(View.VISIBLE);
                btnMessage.setVisibility(View.VISIBLE);
            } else {
                btnAdd.setVisibility(View.VISIBLE);
            }
        } else if (preference.getUserRoleID().equalsIgnoreCase("3")) {
            if (flag == true) {
                btnAdd.setVisibility(View.VISIBLE);
                btnMessage.setVisibility(View.VISIBLE);
            } else {
                btnAdd.setVisibility(View.VISIBLE);
            }

        }


        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ids = "";

                if (flag == true || flag == false) {
                    ids = Id;
                } else {
                    ids = getResponse.getUser_id();
                }


                Intent intent = new Intent(getApplicationContext(), FinalSendMessage.class);
                intent.putExtra("name", txt_name.getText().toString().trim());
                intent.putExtra("id", ids);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String checkMemberId = "";


                if (flag == true || false == false) {

                    checkMemberId = Id;

                } else {

                    if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
                        checkMemberId = getResponse.getId();

                    }
                    if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
                        checkMemberId = getResponse.getId();

                    }
                    if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                        checkMemberId = getResponse.getId();

                    }
                }

                if (isMemberJoined == 0) {
                    // new AddMember().execute();

                    AddGeneralMember();

                } else if (isMemberJoined == 1) {
                  //  new RespondMemberRequest("2", checkMemberId).execute();

                    RespondGeneralMembersRequest("2", checkMemberId);

                } else if (isMemberJoined == 2) {

                    if (isDialogOpen == 1) {
                        RespondForMemberRequest("", "Respond to Request", "");
                    } else {
                        C.INSTANCE.showToast(getApplicationContext(), "Your request to add " + txt_name.getText().toString().trim() + " is pending.");
                    }


                }


            }
        });


      //  new GetAllDetails().execute();

        GetAllMemberDetails();

    }


    private void RespondForMemberRequest(final String userID, String message, String title) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle(title.isEmpty() ? "Member Request Pending" : title);
        builder.setMessage(message);
        builder.setCancelable(false);

        String checkMemberId = "";

        if (flag == true || flag == false) {
            checkMemberId = Id;
        } else {

            if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
                checkMemberId = getResponse.getId();

            }
            if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
                checkMemberId = getResponse.getId();

            }
            if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                checkMemberId = getResponse.getId();

            }
        }

        final String finalCheckMemberId = checkMemberId;
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

               // new RespondMemberRequest("1", finalCheckMemberId).execute();

                RespondGeneralMembersRequest("1", finalCheckMemberId);
            }
        });

        builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
              //  new RespondMemberRequest("2", finalCheckMemberId).execute();

                RespondGeneralMembersRequest("2", finalCheckMemberId);
            }
        });


        AlertDialog bDialog = builder.create();
        bDialog.show();


    }


    private void CheckMemberIsjoined(String checkMemberId, String userRoleId) {
        Log.e("parameters", "-->" + checkMemberId + "-->" + userRoleId + "--->" + preference.getUserID());
        dialog.show();
        Call<JoinMemberModel> appModelCall = adminAPI.checkJoinMember(checkMemberId, /*preference.getUserRoleID()*/ userRoleId, /*preference.getUserID()*/  preference.getUserID());
        appModelCall.enqueue(new Callback<JoinMemberModel>() {
            @Override
            public void onResponse(Call<JoinMemberModel> call, Response<JoinMemberModel> response) {
                dialog.dismiss();
                JoinMemberModel appModel = response.body();
                if (appModel != null) {
                    //   C.INSTANCE.showToast(getApplicationContext(), appModel.getMessage());
                    if (appModel.isStatus()) {
                        if (appModel.getData() == 1) {
                            btnAdd.setText("Remove Member");
                            isMemberJoined = 1;
                        }
                        if (appModel.getData() == 2) {

                            if (appModel.getOwner_role_id() == 1) {

                                btnAdd.setText("Respond To Request");
                                isDialogOpen = 1;
                                isMemberJoined = 2;

                            } else {

                                //    C.INSTANCE.showToast(getApplicationContext(), "Your request to add " + txt_name.getText().toString().trim() + " is pending.");

                                btnAdd.setText("Pending");
                                isMemberJoined = 2;
                            }



                           /* btnAdd.setText("Pending");
                            isMemberJoined=2;*/
                        }
                        if (appModel.getData() == 3) {
                            btnAdd.setVisibility(View.GONE);
                        }
                    }


                } else {
                    C.INSTANCE.defaultError(getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<JoinMemberModel> call, Throwable t) {
                dialog.dismiss();
                C.INSTANCE.errorToast(getApplicationContext(), t);
            }
        });


    }

    private void GetAllMemberDetails() {
        dialog.show();
        Call<GeneralMemberProfileResponse> memberProfileResponseCall = adminAPI.ViewGeneralMemberProfile(preference.getUserID(), preference.getTokenHash(), Id);
        memberProfileResponseCall.enqueue(new Callback<GeneralMemberProfileResponse>() {
            @Override
            public void onResponse(Call<GeneralMemberProfileResponse> call, Response<GeneralMemberProfileResponse> response) {
                dialog.dismiss();
                GeneralMemberProfileResponse memberProfileResponse = response.body();
                if (memberProfileResponse != null) {
                    if (memberProfileResponse.isStatus()) {


                        if (memberProfileResponse.getData().getMember().getFundspot_names().equalsIgnoreCase("")) {
                            layout_fundspot.setVisibility(View.GONE);
                            txt_fundspots.setVisibility(View.GONE);
                        } else {
                            layout_fundspot.setVisibility(View.VISIBLE);
                            txt_fundspots.setVisibility(View.VISIBLE);
                            txt_fundspots.setText(memberProfileResponse.getData().getMember().getFundspot_names());
                        }


                        txt_name.setText(memberProfileResponse.getData().getUser().getTitle());
                        txt_emailID.setText(memberProfileResponse.getData().getUser().getEmail_id());
                        layout_mail.setVisibility(View.GONE);


                        txt_address.setText(memberProfileResponse.getData().getMember().getLocation() + "\n" + memberProfileResponse.getData().getMember().getCity_name() + ", " + memberProfileResponse.getData().getState().getState_code() + " " + memberProfileResponse.getData().getMember().getZip_code());


                        if (memberProfileResponse.getData().getMember().getOrganization_names().equalsIgnoreCase("")) {
                            layout_org.setVisibility(View.GONE);
                            txt_organizations.setVisibility(View.GONE);
                        } else {
                            txt_organizations.setText(memberProfileResponse.getData().getMember().getOrganization_names());
                        }


                        if (memberProfileResponse.getData().getMember().getContact_info_email() == null || memberProfileResponse.getData().getMember().getContact_info_email().equalsIgnoreCase("")) {
                            layput_contact_email.setVisibility(View.GONE);
                        } else {
                            layput_contact_email.setVisibility(View.VISIBLE);
                            txt_email.setText(memberProfileResponse.getData().getMember().getContact_info_email());
                        }


                        if (memberProfileResponse.getData().getMember().getContact_info_mobile() == null || memberProfileResponse.getData().getMember().getContact_info_mobile().equalsIgnoreCase("")) {
                            layput_contact_mobile.setVisibility(View.GONE);
                        } else {
                            layput_contact_mobile.setVisibility(View.VISIBLE);
                            txt_mobile.setText(memberProfileResponse.getData().getMember().getContact_info_mobile());
                        }


                        String getURL = W.FILE_URL + memberProfileResponse.getData().getMember().getImage();

                        Picasso.with(getApplicationContext())
                                .load(getURL)
                                .into(circleImageView);

                        String checkMemberId = "";

                        if (flag == false) {
                            if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
                                checkMemberId = getResponse.getId();

                            }
                            if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
                                checkMemberId = getResponse.getId();

                            }
                            if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                                checkMemberId = getResponse.getId();

                            }

                            CheckMemberIsjoined(checkMemberId, preference.getUserRoleID());
                        } else {

                            checkMemberId = Id;

                            CheckMemberIsjoined(checkMemberId, preference.getUserRoleID());
                        }

                    }
                } else {
                    C.INSTANCE.defaultError(getApplicationContext());
                }

            }

            @Override
            public void onFailure(Call<GeneralMemberProfileResponse> call, Throwable t) {
                dialog.dismiss();
                C.INSTANCE.errorToast(getApplicationContext(), t);
            }
        });
    }

    private void AddGeneralMember() {

        String organizationsIds = null, fundspotsIds = null;
        if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
            organizationsIds = preference.getUserID();
        }
        if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
            fundspotsIds = preference.getUserID();
        }
        dialog.show();
        Call<AppModel> modelCall = adminAPI.AddGeneralMember(preference.getUserID(), preference.getTokenHash(), Id, organizationsIds, fundspotsIds);
        modelCall.enqueue(new Callback<AppModel>() {
            @Override
            public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                dialog.dismiss();
                AppModel model = response.body();
                if (model != null) {
                    if (model.isStatus()) {

                        String checkMemberId = "";

                        if (flag == true || flag == false) {
                            checkMemberId = Id;
                        } else {

                            if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
                                checkMemberId = getResponse.getId();

                            }
                            if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
                                checkMemberId = getResponse.getId();

                            }
                            if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                                checkMemberId = getResponse.getId();

                            }
                        }

                        CheckMemberIsjoined(checkMemberId, preference.getUserRoleID());
//                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);


                    }

                } else {
                    C.INSTANCE.defaultError(getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<AppModel> call, Throwable t) {
                dialog.dismiss();
                C.INSTANCE.errorToast(getApplicationContext(), t);
            }
        });


    }

    private void RespondGeneralMembersRequest(String s, String finalCheckMemberId) {
        dialog.show();
        Call<AppModel> appModelCall = adminAPI.RespondRequest(preference.getUserID(), preference.getTokenHash(), finalCheckMemberId, s);
        appModelCall.enqueue(new Callback<AppModel>() {
            @Override
            public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                dialog.dismiss();
                AppModel model = response.body();
                if (model != null) {
                    if (model.isStatus()) {
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }

                } else {
                    C.INSTANCE.defaultError(getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<AppModel> call, Throwable t) {
                dialog.dismiss();
                C.INSTANCE.errorToast(getApplicationContext(), t);
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        System.gc();
    }

    // Following are the ASYNCTASK Services that are already converted to retrofit . If you found any issues in Retrofit API please refer the following.

    /*public class AddMember extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                dialog.setCancelable(false);
                dialog.show();


            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... params) {

            List<NameValuePair> pairs = new ArrayList<>();

            pairs.add(new BasicNameValuePair(W.KEY_USERID, preference.getUserID()));
            pairs.add(new BasicNameValuePair(W.KEY_TOKEN, preference.getTokenHash()));
            pairs.add(new BasicNameValuePair("member_id", Id));

            if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {


                pairs.add(new BasicNameValuePair("organization_id", preference.getUserID()));
            }

            if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {

                pairs.add(new BasicNameValuePair("fundspot_id", preference.getUserID()));

            }

            String json = new ServiceHandler(getApplicationContext()).makeServiceCall(W.ASYNC_BASE_URL + "Member/app_add_member", ServiceHandler.POST, pairs);

            Log.e("parameters", "-->" + pairs);
            Log.e("json", json);
            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();


            if (s.isEmpty()) {

                C.INSTANCE.defaultError(getApplicationContext());
            } else {

                try {
                    JSONObject mainObject = new JSONObject(s);

                    boolean status = false;
                    String message = "";


                    status = mainObject.getBoolean("status");
                    message = mainObject.getString("message");

                    //  C.INSTANCE.showToast(getApplicationContext(), message);
                    if (status) {

                        String checkMemberId = "";

                        if (flag == true || flag == false) {
                            checkMemberId = Id;
                        } else {

                            if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
                                checkMemberId = getResponse.getId();

                            }
                            if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
                                checkMemberId = getResponse.getId();

                            }
                            if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                                checkMemberId = getResponse.getId();

                            }
                        }

                        CheckMemberIsjoined(checkMemberId, preference.getUserRoleID());
//                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public class RespondMemberRequest extends AsyncTask<Void, Void, String> {

        String getStatus = "";
        String getMemberId = "";

        public RespondMemberRequest(String getStatus, String getMemberId) {
            this.getStatus = getStatus;
            this.getMemberId = getMemberId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {

                dialog = new CustomDialog(SerchPeopleActivity.this);
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
            pairs.add(new BasicNameValuePair("member_id", getMemberId));
            pairs.add(new BasicNameValuePair("status", getStatus));

            json = new ServiceHandler(getApplicationContext()).makeServiceCall(W.ASYNC_BASE_URL + "Member/app_respond_member_request", ServiceHandler.POST, pairs);

            Log.e("parameters", "" + pairs);


            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            try {

                if (s.equalsIgnoreCase("") || s.isEmpty()) {

                    C.INSTANCE.noInternet(getApplicationContext());
                } else {

                    JSONObject mainObject = new JSONObject(s);

                    boolean status = false;
                    String message = "";

                    status = mainObject.getBoolean("status");
                    message = mainObject.getString("message");

                    // C.INSTANCE.showToast(getApplicationContext(), message);
                    if (status == true) {
                        *//*Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);*//*
                        // onBackPressed();
                        //  recreate();

                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);


                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
*/

    /*public class GetAllDetails extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                dialog.setCancelable(false);
                dialog.show();


            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... params) {

            List<NameValuePair> pairs = new ArrayList<>();

            pairs.add(new BasicNameValuePair(W.KEY_USERID, preference.getUserID()));
            pairs.add(new BasicNameValuePair(W.KEY_TOKEN, preference.getTokenHash()));
            pairs.add(new BasicNameValuePair("member_id", Id));


            String json = new ServiceHandler(getApplicationContext()).makeServiceCall(W.ASYNC_BASE_URL + "Member/app_view_member_profile", ServiceHandler.POST, pairs);

            Log.e("parameters", "-->" + pairs);
            Log.e("json", json);
            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();


            if (s.isEmpty()) {

                C.INSTANCE.defaultError(getApplicationContext());
            } else {

                try {
                    JSONObject mainObject = new JSONObject(s);

                    boolean status = false;
                    String message = "";


                    status = mainObject.getBoolean("status");

                    if (status) {


                        JSONObject dataObject = mainObject.getJSONObject("data");
                        JSONObject memberObject = dataObject.getJSONObject("Member");
                        JSONObject userObject = dataObject.getJSONObject("User");
                        JSONObject stateObject = dataObject.getJSONObject("State");
                        JSONObject cityObject = dataObject.getJSONObject("City");


                        if (memberObject.getString("fundspot_names").equalsIgnoreCase("")) {
                            layout_fundspot.setVisibility(View.GONE);
                            txt_fundspots.setVisibility(View.GONE);
                        } else {
                            layout_fundspot.setVisibility(View.VISIBLE);
                            txt_fundspots.setVisibility(View.VISIBLE);
                            txt_fundspots.setText(memberObject.getString("fundspot_names"));
                        }


                        txt_name.setText(userObject.getString("title"));
                        txt_emailID.setText(userObject.getString("email_id"));
                        layout_mail.setVisibility(View.GONE);


                        txt_address.setText(memberObject.getString("location") + "\n" + memberObject.getString("city_name") + ", " + stateObject.getString("state_code") + " " + memberObject.getString("zip_code"));

                        if (memberObject.getString("organization_names").equalsIgnoreCase("")) {
                            layout_org.setVisibility(View.GONE);
                            txt_organizations.setVisibility(View.GONE);
                        } else {
                            txt_organizations.setText(memberObject.getString("organization_names"));
                        }


                        if (memberObject.getString("contact_info_email") == null || memberObject.getString("contact_info_email").equalsIgnoreCase("")) {
                            layput_contact_email.setVisibility(View.GONE);
                        } else {
                            layput_contact_email.setVisibility(View.VISIBLE);
                            txt_email.setText(memberObject.getString("contact_info_email"));
                        }


                        if (memberObject.getString("contact_info_mobile") == null || memberObject.getString("contact_info_mobile").equalsIgnoreCase("")) {
                            layput_contact_mobile.setVisibility(View.GONE);
                        } else {
                            layput_contact_mobile.setVisibility(View.VISIBLE);
                            txt_mobile.setText(memberObject.getString("contact_info_mobile"));
                        }


                        String getURL = W.FILE_URL + memberObject.getString("image");

                        Picasso.with(getApplicationContext())
                                .load(getURL)
                                .into(circleImageView);

                        String checkMemberId = "";

                        if (flag == false) {
                            if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
                                checkMemberId = getResponse.getId();

                            }
                            if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
                                checkMemberId = getResponse.getId();

                            }
                            if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                                checkMemberId = getResponse.getId();

                            }

                            CheckMemberIsjoined(checkMemberId, preference.getUserRoleID());
                        } else {

                            checkMemberId = Id;

                            CheckMemberIsjoined(checkMemberId, preference.getUserRoleID());
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }
    }*/

}