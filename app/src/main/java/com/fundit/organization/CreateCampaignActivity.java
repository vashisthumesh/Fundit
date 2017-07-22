package com.fundit.organization;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.AppModel;
import com.fundit.model.FundspotListResponse;
import com.fundit.model.ProductListResponse;
import com.fundit.model.VerifyResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCampaignActivity extends AppCompatActivity {

    TextView txt_browseFundspot, txt_itemLabel;
    AutoCompleteTextView auto_searchFundspot;
    ArrayList<String> fundSpotNames = new ArrayList<>();
    List<VerifyResponse.VerifyResponseData> fundSpotList = new ArrayList<>();
    ArrayAdapter<String> autoAdapter;

    AdminAPI adminAPI;
    AppPreference preference;

    String selectedFundSpotName = null;
    String selectedFundSpotID = null;
    ProductListResponse.Product product = null;

    EditText edt_itemName,edt_couponCost,edt_organizationSplit,edt_fundSplit,edt_maxLimitCoupon,edt_campaignDuration,edt_couponExpireDay,edt_finePrint;
    CheckBox chk_indefinite;

    int REQUEST_PRODUCT = 369;

    Button btn_continue;
    CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_campaign);

        adminAPI= ServiceGenerator.getAPIClass();
        preference = new AppPreference(this);
        dialog = new CustomDialog(this);

        fetchIDs();
        setupToolbar();
    }

    private void fetchIDs() {
        txt_itemLabel = (TextView) findViewById(R.id.txt_itemLabel);
        txt_browseFundspot=(TextView) findViewById(R.id.txt_browseFundspot);
        auto_searchFundspot=(AutoCompleteTextView) findViewById(R.id.auto_searchFundspot);
        autoAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, fundSpotNames);
        btn_continue = (Button) findViewById(R.id.btn_continue);

        txt_browseFundspot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),FundspotListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent,REQUEST_PRODUCT);
            }
        });

        edt_itemName = (EditText) findViewById(R.id.edt_itemName);
        edt_couponCost = (EditText) findViewById(R.id.edt_couponCost);
        edt_organizationSplit = (EditText) findViewById(R.id.edt_organizationSplit);
        edt_fundSplit = (EditText) findViewById(R.id.edt_fundSplit);
        edt_maxLimitCoupon = (EditText) findViewById(R.id.edt_maxLimitCoupon);
        edt_campaignDuration = (EditText) findViewById(R.id.edt_campaignDuration);
        edt_couponExpireDay = (EditText) findViewById(R.id.edt_couponExpireDay);
        edt_finePrint = (EditText) findViewById(R.id.edt_finePrint);

        edt_itemName.setEnabled(false);
        edt_couponCost.setEnabled(false);

        chk_indefinite = (CheckBox) findViewById(R.id.chk_indefinite);

        chk_indefinite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chk_indefinite.isChecked()) {
                    edt_campaignDuration.setEnabled(false);
                } else {
                    edt_campaignDuration.setEnabled(true);
                }
            }
        });

        edt_organizationSplit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String orgSplit = edt_organizationSplit.getText().toString().trim();

                if (orgSplit.isEmpty()) {
                    edt_fundSplit.setText("100");
                } else {

                    if (orgSplit.contains(".")) {
                        float fSplit = Float.parseFloat(orgSplit);

                        if (fSplit > 100) {
                            edt_organizationSplit.setText("99");
                            fSplit = 99;
                        }

                        float fundSplit = 100 - fSplit;
                        edt_fundSplit.setText(String.format(Locale.getDefault(), "%.2f", fundSplit));
                    } else {
                        int fSplit = Integer.parseInt(orgSplit);

                        if (fSplit > 100) {
                            edt_organizationSplit.setText("99");
                            fSplit = 99;
                        }

                        int fundSplit = 100 - fSplit;
                        edt_fundSplit.setText(String.valueOf(fundSplit));
                    }
                }
            }
        });

        auto_searchFundspot.setThreshold(1);

        auto_searchFundspot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchKey = auto_searchFundspot.getText().toString().trim();
                if (searchKey.length() >= 3) {
                    searchFundspot(searchKey);
                } else {
                    fundSpotList.clear();
                    fundSpotNames.clear();
                    autoAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_textview, fundSpotNames);
                    auto_searchFundspot.setAdapter(autoAdapter);
                    auto_searchFundspot.showDropDown();
                }
            }
        });

        auto_searchFundspot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                VerifyResponse.VerifyResponseData data = fundSpotList.get(i);
                Intent intent = new Intent(getApplicationContext(), FundspotProductListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("fundspotName", data.getFundspot().getTitle());
                intent.putExtra("fundspotID", data.getFundspot().getUser_id());
                startActivityForResult(intent, REQUEST_PRODUCT);
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String organizationSplit = edt_organizationSplit.getText().toString().trim();
                String fundSpotSplit = edt_fundSplit.getText().toString().trim();
                String campaignDuration = edt_campaignDuration.getText().toString().trim();
                String maxLimitCoupon = edt_maxLimitCoupon.getText().toString().trim();
                String couponExpiry = edt_couponExpireDay.getText().toString().trim();
                String couponFinePrint = edt_finePrint.getText().toString().trim();

                float orgSplit = Float.parseFloat(organizationSplit.isEmpty() ? "0" : organizationSplit);
                int campDurationNum = Integer.parseInt(campaignDuration.isEmpty() ? "0" : campaignDuration);
                int maxLimitCouponNum = Integer.parseInt(maxLimitCoupon.isEmpty() ? "0" : maxLimitCoupon);
                int couponExpiryNum = Integer.parseInt(couponExpiry.isEmpty() ? "0" : couponExpiry);

                if (product == null) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select Fundspot and its Product");
                } else if (orgSplit < 1) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter Organization split min. 1%");
                } else if (!chk_indefinite.isChecked() && campDurationNum < 1) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter campaign duration min. 1");
                } else if (maxLimitCouponNum < 1) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter maximum limit of coupon min. 1");
                } else if (couponExpiryNum < 1) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter coupon expiry days min. 1");
                } else if (couponFinePrint.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter fine print");
                } else {

                    if (chk_indefinite.isChecked()) {
                        campaignDuration = "0";
                    }
                    dialog.show();
                    Call<AppModel> addCampCall = adminAPI.addCampaign(preference.getUserID(), preference.getTokenHash(), selectedFundSpotID, product.getType_id(), product.getId(), product.getPrice(), fundSpotSplit, organizationSplit, campaignDuration, maxLimitCoupon, couponExpiry, couponFinePrint);
                    addCampCall.enqueue(new Callback<AppModel>() {
                        @Override
                        public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                            dialog.dismiss();
                            AppModel model = response.body();
                            if (model != null) {
                                if (model.isStatus()) {
                                    C.INSTANCE.showToast(getApplicationContext(), model.getMessage());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_PRODUCT && resultCode == RESULT_OK && data!=null){
            selectedFundSpotName = data.getStringExtra("fundspotName");
            selectedFundSpotID = data.getStringExtra("fundspotID");
            product = (ProductListResponse.Product) data.getSerializableExtra("product");
            auto_searchFundspot.setText("");
            fillupSelectedData();
        }
    }

    private void fillupSelectedData() {
        auto_searchFundspot.setText(selectedFundSpotName);
        edt_itemName.setText(product.getName());
        edt_couponCost.setText(product.getPrice());
        edt_organizationSplit.setText(product.getOrganization_percent());
        edt_campaignDuration.setText(product.getCampaign_duration());
        edt_maxLimitCoupon.setText(product.getMax_limit_of_coupons());
        edt_finePrint.setText(product.getFine_print());
        edt_couponExpireDay.setText(product.getCoupon_expire_day());

        if(product.getType_id().equals(C.TYPE_GIFTCARD)){
            txt_itemLabel.setText("Gift Card being sold");
        }
        else {
            txt_itemLabel.setText("Item being sold");
        }
    }

    private void searchFundspot(String title) {
        Call<FundspotListResponse> searchCall = adminAPI.searchFundspot(preference.getUserID(), preference.getTokenHash(), title);
        searchCall.enqueue(new Callback<FundspotListResponse>() {
            @Override
            public void onResponse(Call<FundspotListResponse> call, Response<FundspotListResponse> response) {
                fundSpotList.clear();
                fundSpotNames.clear();
                FundspotListResponse listResponse = response.body();
                if (listResponse != null) {
                    if (listResponse.isStatus()) {
                        fundSpotList.addAll(listResponse.getData());
                        fundSpotNames.addAll(listResponse.getFundSpotNames());
                    }
                }

                autoAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_textview, fundSpotNames);
                auto_searchFundspot.setAdapter(autoAdapter);
                auto_searchFundspot.showDropDown();

            }

            @Override
            public void onFailure(Call<FundspotListResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
