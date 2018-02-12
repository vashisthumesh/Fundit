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
import com.fundit.model.Address;
import com.fundit.model.App_Single_Fundspot;
import com.fundit.model.App_Single_Organization;
import com.fundit.model.Fundspot;
import com.fundit.model.JoinMemberModel;
import com.fundit.model.Member;
import com.fundit.model.News_model;
import com.fundit.model.Organization;
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


public class AddMemberFudActivity extends AppCompatActivity {
    App_Single_Fundspot app_fund_list;
    App_Single_Organization app_single_org_list;
    AppPreference preference;
    CustomDialog dialog;
    AdminAPI adminAPI;
    Member member = new Member();
    CircleImageView circleImageView;

    TextView txt_name, txt_address, txt_emailID, txt_organizations, txt_fundspots, txt_currentCampaigns, txt_pastCampaigns, txt_contct, txt_con_info_email, txt_con_info_mobile, txt_fundtitle, txt_fundraiser_split;
    LinearLayout layout_contact_info_email, layout_contact_info_mobile;

    Button btnAdd, btnJoin, btnFollow, btnMessage, btnrequest;

    LinearLayout layout_contact, current, past, layout_buttons, layout_mail, layout_category, layout_fundraiser, layout_type, layout_description, layout_org, layout_fun;


    String memberId = "";
    String Flag="";
    String location="";
    String city_name="";
    String state_code="";
    String Zipcode="";
    String checkMemberId = "";
    String userRoleId = "";
    String userID="";


