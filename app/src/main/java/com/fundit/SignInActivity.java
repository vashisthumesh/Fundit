package com.fundit;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.AppModel;
import com.fundit.model.VerifyResponse;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    EditText ed_input_email,ed_input_password;
    TextView tv_forget_password;
    Button bt_signin,bt_Create_account;

    AppPreference preference;

    CustomDialog dialog;

    AdminAPI adminAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        adminAPI= ServiceGenerator.getAPIClass();
        preference=new AppPreference(this);
        dialog=new CustomDialog(this);

        if (preference.isLoggedIn()) {

            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            if (preference.getMemberData().isEmpty()) {
                switch (preference.getUserRoleID()) {
                    case C.ORGANIZATION:
                        intent = new Intent(this, OrganizationProfileActivity.class);
                        break;
                    case C.FUNDSPOT:
                        intent = new Intent(this, FundSpotProfile.class);
                        break;
                    case C.GENERAL_MEMBER:
                        intent = new Intent(this, GeneralMemberProfileActivity.class);
                        break;
                }
                intent.putExtra("firstTime", true);
            }


            startActivity(intent);
            finish();
        }

        fetchid();

    }

    private void fetchid() {

        ed_input_email=(EditText)findViewById(R.id.ed_input_email);
        ed_input_password=(EditText)findViewById(R.id.ed_input_password);
        tv_forget_password=(TextView)findViewById(R.id.tv_forget_password);
        bt_signin=(Button)findViewById(R.id.bt_signin);
        bt_Create_account=(Button)findViewById(R.id.bt_Create_account);

        bt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_input_email.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(SignInActivity.this, "Please Enter Email Address", Toast.LENGTH_LONG).show();
                    // GlobalFile.CustomToast(Activity_Login.this,"Please Enter Email Address",getLayoutInflater());

                } else if (validateEmail1(ed_input_email.getText().toString()) != true) {
                    Toast.makeText(SignInActivity.this, "Please enter valid email address", Toast.LENGTH_LONG).show();
                } else if (ed_input_password.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(SignInActivity.this, "Please Enter Your Password", Toast.LENGTH_LONG).show();
                } else {
                    String email=ed_input_email.getText().toString().trim();
                    String password=ed_input_password.getText().toString().trim();
                    String firebase_token= FirebaseInstanceId.getInstance().getToken();
                    dialog.show();
                    Call<VerifyResponse> responseCall=adminAPI.signInUser(email,password,firebase_token);
                    responseCall.enqueue(new Callback<VerifyResponse>() {
                        @Override
                        public void onResponse(Call<VerifyResponse> call, Response<VerifyResponse> response) {
                            dialog.dismiss();
                            VerifyResponse verifyResponse=response.body();
                            if(verifyResponse!=null){
                                if(verifyResponse.isStatus()){
                                    String userData= new Gson().toJson(verifyResponse.getData().getUser());
                                    String memberData = "";
                                    Intent in;
                                    in = new Intent(SignInActivity.this, HomeActivity.class);
                                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    switch (verifyResponse.getData().getUser().getRole_id()) {
                                        case C.ORGANIZATION:
                                            if (verifyResponse.getData().getOrganization().getId().isEmpty()) {
                                                in = new Intent(getApplicationContext(), OrganizationProfileActivity.class);
                                                in.putExtra("firstTime", true);
                                            } else {
                                                memberData = new Gson().toJson(verifyResponse.getData().getOrganization());
                                            }

                                            break;
                                        case C.FUNDSPOT:
                                            if (verifyResponse.getData().getFundspot().getId().isEmpty()) {
                                                in = new Intent(getApplicationContext(), FundSpotProfile.class);
                                                in.putExtra("firstTime", true);
                                            } else {
                                                memberData = new Gson().toJson(verifyResponse.getData().getFundspot());
                                            }

                                            break;
                                        case C.GENERAL_MEMBER:
                                            if (verifyResponse.getData().getFundspot().getId().isEmpty()) {
                                                in = new Intent(getApplicationContext(), GeneralMemberProfileActivity.class);
                                                in.putExtra("firstTime", true);
                                            } else {
                                                memberData = new Gson().toJson(verifyResponse.getData().getMember());
                                            }

                                            break;
                                    }

                                    preference.setLoggedIn(true);
                                    preference.setUserID(verifyResponse.getData().getUser().getId());
                                    preference.setUserRoleID(verifyResponse.getData().getUser().getRole_id());
                                    preference.setTokenHash(verifyResponse.getData().getUser().getTokenhash());
                                    preference.setUserData(userData);
                                    preference.setMemberData(memberData);
                                    startActivity(in);

                                }
                                else {
                                    C.INSTANCE.showToast(getApplicationContext(),verifyResponse.getMessage());
                                }
                            }
                            else{
                                C.INSTANCE.defaultError(getApplicationContext());
                            }
                        }

                        @Override
                        public void onFailure(Call<VerifyResponse> call, Throwable t) {
                            dialog.dismiss();
                            C.INSTANCE.errorToast(getApplicationContext(),t);
                        }
                    });

                }
            }
        });

        tv_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            showdialog();
            }
        });

        bt_Create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(SignInActivity.this,AccountTypeActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });
    }

    private void showdialog() {

        final Dialog dialog_forget = new Dialog(SignInActivity.this);
        LayoutInflater inflater = SignInActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_forget_password, null);
        //setting custom layout to dialog
        dialog_forget.setContentView(dialogView);
        dialog_forget.setTitle("Send Email");

        final EditText Email =(EditText)dialogView.findViewById(R.id.ed_forget_pass_email);
        final Button bt_cancel=(Button)dialogView.findViewById(R.id.bt_cancel);
        final Button bt_send=(Button)dialogView.findViewById(R.id.bt_send);

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email.setText("");
                dialog_forget.cancel();
            }
        });
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                Call<AppModel> forgetpasswordcall=adminAPI.forgetPassword("");

                forgetpasswordcall.enqueue(new Callback<AppModel>() {
                    @Override
                    public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                        dialog.dismiss();
                        AppModel  appModel=response.body();
                        if(appModel!=null){
                            if(appModel.isStatus()){
                                C.INSTANCE.showToast(getApplicationContext(),appModel.getMessage());

                                dialog_forget.dismiss();

                            }
                            else {
                                C.INSTANCE.showToast(getApplicationContext(),appModel.getMessage());
                            }
                        }
                        else{
                            C.INSTANCE.defaultError(getApplicationContext());
                        }
                    }

                    @Override
                    public void onFailure(Call<AppModel> call, Throwable t) {
                        dialog.dismiss();
                        C.INSTANCE.errorToast(getApplicationContext(),t);
                    }
                });


            }
        });

        dialog_forget.show();


    }

    private boolean validateEmail1(String email) {
        // TODO Auto-generated method stub

        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();

    }
}
