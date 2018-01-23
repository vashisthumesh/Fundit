package com.fundit;

import android.content.Intent;
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
import com.fundit.a.W;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.Address;
import com.fundit.model.App_Single_Fundspot;
import com.fundit.model.App_Single_Organization;
import com.fundit.model.News_model;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddMemberFudActivity extends AppCompatActivity {

    AppPreference preference;
    CustomDialog dialog;
    AdminAPI adminAPI;

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




    int isMemberJoined = 0;


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
                App_Single_Fundspot app_fund_list=response.body();
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
                            btnrequest.setVisibility(View.VISIBLE);
                            btnFollow.setVisibility(View.VISIBLE);
                            btnJoin.setVisibility(View.GONE);
                            btnMessage.setVisibility(View.VISIBLE);
                        }






                        String imagePath = W.FILE_URL + app_fund_list.getData().getFundspot().getImage();
                        Log.e("path", imagePath);
                        Picasso.with(getApplicationContext())
                                .load(imagePath)
                                .into(circleImageView);

                        if(app_fund_list.getData().getFundspot().getSplit_visibility().equalsIgnoreCase("1"))
                        {
                            layout_fundraiser.setVisibility(View.VISIBLE);
                            txt_fundtitle.setText("% Split ( Fundspot/Organization ): ");
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
                        txt_address.setText(location+'\n'+city_name+","+state_code+" "+Zipcode);



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
                App_Single_Organization app_single_org_list=response.body();
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
                            btnrequest.setVisibility(View.VISIBLE);
                            btnFollow.setVisibility(View.VISIBLE);
                            btnJoin.setVisibility(View.GONE);
                            btnMessage.setVisibility(View.VISIBLE);
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
                        txt_address.setText(location+'\n'+city_name+","+state_code+" "+Zipcode);



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




}