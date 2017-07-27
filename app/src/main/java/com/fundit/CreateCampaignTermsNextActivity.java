package com.fundit;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
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

    EditText edt_campaignName, edt_description, edt_startDate, edt_message;
    CheckBox chk_startDateAsPossible, chk_allOrgMembers;
    TextView txt_couponSellerLabel;
    AutoCompleteTextView auto_searchMember;
    ListView listMembers;
    Button btn_request;

    String dateSelected = null;

    List<Member> memberList = new ArrayList<>();
    ArrayList<String> memberNames = new ArrayList<>();
    ArrayAdapter<String> memberArrayAdapter;
    MemberListAdapter memberListAdapter;

    AdminAPI adminAPI;
    AppPreference preference;
    CustomDialog dialog;

    CampaignListResponse.CampaignList campaignList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_campaign_next);

        adminAPI = ServiceGenerator.getAPIClass();
        preference = new AppPreference(this);
        dialog = new CustomDialog(this);

        Intent intent = getIntent();
        campaignList = (CampaignListResponse.CampaignList) intent.getSerializableExtra("campaignList");

        fetchIDs();

    }

    private void fetchIDs() {
        edt_campaignName = (EditText) findViewById(R.id.edt_campaignName);
        edt_description = (EditText) findViewById(R.id.edt_description);
        edt_startDate = (EditText) findViewById(R.id.edt_startDate);
        edt_message = (EditText) findViewById(R.id.edt_message);

        edt_campaignName.setEnabled(false);
        edt_description.setEnabled(false);

        txt_couponSellerLabel = (TextView) findViewById(R.id.txt_couponSellerLabel);
        txt_couponSellerLabel.setText("Coupon redeemers");

        chk_startDateAsPossible = (CheckBox) findViewById(R.id.chk_startDateAsPossible);
        chk_startDateAsPossible.setVisibility(View.GONE);
        chk_allOrgMembers = (CheckBox) findViewById(R.id.chk_allOrgMembers);
        chk_allOrgMembers.setText("All fundspot members");

        auto_searchMember = (AutoCompleteTextView) findViewById(R.id.auto_searchMember);
        memberArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_textview, memberNames);
        auto_searchMember.setAdapter(memberArrayAdapter);

        btn_request = (Button) findViewById(R.id.btn_request);
        btn_request.setText("Start Campaign");

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

        if(campaignList.getCampaign().getStart_date()!=null && !campaignList.getCampaign().getStart_date().isEmpty()){
            edt_startDate.setText(C.INSTANCE.convertDate("yyyy-MM-dd","MM/dd/yy",campaignList.getCampaign().getStart_date()));
            dateSelected = campaignList.getCampaign().getStart_date();
        }


        dialog.show();
        Call<MemberListResponse> memberListResponseCall = adminAPI.getAllMemberList(preference.getUserID(), preference.getTokenHash(), preference.getUserRoleID(), null, preference.getUserID());
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
            List<Member> checkedmemberList=memberListAdapter.getMemberList();
                String message=edt_message.getText().toString().trim();

                if(dateSelected==null){
                    C.INSTANCE.showToast(getApplicationContext(), "Please select start date ");

                }else if(!chk_allOrgMembers.isChecked() && checkedmemberList.size()==0){
                    C.INSTANCE.showToast(getApplicationContext(), "Please select fundspot member ");

                }else if(message.isEmpty()){
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter message to sellers");

                }else{
                    JSONArray campaignDetailArray = new JSONArray();
                    JSONObject detailObject = new JSONObject();
                    JSONArray memberIDArray = new JSONArray();

                    try {

                        detailObject.put("start_date", dateSelected);


                        if (chk_allOrgMembers.isChecked()) {
                            detailObject.put("all_member", "1");
                        } else {
                            detailObject.put("all_member", "0");
                        }
                        detailObject.put("message", message);

                        campaignDetailArray.put(detailObject);


                        if (!chk_allOrgMembers.isChecked()) {
                            for (int i = 0; i < checkedmemberList.size(); i++) {
                                JSONObject object = new JSONObject();
                                object.put("member_id", checkedmemberList.get(i).getUser_id());
                                memberIDArray.put(object);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.show();
                    Call<AppModel> addCampCall = adminAPI.appEditcampaign(preference.getUserID(), preference.getTokenHash(),campaignList.getCampaign().getId() ,campaignDetailArray.toString(), memberIDArray.toString());
                    addCampCall.enqueue(new Callback<AppModel>() {
                        @Override
                        public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                            dialog.dismiss();
                            AppModel model = response.body();
                            if (model != null) {
                                if (model.isStatus()) {
                                    C.INSTANCE.showToast(getApplicationContext(), model.getMessage());
                                    setResult(RESULT_OK);
                                    onBackPressed();
                                } else {
                                    C.INSTANCE.showToast(getApplicationContext(), model.getMessage());
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