    int isMemberJoined = 0;
    Fundspot fundspot = new Fundspot();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member_fud);

        preference = new AppPreference(getApplicationContext());
        dialog = new CustomDialog(this);
        adminAPI = ServiceGenerator.getAPIClass();


        Intent intent = getIntent();
        memberId = intent.getStringExtra("memberId");
        Flag=intent.getStringExtra("Flag");

        try {
            member = new Gson().fromJson(preference.getMemberData(), Member.class);
            fundspot = new Gson().fromJson(preference.getMemberData(), Fundspot.class);
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
        actionTitle.setText("");

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
        layout_mail.setVisibility(View.GONE);

        circleImageView = (CircleImageView) findViewById(R.id.img_profilePic);


        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnJoin = (Button) findViewById(R.id.button_join);
        btnFollow = (Button) findViewById(R.id.button_follow);
        btnMessage = (Button) findViewById(R.id.btnmessage);
        btnrequest = (Button) findViewById(R.id.btnrequest);


        if(Flag.equalsIgnoreCase("fund"))
        {
            Fundspot_Data(memberId);
        }
        else if(Flag.equalsIgnoreCase("org"))
        {
            Organization_Data(memberId);
        }



        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedUsersId = "";
                String membersssIdss = "";
                Log.e("checking", "-->");
                if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                    membersssIdss = member.getId();
                }

                if (isMemberJoined == 0) {
                    new  JoinMember(membersssIdss, memberId).execute();
                } else if(isMemberJoined==1) {
                    new  LeaveMember(preference.getUserID(), preference.getUserRoleID(), membersssIdss).execute();
                }else if(isMemberJoined==2){
                    C.INSTANCE.showToast(getApplicationContext() , "You request are to join is pending!");
                }
            }
        });



        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(Flag.equalsIgnoreCase("fund"))
                {
                    Intent intent = new Intent(getApplicationContext(), FinalSendMessage.class);
                    intent.putExtra("name", app_fund_list.getData().getUser().getTitle());
                    intent.putExtra("id", app_fund_list.getData().getUser().getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else if(Flag.equalsIgnoreCase("org"))
                {
                    Intent intent = new Intent(getApplicationContext(), FinalSendMessage.class);
                    intent.putExtra("name", app_single_org_list.getData().getUser().getTitle());
                    intent.putExtra("id", app_single_org_list.getData().getUser().getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }


            }
        });




        btnrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FundspotProductListActivity.class);
                    /*intent.putExtra("fundspotName", fundSpotList.get(i).getFundspot().getTitle());
                    intent.putExtra("fundspotID", fundSpotList.get(i).getFundspot().getUser_id());
*/
                /*if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
                    intent.putExtra("fundspotName", app_fund_list.getData().getFundspot().getTitle());
                    intent.putExtra("fundspotID", app_fund_list.getData().getFundspot().getUser_id());
                    intent.putExtra("profileMode", true);
                    startActivity(intent);
                } else {
                    intent.putExtra("fundspotName", app_single_org_list.getData().getOrganization().getTitle());
                    intent.putExtra("fundspotID", app_single_org_list.getData().getOrganization().getUser_id());
                    intent.putExtra("profileMode", true);
                    startActivity(intent);
                }*/

                if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {

                    if ((app_fund_list.getData().getFundspot().getFundspot_percent().equalsIgnoreCase("0") && app_fund_list.getData().getFundspot().getOrganization_percent().equalsIgnoreCase("0"))) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddMemberFudActivity.this);
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
                    } else if (app_fund_list.getData().getFundspot().getProduct_count().equalsIgnoreCase("0")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddMemberFudActivity.this);
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
                        intent.putExtra("fundspotName", app_fund_list.getData().getFundspot().getTitle());
                        intent.putExtra("fundspotID", app_fund_list.getData().getFundspot().getUser_id());
                        intent.putExtra("profileMode", true);
                        startActivity(intent);

                    }
                } else if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {

                    Log.e("requestSettings", "--->" + fundspot.getFundspot_percent() + "-->" + fundspot.getOrganization_percent() + "-->" + preference.getfundspot_product_count());
                    if ((fundspot.getFundspot_percent().equalsIgnoreCase("0") && fundspot.getOrganization_percent().equalsIgnoreCase("0"))) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(AddMemberFudActivity.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddMemberFudActivity.this);
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

                        intent.putExtra("fundspotName", fundspot.getTitle());
                        intent.putExtra("fundspotID", fundspot.getUser_id());
                        intent.putExtra("organizationID", app_single_org_list.getData().getOrganization().getUser_id());
                        intent.putExtra("profileMode", true);
                        startActivity(intent);
                    }
                }













            }
        });



    }



    public void Fundspot_Data(String user_id)
    {


        dialog.show();
        Call<App_Single_Fundspot> app_single_fundspotCall=null;
        app_single_fundspotCall=adminAPI.GetAllFundspot(user_id);
        Log.e("user_id","--->"+user_id);

        app_single_fundspotCall.enqueue(new Callback<App_Single_Fundspot>() {
            @Override
            public void onResponse(Call<App_Single_Fundspot> call, Response<App_Single_Fundspot> response) {
                dialog.dismiss();
                 app_fund_list=response.body();
                if(app_fund_list != null)
                {
                    if(app_fund_list.isStatus())
                    {

                        if(preference.getUserID().equalsIgnoreCase(memberId))
                        {
                            layout_buttons.setVisibility(View.GONE);
                            btnrequest.setVisibility(View.GONE);
                            btnMessage.setVisibility(View.GONE);
                        }
                        else if(preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION))
                        {
                            layout_buttons.setVisibility(View.VISIBLE);
                            btnrequest.setVisibility(View.VISIBLE);
                            btnFollow.setVisibility(View.VISIBLE);
                            btnJoin.setVisibility(View.GONE);
                            btnMessage.setVisibility(View.VISIBLE);
                        }
                        else if(preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT))
                        {
                            btnrequest.setVisibility(View.GONE);
                            layout_buttons.setVisibility(View.VISIBLE);
                            btnFollow.setVisibility(View.VISIBLE);
                            btnJoin.setVisibility(View.GONE);
                            btnMessage.setVisibility(View.VISIBLE);
                        }
                        else if(preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER))
                        {
                            layout_buttons.setVisibility(View.VISIBLE);
                            btnrequest.setVisibility(View.GONE);
                            btnFollow.setVisibility(View.VISIBLE);
                            btnJoin.setVisibility(View.VISIBLE);
                            btnMessage.setVisibility(View.VISIBLE);

                            checkMemberId = member.getId();

                            userRoleId = app_fund_list.getData().getUser().getRole_id();
                            userID=app_fund_list.getData().getUser().getId();
                            CheckMemberIsjoined(checkMemberId , userRoleId,userID);
                        }






                        String imagePath = W.FILE_URL + app_fund_list.getData().getFundspot().getImage();
                        Log.e("path", imagePath);
                        Picasso.with(getApplicationContext())
                                .load(imagePath)
                                .into(circleImageView);

                        if(app_fund_list.getData().getFundspot().getSplit_visibility().equalsIgnoreCase("1"))
                        {
                            layout_fundraiser.setVisibility(View.VISIBLE);
                            txt_fundtitle.setText("% Split ( Fundspot/Organization ):");
                            txt_fundraiser_split.setText(app_fund_list.getData().getFundspot().getFundspot_percent() + " / " + app_fund_list.getData().getFundspot().getOrganization_percent());

                        }
                        else {
                            layout_fundraiser.setVisibility(View.GONE);
                        }



                        txt_name.setText(app_fund_list.getData().getFundspot().getTitle());

                        if(app_fund_list.getData().getFundspot().getLocation() != null)
                        {
                            location =app_fund_list.getData().getFundspot().getLocation();

                        }
                        if(app_fund_list.getData().getFundspot().getCity_name() != null)
                        {
                            city_name=app_fund_list.getData().getFundspot().getCity_name();
                        }
                        if(app_fund_list.getData().getState().getState_code() != null)
                        {
                            state_code=app_fund_list.getData().getState().getState_code();
                        }
                        if(app_fund_list.getData().getFundspot().getZip_code() != null)
                        {
                            Zipcode=app_fund_list.getData().getFundspot().getZip_code();
                        }

                        if(app_fund_list.getData().getFundspot().getDescription() == null || app_fund_list.getData().getFundspot().getDescription().equalsIgnoreCase(""))
                        {
                            layout_contact.setVisibility(View.GONE);
                        }

                        else
                        {
                            layout_contact.setVisibility(View.VISIBLE);
                            txt_contct.setText(app_fund_list.getData().getFundspot().getDescription());
                        }
                        txt_address.setText(location+'\n'+city_name+", "+state_code+" "+Zipcode);



                        if (app_fund_list.getData().getFundspot().getContact_info_email() == null || app_fund_list.getData().getFundspot().getContact_info_email().equalsIgnoreCase("")) {
                            layout_contact_info_email.setVisibility(View.GONE);

                        } else {
                            layout_contact_info_email.setVisibility(View.VISIBLE);
                            txt_con_info_email.setText(app_fund_list.getData().getFundspot().getContact_info_email());

                        }

                        if(app_fund_list.getData().getFundspot().getContact_info_mobile() == null  || app_fund_list.getData().getFundspot().getContact_info_mobile().equalsIgnoreCase(""))
                        {
                            layout_contact_info_mobile.setVisibility(View.GONE);
                        }
                        else {
                            layout_contact_info_mobile.setVisibility(View.VISIBLE);
                            txt_con_info_mobile.setText(app_fund_list.getData().getFundspot().getContact_info_mobile());
                        }


                    }
                    else {
                        C.INSTANCE.showToast(getApplicationContext(), app_fund_list.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<App_Single_Fundspot> call, Throwable t) {
                dialog.dismiss();
                C.INSTANCE.errorToast(getApplicationContext(), t);
            }
        });
    }


    public void Organization_Data(final String user_id)
    {
        dialog.show();
        Call<App_Single_Organization> app_single_organizationCall=null;
        app_single_organizationCall=adminAPI.GetAllOrganization(user_id);
        Log.e("user_id","--->"+user_id);

        app_single_organizationCall.enqueue(new Callback<App_Single_Organization>() {
            @Override
            public void onResponse(Call<App_Single_Organization> call, Response<App_Single_Organization> response) {
                dialog.dismiss();
                 app_single_org_list=response.body();
                if(app_single_org_list != null)
                {
                    if(app_single_org_list.isStatus())
                    {
                        Log.e("user_idAdd","--->"+preference.getUserID() + "-->" + memberId);

                        if(preference.getUserID().equalsIgnoreCase(memberId))
                        {
                            layout_buttons.setVisibility(View.GONE);
                            btnrequest.setVisibility(View.GONE);
                            btnMessage.setVisibility(View.GONE);

                        }
                        else if(preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION))
                        {
                            layout_buttons.setVisibility(View.VISIBLE);
                            btnrequest.setVisibility(View.GONE);
                            btnFollow.setVisibility(View.VISIBLE);
                            btnJoin.setVisibility(View.GONE);
                            btnMessage.setVisibility(View.VISIBLE);
                        }
                        else if(preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT))
                        {

                            layout_buttons.setVisibility(View.VISIBLE);
                            btnrequest.setVisibility(View.VISIBLE);
                            btnFollow.setVisibility(View.VISIBLE);
                            btnJoin.setVisibility(View.GONE);
                            btnMessage.setVisibility(View.VISIBLE);
                        }
                        else if(preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER))
                        {
                            layout_buttons.setVisibility(View.VISIBLE);
                            btnrequest.setVisibility(View.GONE);
                            btnFollow.setVisibility(View.VISIBLE);
                            btnJoin.setVisibility(View.VISIBLE);
                            btnMessage.setVisibility(View.VISIBLE);

                            checkMemberId = member.getId();

                            userRoleId = app_single_org_list.getData().getUser().getRole_id();
                            userID=app_single_org_list.getData().getUser().getId();
                            CheckMemberIsjoined(checkMemberId , userRoleId,userID);
                        }






                        String imagePath = W.FILE_URL + app_single_org_list.getData().getOrganization().getImage();
                        Log.e("path", imagePath);
                        Picasso.with(getApplicationContext())
                                .load(imagePath)
                                .into(circleImageView);


                            layout_fundraiser.setVisibility(View.GONE);




                        txt_name.setText(app_single_org_list.getData().getOrganization().getTitle());

                        if(app_single_org_list.getData().getOrganization().getLocation() != null)
                        {
                            location =app_single_org_list.getData().getOrganization().getLocation();

                        }
                        if(app_single_org_list.getData().getOrganization().getCity_name() != null)
                        {
                            city_name=app_single_org_list.getData().getOrganization().getCity_name();
                        }
                        if(app_single_org_list.getData().getState().getState_code() != null)
                        {
                            state_code=app_single_org_list.getData().getState().getState_code();
                        }
                        if(app_single_org_list.getData().getOrganization().getZip_code() != null)
                        {
                            Zipcode=app_single_org_list.getData().getOrganization().getZip_code();
                        }

                        if(app_single_org_list.getData().getOrganization().getDescription() == null || app_single_org_list.getData().getOrganization().getDescription().equalsIgnoreCase(""))
                        {
                            layout_contact.setVisibility(View.GONE);
                        }

                        else
                        {
                            layout_contact.setVisibility(View.VISIBLE);
                            txt_contct.setText(app_single_org_list.getData().getOrganization().getDescription());
                        }
                        txt_address.setText(location+'\n'+city_name+", "+state_code+" "+Zipcode);



                        if (app_single_org_list.getData().getOrganization().getContact_info_email() == null || app_single_org_list.getData().getOrganization().getContact_info_email().equalsIgnoreCase("")) {
                            layout_contact_info_email.setVisibility(View.GONE);

                        } else {
                            layout_contact_info_email.setVisibility(View.VISIBLE);
                            txt_con_info_email.setText(app_single_org_list.getData().getOrganization().getContact_info_email());

                        }

                        if(app_single_org_list.getData().getOrganization().getContact_info_mobile() == null  || app_single_org_list.getData().getOrganization().getContact_info_mobile().equalsIgnoreCase(""))
                        {
                            layout_contact_info_mobile.setVisibility(View.GONE);
                        }
                        else {
                            layout_contact_info_mobile.setVisibility(View.VISIBLE);
                            txt_con_info_mobile.setText(app_single_org_list.getData().getOrganization().getContact_info_mobile());
                        }





                    }
                    else {
                        C.INSTANCE.showToast(getApplicationContext(),app_single_org_list.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<App_Single_Organization> call, Throwable t) {
                C.INSTANCE.errorToast(getApplicationContext(),t);
            }
        });
    }

    private void CheckMemberIsjoined(String checkMemberId , String userRoleId,String userID) {

        dialog.show();
        Call<JoinMemberModel> appModelCall = adminAPI.checkJoinMember(checkMemberId, /*preference.getUserRoleID()*/ userRoleId, /*preference.getUserID()*/  userID);
        Log.e("parameters" , "-->" + checkMemberId + "-->" + userRoleId + "--->" + userID);
        appModelCall.enqueue(new Callback<JoinMemberModel>() {
            @Override
            public void onResponse(Call<JoinMemberModel> call, Response<JoinMemberModel> response) {
                dialog.dismiss();
                JoinMemberModel appModel = response.body();
                if (appModel != null) {
                    C.INSTANCE.showToast(getApplicationContext(), appModel.getMessage());
                    if (appModel.isStatus()) {
                        if (appModel.getData() == 1) {
                            btnJoin.setText("Leave Us");
                            isMemberJoined = 1;
                        }if(appModel.getData()==2){
                            btnJoin.setText("Pending");
                            isMemberJoined=2;
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


            if(Flag.equalsIgnoreCase("fund"))
            {
                pairs.add(new BasicNameValuePair("fundspot_id", selectedUserIdss));
            }
            else if(Flag.equalsIgnoreCase("org"))
            {
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

            String json = new ServiceHandler().makeServiceCall(W.BASE_URL + W.LEAVE_MEMBER, ServiceHandler.POST, valuePairs);

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
                    C.INSTANCE.showToast(getApplicationContext(), message);
                    if (status) {
                        btnJoin.setText("Join Us");
                        isMemberJoined = 0;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
