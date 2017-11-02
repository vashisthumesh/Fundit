package com.fundit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fundit.Bean.Bean_Notification_history;
import com.fundit.a.W;
import com.fundit.helper.CustomDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationDetailActivity extends AppCompatActivity {


    String title= "";
    String location="";
    String city="";
    String zipcode="";
    String desc="";
    String state_code="";
    String role_id="";
    String user_id="";
    TextView txt_title,txt_add,txt_about;
    CircleImageView  circleImageView;
    String imagePath="";
    Button btn;
    String id="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        Intent i=getIntent();
        role_id=i.getStringExtra("role_id");
        user_id=i.getStringExtra("sent_user");



        fetchIDs();
        setUpToolBar();


    }


    private void setUpToolBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        actionTitle.setText("Notification Details");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    private  void  fetchIDs()
        {


            txt_title= (TextView) findViewById(R.id.txt_title);
            txt_about= (TextView) findViewById(R.id.txt_about);
            txt_add= (TextView) findViewById(R.id.txt_address);
            btn=(Button)findViewById(R.id.btnmessage);
            circleImageView = (CircleImageView) findViewById(R.id.img_profilePic);



            if(role_id.equalsIgnoreCase("3"))
            {

                getNotificationsFundspot(user_id);
            }

            if(role_id.equalsIgnoreCase("2"))
            {

                getNotificationsOrganization(user_id);

            }


            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext() , FinalSendMessage.class);
                    intent.putExtra("name" ,title);
                    intent.putExtra("id" ,id);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });


        }





    private void getNotificationsOrganization(final String user_id) {
        final CustomDialog loadingView = new CustomDialog(NotificationDetailActivity.this, "");
        loadingView.setCancelable(false);
        loadingView.show();
        StringRequest request = new StringRequest(W.POST, W.BASE_URL + "Organization/app_get_single_organization",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {

                        Log.e("JSON", json);
                        try {
                            JSONObject jObj = new JSONObject(json);

                            boolean date = jObj.getBoolean("status");


                            if(date == true)
                            {

                                String Message = jObj.getString("message");
                              //  Toast.makeText(getApplicationContext(), "" + Message, Toast.LENGTH_LONG).show();
                                JSONObject data = jObj.getJSONObject("data");
                                JSONObject Organization=data.getJSONObject("Organization");
                                JSONObject User=data.getJSONObject("User");
                                JSONObject State=data.getJSONObject("State");
                                JSONObject City=data.getJSONObject("City");


                                title=Organization.getString("title");
                                location=Organization.getString("location");
                                city=City.getString("name");
                                state_code=State.getString("state_code");
                                zipcode=Organization.getString("zip_code");
                                desc=Organization.getString("description");
                                imagePath = W.FILE_URL + Organization.getString("image");
                                id=Organization.getString("id");


                                Picasso.with(getApplicationContext())
                                        .load(imagePath)
                                        .into(circleImageView);
                                txt_title.setText(title);
                                txt_add.setText(location +"\n"+city+","+" "+state_code+" "+zipcode);
                                txt_about.setText(desc);
                            }
                            else {
                                String Message = jObj.getString("message");
                                Toast.makeText(getApplicationContext(), "" + Message, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException j) {
                            j.printStackTrace();
                            Log.e("Exception", "-->" + j.getMessage());
                        }
                        loadingView.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingView.dismiss();
                        Log.e("ERROR", error.getMessage());

                        if (error instanceof NetworkError) {
                            // noInternet(getApplicationContext());
                        } else {
                            // serverError(getApplicationContext());
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user_id);
                Log.e("params", params.toString());
                return params;
            }
        };
        Fundit.getInstance().addToRequestQueue(request);

    }

    private void getNotificationsFundspot(final String user_id) {
        final CustomDialog loadingView = new CustomDialog(NotificationDetailActivity.this, "");
        loadingView.setCancelable(false);
        loadingView.show();
        StringRequest request = new StringRequest(W.POST, W.BASE_URL + "Fundspot/app_get_single_fundspot",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {

                        Log.e("JSON", json);
                        try {
                            JSONObject jObj = new JSONObject(json);

                            boolean date = jObj.getBoolean("status");


                            if(date == true)
                            {

                                String Message = jObj.getString("message");
                              //  Toast.makeText(getApplicationContext(), "" + Message, Toast.LENGTH_LONG).show();
                                JSONObject data = jObj.getJSONObject("data");
                                JSONObject Fundspot=data.getJSONObject("Fundspot");
                                JSONObject User=data.getJSONObject("User");
                                JSONObject State=data.getJSONObject("State");
                                JSONObject City=data.getJSONObject("City");


                              title=Fundspot.getString("title");
                                location=Fundspot.getString("location");
                               city=City.getString("name");
                               state_code=State.getString("state_code");
                                zipcode=Fundspot.getString("zip_code");
                              desc=Fundspot.getString("description");
                                imagePath = W.FILE_URL + Fundspot.getString("image");
                                id=Fundspot.getString("id");

                                Picasso.with(getApplicationContext())
                                        .load(imagePath)
                                        .into(circleImageView);


                                txt_title.setText(title);
                                txt_add.setText(location +"\n"+city+","+" "+state_code+" "+zipcode);
                                txt_about.setText(desc);
                            }
                            else {
                                String Message = jObj.getString("message");
                                Toast.makeText(getApplicationContext(), "" + Message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException j) {
                            j.printStackTrace();
                            Log.e("Exception", "-->" + j.getMessage());
                        }
                        loadingView.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingView.dismiss();
                        Log.e("ERROR", error.getMessage());

                        if (error instanceof NetworkError) {
                            // noInternet(getApplicationContext());
                        } else {
                            // serverError(getApplicationContext());
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user_id);
                Log.e("params", params.toString());
                return params;
            }
        };
        Fundit.getInstance().addToRequestQueue(request);

    }
}
