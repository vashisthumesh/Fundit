package com.fundit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fundit.a.C;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.AppModel;
import com.fundit.model.ForgotPasswordEmailResponse;
import com.fundit.model.VerifyResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {


    LinearLayout lv_forget_email,lv_forget_otp,lv_re_enter_password;
    EditText ed_forget_pass_email,ed_otp,ed_new_password,ed_re_enter_password;
    Button  bt_send,ed_otp_continue,bt_forget_update;
    boolean flag=false;


    AdminAPI adminAPI;
    CustomDialog dialog;

    String userID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        adminAPI= ServiceGenerator.getAPIClass();
        dialog=new CustomDialog(this);
        fetchid();
        setupToolbar();
    }
    private void fetchid() {
        lv_forget_email=(LinearLayout)findViewById(R.id.lv_forget_email);
        lv_forget_otp=(LinearLayout)findViewById(R.id.lv_forget_otp);
        lv_re_enter_password=(LinearLayout)findViewById(R.id.lv_re_enter_password);

        ed_forget_pass_email=(EditText) findViewById(R.id.ed_forget_pass_email);
        ed_otp=(EditText) findViewById(R.id.ed_otp);
        ed_new_password=(EditText) findViewById(R.id.ed_new_password);
        ed_re_enter_password=(EditText) findViewById(R.id.ed_re_enter_password);


        bt_send=(Button) findViewById(R.id.bt_send);
        ed_otp_continue=(Button) findViewById(R.id.ed_otp_continue);
        bt_forget_update=(Button) findViewById(R.id.bt_forget_update);



        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailID = ed_forget_pass_email.getText().toString().trim();
                if (emailID.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter email address");
                }else {
                    dialog.show();
                    Call<ForgotPasswordEmailResponse> responseCall = adminAPI.ForgetPass(emailID );
                    responseCall.enqueue(new Callback<ForgotPasswordEmailResponse>() {
                        @Override
                        public void onResponse(Call<ForgotPasswordEmailResponse> call, Response<ForgotPasswordEmailResponse> response) {
                            dialog.dismiss();
                            ForgotPasswordEmailResponse verifyResponse = response.body();
                            if (verifyResponse != null) {
                                if (verifyResponse.isStatus()) {
                                    userID=verifyResponse.getData().getUser_id();
                                    lv_forget_email.setVisibility(View.GONE);
                                    lv_forget_otp.setVisibility(View.VISIBLE);
                                    flag=true;
                                    setupToolbar();

                                } else {
                                    C.INSTANCE.showToast(getApplicationContext(), verifyResponse.getMessage());
                                }
                            } else {
                                C.INSTANCE.defaultError(getApplicationContext());
                            }
                        }

                        @Override
                        public void onFailure(Call<ForgotPasswordEmailResponse> call, Throwable t) {
                            dialog.dismiss();
                            C.INSTANCE.errorToast(getApplicationContext(), t);
                        }
                    });
                }
            }
        });

        ed_otp_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = ed_otp.getText().toString().trim();

                if (otp.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter OTP here");
                }else {

                    dialog.show();
                    Call<ForgotPasswordEmailResponse> responseCall = adminAPI.ForgetPass_otp(userID,otp );
                    responseCall.enqueue(new Callback<ForgotPasswordEmailResponse>() {
                        @Override
                        public void onResponse(Call<ForgotPasswordEmailResponse> call, Response<ForgotPasswordEmailResponse> response) {
                            dialog.dismiss();
                            ForgotPasswordEmailResponse verifyResponse = response.body();
                            if (verifyResponse != null) {
                                if (verifyResponse.isStatus()) {
                                    userID=verifyResponse.getData().getUser_id();
                                    lv_forget_email.setVisibility(View.GONE);
                                    lv_forget_otp.setVisibility(View.GONE);
                                    lv_re_enter_password.setVisibility(View.VISIBLE);
                                    flag=false;
                                    setupToolbar();
                                } else {
                                    C.INSTANCE.showToast(getApplicationContext(), verifyResponse.getMessage());
                                }
                            } else {
                                C.INSTANCE.defaultError(getApplicationContext());
                            }
                        }

                        @Override
                        public void onFailure(Call<ForgotPasswordEmailResponse> call, Throwable t) {
                            dialog.dismiss();
                            C.INSTANCE.errorToast(getApplicationContext(), t);
                        }
                    });
                }

            }
        });

        bt_forget_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String new_password=ed_new_password.getText().toString().trim();
                String re_enter_password=ed_re_enter_password.getText().toString().trim();
                if (new_password.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter your new password");
                }
               else if(re_enter_password.isEmpty()){
                    C.INSTANCE.showToast(getApplicationContext(), "Please re-enter your password");
                }
                else if(!re_enter_password.equals(new_password)){
                    C.INSTANCE.showToast(getApplicationContext(), "Your password does not match");

                }
                else {

                    dialog.show();
                    Call<AppModel> forgetpasswordcall = adminAPI.ForgetPass_change_edit(userID, re_enter_password);

                    forgetpasswordcall.enqueue(new Callback<AppModel>() {
                        @Override
                        public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                            dialog.dismiss();
                            AppModel appModel = response.body();
                            if (appModel != null) {
                                if (appModel.isStatus()) {
                                    C.INSTANCE.showToast(getApplicationContext(), appModel.getMessage());
                                    Intent in3=new Intent(getApplicationContext(),SignInActivity.class);
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

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Log.e("flag","--->"+flag);
        if(flag)
        {
            actionTitle.setText("Verification");
        }
        else {
            actionTitle.setText("");
        }


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        System.gc();
    }
}
