package com.fundit.organization;

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
import android.widget.TextView;

import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.adapter.MemberListAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.AdjustableListView;
import com.fundit.helper.CustomDialog;
import com.fundit.model.AppModel;
import com.fundit.model.Member;
import com.fundit.model.MemberListResponse;
import com.fundit.model.ProductListResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCampaignNextActivity extends AppCompatActivity {

    AdminAPI adminAPI;
    AppPreference preference;
    CustomDialog dialog;
    String dateSelected = null;

    AdjustableListView listMembers;
    EditText edt_campaignName, edt_description, edt_startDate, edt_message , edt_amount , edt_msgFundspot;
    CheckBox chk_startDateAsPossible, chk_allOrgMembers , chk_maxAmount;
    AutoCompleteTextView auto_searchMember;
    Button btn_request;
    TextView txt_message;

    String selectedFundspotID, fundspotSplit, organizationSplit, campaignDuration, maxLimitCoupon, couponExpiry, couponFinePrint;
    ProductListResponse.Product product;

    List<Member> memberList = new ArrayList<>();
    ArrayList<String> memberNames = new ArrayList<>();
    ArrayAdapter<String> memberArrayAdapter;
    ArrayList<String> selectedProducts = new ArrayList<>();
    MemberListAdapter memberListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_campaign_next);

        preference = new AppPreference(this);
        adminAPI = ServiceGenerator.getAPIClass();
        dialog = new CustomDialog(this);

        Intent intent = getIntent();
        selectedFundspotID = intent.getStringExtra("selectedFundspotID");
        fundspotSplit = intent.getStringExtra("fundspotSplit");
        organizationSplit = intent.getStringExtra("organizationSplit");
        campaignDuration = intent.getStringExtra("campaignDuration");
        maxLimitCoupon = intent.getStringExtra("maxLimitCoupon");
        couponExpiry = intent.getStringExtra("couponExpiry");
        selectedProducts = intent.getStringArrayListExtra("products");

        //couponFinePrint = intent.getStringExtra("couponFinePrint");
       // product = (ProductListResponse.Product) intent.getSerializableExtra("product");

        fetchIDs();
        setupToolbar();
    }

    private void fetchIDs() {
        edt_campaignName = (EditText) findViewById(R.id.edt_campaignName);
        edt_description = (EditText) findViewById(R.id.edt_description);
        edt_startDate = (EditText) findViewById(R.id.edt_startDate);
        edt_message = (EditText) findViewById(R.id.edt_message);
        edt_amount = (EditText) findViewById(R.id.edt_amount);
        edt_msgFundspot = (EditText) findViewById(R.id.edt_msg_fundspot);
        txt_message= (TextView) findViewById(R.id.txt_message);
        txt_message.setText("Message to Fundspot");

        chk_startDateAsPossible = (CheckBox) findViewById(R.id.chk_startDateAsPossible);
        chk_allOrgMembers = (CheckBox) findViewById(R.id.chk_allOrgMembers);
        chk_maxAmount = (CheckBox) findViewById(R.id.chk_max_amount);

        auto_searchMember = (AutoCompleteTextView) findViewById(R.id.auto_searchMember);
        btn_request = (Button) findViewById(R.id.btn_request);
        memberArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_textview, memberNames);
        auto_searchMember.setAdapter(memberArrayAdapter);

        listMembers = (AdjustableListView) findViewById(R.id.listMembers);
        memberListAdapter = new MemberListAdapter(this);
        listMembers.setAdapter(memberListAdapter);

        auto_searchMember.setThreshold(1);

        edt_startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectStartDate();
            }
        });

        auto_searchMember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text = auto_searchMember.getText().toString().trim();
                addSelectedMember(i, text);
            }
        });


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

                String maxLimitCoupon_final=maxLimitCoupon.replace("$","");
                Log.e("amount",amount);
                if(!amount.trim().isEmpty()) {
                    if (Double.parseDouble(amount.trim()) > Double.parseDouble(maxLimitCoupon_final.trim())) {
                        edt_amount.setText(maxLimitCoupon_final);
                    }
                    if(Double.parseDouble(amount.trim()) == 0)
                    {
                        edt_amount.setText("1");
                    }
                }

            }
        });
        chk_maxAmount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(buttonView.isChecked()){

                    edt_amount.setText(maxLimitCoupon);
                    edt_amount.setEnabled(false);
                }else{

                    edt_amount.setEnabled(true);

                }



            }
        });

        dialog.show();
        Call<MemberListResponse> memberListResponseCall = adminAPI.getAllMemberList(preference.getUserID(), preference.getTokenHash(), preference.getUserRoleID(), preference.getUserID(), null);
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
                String campaignTitle = edt_campaignName.getText().toString().trim();
                String description = edt_description.getText().toString().trim();
                String message = edt_message.getText().toString().trim();
                String fundspotMessage = edt_msgFundspot.getText().toString().trim();
                String maxAmount1 = edt_amount.getText().toString().trim();


                int amount = 0;
                if(!maxAmount1.isEmpty())
                {
                    amount = Integer.parseInt(maxAmount1.trim());

                    Log.e("amount" , "-->" + amount);
                }



                List<Member> selectedMemberList = memberListAdapter.getMemberList();

                if (campaignTitle.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter campaign title");
                } else if (description.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter description");
                    }

                else if (message.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter message");
                }else if(fundspotMessage.isEmpty()){
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter message for fundspot");
                }

                else {

                    JSONArray campaignDetailArray = new JSONArray();
                    JSONObject detailObject = new JSONObject();
                    JSONArray memberIDArray = new JSONArray();
                    JSONArray   selectedProductArray = new JSONArray();
                    try {
                        detailObject.put("user_id", preference.getUserID());
                        detailObject.put("role_id" , preference.getUserRoleID());
                        detailObject.put("title", campaignTitle);
                        detailObject.put("description", description);
                        detailObject.put("receiver_id", selectedFundspotID);
                        //detailObject.put("type_id", product.getType_id());
                        //detailObject.put("product_id", product.getId());
                        //detailObject.put("price", product.getPrice());
                        //detailObject.put("fine_print", couponFinePrint);
                        detailObject.put("fundspot_percent", fundspotSplit);
                        detailObject.put("organization_percent", organizationSplit);
                        detailObject.put("campaign_duration", campaignDuration);
                        detailObject.put("max_limit_of_coupons", maxLimitCoupon);
                        detailObject.put("coupon_expire_day", couponExpiry);
                        detailObject.put("msg_seller", message);
                        detailObject.put("msg" , fundspotMessage);
                        detailObject.put("all_member", "1");
                        if (chk_startDateAsPossible.isChecked()) {
                            detailObject.put("campaign_asap", "1");
                            detailObject.put("start_date", "");
                        } else {
                            detailObject.put("campaign_asap", "0");
                            detailObject.put("start_date", "");
                        }
//                        if (chk_allOrgMembers.isChecked()) {
//                            detailObject.put("all_member", "1");
//                        }else {
//                            detailObject.put("all_member", "0");
//                        }
                        if(chk_maxAmount.isChecked()){

                            detailObject.put("target_amount" , 0);
                        }else{

                            detailObject.put("target_amount" , maxAmount1);
                        }
                        campaignDetailArray.put(detailObject);


//                        if (!chk_allOrgMembers.isChecked()) {
//                            for (int i = 0; i < selectedMemberList.size(); i++) {
//                                JSONObject object = new JSONObject();
//                                object.put("member_id", selectedMemberList.get(i).getUser_id());
//                                memberIDArray.put(object);
//                            }
//                        }

                        for (int i=0 ; i <selectedProducts.size();i++){

                            JSONObject mainObject = new JSONObject();

                            mainObject.put("id", selectedProducts.get(i));

                            Log.e("selectedProducts" , "---->" + selectedProducts);

                            selectedProductArray.put(mainObject);

                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.show();
                    Call<AppModel> addCampCall = adminAPI.addCampaign(preference.getUserID(), preference.getTokenHash(), campaignDetailArray.toString(), memberIDArray.toString() , selectedProductArray.toString());

                    Log.e("Parameter", preference.getUserID() + " - " + preference.getTokenHash() + " - " + "-" + campaignDetailArray.toString() + "-" + memberIDArray.toString() + "-" + selectedProductArray.toString());


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
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionTitle.setText("Create Campaign");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
}
