package com.fundit.fundspot;

import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.fundit.Bean.GetProductsFundspotBean;
import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.W;
import com.fundit.adapter.SelectedProductListAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.apis.ServiceHandler;
import com.fundit.helper.CustomDialog;
import com.fundit.model.Fundspot;
import com.fundit.model.FundspotListResponse;
import com.fundit.model.ProductListResponse;
import com.fundit.model.VerifyResponse;

import com.fundit.organization.CreateCampaignActivity;
import com.fundit.organization.CreateCampaignNextActivity;
import com.fundit.organization.FundspotListActivity;
import com.fundit.organization.FundspotProductListActivity;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NWSPL-17 on 10-Aug-17.
 */

public class FundspotCampaignActivity extends AppCompatActivity {
    TextView txt_browseFundspot, txt_itemLabel , txt_partnerLabel;
    AutoCompleteTextView auto_searchFundspot;
    ArrayList<String> fundSpotNames = new ArrayList<>();
    List<VerifyResponse.VerifyResponseData> fundSpotList = new ArrayList<>();
    List<GetProductsFundspotBean> fundspotBeen = new ArrayList<>();
    ArrayAdapter<String> autoAdapter;

    AdminAPI adminAPI;
    AppPreference preference;

    String selectedFundSpotName = null;
    String selectedFundSpotID = null;
    ProductListResponse.Product product = null;

    EditText edt_itemName, edt_couponCost, edt_organizationSplit, edt_fundSplit, edt_maxLimitCoupon, edt_campaignDuration, edt_couponExpireDay, edt_finePrint;
    CheckBox chk_indefinite;

    ArrayList<String> selectedProducts = new ArrayList<>();

    int REQUEST_PRODUCT = 369;
    int NEXT_STEP = 936;

    Button btn_continue;
    CustomDialog dialog;

    ListView listSelectedProducts;

    SelectedProductListAdapter productListAdapter;

    JSONArray jsonArrayProductId = new JSONArray();

    String json = "";
    String selectedOrganizationID = "";

