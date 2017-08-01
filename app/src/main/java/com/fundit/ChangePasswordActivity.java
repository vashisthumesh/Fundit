package com.fundit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.AppModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText edt_password;
    Button btn_change;

    String userID = "";

    AdminAPI adminAPI;
    CustomDialog dialog;

    AppPreference preference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        adminAPI = ServiceGenerator.getAPIClass();
        dialog = new CustomDialog(this);
        preference= new AppPreference(getApplicationContext());
        fetchID();
        setupToolbar();

    }

    private void setupToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionTitle.setText("Change Password");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void fetchID() {

        userID = preference.getUserID();

        edt_password = (EditText) findViewById(R.id.edt_password);
        btn_change = (Button) findViewById(R.id.btn_change);


        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getPassword = edt_password.getText().toString().trim();

                if (getPassword.isEmpty()) {


                    C.INSTANCE.showToast(getApplicationContext(), "Please enter password");
                    edt_password.requestFocus();


                } else {

                    Call<AppModel> forgetpasswordcall = adminAPI.ForgetPass_change_edit(userID, getPassword);

                    forgetpasswordcall.enqueue(new Callback<AppModel>() {
                        @Override
                        public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                            dialog.dismiss();
                            AppModel appModel = response.body();
                            if (appModel != null) {
                                if (appModel.isStatus()) {
                                    C.INSTANCE.showToast(getApplicationContext(), appModel.getMessage());
                                    Intent in3 = new Intent(getApplicationContext(), HomeActivity.class);
                                    in3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(in3);

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


            }
        });


    }
}
