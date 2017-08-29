package com.fundit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.adapter.GetAllProductsAdapter;
import com.fundit.adapter.OrderTimeProductAdapter;
import com.fundit.adapter.ProductListAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.apis.StringConverterFactory;
import com.fundit.helper.CustomDialog;
import com.fundit.model.AppModel;
import com.fundit.model.CampaignListResponse;
import com.fundit.model.Member;
import com.fundit.model.MultipleProductResponse;
import com.fundit.model.ProductListResponse;
import com.fundit.model.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinalOrderPlace extends AppCompatActivity {

    AppPreference preference;
    AdminAPI adminAPI;


    TextView txt_fundraiser, txt_partnerTitle, txt_partnerName, txt_targetAmt;

    EditText edt_name, edt_email;

    ListView list_products;

    RadioGroup radioGroup_paymentType;

    RadioButton radio_cardPayment, radio_cashPayment;

    Button btn_placeOrder;

    CampaignListResponse.CampaignList campaignList;

    User user = new User();
    Member member = new Member();

    List<MultipleProductResponse> productList = new ArrayList<>();
    OrderTimeProductAdapter productAdapter;

    CustomDialog dialog;

    JSONArray selectedProductArray = new JSONArray();
    JSONObject mainObject = new JSONObject();

    String organizationId = "";
    String fundspotId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_order_place);


        Intent intent = getIntent();

        campaignList = (CampaignListResponse.CampaignList) intent.getSerializableExtra("details");

        preference = new AppPreference(getApplicationContext());
        adminAPI = ServiceGenerator.getAPIClass();
        dialog = new CustomDialog(FinalOrderPlace.this);

        try {

            user = new Gson().fromJson(preference.getUserData(), User.class);
            member = new Gson().fromJson(preference.getMemberData(), Member.class);


        } catch (Exception e) {

            Log.e("Exception", e.getMessage());
        }


        setupToolbar();
        fetchIDs();


    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void fetchIDs() {

        txt_fundraiser = (TextView) findViewById(R.id.txt_fundraiser);
        txt_partnerTitle = (TextView) findViewById(R.id.txt_partnerTitle);
        txt_partnerName = (TextView) findViewById(R.id.txt_partnerName);
        txt_targetAmt = (TextView) findViewById(R.id.txt_targetAmt);

        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_email = (EditText) findViewById(R.id.edt_email);

        list_products = (ListView) findViewById(R.id.list_products);

        radioGroup_paymentType = (RadioGroup) findViewById(R.id.radioGroup_paymentType);

        radio_cardPayment = (RadioButton) findViewById(R.id.radio_cardPayment);
        radio_cashPayment = (RadioButton) findViewById(R.id.radio_cashPayment);

        btn_placeOrder = (Button) findViewById(R.id.btn_placeOrder);


        txt_fundraiser.setText("Fundriser : " + campaignList.getCampaign().getTitle());
        txt_targetAmt.setText(" $" + campaignList.getCampaign().getTarget_amount());

        productAdapter = new OrderTimeProductAdapter(productList, getApplicationContext());
        list_products.setAdapter(productAdapter);
        for (int i = 0; i < campaignList.getCampaignProduct().size(); i++) {

            productList.add(campaignList.getCampaignProduct().get(i));
            productAdapter.notifyDataSetChanged();

        }


        if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
            txt_partnerTitle.setText("Organization :");
            if (campaignList.getCampaign().getReview_status().equals(1)) {
                txt_partnerName.setText(campaignList.getUserFundspot().getTitle());
            } else {
                txt_partnerName.setText(campaignList.getUserFundspot().getTitle());
            }
        }

        if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
            txt_partnerTitle.setText("Fundspot :");
            if (campaignList.getCampaign().getReview_status().equals(1)) {
                txt_partnerName.setText(campaignList.getUserFundspot().getTitle());
            } else {
                txt_partnerName.setText(campaignList.getUserFundspot().getTitle());
            }
        }

        if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {

            txt_partnerName.setText("Fundspot : " + campaignList.getUserFundspot().getTitle());


        }

        edt_name.setText(user.getTitle());
        edt_email.setText(user.getEmail_id());


        btn_placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int allPrice = 0;

                int checkedPaymentType = radioGroup_paymentType.getCheckedRadioButtonId();

                if (checkedPaymentType == R.id.radio_cardPayment) {

                    checkedPaymentType = 2;
                }
                if (checkedPaymentType == R.id.radio_cashPayment) {

                    checkedPaymentType = 1;
                }


                final List<MultipleProductResponse> getSelectedProducts = productAdapter.getProducts();

                for (int i = 0; i < getSelectedProducts.size(); i++) {
                    String name = getSelectedProducts.get(i).getName();
                    String price = getSelectedProducts.get(i).getPrice();
                    String totalPrice = getSelectedProducts.get(i).getTotal_price();
                    String quantity = String.valueOf(getSelectedProducts.get(i).getQty());
                    String id = getSelectedProducts.get(i).getProduct_id();


                    float totalProductsPrice = Float.parseFloat(totalPrice);

                    allPrice += totalProductsPrice;

                    Log.e("totalPrice", "" + allPrice);


                    try {
                        mainObject.put("product_id", id);
                        mainObject.put("name", name);
                        mainObject.put("quantity", quantity);
                        mainObject.put("selling_price", price);
                        mainObject.put("item_total", totalPrice);

                        selectedProductArray.put(mainObject);


                        Log.e("selectedProducts", "---->" + selectedProductArray);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                if (getSelectedProducts.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please Select Product");

                } else {
                    if (checkedPaymentType == 1) {
                        if(campaignList.getCampaign().getRole_id().equalsIgnoreCase(C.ORGANIZATION)){
                            organizationId = campaignList.getUserOrganization().getId();
                            fundspotId = campaignList.getUserFundspot().getId();
                        }

                        if(campaignList.getCampaign().getRole_id().equalsIgnoreCase(C.FUNDSPOT)){
                            fundspotId = campaignList.getUserOrganization().getId();
                            organizationId = campaignList.getUserFundspot().getId();
                        }
                        dialog.show();
                        final Call<AppModel> addOrder = adminAPI.AddOrder(preference.getUserID(), preference.getUserRoleID(), preference.getTokenHash(), campaignList.getCampaign().getId(), user.getFirst_name(), user.getLast_name(), user.getEmail_id(), member.getContact_info(), member.getLocation(), member.getCity().getName(), member.getZip_code(), member.getState().getName(), String.valueOf(checkedPaymentType), String.valueOf(allPrice), "0", "0.0", "0.0", organizationId, fundspotId, selectedProductArray.toString());

                        addOrder.enqueue(new Callback<AppModel>() {
                            @Override
                            public void onResponse(Call<AppModel> call, Response<AppModel> response) {

                                dialog.dismiss();
                                AppModel appModel = response.body();
                                if (addOrder != null) {
                                    if (appModel.isStatus()) {
                                        C.INSTANCE.showToast(getApplicationContext(), appModel.getMessage());

                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        startActivity(intent);

                                    }

                                } else {

                                    C.INSTANCE.defaultError(getApplicationContext());
                                }

                            }

                            @Override
                            public void onFailure(Call<AppModel> call, Throwable t) {

                                C.INSTANCE.errorToast(getApplicationContext(), t);

                            }
                        });

                    }
                    if (checkedPaymentType == 2) {

                        Intent intent = new Intent(getApplicationContext() , CardPaymentActivity.class);
                        intent.putExtra("details" , campaignList);
                        intent.putExtra("productArray" , selectedProductArray.toString());
                        intent.putExtra("allPrice" , String.valueOf(allPrice));
                        Log.e("productArray" , selectedProductArray.toString());

                        startActivity(intent);
                    }
                }


            }


        });


    }
}
