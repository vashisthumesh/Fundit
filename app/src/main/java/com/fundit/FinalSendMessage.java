package com.fundit;

import android.content.Intent;
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
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.AppModel;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinalSendMessage extends AppCompatActivity {


    AppPreference preference;
    CustomDialog dialog;
    AdminAPI adminAPI;

    TextView txtSenderName;
    EditText edtSubject , edtMessage;
    Button btnSend;


    String name = "";
    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_send_message);


        preference = new AppPreference(getApplicationContext());
        dialog = new CustomDialog(this);
        dialog.setCancelable(false);
        adminAPI = ServiceGenerator.getAPIClass();

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        id = intent.getStringExtra("id");


        setupToolBar();
        fetchIds();

    }


    private void setupToolBar() {


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionTitle.setText("New Message");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }


    private void fetchIds() {

        txtSenderName = (TextView) findViewById(R.id.txt_senderName);

        txtSenderName.setText(name);

        edtSubject = (EditText) findViewById(R.id.edt_subject);
        edtMessage = (EditText) findViewById(R.id.edt_message);
        btnSend = (Button) findViewById(R.id.btn_send);



        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String subject = edtSubject.getText().toString().trim();
                String message = edtMessage.getText().toString().trim();

                dialog.show();
                Call<AppModel> sendFinalMessage = adminAPI.SendMessage(preference.getUserID() , preference.getTokenHash() , id , subject , message);
                sendFinalMessage.enqueue(new Callback<AppModel>() {
                    @Override
                    public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                        dialog.dismiss();
                        AppModel model = response.body();

                        Log.e("response" , "" + new Gson().toJson(model));
                        if(model!=null){
                            if(model.isStatus()){
                                C.INSTANCE.showToast(getApplicationContext() , model.getMessage());
                                Intent intent = new Intent(getApplicationContext() , HomeActivity.class);
                                startActivity(intent);
                            }else {
                                C.INSTANCE.showToast(getApplicationContext() , model.getMessage());
                            }
                        }else {
                            C.INSTANCE.defaultError(getApplicationContext());
                        }
                    }
                    @Override
                    public void onFailure(Call<AppModel> call, Throwable t) {
                        dialog.dismiss();
                        C.INSTANCE.errorToast(getApplicationContext() , t);
                    }
                });
            }
        });


    }
}
