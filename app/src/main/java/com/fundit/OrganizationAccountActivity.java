package com.fundit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_account);

        adminAPI= ServiceGenerator.getAPIClass();

        fetchid();
    }

    private void fetchid() {

        et_organization_name=(EditText)findViewById(R.id.et_organization_name);
        et_organization_email=(EditText)findViewById(R.id.et_organization_email);
        et_password=(EditText)findViewById(R.id.et_password);
        et_confirm_password=(EditText)findViewById(R.id.et_confirm_password);

        et_fname=(EditText)findViewById(R.id.et_fname);
        et_lname=(EditText)findViewById(R.id.et_lname);

        bt_organization_comtinue=(Button)findViewById(R.id.bt_organization_comtinue);


        bt_organization_comtinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName=et_fname.getText().toString();
                String lastName=et_lname.getText().toString();

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
