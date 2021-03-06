package com.fundit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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
import com.fundit.a.J;
import com.fundit.a.W;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.apis.ServiceHandler;
import com.fundit.fundspot.AddProductActivity;
import com.fundit.helper.CustomDialog;
import com.fundit.model.AppModel;
import com.fundit.model.Fundspot;
import com.fundit.model.GeneralMemberProfileResponse;
import com.fundit.model.GetDataResponses;
import com.fundit.model.JoinMemberModel;
import com.fundit.model.Member;
import com.fundit.model.Organization;
import com.fundit.model.User;
import com.fundit.organization.FundspotListActivity;
import com.fundit.organization.FundspotProductListActivity;
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

    TextView txt_name, txt_address, txt_emailID, txt_organizations, txt_fundspots, txt_currentCampaigns, txt_pastCampaigns, txt_contct, txt_con_info_email, txt_con_info_mobile, txt_fundtitle, txt_fundraiser_split;
    LinearLayout layout_contact_info_email, layout_contact_info_mobile;

    Button btnAdd, btnJoin, btnFollow, btnMessage, btnrequest;

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

    int isMemberJoined = 0;
    int isDialogOpen = 0;

    String json = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_profile);

        Log.e("addMembers", "-->");

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


        //setupToolbar();
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
        txt_fundspots = (TextView) findViewById(R.id.txt_fundspots);
        txt_currentCampaigns = (TextView) findViewById(R.id.txt_currentCampaigns);
        txt_pastCampaigns = (TextView) findViewById(R.id.txt_pastCampaigns);
        txt_con_info_email = (TextView) findViewById(R.id.txt_con_info_email);
        txt_con_info_mobile = (TextView) findViewById(R.id.txt_con_info_mobile);
        txt_fundtitle = (TextView) findViewById(R.id.txt_fundtitle);
        txt_fundraiser_split = (TextView) findViewById(R.id.txt_fundraiser_split);


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
        btnrequest = (Button) findViewById(R.id.btnrequest);
        btnAdd.setVisibility(View.VISIBLE);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (campaignId == null) {

                    // new AddMember().execute();


                    AddGeneralMember();

                } else {
                    // new CampaignAddMember().execute();

                    AddCampaignMember();

                }


            }
        });


        if (profileMode) {

            String getCityName = "";


            layout_buttons.setVisibility(View.VISIBLE);
            btnMessage.setVisibility(View.VISIBLE);
            txt_emailID.setVisibility(View.GONE);
            layout_mail.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
            btnFollow.setText("Follow");

            setupToolbar();


            if (status) {

                if (getResponse.getFundspot().getCity_name() != null) {
                    getCityName = getResponse.getFundspot().getCity_name();
                }

                if (getResponse.getFundspot().getSplit_visibility().equalsIgnoreCase("0")) {
                    txt_fundtitle.setVisibility(View.GONE);
                    txt_fundraiser_split.setVisibility(View.GONE);
                }

                layout_fundraiser.setVisibility(View.VISIBLE);

                if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
                    btnrequest.setVisibility(View.VISIBLE);
                }
                String imagePath = W.FILE_URL + getResponse.getFundspot().getImage();
                Log.e("path", imagePath);

                txt_fundtitle.setText("% Split ( Fundspot/Organization ):");
                txt_fundraiser_split.setText(getResponse.getFundspot().getFundspot_percent() + " / " + getResponse.getFundspot().getOrganization_percent());


                Picasso.with(getApplicationContext())
                        .load(imagePath)
                        .into(circleImageView);

                txt_name.setText(getResponse.getFundspot().getTitle());


                txt_address.setText(getResponse.getFundspot().getLocation() + "\n" + getCityName + ", " + getResponse.getState().getState_code() + " " + getResponse.getFundspot().getZip_code());


                txt_contct.setText(getResponse.getFundspot().getDescription());
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


                if (getResponse.getOrganization().getCity_name() != null) {
                    getCityName = getResponse.getOrganization().getCity_name();
                }

                if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
                    btnrequest.setVisibility(View.VISIBLE);
                }


                String imagePath = W.FILE_URL + getResponse.getOrganization().getImage();
                Log.e("path", imagePath);

                Picasso.with(getApplicationContext())
                        .load(imagePath)
                        .into(circleImageView);

                txt_name.setText(getResponse.getOrganization().getTitle());
                txt_address.setText(getResponse.getOrganization().getLocation() + "\n" + getCityName + ", " + getResponse.getState().getState_code() + " " + getResponse.getOrganization().getZip_code());

                Log.e("location", getResponse.getOrganization().getLocation());
                Log.e("location", "--->" + getResponse.getOrganization().getState().getState_code());
                Log.e("location", getResponse.getOrganization().getZip_code());

                txt_contct.setText(getResponse.getOrganization().getDescription());
                Log.e("email", "--->" + getResponse.getOrganization().getContact_info_email());
                if (getResponse.getOrganization().getContact_info_email() == null || getResponse.getOrganization().getContact_info_email().equalsIgnoreCase("")) {
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


            btnrequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Intent intent = new Intent(getApplicationContext(), FundspotProductListActivity.class);

                   /* Log.e("requestSettings" , "--->" + getResponse.getFundspot().getFundspot_percent() + "-->" + getResponse.getFundspot().getOrganization_percent() + "-->" + getResponse.getFundspot().getProduct_count());*/
                    if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {

                        if ((getResponse.getFundspot().getFundspot_percent().equalsIgnoreCase("0") && getResponse.getFundspot().getOrganization_percent().equalsIgnoreCase("0"))) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddMembersActivity.this);
                            builder.setTitle("Sorry, Fundraiser settings not valid");
                            builder.setMessage("You can't start campaign with this fundspot");
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog bDialog = builder.create();
                            bDialog.show();
                        } else if (getResponse.getFundspot().getProduct_count().equalsIgnoreCase("0")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddMembersActivity.this);
                            builder.setTitle("Sorry,No product available");
                            builder.setMessage("You can't start campaign with this fundspot");
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog bDialog = builder.create();
                            bDialog.show();
                        } else {
                            intent.putExtra("fundspotName", getResponse.getFundspot().getTitle());
                            intent.putExtra("fundspotID", getResponse.getFundspot().getUser_id());
                            intent.putExtra("profileMode", true);
                            startActivity(intent);

                        }
                    } else if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {

                        Log.e("requestSettings", "--->" + fundspot.getFundspot_percent() + "-->" + fundspot.getOrganization_percent() + "-->" + preference.getfundspot_product_count());
                        if ((fundspot.getFundspot_percent().equalsIgnoreCase("0") && fundspot.getOrganization_percent().equalsIgnoreCase("0"))) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(AddMembersActivity.this);
                            builder.setTitle("First set your campaign terms!");
                            builder.setMessage("Before starting your first campaign, make sure to set your campaign terms and products.");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Set Terms", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent1 = new Intent(getApplicationContext(), CampaignSetting.class);
                                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent1.putExtra("editMode", true);
                                    startActivity(intent1);
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog bDialog = builder.create();
                            bDialog.show();
                        } else if (preference.getfundspot_product_count() == 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddMembersActivity.this);
                            builder.setTitle("First add your Products!");
                            builder.setMessage("Please add your products or services available for sale during campaigns.");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Add Products", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent in = new Intent(getApplicationContext(), AddProductActivity.class);
                                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(in);
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog bDialog = builder.create();
                            bDialog.show();
                        } else {

                            intent.putExtra("fundspotName", getResponse.getOrganization().getTitle());
                            intent.putExtra("fundspotID", fundspot.getUser_id());
                            intent.putExtra("organizationID", getResponse.getOrganization().getUser_id());
                            intent.putExtra("profileMode", true);
                            startActivity(intent);
                        }
                    }

                }
            });


            btnJoin.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    String selectedUsersId = "";
                    String membersssIdss = "";
                    String selectedUsersRoleId = "";

                    Log.e("checking", "-->");
                    if (status) {
                        selectedUsersId = getResponse.getFundspot().getUser_id();
                        selectedUsersRoleId = C.FUNDSPOT;
                    } else {
                        selectedUsersId = getResponse.getOrganization().getUser_id();
                        selectedUsersRoleId = C.ORGANIZATION;
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


                    if (isMemberJoined == 0) {
                        // new JoinMember(membersssIdss, selectedUsersId).execute();

                        JoinGeneralMember(membersssIdss, selectedUsersId);

                    } else if (isMemberJoined == 1) {
                        //new LeaveMember(preference.getUserID(), preference.getUserRoleID(), membersssIdss).execute();
                        // new LeaveMember(selectedUsersId, selectedUsersRoleId, membersssIdss).execute();

                        LeaveGeneralMember(selectedUsersId, selectedUsersRoleId, membersssIdss);
                    } else if (isMemberJoined == 2) {
                        if (isDialogOpen == 1) {
                            RespondForMemberRequest("", "Respond to Request", "");
                        } else {
                            C.INSTANCE.showToast(getApplicationContext(), "Your request to add " + txt_name.getText().toString().trim() + " is pending.");
                        }
                    }

                }
            });


            btnMessage.setOnClickListener(new View.OnClickListener()

            {
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
            String userRoleId = "";

            if (preference.getUserRoleID().

                    equalsIgnoreCase(C.FUNDSPOT))

            {
                checkMemberId = fundspot.getId();

            }
            if (preference.getUserRoleID().

                    equalsIgnoreCase(C.ORGANIZATION))

            {
                checkMemberId = organization.getId();

            }
            if (preference.getUserRoleID().

                    equalsIgnoreCase(C.GENERAL_MEMBER))

            {
                checkMemberId = member.getId();

            }


            if (preference.getUserRoleID().

                    equalsIgnoreCase(C.GENERAL_MEMBER))

            {
                Log.e("path", "check");
                userRoleId = getResponse.getUser().getRole_id();
                CheckMemberIsjoined(checkMemberId, userRoleId);
            }

            Log.e("lastCheck", "-->");
        }


        if (!profileMode) {


            btnMessage.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {

                    String names = "";
                    names = txt_name.getText().toString();


                    Intent intent = new Intent(getApplicationContext(), FinalSendMessage.class);
                    intent.putExtra("name", names);
                    intent.putExtra("id", memberId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });


            btnJoin.setVisibility(View.GONE);
            btnFollow.setVisibility(View.GONE);
            btnMessage.setVisibility(View.VISIBLE);
          //  new GetAllDetails().execute();

            GetAllMemberDetails();

        }

    }


    private void RespondForMemberRequest(final String userID, String message, String title) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle(title.isEmpty() ? "Member Request Pending" : title);
        builder.setMessage(message);
        builder.setCancelable(false);
        String checkMemberId = "";
        String userRoleId = "";

        if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
            checkMemberId = fundspot.getId();

        }
        if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
            checkMemberId = organization.getId();

        }
        if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
            checkMemberId = member.getId();

        }
        userRoleId = getResponse.getUser().getRole_id();
        final String finalUserRoleId = userRoleId;
        final String finalCheckMemberId = checkMemberId;
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                // new RespondMemberRequest(getResponse.getUser().getId(), finalUserRoleId, "1", finalCheckMemberId).execute();

                RespondGeneralMembersRequest(getResponse.getUser().getId(), finalUserRoleId, "1", finalCheckMemberId);


            }
        });

        builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //new RespondMemberRequest(getResponse.getUser().getId(), finalUserRoleId, "2", finalCheckMemberId).execute();

                RespondGeneralMembersRequest(getResponse.getUser().getId(), finalUserRoleId, "2", finalCheckMemberId);
            }
        });


        AlertDialog bDialog = builder.create();
        bDialog.show();


    }

    private void CheckMemberIsjoined(String checkMemberId, String userRoleId) {

        dialog.show();
        Call<JoinMemberModel> appModelCall = adminAPI.checkJoinMember(checkMemberId, /*preference.getUserRoleID()*/ userRoleId, /*preference.getUserID()*/  getResponse.getUser().getId());
        Log.e("parameterscheck", "-->" + checkMemberId + "-->" + userRoleId + "--->" + getResponse.getUser().getId());
        appModelCall.enqueue(new Callback<JoinMemberModel>() {
            @Override
            public void onResponse(Call<JoinMemberModel> call, Response<JoinMemberModel> response) {
                dialog.dismiss();
                JoinMemberModel appModel = response.body();
                if (appModel != null) {

                    if (appModel.isStatus()) {
                        if (appModel.getData() == 1) {
                            btnJoin.setText("Leave Us");
                            btnFollow.setText("Unfollow");
                            isMemberJoined = 1;
                        } else if (appModel.getData() == 2) {

                            /*if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION) || preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
*/
                            if (appModel.getOwner_role_id() == 0) {

                                btnJoin.setText("Respond To Request");
                                btnFollow.setText("Unfollow");
                                isDialogOpen = 1;
                                isMemberJoined = 2;

                            } else {
                                   /* C.INSTANCE.showToast(getApplicationContext(), "Your request to add " + txt_name.getText().toString().trim() + " is pending.");*/
                                btnJoin.setText("Pending");
                                btnFollow.setText("Unfollow");
                                isMemberJoined = 2;
                            }

                        }/* else {
                           *//*     C.INSTANCE.showToast(getApplicationContext(), "Your request to add " + txt_name.getText().toString().trim() + " is pending.");*//*
                                btnJoin.setText("Pending");
                                btnFollow.setText("Unfollow");
                                isMemberJoined = 2;
                            }
*/

                        //  }

                        else {

                            btnJoin.setText("Join Us");
                            btnFollow.setText("Follow");
                            isMemberJoined = 0;
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
        Call<GeneralMemberProfileResponse> memberProfileResponseCall = adminAPI.ViewGeneralMemberProfile(preference.getUserID(), preference.getTokenHash(), memberId);
        memberProfileResponseCall.enqueue(new Callback<GeneralMemberProfileResponse>() {
            @Override
            public void onResponse(Call<GeneralMemberProfileResponse> call, Response<GeneralMemberProfileResponse> response) {
                dialog.dismiss();
                GeneralMemberProfileResponse memberProfileResponse = response.body();
                if (memberProfileResponse != null) {
                    if (memberProfileResponse.isStatus()) {

                        String name = "", emailids = "", contact = "", contactinfoMobile = "", contactinfoEmail = "", organizationName = "", fundspotName = "";
                        name = memberProfileResponse.getData().getUser().getTitle();
                        emailids = memberProfileResponse.getData().getUser().getEmail_id();
                        contact = memberProfileResponse.getData().getMember().getContact_info();
                        contactinfoMobile = memberProfileResponse.getData().getMember().getContact_info_mobile();
                        contactinfoEmail = memberProfileResponse.getData().getMember().getContact_info_email();
                        organizationName = memberProfileResponse.getData().getMember().getOrganization_names();
                        fundspotName = memberProfileResponse.getData().getMember().getFundspot_names();


                        txt_name.setText(name);


                        if (emailids == null || emailids.equalsIgnoreCase("")) {
                            txt_emailID.setVisibility(View.GONE);
                            layout_mail.setVisibility(View.GONE);
                        } else {
                            txt_emailID.setText(emailids);
                            layout_mail.setVisibility(View.GONE);
                        }


                        if (contact == null || contact.equalsIgnoreCase("") || contact.equalsIgnoreCase("null") || contact.equalsIgnoreCase(null)) {
                            layout_contact.setVisibility(View.GONE);
                        } else {
                            txt_contct.setText(contact);
                        }


                        if (contactinfoMobile == null || contactinfoMobile.equalsIgnoreCase("") || contactinfoMobile.equalsIgnoreCase("null") || contactinfoMobile.equalsIgnoreCase(null)) {
                            layout_contact_info_mobile.setVisibility(View.GONE);
                        } else {
                            txt_con_info_mobile.setText(contactinfoMobile);
                        }

                        if (contactinfoEmail == null || contactinfoEmail.equalsIgnoreCase("") || contactinfoEmail.equalsIgnoreCase("null") || contactinfoEmail.equalsIgnoreCase(null)) {
                            layout_contact_info_email.setVisibility(View.GONE);
                        } else {
                            txt_con_info_email.setText(contactinfoEmail);
                        }

                        Log.e("check", "-->" + organizationName + "-->" + fundspotName + "-->" + layout_org.getVisibility() + "-->");
                        if (organizationName == null || organizationName.equalsIgnoreCase("") || organizationName.equalsIgnoreCase("null") || organizationName.equalsIgnoreCase(null)) {
                            layout_org.setVisibility(View.GONE);
                        } else {
                            layout_org.setVisibility(View.VISIBLE);
                            txt_organizations.setText(organizationName);
                        }

                        if (fundspotName == null || fundspotName.equalsIgnoreCase("") || fundspotName.equalsIgnoreCase("null") || fundspotName.equalsIgnoreCase(null)) {
                            layout_fun.setVisibility(View.GONE);
                        } else {
                            layout_fun.setVisibility(View.VISIBLE);
                            txt_fundspots.setText(fundspotName);
                        }


                        txt_address.setText(memberProfileResponse.getData().getMember().getLocation() + "\n" + memberProfileResponse.getData().getMember().getCity_name() + ", " + memberProfileResponse.getData().getState().getState_code() + " " + memberProfileResponse.getData().getMember().getZip_code());


                        String getURL = W.FILE_URL + memberProfileResponse.getData().getMember().getImage();

                        Picasso.with(getApplicationContext())
                                .load(getURL)
                                .into(circleImageView);


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
        Call<AppModel> modelCall = adminAPI.AddGeneralMember(preference.getUserID(), preference.getTokenHash(), memberId, organizationsIds, fundspotsIds);
        modelCall.enqueue(new Callback<AppModel>() {
            @Override
            public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                dialog.dismiss();
                AppModel model = response.body();
                if (model != null) {
                    if (model.isStatus()) {

                        String checkMemberId = "";
                        String userRoleId = "";

                        if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
                            checkMemberId = fundspot.getId();

                        }
                        if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
                            checkMemberId = organization.getId();

                        }
                        if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                            checkMemberId = member.getId();

                        }

                        if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                            Log.e("path", "check");
                            userRoleId = getResponse.getUser().getRole_id();
                            CheckMemberIsjoined(checkMemberId, userRoleId);
                        }

                        if (!profileMode) {
                            onBackPressed();
                        }

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

    private void AddCampaignMember() {
        dialog.show();
        Call<AppModel> modelCall = adminAPI.AddCampaignMember(preference.getUserID(), memberId, campaignId, preference.getUserRoleID());
        modelCall.enqueue(new Callback<AppModel>() {
            @Override
            public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                dialog.dismiss();
                AppModel model = response.body();
                if (model != null) {
                    if (model.isStatus()) {
                        Intent intent = new Intent(getApplicationContext(), AddMemberToCampaign.class);
                        intent.putExtra("campaignId", campaignId);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
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

    private void JoinGeneralMember(String membersssIdss, String memberId) {
        String fundspotsId = null;
        String organizationsId = null;
        dialog.show();
        if (status) {
            fundspotsId = memberId;
        } else {
            organizationsId = memberId;
        }
        Call<AppModel> appModelCall = adminAPI.JoinMember(preference.getUserID(), membersssIdss, preference.getUserRoleID(), preference.getTokenHash(), fundspotsId, organizationsId);

        appModelCall.enqueue(new Callback<AppModel>() {
            @Override
            public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                dialog.dismiss();
                AppModel model = response.body();
                if (model != null) {
                    if (model.isStatus()) {
                        btnJoin.setText("Leave Us");
                        btnFollow.setText("Unfollow");

                        if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                            btnJoin.setText("Pending");
                            btnFollow.setText("Unfollow");
                        }


                        isMemberJoined = 2;


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

    private void LeaveGeneralMember(String memberId, String selectedUserRoleId, String membersssIdss) {
        dialog.show();
        Call<AppModel> appModelCall = adminAPI.LeaveMemberFromMyList(memberId, selectedUserRoleId, membersssIdss);
        appModelCall.enqueue(new Callback<AppModel>() {
            @Override
            public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                dialog.dismiss();
                AppModel model = response.body();
                if (model != null) {
                    if (model.isStatus()) {
                        btnJoin.setText("Join Us");
                        btnFollow.setText("Follow");
                        isMemberJoined = 0;
                    }
                }
            }

            @Override
            public void onFailure(Call<AppModel> call, Throwable t) {
                dialog.dismiss();
                C.INSTANCE.errorToast(getApplicationContext(), t);
            }
        });
    }

    private void RespondGeneralMembersRequest(final String memberId, final String selectedUserRoleId, String s, String finalCheckMemberId) {
        dialog.show();
        Call<AppModel> appModelCall = adminAPI.Acc_Decline_Request(memberId, finalCheckMemberId, s, selectedUserRoleId);
        appModelCall.enqueue(new Callback<AppModel>() {
            @Override
            public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                dialog.dismiss();
                AppModel model = response.body();
                if (model != null) {
                    if (model.isStatus()) {
                        btnJoin.setText("Leave Us");
                        btnFollow.setText("Unfollow");
                        isMemberJoined = 1;

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
        try {
            fundspot = new Gson().fromJson(preference.getMemberData(), Fundspot.class);
            organization = new Gson().fromJson(preference.getMemberData(), Organization.class);
            member = new Gson().fromJson(preference.getMemberData(), Member.class);
            Log.e("userData", preference.getUserData());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

        J.GetNotificationCountGlobal(preference.getUserID(), preference.getTokenHash(), preference, getApplicationContext(), this);
    }


    // Following are the ASYNCTASK Services that are already converted to retrofit . If you found any issues in Retrofit API please refer the following.



    /*public class JoinMember extends AsyncTask<Void, Void, String> {

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


            String json = new ServiceHandler(getApplicationContext()).makeServiceCall(W.ASYNC_BASE_URL + "Member/app_request_join_member", ServiceHandler.POST, pairs);

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
                        btnJoin.setText("Leave Us");
                        btnFollow.setText("Unfollow");

                        if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                            btnJoin.setText("Pending");
                            btnFollow.setText("Unfollow");
                        }


                        isMemberJoined = 2;

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

    public class LeaveMember extends AsyncTask<Void, Void, String> {

        String userId = "";
        String roleId = "";
        String memberId = "";

        public LeaveMember(String userId, String roleId, String memberId) {
            this.userId = userId;
            this.roleId = roleId;
            this.memberId = memberId;
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
        protected String doInBackground(Void... voids) {

            List<NameValuePair> valuePairs = new ArrayList<>();

            valuePairs.add(new BasicNameValuePair("user_id", userId));
            valuePairs.add(new BasicNameValuePair("role_id", roleId));
            valuePairs.add(new BasicNameValuePair("member_id", memberId));

            String json = new ServiceHandler(getApplicationContext()).makeServiceCall(W.ASYNC_BASE_URL + W.LEAVE_MEMBER, ServiceHandler.POST, valuePairs);

            Log.e("parameters", "-->" + valuePairs.toString());
            Log.e("response", "--->" + json);


            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            try {
                if (s.isEmpty()) {
                    C.INSTANCE.defaultError(getApplicationContext());
                } else {
                    JSONObject mainObject = new JSONObject(s);
                    boolean status = false;
                    String message = "";
                    status = mainObject.getBoolean("status");
                    message = mainObject.getString("message");
                    // C.INSTANCE.showToast(getApplicationContext(), message);
                    if (status) {
                        btnJoin.setText("Join Us");
                        btnFollow.setText("Follow");
                        isMemberJoined = 0;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class RespondMemberRequest extends AsyncTask<Void, Void, String> {

        String getStatus = "";
        String getMemberId = "";
        String roleId = "";
        String userId = "";

        public RespondMemberRequest(String userId, String roleId, String getStatus, String getMemberId) {
            this.userId = userId;
            this.roleId = roleId;
            this.getStatus = getStatus;
            this.getMemberId = getMemberId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {

                dialog = new CustomDialog(AddMembersActivity.this);
                dialog.show();
                dialog.setCancelable(false);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... params) {


            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("user_id", userId));
            pairs.add(new BasicNameValuePair("role_id", roleId));
            pairs.add(new BasicNameValuePair("member_id", getMemberId));
            pairs.add(new BasicNameValuePair("status", getStatus));

            json = new ServiceHandler(getApplicationContext()).makeServiceCall(W.ASYNC_BASE_URL + "Member/app_respond_other_request", ServiceHandler.POST, pairs);

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
                        // recreate();


                        btnJoin.setText("Leave Us");
                        btnFollow.setText("Unfollow");
                        isMemberJoined = 1;



                        *//*finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);*//*
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
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


            String json = new ServiceHandler(getApplicationContext()).makeServiceCall(W.ASYNC_BASE_URL + W.ADD_MEMBER_TO_CAMPAIGN, ServiceHandler.POST, pairs);

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

                   // C.INSTANCE.showToast(getApplicationContext(), message);
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

                    // C.INSTANCE.showToast(getApplicationContext(), message);
                    if (status) {

                        String checkMemberId = "";
                        String userRoleId = "";

                        if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
                            checkMemberId = fundspot.getId();

                        }
                        if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
                            checkMemberId = organization.getId();

                        }
                        if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                            checkMemberId = member.getId();

                        }

                        if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                            Log.e("path", "check");
                            userRoleId = getResponse.getUser().getRole_id();
                            CheckMemberIsjoined(checkMemberId, userRoleId);
                        }

                        if (!profileMode) {
                            onBackPressed();
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
            pairs.add(new BasicNameValuePair("member_id", memberId));


            String json = new ServiceHandler(getApplicationContext()).makeServiceCall(W.ASYNC_BASE_URL + "Member/app_view_member_profile", ServiceHandler.POST, pairs);

            Log.e("parametersAddMembers", "-->" + pairs);
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

                        String name = "", emailids = "", contact = "", contactinfoMobile = "", contactinfoEmail = "", organizationName = "", fundspotName = "";
                        name = userObject.getString("title");
                        emailids = userObject.getString("email_id");
                        contact = memberObject.getString("contact_info");
                        contactinfoMobile = memberObject.getString("contact_info_mobile");
                        contactinfoEmail = memberObject.getString("contact_info_email");
                        organizationName = memberObject.getString("organization_names");
                        fundspotName = memberObject.getString("fundspot_names");


                        txt_name.setText(name);


                        if (emailids == null || emailids.equalsIgnoreCase("")) {
                            txt_emailID.setVisibility(View.GONE);
                            layout_mail.setVisibility(View.GONE);
                        } else {
                            txt_emailID.setText(emailids);
                            layout_mail.setVisibility(View.GONE);
                        }


                        if (contact == null || contact.equalsIgnoreCase("") || contact.equalsIgnoreCase("null") || contact.equalsIgnoreCase(null)) {
                            layout_contact.setVisibility(View.GONE);
                        } else {
                            txt_contct.setText(contact);
                        }


                        if (contactinfoMobile == null || contactinfoMobile.equalsIgnoreCase("") || contactinfoMobile.equalsIgnoreCase("null") || contactinfoMobile.equalsIgnoreCase(null)) {
                            layout_contact_info_mobile.setVisibility(View.GONE);
                        } else {
                            txt_con_info_mobile.setText(contactinfoMobile);
                        }

                        if (contactinfoEmail == null || contactinfoEmail.equalsIgnoreCase("") || contactinfoEmail.equalsIgnoreCase("null") || contactinfoEmail.equalsIgnoreCase(null)) {
                            layout_contact_info_email.setVisibility(View.GONE);
                        } else {
                            txt_con_info_email.setText(contactinfoEmail);
                        }

                        Log.e("check", "-->" + organizationName + "-->" + fundspotName + "-->" + layout_org.getVisibility() + "-->");
                        if (organizationName == null || organizationName.equalsIgnoreCase("") || organizationName.equalsIgnoreCase("null") || organizationName.equalsIgnoreCase(null)) {
                            layout_org.setVisibility(View.GONE);
                        } else {
                            layout_org.setVisibility(View.VISIBLE);
                            txt_organizations.setText(organizationName);
                        }

                        if (fundspotName == null || fundspotName.equalsIgnoreCase("") || fundspotName.equalsIgnoreCase("null") || fundspotName.equalsIgnoreCase(null)) {
                            layout_fun.setVisibility(View.GONE);
                        } else {
                            layout_fun.setVisibility(View.VISIBLE);
                            txt_fundspots.setText(fundspotName);
                        }


                        txt_address.setText(memberObject.getString("location") + "\n" + memberObject.getString("city_name") + ", " + stateObject.getString("state_code") + " " + memberObject.getString("zip_code"));


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
    }*/

}