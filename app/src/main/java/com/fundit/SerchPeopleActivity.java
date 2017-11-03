package com.fundit;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.fundit.model.Fundspot;
import com.fundit.model.GetDataResponses;
import com.fundit.model.GetSearchPeople;
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

public class SerchPeopleActivity extends AppCompatActivity {

    boolean flag=false;
    AppPreference preference;
    CustomDialog dialog;
    AdminAPI adminAPI;

    CircleImageView circleImageView;

    TextView txt_name, txt_address, txt_emailID, txt_organizations, txt_fundspots, txt_currentCampaigns, txt_pastCampaigns, txt_email,txt_mobile;

    Button btnAdd, btnJoin, btnFollow, btnMessage;

    LinearLayout layout_contact, current, past, layout_buttons , layout_mail,layout_fundspot,layput_contact_mobile,layput_contact_email;


    String Id = "";
    String campaignId = "";
    String roleIdss = "";
    boolean profileMode = false;
    boolean status = false;

    GetSearchPeople.People getResponse;


    Fundspot fundspot = new Fundspot();
    Organization organization = new Organization();
    Member member = new Member();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search_people);

        preference = new AppPreference(getApplicationContext());
        dialog = new CustomDialog(this);
        adminAPI = ServiceGenerator.getAPIClass();


        Intent intent = getIntent();
        Id = intent.getStringExtra("id");
        flag=intent.getBooleanExtra("flag",false);
        getResponse = (GetSearchPeople.People) intent.getSerializableExtra("details");

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
        circleImageView = (CircleImageView) findViewById(R.id.img_profilePic);
        txt_fundspots= (TextView) findViewById(R.id.txt_fundspot);
        layout_fundspot= (LinearLayout) findViewById(R.id.layout_fundspot);
        layput_contact_email= (LinearLayout) findViewById(R.id.layput_contact_email);
        layput_contact_mobile= (LinearLayout) findViewById(R.id.layput_contact_mobile);
        txt_email= (TextView) findViewById(R.id.txt_email);
        txt_mobile= (TextView) findViewById(R.id.txt_mobile);


        btnAdd = (Button) findViewById(R.id.btnAdd);

        btnMessage = (Button) findViewById(R.id.btnmessage);
        new GetAllDetails().execute();


        if(preference.getUserRoleID().equalsIgnoreCase("4"))
        {
            btnAdd.setVisibility(View.GONE);
        }
        else if(preference.getUserRoleID().equalsIgnoreCase("2"))
        {
            if(flag==true) {
                btnAdd.setVisibility(View.GONE);
            }
            else {
                btnAdd.setVisibility(View.VISIBLE);
            }
        }
        else if (preference.getUserRoleID().equalsIgnoreCase("3"))
        {
            if(flag == true)
            {
                btnAdd.setVisibility(View.GONE);
            }
            else {
                btnAdd.setVisibility(View.VISIBLE);
            }

        }



        btnMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext() , FinalSendMessage.class);
                    intent.putExtra("name" ,txt_name.getText().toString().trim());
                    intent.putExtra("id" ,Id);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
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
            pairs.add(new BasicNameValuePair(W.KEY_TOKEN,preference.getTokenHash()));
            pairs.add(new BasicNameValuePair("member_id", Id));


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


                        if(memberObject.getString("fundspot_names").equalsIgnoreCase(""))
                        {
                            layout_fundspot.setVisibility(View.GONE);
                            txt_fundspots.setVisibility(View.GONE);
                        }
                        else {
                            layout_fundspot.setVisibility(View.VISIBLE);
                            txt_fundspots.setVisibility(View.VISIBLE);
                            txt_fundspots.setText(memberObject.getString("fundspot_names"));
                        }


                        txt_name.setText(userObject.getString("title"));
                        txt_emailID.setText(userObject.getString("email_id"));


                        txt_address.setText(cityObject.getString("name") + stateObject.getString("state_code") + " , " + memberObject.getString("zip_code"));
                        txt_organizations.setText(memberObject.getString("organization_names"));


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










}