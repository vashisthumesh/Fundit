package com.fundit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fundit.a.AppPreference;
import com.fundit.a.C;

public class AccountTypeActivity extends AppCompatActivity {


    TextView tv_organization, tv_fundspot, tv_General_member;

    AppPreference preferences;

    static AccountTypeActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_type);

        activity = this;
        preferences = new AppPreference(getApplicationContext());

        fetchid();
        setupToolbar();

    }

    public static AccountTypeActivity getInstance() {
        return activity;
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionTitle.setText("Choose an account to create");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void fetchid() {

        tv_organization = (TextView) findViewById(R.id.tv_organization);
        tv_fundspot = (TextView) findViewById(R.id.tv_fundspot);
        tv_General_member = (TextView) findViewById(R.id.tv_General_member);


        tv_organization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountTypeActivity.this, OrganizationAccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("roleID", C.ORGANIZATION);
                startActivity(intent);
            }
        });

        tv_fundspot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountTypeActivity.this, OrganizationAccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("roleID", C.FUNDSPOT);
                startActivity(intent);
            }
        });

        tv_General_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountTypeActivity.this, OrganizationAccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("roleID", C.GENERAL_MEMBER);
                startActivity(intent);
            }
        });

        tv_organization.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (tv_organization.getRight() - tv_organization.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        String message = "";

                        message = "An organization is any group that wants to raise money for their needs or causes. Create this account to manage your organization's fundraisers.";

                        openDialog(message);

                        return true;

                    }
                }

                return false;
            }
        });


        tv_fundspot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (tv_fundspot.getRight() - tv_fundspot.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        String message = "";

                        message = "Any business wanting to contribute a portion of sales to an organization's cause. Advertising and sales will be generated by the organization.";

                        openDialog(message);

                        return true;

                    }
                }

                return false;
            }
        });


        tv_General_member.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (tv_General_member.getRight() - tv_General_member.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        String message = "";
                        message = "Organization members and business employees will use this account to buy, sell or redeem coupons. Anyone can use this account to contribute to fundraisers in their area.";

                        openDialog(message);

                        return true;

                    }
                }

                return false;
            }
        });


    }

    private void openDialog(String message) {

        Log.e("message", message);

        AlertDialog.Builder builder = new AlertDialog.Builder(AccountTypeActivity.this);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_forget_password, null);
        builder.setView(dialogView);
        //  builder.setTitle("Notification");
        final AlertDialog dialogB = builder.create();
        //setting custom layout to dialog


        final TextView Email = (TextView) dialogView.findViewById(R.id.tv_message);
        final TextView title = (TextView) dialogView.findViewById(R.id.title);
        final View view = (View) dialogView.findViewById(R.id.view);
        final Button bt_cancel = (Button) dialogView.findViewById(R.id.bt_cancel);
        bt_cancel.setText("Okay");

        Email.setText(message);
        Email.setTextColor(Color.parseColor("#000000"));

        title.setVisibility(View.GONE);
        view.setVisibility(View.GONE);

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogB.dismiss();
            }
        });

        dialogB.show();
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.gc();
    }
}
