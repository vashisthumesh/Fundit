package com.fundit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fundit.a.C;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.model.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizationAccountActivity extends AppCompatActivity {


    EditText et_organization_name,et_organization_email,et_password,et_confirm_password,et_fname,et_lname;
    Button bt_organization_comtinue;

    String roleID="";

    AdminAPI adminAPI;
    boolean isRegisteredEmail=false, checkedForUniqueEmail=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_account);

        adminAPI= ServiceGenerator.getAPIClass();

        Intent intent=getIntent();
        roleID=intent.getStringExtra("roleID");

        fetchid();
    }

    private void fetchid() {

        et_organization_name=(EditText)findViewById(R.id.et_organization_name);
        et_organization_email=(EditText)findViewById(R.id.et_organization_email);
        et_password=(EditText)findViewById(R.id.et_password);
        et_confirm_password=(EditText)findViewById(R.id.et_confirm_password);

        et_fname=(EditText)findViewById(R.id.et_fname);
        et_lname=(EditText)findViewById(R.id.et_lname);

        if(roleID.equals(C.GENERAL_MEMBER)){
            et_fname.setVisibility(View.VISIBLE);
            et_lname.setVisibility(View.VISIBLE);
            et_organization_name.setVisibility(View.GONE);
        }

        bt_organization_comtinue=(Button)findViewById(R.id.bt_organization_comtinue);

        bt_organization_comtinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName=et_fname.getText().toString().trim();
                String lastName=et_lname.getText().toString().trim();
                String title = et_organization_name.getText().toString().trim();
                String email = et_organization_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();
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
                    C.INSTANCE.showToast(getApplicationContext(),"Please enter organization title");
                    return;
                }

                if(email.isEmpty()){
                    C.INSTANCE.showToast(getApplicationContext(),"Please enter valid email id");
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



                Call<RegisterResponse> responseCall=adminAPI.registerUser("","","","","","");
                responseCall.enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        RegisterResponse registerResponse=response.body();
                        if(registerResponse!=null){
                            if(registerResponse.isStatus()){
                                registerResponse.getData().getUser_id();
                            }
                            else {
                                registerResponse.getMessage();
                            }
                        }
                        else{

                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    }
                });

            }
        });

    }
}
