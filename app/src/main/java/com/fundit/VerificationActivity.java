package com.fundit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.VerifyResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationActivity extends AppCompatActivity {


    EditText ed_hint_verification;

    Button bt_verify_continue;

    String userID="";

    AppPreference preference;

    CustomDialog dialog;

    AdminAPI adminAPI;
    static VerificationActivity activity ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        adminAPI= ServiceGenerator.getAPIClass();
        preference=new AppPreference(this);
        dialog=new CustomDialog(this);

        Intent in=getIntent();
        userID=in.getStringExtra("userID");
        Log.e("userId" , userID);
        setupToolbar();



        ed_hint_verification=(EditText)findViewById(R.id.ed_hint_verification);
        bt_verify_continue=(Button)findViewById(R.id.bt_verify_continue);

        bt_verify_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id=userID;
                String otp=ed_hint_verification.getText().toString().trim();


                if(otp.isEmpty() || otp.length()<6){
                        C.INSTANCE.showToast(getApplicationContext() , "Please enter 6 digit otp");
                } else {

                    dialog.show();
                    Call<VerifyResponse> responseCall = adminAPI.userVerification(user_id, otp);
                    Log.e("parameters", "" + user_id + otp);
                    responseCall.enqueue(new Callback<VerifyResponse>() {
                        @Override
                        public void onResponse(Call<VerifyResponse> call, Response<VerifyResponse> response) {
                            dialog.dismiss();
                            VerifyResponse verifyResponse = response.body();
                            if (verifyResponse != null) {
                                if (verifyResponse.isStatus()) {

                                    C.INSTANCE.showToast(getApplicationContext() , verifyResponse.getMessage());

                                    Log.e("status", "" + verifyResponse.isStatus());
                                    String userData = new Gson().toJson(verifyResponse.getData().getUser());
                                    preference.setLoggedIn(true);
                                    preference.setUserID(verifyResponse.getData().getUser().getId());
                                    preference.setUserRoleID(verifyResponse.getData().getUser().getRole_id());
                                    preference.setTokenHash(verifyResponse.getData().getUser().getTokenhash());
                                    preference.setUserData(userData);

                                    if (verifyResponse.getData().getUser().getRole_id().equals(C.ORGANIZATION)) {
                                        Intent in = new Intent(VerificationActivity.this, OrganizationProfileActivity.class);
                                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        in.putExtra("firstTime", true);
                                        startActivity(in);
                                        finish();
                                    } else if (verifyResponse.getData().getUser().getRole_id().equals(C.FUNDSPOT)) {
                                        Intent in = new Intent(VerificationActivity.this, FundSpotProfile.class);
                                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        in.putExtra("firstTime", true);
                                        startActivity(in);
                                        finish();
                                    } else if (verifyResponse.getData().getUser().getRole_id().equals(C.GENERAL_MEMBER)) {
                                        Intent in = new Intent(VerificationActivity.this, GeneralMemberProfileActivity.class);
                                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        in.putExtra("firstTime", true);
                                        startActivity(in);
                                        finish();
                                    }

                                } else {
                                    C.INSTANCE.showToast(getApplicationContext(), verifyResponse.getMessage());
                                }
                            } else {
                                C.INSTANCE.defaultError(getApplicationContext());
                            }
                        }

                        @Override
                        public void onFailure(Call<VerifyResponse> call, Throwable t) {
                            dialog.dismiss();
                            C.INSTANCE.errorToast(getApplicationContext(), t);
                        }
                    });
                }
            }
        });

    }

    public static VerificationActivity getInstance(){
        return activity ;
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionTitle.setText("Verification");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.gc();
    }
}
