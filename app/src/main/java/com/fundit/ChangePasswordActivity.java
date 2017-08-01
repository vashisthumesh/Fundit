package com.fundit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fundit.a.C;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText edt_password;
    Button btn_change;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


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


        edt_password = (EditText) findViewById(R.id.edt_password);
        btn_change = (Button) findViewById(R.id.btn_change);


        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getPassword = edt_password.getText().toString().trim();

                if (getPassword.isEmpty()) {


                    C.INSTANCE.showToast(getApplicationContext(), "Please enter password");
                    edt_password.requestFocus();


                }else {








                }


            }
        });


    }
}