    Fundspot fundspot = new Fundspot();
    boolean isProfileMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_campaign_fundspot);

        Intent intent = getIntent();
        isProfileMode = intent.getBooleanExtra("isProfileMode", false);


        adminAPI = ServiceGenerator.getAPIClass();
        preference = new AppPreference(this);
        dialog = new CustomDialog(this);


        try {

            fundspot = new Gson().fromJson(preference.getMemberData() , Fundspot.class);


        }catch (Exception e){


        }

        fetchIDs();
        setupToolbar();
    }

    private void fetchIDs() {
        txt_itemLabel = (TextView) findViewById(R.id.txt_itemLabel);
        txt_browseFundspot = (TextView) findViewById(R.id.txt_browseFundspot);
        txt_partnerLabel = (TextView) findViewById(R.id.txt_partnerLabel);
        txt_partnerLabel.setText("Organization Partner");
        txt_browseFundspot.setText("Browse Organizations");

        auto_searchFundspot = (AutoCompleteTextView) findViewById(R.id.auto_searchFundspot);
        autoAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, fundSpotNames);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        listSelectedProducts = (ListView) findViewById(R.id.list_selected_products);
        listSelectedProducts.setVisibility(View.GONE);

        txt_browseFundspot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OrganizationListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, REQUEST_PRODUCT);
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
                intent.putExtra("fundspotName",fundSpotList.get(i).getOrganization().getTitle());
                intent.putExtra("fundspotID", preference.getUserID());
                intent.putExtra("organizationID" , fundSpotList.get(i).getOrganization().getUser_id());
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

                if (selectedFundSpotName == null) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select Organization");
                } else if (orgSplit < 1) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter Organization split min. 1%");
                } else if (!chk_indefinite.isChecked() && campDurationNum < 1) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter campaign duration min. 1");
                } else if (maxLimitCouponNum < 1) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter maximum limit of coupon min. 1");
                } else if (couponExpiryNum < 1) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter coupon expiry days min. 1");
                } /*else if (couponFinePrint.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter fine print");
                }*/ else {

                    if (chk_indefinite.isChecked()) {
                        campaignDuration = "0";
                    }

                    Intent intent = new Intent(getApplicationContext(), FundspotCampaignNextActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("selectedFundspotID", selectedOrganizationID);
                    //intent.putExtra("product", product);
                    intent.putExtra("fundspotSplit", fundSpotSplit);
                    intent.putExtra("organizationSplit", organizationSplit);
                    intent.putExtra("campaignDuration", campaignDuration);
                    intent.putExtra("maxLimitCoupon", maxLimitCoupon);
                    intent.putExtra("couponExpiry", couponExpiry);
                    //intent.putExtra("couponFinePrint", couponFinePrint);
                    intent.putStringArrayListExtra("products" , selectedProducts);
                    startActivityForResult(intent, NEXT_STEP);
                }
            }
        });

        if(isProfileMode){
            Intent data = getIntent();


            productListAdapter = new SelectedProductListAdapter(fundspotBeen, getApplicationContext());
            listSelectedProducts.setAdapter(productListAdapter);

            selectedProducts = data.getStringArrayListExtra("ProductsId");
            selectedFundSpotName = data.getStringExtra("fundspotName");
            selectedOrganizationID = data.getStringExtra("organizationID");


            Log.e("selected" , "-->" + selectedOrganizationID);

            selectedFundSpotID = data.getStringExtra("fundspotID");

            for (int i = 0; i < selectedProducts.size(); i++) {

                try {
                    JSONObject mainObject = new JSONObject();

                    mainObject.put("id", selectedProducts.get(i));

                    jsonArrayProductId.put(mainObject);

                    Log.e("check", "ckeck12345");

                } catch (Exception e) {

                    e.printStackTrace();
                }


            }


            Log.e("selectedProducts", "-->" + selectedProducts);
            new GetAllDatas().execute();
//             auto_searchFundspot.setText("");

            auto_searchFundspot.setText(selectedFundSpotName);

    }

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
        if (requestCode == REQUEST_PRODUCT && resultCode == RESULT_OK && data != null) {

            // product = (ProductListResponse.Product) data.getSerializableExtra("product");

            productListAdapter = new SelectedProductListAdapter(fundspotBeen, getApplicationContext());
            listSelectedProducts.setAdapter(productListAdapter);

            selectedProducts = data.getStringArrayListExtra("ProductsId");
            selectedFundSpotName = data.getStringExtra("fundspotName");
            selectedOrganizationID = data.getStringExtra("organizationID");

            Log.e("selectedOrganizationID" , selectedOrganizationID);

            selectedFundSpotID = data.getStringExtra("fundspotID");

            for (int i = 0; i < selectedProducts.size(); i++) {

                try {
                    JSONObject mainObject = new JSONObject();

                    mainObject.put("id", selectedProducts.get(i));

                    jsonArrayProductId.put(mainObject);

                    Log.e("check", "ckeck12345");

                } catch (Exception e) {

                    e.printStackTrace();
                }


            }


            Log.e("selectedProducts", "-->" + selectedProducts);
            new GetAllDatas().execute();

            auto_searchFundspot.setText(selectedFundSpotName);
            //fillupSelectedData();
        } else if (requestCode == NEXT_STEP && resultCode == RESULT_OK) {
            onBackPressed();
        }
    }

    private void fillupSelectedData() {
        auto_searchFundspot.setText(selectedFundSpotName);
        //  edt_itemName.setText(product.getName());
        // edt_couponCost.setText(product.getPrice());
        edt_organizationSplit.setText(product.getOrganization_percent());
        edt_campaignDuration.setText(product.getCampaign_duration());
        edt_maxLimitCoupon.setText(product.getMax_limit_of_coupons());
        //  edt_finePrint.setText(product.getFine_print());
        edt_couponExpireDay.setText(product.getCoupon_expire_day());

        if (product.getType_id().equals(C.TYPE_GIFTCARD)) {
            txt_itemLabel.setText("Gift Card being sold");
        } else {
            txt_itemLabel.setText("Item being sold");
        }

        Log.e("product data", new Gson().toJson(product));
    }

    private void searchFundspot(String title) {
        Call<FundspotListResponse> searchCall = adminAPI.searchOrganization(preference.getUserID(), preference.getTokenHash(), title);
        searchCall.enqueue(new Callback<FundspotListResponse>() {
            @Override
            public void onResponse(Call<FundspotListResponse> call, Response<FundspotListResponse> response) {
                fundSpotList.clear();
                fundSpotNames.clear();
                FundspotListResponse listResponse = response.body();
                if (listResponse != null) {
                    if (listResponse.isStatus()) {
                        fundSpotList.addAll(listResponse.getData());
                        fundSpotNames.addAll(listResponse.getOrganizationNames());
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


    public class GetAllDatas extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {


                dialog = new CustomDialog(getApplicationContext());
                dialog.show();
                dialog.setCancelable(false);


            } catch (Exception e) {


            }
        }

        @Override
        protected String doInBackground(Void... params) {


            List<NameValuePair> pairs = new ArrayList<>();

            pairs.add(new BasicNameValuePair("user_id", preference.getUserID()));
            pairs.add(new BasicNameValuePair("tokenhash", preference.getTokenHash()));
            pairs.add(new BasicNameValuePair("product_id", "" + jsonArrayProductId));
            pairs.add(new BasicNameValuePair("fundspot_id", selectedFundSpotID));


            json = new ServiceHandler().makeServiceCall(W.BASE_URL + W.GetProductsFundspotProducts, ServiceHandler.POST, pairs);


            Log.e("parameters", "" + pairs);
            Log.e("json", json);

            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            if (s.isEmpty() || s.equalsIgnoreCase("")) {

                C.INSTANCE.noInternet(getApplicationContext());

            } else {

                try {
                    JSONObject mainObject = new JSONObject(s);

                    boolean status = true;
                    String message = "";

                    status = mainObject.getBoolean("status");
                    message = mainObject.getString("message");


                    if (status) {




                        JSONObject dataObject = mainObject.getJSONObject("data");
                        JSONObject fundspotObject = dataObject.getJSONObject("Fundspot");


                       // auto_searchFundspot.setText(fundspotObject.getString("title"));
                        edt_organizationSplit.setText(fundspotObject.getString("organization_percent"));
                        edt_couponExpireDay.setText(fundspotObject.getString("coupon_expire_day"));
                        edt_campaignDuration.setText(fundspotObject.getString("campaign_duration"));
                        edt_maxLimitCoupon.setText(fundspotObject.getString("max_limit_of_coupon_price"));



                        if(edt_campaignDuration.getText().toString().trim().equalsIgnoreCase("0"))
                        {
                            chk_indefinite.setChecked(true);
                        }

//                        auto_searchFundspot.setEnabled(false);
//                        edt_organizationSplit.setEnabled(false);
//                        edt_couponExpireDay.setEnabled(false);
//                        edt_campaignDuration.setEnabled(false);
                        //edt_maxLimitCoupon.setEnabled(false);


                        fundspotBeen.clear();
                        JSONArray productArray = dataObject.getJSONArray("Product");

                        for (int i = 0; i < productArray.length(); i++) {
                            GetProductsFundspotBean productsFundspotBean = new GetProductsFundspotBean();

                            JSONObject object = productArray.getJSONObject(i);

                            productsFundspotBean.setProductId(object.getString("id"));
                            productsFundspotBean.setType_id(object.getString("type_id"));
                            productsFundspotBean.setName(object.getString("name"));
                            productsFundspotBean.setProductDescription(object.getString("description"));
                            productsFundspotBean.setImage(object.getString("image"));
                            productsFundspotBean.setPrice(object.getString("price"));
                            productsFundspotBean.setFine_print(object.getString("fine_print"));


                            fundspotBeen.add(productsFundspotBean);

                        }


                        listSelectedProducts.setVisibility(View.VISIBLE);
                        productListAdapter.notifyDataSetChanged();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }




}
