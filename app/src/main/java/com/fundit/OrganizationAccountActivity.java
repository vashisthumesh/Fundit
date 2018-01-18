package com.fundit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.fundit.model.RegisterResponse;
import com.fundit.model.UniqueEmailResponse;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizationAccountActivity extends AppCompatActivity {


    EditText et_organization_name,et_organization_email,et_password,et_confirm_password,et_fname,et_lname;
    Button bt_organization_comtinue;
    LinearLayout layout_title, layout_names;

    String roleID="";

    AdminAPI adminAPI;
    boolean isRegisteredEmail=false, checkedForUniqueEmail=false;
    CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_account);

        adminAPI= ServiceGenerator.getAPIClass();
        dialog=new CustomDialog(this);
        Intent intent=getIntent();
        roleID=intent.getStringExtra("roleID");

        fetchid();
        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (roleID.equals(C.ORGANIZATION)) {
            actionTitle.setText("Organization Account");
        } else if (roleID.equals(C.FUNDSPOT)) {
            actionTitle.setText("Fundspot Account");
        } else {
            actionTitle.setText("General Member Account");
        }


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void fetchid() {

        et_organization_name=(EditText)findViewById(R.id.et_organization_name);
        et_organization_email=(EditText)findViewById(R.id.et_organization_email);
        et_password=(EditText)findViewById(R.id.et_password);
        et_confirm_password=(EditText)findViewById(R.id.et_confirm_password);

        if (roleID.equals(C.FUNDSPOT)) {
            et_organization_name.setHint("Fundspot Title");
        }

        layout_names=(LinearLayout) findViewById(R.id.layout_names);
        layout_title=(LinearLayout) findViewById(R.id.layout_title);

        et_organization_email.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    String email=et_organization_email.getText().toString().trim();
                    if(!email.isEmpty() && C.INSTANCE.validEmail(email)){
                        dialog.setMessage("Checking email...");
                        dialog.show();
                        Call<UniqueEmailResponse> uniqueMailCall=adminAPI.checkUniqueMail(email);
                        uniqueMailCall.enqueue(new Callback<UniqueEmailResponse>() {
                            @Override
                            public void onResponse(Call<UniqueEmailResponse> call, Response<UniqueEmailResponse> response) {
                                dialog.resetMessage();
                                dialog.dismiss();
                                UniqueEmailResponse emailResponse=response.body();
                                if(emailResponse!=null){


                                    if(emailResponse.isStatus()){
                                        checkedForUniqueEmail=true;
                                        isRegisteredEmail=false;
                                        C.INSTANCE.showToast(getApplicationContext(), emailResponse.getMessage());
                                    }
                                    else {
                                        if(emailResponse.getData().getIs_verified().equals(C.VERIFIED)){
                                            showSignInDialog(emailResponse.getMessage());
                                        }
                                        else {
                                            showNotVerifiedDialog(emailResponse.getData().getUser_id(),emailResponse.getMessage(),"");
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<UniqueEmailResponse> call, Throwable t) {
                                dialog.resetMessage();
                                dialog.dismiss();
                            }
                        });
                    }
                    else {
                        checkedForUniqueEmail=false;
                    }
                }
            }
        });

        et_fname=(EditText)findViewById(R.id.et_fname);
        et_lname=(EditText)findViewById(R.id.et_lname);

        if(roleID.equals(C.GENERAL_MEMBER)){
            layout_names.setVisibility(View.VISIBLE);
            layout_title.setVisibility(View.GONE);
        }

        bt_organization_comtinue=(Button)findViewById(R.id.bt_organization_comtinue);

        bt_organization_comtinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstName=et_fname.getText().toString().trim();
                final String lastName=et_lname.getText().toString().trim();
                final String title = et_organization_name.getText().toString().trim();
                final String email = et_organization_email.getText().toString().trim();
                final String password = et_password.getText().toString().trim();
                String confirmPassowrd= et_confirm_password.getText().toString().trim();

                if(roleID.equals(C.GENERAL_MEMBER) && firstName.isEmpty()){
                    C.INSTANCE.showToast(getApplicationContext(),"Please enter first name");
                    return;
                }

                if(roleID.equals(C.GENERAL_MEMBER) && lastName.isEmpty()){
                    C.INSTANCE.showToast(getApplicationContext(),"Please enter last name");
                    return;
                }

                if(!roleID.equals(C.GENERAL_MEMBER) && title.isEmpty()){
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter title");
                    return;
                }

                if (!roleID.equals(C.GENERAL_MEMBER) && title.length() < 2) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter title more than 1 char");
                    return;
                }

                if(email.isEmpty()){
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter email id");
                    return;
                }

                if(!email.isEmpty() && !C.INSTANCE.validEmail(email)){
                    C.INSTANCE.showToast(getApplicationContext(),"Please enter valid email id");
                    return;
                }

                if(checkedForUniqueEmail && isRegisteredEmail){
                    C.INSTANCE.showToast(getApplicationContext(),"Please enter unique email id");
                    return;
                }

                if(password.isEmpty() || password.length()<6){
                    C.INSTANCE.showToast(getApplicationContext(),"Please enter password min. 6 chars");
                    return;
                }

                if(confirmPassowrd.isEmpty()){
                    C.INSTANCE.showToast(getApplicationContext(),"Please enter confirm password");
                    return;
                }

                if(!confirmPassowrd.equals(password)){
                    C.INSTANCE.showToast(getApplicationContext(),"Confirm password does not match with password");
                    return;
                }

                if(!checkedForUniqueEmail){
                    dialog.setMessage("Checking email...");
                    dialog.show();
                    Call<UniqueEmailResponse> uniqueMailCall=adminAPI.checkUniqueMail(email);
                    uniqueMailCall.enqueue(new Callback<UniqueEmailResponse>() {
                        @Override
                        public void onResponse(Call<UniqueEmailResponse> call, Response<UniqueEmailResponse> response) {
                            dialog.resetMessage();
                            dialog.dismiss();
                            UniqueEmailResponse emailResponse=response.body();
                            if(emailResponse!=null){
                                if(emailResponse.isStatus()){
                                    sendRegistrationData(title,firstName,lastName,email,password);
                                }
                                else {
                                    if(emailResponse.getData().getIs_verified().equals(C.VERIFIED)){
                                        showSignInDialog(emailResponse.getMessage());
                                    }
                                    else {
                                        showNotVerifiedDialog(emailResponse.getData().getUser_id(),emailResponse.getMessage(),"");
                                    }
                                }
                            }
                            else {
                                C.INSTANCE.defaultError(getApplicationContext());
                            }
                        }

                        @Override
                        public void onFailure(Call<UniqueEmailResponse> call, Throwable t) {
                            dialog.resetMessage();
                            dialog.dismiss();
                            C.INSTANCE.errorToast(getApplicationContext(),t);
                        }
                    });
                    return;
                }

                if (checkedForUniqueEmail) {
                    sendRegistrationData(title, firstName, lastName, email, password);
                }

            }
        });

    }

    private void showNotVerifiedDialog(final String userID, String message,String title) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title.isEmpty() ? "Account verification Pending!" : title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent=new Intent(getApplicationContext(),VerificationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("userID",userID);
                Log.e("userID" , userID);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog bDialog=builder.create();
        bDialog.show();
    }

    private void showSignInDialog(String message) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Email already Registered!");
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent=new Intent(getApplicationContext(),SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog bDialog=builder.create();
        bDialog.show();
    }

    void sendRegistrationData(String title, String firstName, String lastName, String emailID, String password){
        if (roleID.equals(C.GENERAL_MEMBER)) {
            title = firstName + " " + lastName;
        }

        dialog.show();
        Call<RegisterResponse> responseCall=adminAPI.registerUser(roleID,title,firstName,lastName,emailID,password, FirebaseInstanceId.getInstance().getToken());
        responseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                dialog.dismiss();
                RegisterResponse registerResponse=response.body();
                if(registerResponse!=null){
                    if(registerResponse.isStatus()){
                        showNotVerifiedDialog(registerResponse.getData().getUser_id(),registerResponse.getMessage(),"Verify your Email");
                    }
                    else {
                        registerResponse.getMessage();
                    }
                }
                else{
                    C.INSTANCE.defaultError(getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                dialog.dismiss();
                C.INSTANCE.errorToast(getApplicationContext(),t);
            }
        });
    }
}
