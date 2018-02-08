package com.fundit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.Fundspot;
import com.fundit.model.Member;
import com.fundit.model.VerifyResponse;
import com.google.gson.Gson;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampaignSetting extends AppCompatActivity {

    AppPreference preference;
    AdminAPI adminAPI;

    String userid = "", title = "", state_id = "", city_id = "", address = "", zipcode = "", category = "", funsplit = "", description = "", contactInfo = "", contactInfoEmail = "", imagePath = "";

    Button btn_continue, btn_skip;

    EditText edt_fubdraiser, edt_organization, edt_duration, edt_price, edt_total_days;
    CheckBox checkbox_indefinite, checkbox_nolimit, checkbox_visible;

    boolean firstTIme = false;
    boolean editMode = false;

    CustomDialog dialog;

    Fundspot member = new Fundspot();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_setting);
        adminAPI = ServiceGenerator.getAPIClass();
        dialog = new CustomDialog(this);
        preference = new AppPreference(getApplicationContext());
        Intent in = getIntent();
        title = in.getStringExtra("title");
        state_id = in.getStringExtra("state_id");
        city_id = in.getStringExtra("city_id");
        address = in.getStringExtra("address1");
        zipcode = in.getStringExtra("zipcode");
        category = in.getStringExtra("category");
        funsplit = in.getStringExtra("funsplit");
        description = in.getStringExtra("description");
        contactInfo = in.getStringExtra("contactInfo");
        contactInfoEmail = in.getStringExtra("contactInfoEmail");
        imagePath = in.getStringExtra("imagePath");
        firstTIme = in.getBooleanExtra("firstTime", false);
        editMode = in.getBooleanExtra("editMode", false);

        try {
            member = new Gson().fromJson(preference.getMemberData(), Fundspot.class);
            Log.e("member", "-->" + preference.getMemberData());
        } catch (Exception e) {
            e.printStackTrace();
        }


        fetchID();
        setupToolbar();
    }

    private void setupToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        actionTitle.setText("Campaign Terms");
        setSupportActionBar(toolbar);

        if (editMode) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);


            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
    }

    private void fetchID() {

        btn_continue = (Button) findViewById(R.id.btn_continue);
        btn_skip = (Button) findViewById(R.id.btn_skip);

        edt_fubdraiser = (EditText) findViewById(R.id.edt_fubdraiser);
        edt_organization = (EditText) findViewById(R.id.edt_organization);
        edt_duration = (EditText) findViewById(R.id.edt_duration);
        edt_price = (EditText) findViewById(R.id.edt_price);
        edt_total_days = (EditText) findViewById(R.id.edt_total_days);

        edt_organization.setEnabled(false);

        checkbox_indefinite = (CheckBox) findViewById(R.id.checkbox_indefinite);
        checkbox_nolimit = (CheckBox) findViewById(R.id.checkbox_nolimit);
        checkbox_visible = (CheckBox) findViewById(R.id.checkbox_visible);
        checkbox_indefinite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isChecked()) {

                    edt_duration.setText("0");
                    edt_duration.setEnabled(false);

                }

                if (!buttonView.isChecked()) {

                    edt_duration.setEnabled(true);

                }


            }
        });

        if (firstTIme) {
            btn_skip.setVisibility(View.VISIBLE);
        }


        if (editMode) {
            String getMaxCoupons = member.getMax_limit_of_coupon_price();
            String getDuration = member.getCampaign_duration();
            edt_fubdraiser.setText(member.getFundspot_percent());
            edt_organization.setText(member.getOrganization_percent());
            btn_skip.setVisibility(View.GONE);
//            if (getMaxCoupons.equalsIgnoreCase("10000") || getMaxCoupons.equalsIgnoreCase("0")) {
//                checkbox_nolimit.setChecked(true);
//                edt_price.setEnabled(false);
//            } else {
//                edt_price.setText(getMaxCoupons);
//                edt_price.setEnabled(true);
//            }
            edt_price.setText(getMaxCoupons);

            if (getDuration.equalsIgnoreCase("10000") || getDuration.equalsIgnoreCase("0")) {
                checkbox_indefinite.setChecked(true);
                edt_duration.setEnabled(true);
            } else {
                edt_duration.setText(getDuration);
                edt_duration.setEnabled(true);
            }


            edt_total_days.setText(member.getCoupon_expire_day());
        }
        checkbox_nolimit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isChecked()) {
                    edt_price.setText("0");
                    edt_price.setEnabled(false);
                }
                if (!buttonView.isChecked()) {
                    edt_price.setEnabled(true);
                    edt_price.setText("0");
                }
            }
        });

        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<VerifyResponse> fundspotResponse = null;
                dialog.show();
                fundspotResponse = adminAPI.editFundsportProfile(preference.getUserID(), preference.getTokenHash(), title, state_id, city_id, address, zipcode, category, funsplit, description, contactInfoEmail, contactInfo, "", "", "", "", "", "0", ServiceGenerator.prepareFilePart("image", imagePath));

                fundspotResponse.enqueue(new Callback<VerifyResponse>() {
                    @Override
                    public void onResponse(Call<VerifyResponse> call, Response<VerifyResponse> response) {
                        dialog.dismiss();
                        VerifyResponse verifyResponse = response.body();
                        if (verifyResponse != null) {
                            if (verifyResponse.isStatus()) {
                                C.INSTANCE.showToast(getApplicationContext(), verifyResponse.getMessage());
                                String memberData = new Gson().toJson(verifyResponse.getData().getMember().getFundspot());
                                preference.setMemberData(memberData);
                                preference.setSkiped(true);

                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finishAffinity();
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
        });


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String fundraiser = edt_fubdraiser.getText().toString().trim();
                final String organization = edt_organization.getText().toString().trim();
                final String campaign_days = edt_duration.getText().toString().trim();
                final String amount = edt_price.getText().toString().trim();
                final String totalDays = edt_total_days.getText().toString().trim();
                String splitVisibility = "0";

                if (checkbox_visible.isChecked()) {
                    splitVisibility = "1";
                }


                if (fundraiser.isEmpty() || fundraiser.equalsIgnoreCase("0")) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter fundspot percentage");
                } else if (organization.isEmpty() || organization.equalsIgnoreCase("0")) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter organization percentage");
                } else if (campaign_days.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter campaign duration");
                } else if (Double.parseDouble(campaign_days) == 0.0) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter valid campaign duration");
                } else if (amount.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter max selling limit");
                } else if (Double.parseDouble(amount) == 0.0) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter valid max selling limit");
                } else if (totalDays.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter coupons expiration days");
                } else if (Double.parseDouble(totalDays) == 0.0) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter valid coupons expiration days");
                } else {
                    Call<VerifyResponse> fundspotResponse = null;
                    dialog.show();


                    if (firstTIme) {
                        fundspotResponse = adminAPI.editFundsportProfile(preference.getUserID(), preference.getTokenHash(), title, state_id, city_id, address, zipcode, category, funsplit, description, contactInfoEmail, contactInfo, fundraiser, organization, campaign_days, amount, totalDays, splitVisibility, ServiceGenerator.prepareFilePart("image", imagePath));
                    } else if (editMode) {

                        fundspotResponse = adminAPI.onlyCampaignEdit(preference.getUserID(), preference.getTokenHash(), fundraiser, organization, campaign_days, amount, totalDays);

                    }


                    fundspotResponse.enqueue(new Callback<VerifyResponse>() {
                        @Override
                        public void onResponse(Call<VerifyResponse> call, Response<VerifyResponse> response) {
                            dialog.dismiss();

                            Log.e("parameters", "" + preference.getUserID() + "" + preference.getTokenHash() + "" + title + "" + state_id + "" + city_id + "" + address + "" + zipcode + "" + category + "" + funsplit + "" + description + "" + contactInfo + "" + fundraiser + "" + organization + "" + campaign_days + "" + amount + "" + totalDays + "" + imagePath);


                            VerifyResponse verifyResponse = response.body();
                            if (verifyResponse != null) {
                                if (verifyResponse.isStatus()) {
                                    C.INSTANCE.showToast(getApplicationContext(), verifyResponse.getMessage());
                                    String memberData = new Gson().toJson(verifyResponse.getData().getMember().getFundspot());
                                    preference.setMemberData(memberData);
                                    if (firstTIme) {
                                        preference.setSkiped(true);
                                    }

                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finishAffinity();
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

        edt_fubdraiser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String getFundraiser = edt_fubdraiser.getText().toString().trim();

                if (getFundraiser.isEmpty()) {
                    edt_organization.setText("100");
                } else {

                    if (getFundraiser.contains(".")) {

                        float fund = Float.parseFloat(getFundraiser);

                        if (fund > 100) {

                            edt_fubdraiser.setText("99");
                            fund = 99;

                        }

                        float organizationPercent = 100 - fund;
                        edt_organization.setText(String.format(Locale.getDefault(), "%.2f", organizationPercent));
                    } else {

                        int funSplit = Integer.parseInt(getFundraiser);

                        if (funSplit > 100) {
                            edt_fubdraiser.setText("99");
                            funSplit = 99;
                        }

                        int organizationSplit = 100 - funSplit;
                        edt_organization.setText(String.valueOf(organizationSplit));


                    }


                }


            }
        });


    }

    @Override
    public void onBackPressed() {
        if (firstTIme) {
            System.exit(0);
        } else {
            finish();
        }
    }
}
