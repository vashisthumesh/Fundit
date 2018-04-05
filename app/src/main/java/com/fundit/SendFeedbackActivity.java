package com.fundit;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.fundit.model.Inbox;
import com.fundit.model.Member;
import com.fundit.model.Organization;
import com.fundit.model.User;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendFeedbackActivity extends AppCompatActivity {

    AppPreference preference;
    CustomDialog dialog;
    EditText edtName, edtMail, edtMessage;
    Button btnSend;
    Member member = new Member();
    Organization organization = new Organization();
    Fundspot fundspot = new Fundspot();
    User user = new User();
    AdminAPI adminAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback);
        preference = new AppPreference(getApplicationContext());
        dialog = new CustomDialog(SendFeedbackActivity.this);
        adminAPI = ServiceGenerator.getAPIClass();
        try {
            member = new Gson().fromJson(preference.getMemberData(), Member.class);
            organization = new Gson().fromJson(preference.getMemberData(), Organization.class);
            fundspot = new Gson().fromJson(preference.getMemberData(), Fundspot.class);
            user = new Gson().fromJson(preference.getUserData(), User.class);
            Log.e("data", preference.getMemberData());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setUpToolbar();
        fetchIds();
    }

    private void fetchIds() {

        edtName = (EditText) findViewById(R.id.edt_name);
        edtMail = (EditText) findViewById(R.id.edt_subject);
        edtMessage = (EditText) findViewById(R.id.edt_message);

        btnSend = (Button) findViewById(R.id.btn_send);

        Log.e("nameemail", "--->" + user.getTitle() + "--->" + user.getEmail_id());

        edtName.setText(user.getTitle());
        edtMail.setText(user.getEmail_id());

        edtName.setEnabled(false);
        edtMail.setEnabled(false);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edtName.getText().toString().trim();
                String emailId = edtMail.getText().toString().trim();
                String message = edtMessage.getText().toString().trim();


                if (name.isEmpty())
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter your fullname");
                else if (emailId.isEmpty())
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter your email-id");
                else if (message.isEmpty())
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter proper comment");
                else {
                    //   new SendFeedBack(name, emailId, message).execute();

                    SendUsersFeedBack(name, emailId, message);

                }
            }
        });
    }


    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);
        actionTitle.setText("Feedback");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    private void SendUsersFeedBack(String name, String emailId, String message) {
        dialog.show();
        Call<AppModel> modelCall = adminAPI.SendUsersFeedback(preference.getUserID(), name, message, emailId);
        modelCall.enqueue(new Callback<AppModel>() {
            @Override
            public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                dialog.dismiss();
                AppModel model = response.body();
                if (model != null) {
                    if (model.isStatus()) {
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        preference.setRedirection("6");
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


    @Override
    protected void onResume() {
        super.onResume();
        System.gc();
    }

// Following are the ASYNCTASK Services that are already converted to retrofit . If you found any issues in Retrofit API please refer the following.

    /*public class SendFeedBack extends AsyncTask<Void, Void, String> {

        String userName = "";
        String userMail = "";
        String comment = "";

        public SendFeedBack(String userName, String userMail, String comment) {
            this.userName = userName;
            this.userMail = userMail;
            this.comment = comment;
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


            List<NameValuePair> parameters = new ArrayList<>();

            parameters.add(new BasicNameValuePair(W.KEY_USERID, preference.getUserID()));
            parameters.add(new BasicNameValuePair("full_name", userName));
            parameters.add(new BasicNameValuePair("comment", comment));
            parameters.add(new BasicNameValuePair("email_id", userMail));


            String json = new ServiceHandler(getApplicationContext()).makeServiceCall(W.ASYNC_BASE_URL + "User/App_Feedback", ServiceHandler.POST, parameters);

            Log.e("parameters", "--->" + parameters);
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
                    //  C.INSTANCE.showToast(getApplicationContext() , message);
                    if (status) {

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        preference.setRedirection("6");
                        startActivity(intent);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }*/
}
