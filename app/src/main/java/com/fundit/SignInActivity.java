package com.fundit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.model.RegisterResponse;
import com.fundit.model.VerifyResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    EditText ed_input_email,ed_input_password;
    TextView tv_forget_password;
    Button bt_signin,bt_Create_account;

    AdminAPI adminAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        fetchid();
        adminAPI= ServiceGenerator.getAPIClass();
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
                Call<VerifyResponse> responseCall=adminAPI.signInUser("","","");
                responseCall.enqueue(new Callback<VerifyResponse>() {
                    @Override
                    public void onResponse(Call<VerifyResponse> call, Response<VerifyResponse> response) {
                        VerifyResponse verifyResponse=response.body();
                        if(verifyResponse!=null){
                            if(verifyResponse.isStatus()){
                                verifyResponse.getData().getUser();
                            }
                            else {
                                verifyResponse.getMessage();
                            }
                        }
                        else{

                        }
                    }

                    @Override
                    public void onFailure(Call<VerifyResponse> call, Throwable t) {

                    }
                });
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


}
