package com.fundit;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.fundit.model.VerifyResponse;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class SignInActivity extends AppCompatActivity {

    EditText ed_input_email, ed_input_password;
    TextView tv_forget_password;
    Button bt_signin, bt_Create_account;

    AppPreference preference;

    CustomDialog dialog;

    String[] perms = {android.Manifest.permission.SYSTEM_ALERT_WINDOW, android.Manifest.permission.WRITE_EXTERNAL_STORAGE , Manifest.permission.CAMERA};


    AdminAPI adminAPI;

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean isPermissionRequestRequired(Activity activity, @NonNull String[] permissions, int requestCode) {
        if (isMarshmallowPlusDevice() && permissions.length > 0) {
            List<String> newPermissionList = new ArrayList<>();
            for (String permission : permissions) {
                if (PERMISSION_GRANTED != activity.checkSelfPermission(permission)) {
                    newPermissionList.add(permission);
                }
            }
            if (newPermissionList.size() > 0) {
                activity.requestPermissions(newPermissionList.toArray(new String[newPermissionList.size()]), requestCode);
                return true;
            }
        }

        return false;
    }

    public static boolean isMarshmallowPlusDevice() {

        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        adminAPI = ServiceGenerator.getAPIClass();
        preference = new AppPreference(this);
        dialog = new CustomDialog(this);


        /*if (preference.isLoggedIn()) {

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
*/
        isPermissionRequestRequired(this, perms, 1);

        fetchid();


        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
    }

    private void fetchid() {

        ed_input_email = (EditText) findViewById(R.id.ed_input_email);
        ed_input_password = (EditText) findViewById(R.id.ed_input_password);
        tv_forget_password = (TextView) findViewById(R.id.tv_forget_password);
        bt_signin = (Button) findViewById(R.id.bt_signin);
        bt_Create_account = (Button) findViewById(R.id.bt_Create_account);

        bt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailID = ed_input_email.getText().toString().trim();
                String password = ed_input_password.getText().toString().trim();

                if (emailID.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter email address");
                } else if (!C.INSTANCE.validEmail(emailID)) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter valid email address");
                } else if (password.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter password");
                } else if (password.length() < 6) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter min. 6 char password");
                } else {
                    String firebase_token = FirebaseInstanceId.getInstance().getToken();
                    Log.e("token " , "" + firebase_token);
                    dialog.show();
                    Call<VerifyResponse> responseCall = adminAPI.signInUser(emailID, password, firebase_token);
                    Log.e("parameters" , "" + emailID +"-->"+ password+"-->" + firebase_token);
                    responseCall.enqueue(new Callback<VerifyResponse>() {
                        @Override
                        public void onResponse(Call<VerifyResponse> call, Response<VerifyResponse> response) {
                            dialog.dismiss();
                            VerifyResponse verifyResponse = response.body();
                            if (verifyResponse != null) {
                                if (verifyResponse.isStatus()) {
                                    String userData = new Gson().toJson(verifyResponse.getData().getUser());
                                    String memberData = "";

                                    Intent in;
                                    in = new Intent(SignInActivity.this, HomeActivity.class);
                                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    switch (verifyResponse.getData().getUser().getRole_id()) {
                                        case C.ORGANIZATION:
                                            if (verifyResponse.getData().getMember().getOrganization() == null || verifyResponse.getData().getMember().getOrganization().getId() == null || verifyResponse.getData().getMember().getOrganization().getId().isEmpty()) {
                                                in = new Intent(getApplicationContext(), OrganizationProfileActivity.class);
                                                in.putExtra("firstTime", true);
                                            } else {
                                                memberData = new Gson().toJson(verifyResponse.getData().getMember().getOrganization());


                                            }
                                            break;
                                        case C.FUNDSPOT:
                                            if (verifyResponse.getData().getMember().getFundspot() == null || verifyResponse.getData().getMember().getFundspot().getId() == null || verifyResponse.getData().getMember().getFundspot().getId().isEmpty()) {
                                                in = new Intent(getApplicationContext(), FundSpotProfile.class);
                                                in.putExtra("firstTime",true);
                                            } else {
                                                memberData = new Gson().toJson(verifyResponse.getData().getMember().getFundspot());
                                            }
                                            break;
                                        case C.GENERAL_MEMBER:
                                            if (verifyResponse.getData().getMember() == null || verifyResponse.getData().getMember().getId() == null || verifyResponse.getData().getMember().getId().isEmpty()) {
                                                in = new Intent(getApplicationContext(), GeneralMemberProfileActivity.class);
                                                in.putExtra("firstTime",true);
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

        tv_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // showdialog();
                Intent in = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });

        bt_Create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SignInActivity.this, AccountTypeActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });
    }

    private void showdialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = SignInActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_forget_password, null);
        builder.setView(dialogView);
        builder.setTitle("Send Email");
        final AlertDialog dialogB = builder.create();
        //setting custom layout to dialog


        final EditText Email = (EditText) dialogView.findViewById(R.id.ed_forget_pass_email);
        final Button bt_cancel = (Button) dialogView.findViewById(R.id.bt_cancel);
        final Button bt_send = (Button) dialogView.findViewById(R.id.bt_send);

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email.setText("");
                dialogB.dismiss();
            }
        });
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                Call<AppModel> forgetpasswordcall = adminAPI.forgetPassword("");

                forgetpasswordcall.enqueue(new Callback<AppModel>() {
                    @Override
                    public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                        dialog.dismiss();
                        AppModel appModel = response.body();
                        if (appModel != null) {
                            if (appModel.isStatus()) {
                                C.INSTANCE.showToast(getApplicationContext(), appModel.getMessage());

                                dialogB.dismiss();

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
        });

        dialogB.show();


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

    @Override
    public void onBackPressed() {
        System.exit(0);
    }
}
