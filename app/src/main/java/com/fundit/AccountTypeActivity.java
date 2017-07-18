package com.fundit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fundit.a.C;

public class AccountTypeActivity extends AppCompatActivity {


    TextView tv_organization,tv_fundspot,tv_General_member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_type);

        fetchid();

    }

    private void fetchid() {

        tv_organization=(TextView)findViewById(R.id.tv_organization);
        tv_fundspot=(TextView)findViewById(R.id.tv_fundspot);
        tv_General_member=(TextView)findViewById(R.id.tv_General_member);


        tv_organization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AccountTypeActivity.this,OrganizationAccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("roleID", C.ORGANIZATION);
                startActivity(intent);
            }
        });

        tv_fundspot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AccountTypeActivity.this,OrganizationAccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("roleID", C.FUNDSPOT);
                startActivity(intent);
            }
        });

        tv_General_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AccountTypeActivity.this,OrganizationAccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("roleID", C.GENERAL_MEMBER);
                startActivity(intent);
            }
        });
    }
}
