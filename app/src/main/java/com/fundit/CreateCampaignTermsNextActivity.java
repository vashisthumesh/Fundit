package com.fundit;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.adapter.MemberListAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.AdjustableListView;
import com.fundit.helper.CustomDialog;
import com.fundit.model.AppModel;
import com.fundit.model.CampaignListResponse;
import com.fundit.model.Member;
import com.fundit.model.MemberListResponse;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCampaignTermsNextActivity extends AppCompatActivity {

    EditText edt_campaignName, edt_description, edt_startDate, edt_message, edt_amount, edt_msg_fundspot;
    CheckBox chk_startDateAsPossible, chk_allOrgMembers, chk_max_amount;
    TextView txt_couponSellerLabel, txt_sendmessage;
    AutoCompleteTextView auto_searchMember;
    ListView listMembers;
    Button btn_request;
    TextView txt_dollar;

    String dateSelected = null;

    List<Member> memberList = new ArrayList<>();
    ArrayList<String> memberNames = new ArrayList<>();
    ArrayAdapter<String> memberArrayAdapter;
    MemberListAdapter memberListAdapter;

    AdminAPI adminAPI;
    AppPreference preference;
    CustomDialog dialog;


    TextView txt_members, txt_targetAmt, txt_message;
    CampaignListResponse.CampaignList campaignList;
    String createdName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_campaign_next);

        adminAPI = ServiceGenerator.getAPIClass();
        preference = new AppPreference(this);
        dialog = new CustomDialog(this);

        Intent intent = getIntent();
        campaignList = (CampaignListResponse.CampaignList) intent.getSerializableExtra("campaignList");

        Log.e("listResponse", "-->" + new Gson().toJson(campaignList));

        fetchIDs();
        setupToolbar();

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionTitle.setText("Campaign Request Term");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void fetchIDs() {
        edt_campaignName = (EditText) findViewById(R.id.edt_campaignName);
        edt_description = (EditText) findViewById(R.id.edt_description);
        edt_startDate = (EditText) findViewById(R.id.edt_startDate);
        edt_message = (EditText) findViewById(R.id.edt_message);
        edt_amount = (EditText) findViewById(R.id.edt_amount);
        txt_dollar = (TextView) findViewById(R.id.txt_dollar);
        edt_msg_fundspot = (EditText) findViewById(R.id.edt_msg_fundspot);
        //  edt_amount.setVisibility(View.GONE);
        //  txt_dollar.setVisibility(View.GONE);
        edt_msg_fundspot.setVisibility(View.GONE);

        txt_members = (TextView) findViewById(R.id.txt_members);
        txt_targetAmt = (TextView) findViewById(R.id.txt_targetAmt);
        txt_message = (TextView) findViewById(R.id.txt_message);
        txt_sendmessage = (TextView) findViewById(R.id.txt_sendmessage);
        // txt_targetAmt.setVisibility(View.GONE);
        txt_message.setVisibility(View.GONE);

        chk_max_amount = (CheckBox) findViewById(R.id.chk_max_amount);
        // chk_max_amount.setVisibility(View.GONE);

        if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {

            txt_members.setText("Members to redeemer");
            txt_members.setVisibility(View.GONE);
            edt_message.setVisibility(View.GONE);
            txt_sendmessage.setVisibility(View.VISIBLE);
            edt_amount.setText(campaignList.getCampaign().getMax_limit_of_coupons());
            chk_max_amount.setEnabled(false);
            if (campaignList.getCampaign().getReview_status().equalsIgnoreCase("1")) {
                if (campaignList.getCampaign().getAction_status().equalsIgnoreCase("0")) {
                    createdName = campaignList.getUserOrganization().getOrganization().getTitle();
                } else {
                    createdName = campaignList.getUserFundspot().getOrganization().getTitle();
                }
            } else {
                if (campaignList.getCampaign().getAction_status().equalsIgnoreCase("1")) {
                    createdName = campaignList.getUserFundspot().getOrganization().getTitle();
                } else {
                    createdName = campaignList.getUserOrganization().getOrganization().getTitle();
                }
            }
            txt_sendmessage.setText(createdName + " will confirm your response before launching this campaign. ");

            edt_amount.setEnabled(false);
            edt_campaignName.setEnabled(false);
            edt_description.setEnabled(false);


        }
        if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
            txt_members.setText("Members to sellers");
            txt_sendmessage.setVisibility(View.VISIBLE);

            if (campaignList.getCampaign().getReview_status().equalsIgnoreCase("1")) {
                if (campaignList.getCampaign().getAction_status().equalsIgnoreCase("0")) {
                    createdName = campaignList.getUserOrganization().getFundspot().getTitle();
                } else {
                    createdName = campaignList.getUserFundspot().getFundspot().getTitle();
                }
            } else {
                if (campaignList.getCampaign().getAction_status().equalsIgnoreCase("1")) {
                    createdName = campaignList.getUserFundspot().getFundspot().getTitle();
                } else {
                    createdName = campaignList.getUserOrganization().getFundspot().getTitle();
                }
            }

            txt_sendmessage.setText(createdName + " will confirm your response before launching this campaign. ");


            edt_amount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String amount = edt_amount.getText().toString().trim();

                    String maxLimitCoupon_final = campaignList.getCampaign().getMax_limit_of_coupons().replace("$", "");
                    Log.e("amount", amount);
                    if (!amount.trim().isEmpty()) {
                        if (Double.parseDouble(amount.trim()) > Double.parseDouble(maxLimitCoupon_final.trim())) {
                            edt_amount.setText(maxLimitCoupon_final);
                        }
                        if (Double.parseDouble(amount.trim()) == 0) {
                            edt_amount.setText("1");
                        }
                    }

                }
            });

            chk_max_amount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if (b) {

                        edt_amount.setText(campaignList.getCampaign().getMax_limit_of_coupons());
                        edt_amount.setEnabled(false);
                    } else {
                        edt_amount.setText(campaignList.getCampaign().getMax_limit_of_coupons());
                        edt_amount.setEnabled(true);
                    }


                }
            });

        }

        //edt_campaignName.setEnabled(false);
        // edt_description.setEnabled(false);

        txt_couponSellerLabel = (TextView) findViewById(R.id.txt_couponSellerLabel);
        txt_couponSellerLabel.setText("Coupon redeemers");


        chk_startDateAsPossible = (CheckBox) findViewById(R.id.chk_startDateAsPossible);
        chk_startDateAsPossible.setVisibility(View.GONE);
        chk_allOrgMembers = (CheckBox) findViewById(R.id.chk_allOrgMembers);

        if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {

            chk_allOrgMembers.setText("All fundspot members");

        }
        if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {


            chk_allOrgMembers.setText("All organization members");

        }


        auto_searchMember = (AutoCompleteTextView) findViewById(R.id.auto_searchMember);
        memberArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_textview, memberNames);
        auto_searchMember.setAdapter(memberArrayAdapter);

        btn_request = (Button) findViewById(R.id.btn_request);
        btn_request.setText("Send Response");

        listMembers = (AdjustableListView) findViewById(R.id.listMembers);
        memberListAdapter = new MemberListAdapter(this);
        listMembers.setAdapter(memberListAdapter);

        auto_searchMember.setThreshold(1);

        auto_searchMember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text = auto_searchMember.getText().toString().trim();
                addSelectedMember(i, text);
            }
        });

        edt_startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectStartDate();
            }
        });

        edt_campaignName.setText(campaignList.getCampaign().getTitle());
        edt_description.setText(campaignList.getCampaign().getDescription());

        if (campaignList.getCampaign().getStart_date() != null && !campaignList.getCampaign().getStart_date().isEmpty()) {
            edt_startDate.setText(C.INSTANCE.convertDate("yyyy-MM-dd", "MM/dd/yy", campaignList.getCampaign().getStart_date()));
            dateSelected = campaignList.getCampaign().getStart_date();
        }


        Call<MemberListResponse> memberListResponseCall = null;
        dialog.show();

        if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {

            memberListResponseCall = adminAPI.getAllMemberList(preference.getUserID(), preference.getTokenHash(), preference.getUserRoleID(), preference.getUserID(), null);
        }

        if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {

            memberListResponseCall = adminAPI.getAllMemberList(preference.getUserID(), preference.getTokenHash(), preference.getUserRoleID(), null, preference.getUserID());

        }

        /* memberListResponseCall = adminAPI.getAllMemberList(preference.getUserID(), preference.getTokenHash(), preference.getUserRoleID(), null, preference.getUserID());*/
        memberListResponseCall.enqueue(new Callback<MemberListResponse>() {
            @Override
            public void onResponse(Call<MemberListResponse> call, Response<MemberListResponse> response) {
                dialog.dismiss();
                MemberListResponse memberListResponse = response.body();
                if (memberListResponse != null) {
                    if (memberListResponse.isStatus()) {
                        memberList.addAll(memberListResponse.getData());
                        memberNames.addAll(memberListResponse.getNames());
                    }
                } else {
                    C.INSTANCE.defaultError(getApplicationContext());
                }

                Log.e("Size", "--" + memberNames.size());
                memberArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_textview, memberNames);
                auto_searchMember.setAdapter(memberArrayAdapter);
            }

            @Override
            public void onFailure(Call<MemberListResponse> call, Throwable t) {
                dialog.dismiss();
                C.INSTANCE.errorToast(getApplicationContext(), t);
            }
        });

        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Member> checkedmemberList = memberListAdapter.getMemberList();
                String message = edt_message.getText().toString().trim();

                String campaignTitle = edt_campaignName.getText().toString().trim();
                String description = edt_description.getText().toString().trim();
                String maxAmount1 = edt_amount.getText().toString().trim();
                String coupon_expire_day = "";
                String receiver_id = "";
                String fundspot_percent = "";
                String organization_percent = "";
                String campaign_duration = "";
                String max_limit_of_coupons = "";

                int amount = 0;
                if (maxAmount1.isEmpty() && edt_amount.getVisibility() == View.VISIBLE) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter amount");
                } else if (campaignTitle.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter campaign title");
                } else if (description.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter description");
                } else if (edt_message.getVisibility() == View.VISIBLE && message.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter message");
                }


                /*if(dateSelected==null){
                    C.INSTANCE.showToast(getApplicationContext(), "Please select start date ");

                }*/
