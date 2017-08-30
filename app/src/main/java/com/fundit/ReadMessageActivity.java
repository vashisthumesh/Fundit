package com.fundit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.fundit.a.AppPreference;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.InboxMessagesResponse;

import java.io.Serializable;

public class ReadMessageActivity extends AppCompatActivity {

    AppPreference preference;
    AdminAPI adminAPI;
    CustomDialog dialog;

    TextView txtSenderName , txtSubject , txtMessage , txtDate;

    String senderName = "" , subject = "" , message = "" , date = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_message);

        preference = new AppPreference(getApplicationContext());
        adminAPI = ServiceGenerator.getAPIClass();
        dialog = new CustomDialog(this);

        Intent intent = getIntent();
        senderName = intent.getStringExtra("senderName");
        subject = intent.getStringExtra("messages");
        message = intent.getStringExtra("subject");
        date = intent.getStringExtra("date");


        setupToolBar();
        fetchIds();
    }

    private void setupToolBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionTitle.setText("Messages");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void fetchIds() {


        txtSenderName = (TextView) findViewById(R.id.senderName);
        txtSubject = (TextView) findViewById(R.id.subject);
        txtMessage = (TextView) findViewById(R.id.message);
        txtDate = (TextView) findViewById(R.id.date);


        txtSenderName.setText(senderName);
        txtSubject.setText(subject);
        txtMessage.setText(message);
        txtDate.setText(date);




    }
}
