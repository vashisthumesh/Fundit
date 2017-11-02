package com.fundit;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.fundit.model.GetDataResponses;
import com.fundit.model.Member;
import com.fundit.model.Organization;
import com.fundit.model.User;
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

public class AddMembersActivity extends AppCompatActivity {

    AppPreference preference;
    CustomDialog dialog;
    AdminAPI adminAPI;

    CircleImageView circleImageView;

    TextView txt_name, txt_address, txt_emailID, txt_organizations, txt_fundspots, txt_currentCampaigns, txt_pastCampaigns, txt_contct, txt_con_info_email, txt_con_info_mobile;
    LinearLayout layout_contact_info_email, layout_contact_info_mobile;

    Button btnAdd, btnJoin, btnFollow, btnMessage;

    LinearLayout layout_contact, current, past, layout_buttons, layout_mail, layout_category, layout_fundraiser, layout_type, layout_description, layout_org, layout_fun;


    String memberId = "";
    String campaignId = "";
    String roleIdss = "";
    boolean profileMode = false;
    boolean status = false;

    GetDataResponses.Data getResponse;


    Fundspot fundspot = new Fundspot();
    Organization organization = new Organization();
    Member member = new Member();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_profile);

        preference = new AppPreference(getApplicationContext());
        dialog = new CustomDialog(this);
        adminAPI = ServiceGenerator.getAPIClass();


        Intent intent = getIntent();
        memberId = intent.getStringExtra("memberId");
        campaignId = intent.getStringExtra("campaignId");
        getResponse = (GetDataResponses.Data) intent.getSerializableExtra("details");
        profileMode = intent.getBooleanExtra("profileMode", false);
        status = intent.getBooleanExtra("isStatus", false);


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
        actionTitle.setText("Profile");

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
        txt_fundspots = (TextView) findViewById(R.id.txt_fundspots);
        txt_currentCampaigns = (TextView) findViewById(R.id.txt_currentCampaigns);
        txt_pastCampaigns = (TextView) findViewById(R.id.txt_pastCampaigns);
        txt_con_info_email = (TextView) findViewById(R.id.txt_con_info_email);
        txt_con_info_mobile = (TextView) findViewById(R.id.txt_con_info_mobile);
        layout_contact_info_email = (LinearLayout) findViewById(R.id.layout_contact_info_email);
        layout_contact_info_mobile = (LinearLayout) findViewById(R.id.layout_contact_info_mobile);
        layout_fundraiser = (LinearLayout) findViewById(R.id.layout_fundraiser);
        layout_type = (LinearLayout) findViewById(R.id.layout_type);
        layout_description = (LinearLayout) findViewById(R.id.layout_description);
        layout_org = (LinearLayout) findViewById(R.id.layout_org);
        layout_fun = (LinearLayout) findViewById(R.id.layout_fun);

        layout_org.setVisibility(View.GONE);
        layout_description.setVisibility(View.GONE);
        layout_fun.setVisibility(View.GONE);
        layout_type.setVisibility(View.GONE);
        layout_fundraiser.setVisibility(View.GONE);


        txt_currentCampaigns.setVisibility(View.GONE);
        txt_pastCampaigns.setVisibility(View.GONE);

        txt_contct = (TextView) findViewById(R.id.txt_contct);


        current = (LinearLayout) findViewById(R.id.current);
        layout_contact = (LinearLayout) findViewById(R.id.layout_contact);
        layout_mail = (LinearLayout) findViewById(R.id.layout_mail);
        layout_category = (LinearLayout) findViewById(R.id.layout_category);
        past = (LinearLayout) findViewById(R.id.past);
        layout_buttons = (LinearLayout) findViewById(R.id.layout_buttons);


        layout_category.setVisibility(View.GONE);
        layout_contact.setVisibility(View.VISIBLE);
        current.setVisibility(View.GONE);
        past.setVisibility(View.GONE);

        circleImageView = (CircleImageView) findViewById(R.id.img_profilePic);


        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnJoin = (Button) findViewById(R.id.button_join);
        btnFollow = (Button) findViewById(R.id.button_follow);
        btnMessage = (Button) findViewById(R.id.btnmessage);
        btnAdd.setVisibility(View.VISIBLE);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (campaignId == null) {

                    new AddMember().execute();

                } else {
                    new CampaignAddMember().execute();

                }


            }
        });


        if (profileMode) {

            layout_buttons.setVisibility(View.VISIBLE);
            btnMessage.setVisibility(View.VISIBLE);
            txt_emailID.setVisibility(View.GONE);
            layout_mail.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);


            if (status) {
                String imagePath = W.FILE_URL + getResponse.getFundspot().getImage();
                Log.e("path", imagePath);

                Picasso.with(getApplicationContext())
                        .load(imagePath)
                        .into(circleImageView);

                txt_name.setText(getResponse.getFundspot().getTitle());
                txt_address.setText(getResponse.getFundspot().getLocation());
                txt_contct.setText(getResponse.getFundspot().getDescription());
                Log.e("email", "--->" + getResponse.getFundspot().getContact_info_email());
                if ((getResponse.getFundspot().getContact_info_email()) == null || getResponse.getFundspot().getContact_info_email().equalsIgnoreCase("")) {
                    layout_contact_info_email.setVisibility(View.GONE);


                } else {
                    layout_contact_info_email.setVisibility(View.VISIBLE);

                    txt_con_info_email.setText(getResponse.getFundspot().getContact_info_email());
                }
                if (getResponse.getFundspot().getContact_info_mobile() == null || getResponse.getFundspot().getContact_info_mobile().equalsIgnoreCase("")) {
                    layout_contact_info_mobile.setVisibility(View.GONE);


                } else {

                    layout_contact_info_mobile.setVisibility(View.VISIBLE);
                    txt_con_info_mobile.setText(getResponse.getFundspot().getContact_info_mobile());
                }


            } else {
                String imagePath = W.FILE_URL + getResponse.getOrganization().getImage();
                Log.e("path", imagePath);

                Picasso.with(getApplicationContext())
                        .load(imagePath)
                        .into(circleImageView);

                txt_name.setText(getResponse.getOrganization().getTitle());
                txt_address.setText(getResponse.getOrganization().getLocation());
                txt_contct.setText(getResponse.getOrganization().getDescription());
                Log.e("email", "--->" + getResponse.getOrganization().getContact_info_email());
                if (getResponse.getOrganization().getContact_info_email() == null || getResponse.getOrganization().getContact_info_mobile().equalsIgnoreCase("")) {
                    layout_contact_info_email.setVisibility(View.GONE);

                } else {
                    layout_contact_info_email.setVisibility(View.VISIBLE);
                    txt_con_info_email.setText(getResponse.getOrganization().getContact_info_email());

                }
                if (getResponse.getOrganization().getContact_info_mobile() == null || getResponse.getOrganization().getContact_info_mobile().equalsIgnoreCase("")) {
                    layout_contact_info_mobile.setVisibility(View.GONE);


                } else {

                    layout_contact_info_mobile.setVisibility(View.VISIBLE);
                    txt_con_info_mobile.setText(getResponse.getOrganization().getContact_info_mobile());
                }


            }
            if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION) || preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
                btnJoin.setVisibility(View.GONE);
            }


            btnJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String selectedUsersId = "";
                    String membersssIdss = "";
                    if (status) {
                        selectedUsersId = getResponse.getFundspot().getUser_id();
                    } else {
                        selectedUsersId = getResponse.getOrganization().getUser_id();
                    }


                    if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
                        membersssIdss = fundspot.getId();
                    }
                    if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
                        membersssIdss = organization.getId();
                    }
                    if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                        membersssIdss = member.getId();
                    }


                    new JoinMember(membersssIdss, selectedUsersId).execute();


                }
            });


            btnMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), FinalSendMessage.class);
                    intent.putExtra("name", getResponse.getUser().getTitle());
                    intent.putExtra("id", getResponse.getUser().getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });


            String checkMemberId = "";

            if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
                checkMemberId = fundspot.getId();
            }
            if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
                checkMemberId = organization.getId();
            }
            if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                checkMemberId = member.getId();
            }


            CheckMemberIsjoined(checkMemberId);
        }


        if (!profileMode) {
            new GetAllDetails().execute();
        }

    }

    private void CheckMemberIsjoined(String checkMemberId) {

        dialog.show();
        Call<AppModel> appModelCall = adminAPI.checkJoinMember(checkMemberId, preference.getUserRoleID(), preference.getUserID());
        appModelCall.enqueue(new Callback<AppModel>() {
            @Override
            public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                dialog.dismiss();
                AppModel appModel = response.body();
                if (appModel != null) {
                    if (appModel.isStatus()) {
                        btnJoin.setEnabled(false);
                    } else {
                        C.INSTANCE.showToast(getApplicationContext(), appModel.getMessage());
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


    public class GetAllDetails extends AsyncTask<Void, Void, String> {

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
            pairs.add(new BasicNameValuePair("member_id", memberId));


            String json = new ServiceHandler().makeServiceCall(W.BASE_URL + "Member/app_view_member_profile", ServiceHandler.POST, pairs);

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


                        txt_name.setText(userObject.getString("title"));
                        txt_emailID.setText(userObject.getString("email_id"));
                        txt_contct.setText(memberObject.getString("contact_info"));

                        txt_address.setText(memberObject.getString("location") + " , " + cityObject.getString("name") + stateObject.getString("name") + " , " + memberObject.getString("zip_code"));


                        String getURL = W.FILE_URL + memberObject.getString("image");

                        Picasso.with(getApplicationContext())
                                .load(getURL)
                                .into(circleImageView);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }
    }


    public class AddMember extends AsyncTask<Void, Void, String> {
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
            pairs.add(new BasicNameValuePair("member_id", memberId));

            if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {


                pairs.add(new BasicNameValuePair("organization_id", preference.getUserID()));
            }

            if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {

                pairs.add(new BasicNameValuePair("fundspot_id", preference.getUserID()));

            }

            String json = new ServiceHandler().makeServiceCall(W.BASE_URL + "Member/app_add_member", ServiceHandler.POST, pairs);

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

                    C.INSTANCE.showToast(getApplicationContext(), message);
                    if (status) {

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public class CampaignAddMember extends AsyncTask<Void, Void, String> {
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
            pairs.add(new BasicNameValuePair("member_id", memberId));
            pairs.add(new BasicNameValuePair("campaign_id", campaignId));
            pairs.add(new BasicNameValuePair(W.KEY_ROLEID, preference.getUserRoleID()));


            String json = new ServiceHandler().makeServiceCall(W.BASE_URL + W.ADD_MEMBER_TO_CAMPAIGN, ServiceHandler.POST, pairs);

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

                    C.INSTANCE.showToast(getApplicationContext(), message);
                    if (status) {

                        Intent intent = new Intent(getApplicationContext(), AddMemberToCampaign.class);
                        intent.putExtra("campaignId", campaignId);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public class JoinMember extends AsyncTask<Void, Void, String> {

        String member = "";
        String selectedUserIdss = "";


        public JoinMember(String member, String selectedUserIdss) {
            this.member = member;
            this.selectedUserIdss = selectedUserIdss;
        }

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
            pairs.add(new BasicNameValuePair("member_id", member));
            pairs.add(new BasicNameValuePair(W.KEY_ROLEID, preference.getUserRoleID()));
            pairs.add(new BasicNameValuePair(W.KEY_TOKEN, preference.getTokenHash()));

            if (status) {

                pairs.add(new BasicNameValuePair("fundspot_id", selectedUserIdss));
            } else {

                pairs.add(new BasicNameValuePair("organization_id", selectedUserIdss));

            }


            String json = new ServiceHandler().makeServiceCall(W.BASE_URL + "Member/app_request_join_member", ServiceHandler.POST, pairs);

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

                    C.INSTANCE.showToast(getApplicationContext(), message);
                    if (status) {

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}