//                if (!chk_allOrgMembers.isChecked() && checkedmemberList.size() == 0) {
//                    C.INSTANCE.showToast(getApplicationContext(), "Please select fundspot member ");
//
//                }
                //              else
//                   if (message.isEmpty()) {
//                   C.INSTANCE.showToast(getApplicationContext(), "Please enter message to sellers");
//             }
                else {
                    JSONArray campaignDetailArray = new JSONArray();
                    JSONObject detailObject = new JSONObject();
                    JSONArray memberIDArray = new JSONArray();

                    try {

                        detailObject.put("start_date", "");
                        detailObject.put("all_member", "1");
                        detailObject.put("msg_seller", message);
                        detailObject.put("coupon_expire_day", campaignList.getCampaign().getCoupon_expire_day());
                        detailObject.put("receiver_id", preference.getUserID());
                        detailObject.put("description", description);
                        detailObject.put("title", campaignTitle);
                        detailObject.put("user_id", campaignList.getUserOrganization().getId());
                        detailObject.put("fundspot_percent", campaignList.getCampaign().getFundspot_percent());
                        detailObject.put("organization_percent", campaignList.getCampaign().getOrganization_percent());
                        detailObject.put("campaign_duration", campaignList.getCampaign().getCampaign_duration());
                        detailObject.put("max_limit_of_coupons", campaignList.getCampaign().getMax_limit_of_coupons());


                        campaignDetailArray.put(detailObject);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.show();
                    Call<AppModel> addCampCall = adminAPI.appEditcampaign(preference.getUserID(), preference.getTokenHash(), campaignList.getCampaign().getId(), campaignDetailArray.toString(), memberIDArray.toString());


                    Log.e("parametersReviewCamp", "-->" + preference.getUserID() + "-->" + preference.getTokenHash() + "--->" + campaignList.getCampaign().getId() + "-->" + campaignDetailArray.toString() + "--->" + memberIDArray.toString());

                    addCampCall.enqueue(new Callback<AppModel>() {
                        @Override
                        public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                            dialog.dismiss();
                            AppModel model = response.body();
                            if (model != null) {
                                if (model.isStatus()) {
                               /*     C.INSTANCE.showToast(getApplicationContext(), model.getMessage());
                                    setResult(RESULT_OK);
                                    onBackPressed();
*/
                                    AcceptCampaign();
                                } else {
                                    C.INSTANCE.showToast(getApplicationContext(), model.getMessage());
                                }
                            } else {
                                C.INSTANCE.defaultError(getApplicationContext());
                            }
                        }

                        private void AcceptCampaign() {

                            dialog.show();
                            Call<AppModel> cancelCampaign = adminAPI.cancelCampaign(preference.getUserID(), preference.getTokenHash(), campaignList.getCampaign().getId(), preference.getUserRoleID(), "1");


                            Log.e("Parameters", "" + preference.getUserID() + "--" + preference.getTokenHash() + "--" + preference.getUserRoleID() + "--" + campaignList.getCampaign().getId());

                            cancelCampaign.enqueue(new Callback<AppModel>() {
                                @Override
                                public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                                    dialog.dismiss();
                                    AppModel appModel = response.body();
                                    if (appModel != null) {
                                        if (appModel.isStatus()) {
                                            C.INSTANCE.showToast(getApplicationContext(), appModel.getMessage());
                                            setResult(RESULT_OK);
                                            onBackPressed();
                                        } else {
                                            C.INSTANCE.showToast(getApplicationContext(), appModel.getMessage());
                                        }
                                    } else {
                                        C.INSTANCE.defaultError(getApplicationContext());
                                    }
                                }

                                @Override
                                public void onFailure(Call<AppModel> call, Throwable t) {
                                    dialog.dismiss();
                                    C.INSTANCE.errorToast(getApplicationContext(), t);
                                }
                            });


                        }

                        @Override
                        public void onFailure(Call<AppModel> call, Throwable t) {
                            dialog.dismiss();
                            C.INSTANCE.errorToast(getApplicationContext(), t);
                        }
                    });
                }

            }
        });
    }

    private void selectStartDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                dateSelected = year + "-" + (month + 1) + "-" + dayOfMonth;
                edt_startDate.setText(C.INSTANCE.convertDate("yyyy-MM-dd", "MM/dd/yy", dateSelected));
            }
        }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void addSelectedMember(int position, String name) {
        List<Member> searchedMembers = new ArrayList<>();

        for (int i = 0; i < memberList.size(); i++) {
            String memberName = memberList.get(i).getFirst_name() + " " + memberList.get(i).getLast_name();
            memberName = memberName.trim();
            if (memberName.equals(name)) {
                searchedMembers.add(memberList.get(i));
            }
        }

        for (int i = 0; i < searchedMembers.size(); i++) {
            if (i == position) {
                memberListAdapter.addMember(searchedMembers.get(i));
                break;
            }
        }

        auto_searchMember.setText("");
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
