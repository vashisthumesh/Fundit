package com.fundit;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.W;
import com.fundit.adapter.GetAllProductsAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.apis.ServiceHandler;
import com.fundit.helper.CustomDialog;
import com.fundit.model.AppModel;
import com.fundit.model.CampaignListResponse;
import com.fundit.model.MultipleProductResponse;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCampaignTermsActivity extends AppCompatActivity {

    TextView txt_partnerLabel, txt_itemLabel;
    TextView txt_message;
    EditText edt_partner, edt_itemName, edt_price, edt_split, edt_campaignDuration, edt_maxLimitCoupon, edt_couponExpireDay, edt_finePrint, edt_orgSplit;
    RadioGroup rg_productType;
    RadioButton rdo_typeItem, rdo_typeGiftCard;
    CheckBox chk_infinite;
    Button btn_accept, btn_message, btn_decline;

    AppPreference preference;
    CampaignListResponse.CampaignList campaignList;
    MultipleProductResponse multi;
    AdminAPI adminAPI;
    CustomDialog dialog;

    ListView listGetProducts;

    int REQUEST_START_CAMPAIGN = 369;

    List<MultipleProductResponse> productResponse = new ArrayList<>();

    GetAllProductsAdapter allProductsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_campaign_terms);

        adminAPI = ServiceGenerator.getAPIClass();
        dialog = new CustomDialog(this);
        preference = new AppPreference(this);


        Intent intent = getIntent();

        campaignList = (CampaignListResponse.CampaignList) intent.getSerializableExtra("campaignItem");

        Log.e("campaignList", "--->" + new Gson().toJson(campaignList));


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
        txt_partnerLabel = (TextView) findViewById(R.id.txt_partnerLabel);
        txt_itemLabel = (TextView) findViewById(R.id.txt_itemLabel);

        edt_partner = (EditText) findViewById(R.id.edt_partner);
        edt_itemName = (EditText) findViewById(R.id.edt_itemName);
        edt_price = (EditText) findViewById(R.id.edt_price);
        edt_split = (EditText) findViewById(R.id.edt_split);
        edt_orgSplit = (EditText) findViewById(R.id.edt_orgsplit);
        edt_campaignDuration = (EditText) findViewById(R.id.edt_campaignDuration);
        edt_maxLimitCoupon = (EditText) findViewById(R.id.edt_maxLimitCoupon);
        edt_couponExpireDay = (EditText) findViewById(R.id.edt_couponExpireDay);
        edt_finePrint = (EditText) findViewById(R.id.edt_finePrint);
        txt_message = (TextView) findViewById(R.id.txt_message);

        rg_productType = (RadioGroup) findViewById(R.id.rg_productType);

        rdo_typeItem = (RadioButton) findViewById(R.id.rdo_typeItem);
        rdo_typeGiftCard = (RadioButton) findViewById(R.id.rdo_typeGiftCard);

        chk_infinite = (CheckBox) findViewById(R.id.chk_infinite);

        btn_accept = (Button) findViewById(R.id.btn_accept);
        btn_message = (Button) findViewById(R.id.btn_message);
        btn_decline = (Button) findViewById(R.id.btn_decline);

        listGetProducts = (ListView) findViewById(R.id.list_get_products);
        allProductsAdapter = new GetAllProductsAdapter(productResponse, getApplicationContext());
        listGetProducts.setAdapter(allProductsAdapter);


        if (preference.getUserRoleID().equals(C.ORGANIZATION)) {
            txt_partnerLabel.setText("Fundspot Partner");
            if (campaignList.getCampaign().getReview_status().equalsIgnoreCase("1")) {

                edt_partner.setText(campaignList.getUserOrganization().getFundspot().getTitle());
            } else {


                edt_partner.setText(campaignList.getUserFundspot().getFundspot().getTitle());
            }


        } else if (preference.getUserRoleID().equals(C.FUNDSPOT)) {
            txt_partnerLabel.setText("Organization Partner");

            if (campaignList.getCampaign().getReview_status().equalsIgnoreCase("1")) {
                if (campaignList.getCampaign().getAction_status().equalsIgnoreCase("0")) {
                    edt_partner.setText(campaignList.getUserOrganization().getOrganization().getTitle());
                } else {
                    edt_partner.setText(campaignList.getUserFundspot().getOrganization().getTitle());
                }
            } else {
                if (campaignList.getCampaign().getAction_status().equalsIgnoreCase("1")) {
                    edt_partner.setText(campaignList.getUserFundspot().getOrganization().getTitle());
                } else {
                    edt_partner.setText(campaignList.getUserOrganization().getOrganization().getTitle());
                }
            }


        }


        // edt_itemName.setText(campaignList.getProduct().getName());
        // edt_price.setText(campaignList.getCampaign().getPrice());
        edt_split.setText(campaignList.getCampaign().getFundspot_percent());
        edt_orgSplit.setText(campaignList.getCampaign().getOrganization_percent());
        edt_campaignDuration.setText(campaignList.getCampaign().getCampaign_duration());
        edt_maxLimitCoupon.setText("$" + String.format("%.2f" , Double.parseDouble(campaignList.getCampaign().getMax_limit_of_coupons())));
        edt_couponExpireDay.setText(campaignList.getCampaign().getCoupon_expire_day());
        txt_message.setText("Message: " + campaignList.getCampaign().getMsg());
        // edt_finePrint.setText(campaignList.getCampaign().getFine_print());

        if (campaignList.getCampaign().getCampaign_duration().equals("0")) {
            chk_infinite.setChecked(true);
        }

        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                Call<AppModel> cancelCampaign = adminAPI.cancelCampaign(preference.getUserID(), preference.getTokenHash(), campaignList.getCampaign().getId(), preference.getUserRoleID(), "3");


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
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in3 = new Intent(getApplicationContext(), CreateCampaignTermsNextActivity.class);
                in3.putExtra("campaignList", campaignList);
                startActivityForResult(in3, REQUEST_START_CAMPAIGN);
            }
        });


        Log.e("products", "-->" + campaignList.getCampaignProduct().size());

        for (int i = 0; i < campaignList.getCampaignProduct().size(); i++) {


            productResponse.add(campaignList.getCampaignProduct().get(i));

            Log.e("getResponse" , "===>" + new Gson().toJson(productResponse.get(i)));


        }
        listGetProducts.setVisibility(View.VISIBLE);
        allProductsAdapter.notifyDataSetChanged();



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_START_CAMPAIGN && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            onBackPressed();
        }
    }





    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext() , HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        System.gc();
    }



    /*public class GetAllProductsData extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {


                dialog.show();

            } catch (Exception e) {


            }
        }

        @Override
        protected String doInBackground(Void... params) {


            List<NameValuePair> parameters = new ArrayList<>();

            parameters.add(new BasicNameValuePair("user_id", preference.getUserID()));
            parameters.add(new BasicNameValuePair("tokenhash", preference.getTokenHash()));
            parameters.add(new BasicNameValuePair("role_id", preference.getUserRoleID()));

            if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
                parameters.add(new BasicNameValuePair("fundspot_id", preference.getUserID()));
            } else if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
                parameters.add(new BasicNameValuePair("organization_id", preference.getUserID()));
            }


            parameters.add(new BasicNameValuePair("status", "0"));
            parameters.add(new BasicNameValuePair("action_status", "0"));

            Log.e("parameters", "--->" + parameters);

            String json = new ServiceHandler(getApplicationContext()).makeServiceCall(W.ASYNC_BASE_URL + W.CAMPAIGN_LIST, ServiceHandler.POST, parameters);

            return json;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            if (s.equalsIgnoreCase("") || s.isEmpty()) {


                C.INSTANCE.showToast(getApplicationContext(), "Please check your Internet");


            } else {

                try {
                    JSONObject mainObject = new JSONObject(s);

                    boolean status = false;
                    String message = "";

                    status = mainObject.getBoolean("status");
                    message = mainObject.getString("message");


                    productResponse.clear();

                    if (status == true) {


                        JSONArray dataArray = mainObject.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {

                            JSONObject object = dataArray.getJSONObject(i);

                            JSONArray campaignProductArray = object.getJSONArray("CampaignProduct");

                            for (int j = 0; j < campaignProductArray.length(); j++) {

                                JSONObject obj = campaignProductArray.getJSONObject(j);

                                MultipleProductResponse response = new MultipleProductResponse();

                                response.setCampaign_id(obj.getString("campaign_id"));
                                Log.e("campaignId", "--->" + obj.getString("campaign_id"));
                                response.setProduct_id(obj.getString("product_id"));
                                Log.e("productId", "--->" + obj.getString("product_id"));
                                response.setPrice(obj.getString("price"));
                                response.setName(obj.getString("name"));
                                response.setDescription(obj.getString("description"));
                                response.setFine_print(obj.getString("fine_print"));
                                response.setImage(obj.getString("image"));
                                response.setType_id(obj.getString("type_id"));

                                productResponse.add(response);


                            }

                            listGetProducts.setVisibility(View.VISIBLE);
                            allProductsAdapter.notifyDataSetChanged();


                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }
    }*/
}
