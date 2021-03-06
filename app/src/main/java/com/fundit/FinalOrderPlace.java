package com.fundit;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.W;
import com.fundit.adapter.GetAllProductsAdapter;
import com.fundit.adapter.OrderTimeProductAdapter;
import com.fundit.adapter.ProductListAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.apis.ServiceHandler;
import com.fundit.apis.StringConverterFactory;
import com.fundit.helper.AdjustableListView;
import com.fundit.helper.CustomDialog;
import com.fundit.model.Address;
import com.fundit.model.AppModel;
import com.fundit.model.BankCardResponse;
import com.fundit.model.CampaignListResponse;
import com.fundit.model.GetSearchPeople;
import com.fundit.model.Member;
import com.fundit.model.MultipleProductResponse;
import com.fundit.model.ProductListResponse;
import com.fundit.model.User;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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

public class FinalOrderPlace extends AppCompatActivity implements OrderTimeProductAdapter.OnClick {

    AppPreference preference;
    AdminAPI adminAPI;
    public static TabLayout tabLayout;
    LinearLayout tab_layout, confirm_layout, fundraiser;
    ImageView serch_user;
    String Id = "";

    GetSearchPeople.People people;


    TextView txt_fundraiser, txt_partnerTitle, txt_partnerName, txt_targetAmt, txt_address, txt_selectedname;

    EditText edt_name, edt_email, edt_confirm_email;

    AdjustableListView list_products;

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


    String organizationId = "";
    String fundspotId = "";


    String selectedFundsUserId = "";
    String firstName = "";
    String lastName = "";
    String emailId = "";
    String on_behalf_of = "0";

    boolean isotherTimes = false;
    boolean ischeckedTimes = false;
    boolean newsFeedTimes = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_order_place);


        Intent intent = getIntent();

        campaignList = (CampaignListResponse.CampaignList) intent.getSerializableExtra("details");



        preference = new AppPreference(getApplicationContext());
        adminAPI = ServiceGenerator.getAPIClass();
        dialog = new CustomDialog(FinalOrderPlace.this);

        Id = intent.getStringExtra("id");


        try {
            user = new Gson().fromJson(preference.getUserData(), User.class);

            member = new Gson().fromJson(preference.getMemberData(), Member.class);
        } catch (Exception e) {

        }

        if (campaignList.getCampaign().getRole_id().equalsIgnoreCase("2")) {
            getAddress(campaignList.getUserFundspot().getId());
        } else if (campaignList.getCampaign().getRole_id().equalsIgnoreCase("3")) {
            getAddress(campaignList.getUserOrganization().getId());
        }

        setupToolbar();
        fetchIDs();


    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        actionTitle.setText("Place Order");
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
        txt_address = (TextView) findViewById(R.id.txt_address);
        //   txt_selectedname = (TextView) findViewById(R.id.txt_selectedname);

        fundraiser = (LinearLayout) findViewById(R.id.fundraiser);

        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_confirm_email = (EditText) findViewById(R.id.edt_confirm_email);

        list_products = (AdjustableListView) findViewById(R.id.list_products);

        radioGroup_paymentType = (RadioGroup) findViewById(R.id.radioGroup_paymentType);

        radio_cardPayment = (RadioButton) findViewById(R.id.radio_cardPayment);
        radio_cashPayment = (RadioButton) findViewById(R.id.radio_cashPayment);

        btn_placeOrder = (Button) findViewById(R.id.btn_placeOrder);
        btn_placeOrder.setText("Continue");

        tab_layout = (LinearLayout) findViewById(R.id.tab_layout);
        tabLayout = (TabLayout) findViewById(R.id.tabs_layout);
        confirm_layout = (LinearLayout) findViewById(R.id.confirm_layout);

        serch_user = (ImageView) findViewById(R.id.serch_user);


        edt_name.setEnabled(false);
        edt_email.setEnabled(false);
        edt_confirm_email.setEnabled(false);
        txt_targetAmt.setEnabled(false);


        if (campaignList.getCampaign().getTitle().equalsIgnoreCase("") || campaignList.getCampaign().getTitle() == null) {
            fundraiser.setVisibility(View.GONE);
        } else {
            fundraiser.setVisibility(View.VISIBLE);
            txt_fundraiser.setText("Fundraiser: " + campaignList.getCampaign().getTitle());
        }


        //  txt_targetAmt.setText(" $" +String.format("%.2f", Double.parseDouble( campaignList.getCampaign().getTarget_amount())));

        productAdapter = new OrderTimeProductAdapter(productList, getApplicationContext(), FinalOrderPlace.this);
        list_products.setAdapter(productAdapter);
        for (int i = 0; i < campaignList.getCampaignProduct().size(); i++) {

            productList.add(campaignList.getCampaignProduct().get(i));
            productAdapter.notifyDataSetChanged();

        }



        /*if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
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
*/

        txt_partnerTitle.setText("Fundspot:");
       // txt_partnerTitle.setVisibility(View.VISIBLE);
        if (campaignList.getCampaign().getRole_id().equalsIgnoreCase("2")) {
            txt_partnerName.setText("Fundspot: " + campaignList.getUserFundspot().getTitle());
        } else if (campaignList.getCampaign().getRole_id().equalsIgnoreCase("3")) {
            txt_partnerName.setText("Fundspot: " + campaignList.getUserOrganization().getTitle());
        }


        if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
            if (member.getSeller().equalsIgnoreCase("1") || preference.getSeller() == 1) {
                tab_layout.setVisibility(View.VISIBLE);
                confirm_layout.setVisibility(View.VISIBLE);
                me_data();
                newsFeedTimes = true;


                tabLayout.addTab(tabLayout.newTab().setText("Me"));
                tabLayout.addTab(tabLayout.newTab().setText("Fundit User"));
                tabLayout.addTab(tabLayout.newTab().setText("Other"));


                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if (tab.getPosition() == 0) {
                            newsFeedTimes = true;
                            selectedFundsUserId = "";
                            on_behalf_of = "0";
                            me_data();


                        } else if (tab.getPosition() == 1) {
                            newsFeedTimes = false;
                            on_behalf_of = "1";
                            fundit_user();

                        } else if (tab.getPosition() == 2) {
                            newsFeedTimes = false;
                            selectedFundsUserId = "";
                            on_behalf_of = "0";
                            other_user();

                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });


            } else {
                tab_layout.setVisibility(View.GONE);
                confirm_layout.setVisibility(View.GONE);
                radio_cashPayment.setVisibility(View.GONE);
                serch_user.setVisibility(View.GONE);
                edt_name.setText(user.getTitle());
                edt_email.setText(user.getEmail_id());
                edt_confirm_email.setText(user.getEmail_id());
                //  txt_targetAmt.setText("$0.00");
            }


        }

        if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT) || preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {

            // if(member.getSeller().equalsIgnoreCase("1"))
            // {
            tab_layout.setVisibility(View.VISIBLE);
            confirm_layout.setVisibility(View.VISIBLE);


            //  me_data();
            serch_user.setVisibility(View.VISIBLE);
            on_behalf_of = "1";
            // tabLayout.addTab(tabLayout.newTab().setText("Me"));
            tabLayout.addTab(tabLayout.newTab().setText("Fundit User"));
            tabLayout.addTab(tabLayout.newTab().setText("Other"));


            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getPosition() == 0) {
                        fundit_user();
                        on_behalf_of = "1";
                    } else if (tab.getPosition() == 1) {
                        selectedFundsUserId = "";
                        on_behalf_of = "0";
                        other_user();
                    }

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            // }
//            else {
//                tab_layout.setVisibility(View.GONE);
//                confirm_layout.setVisibility(View.GONE);
//                radio_cashPayment.setVisibility(View.GONE);
//                serch_user.setVisibility(View.GONE);
//                edt_name.setText(user.getTitle());
//                edt_email.setText(user.getEmail_id());
//                edt_confirm_email.setText(user.getEmail_id());
//                txt_targetAmt.setText("$0.00");
//            }

        }


        //edt_name.setText(user.getTitle());
        //edt_email.setText(user.getEmail_id());


        serch_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FinalOrderPlace.this, Search_fundituserActivity.class);
                startActivityForResult(i, 1);
            }
        });

        radioGroup_paymentType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (radio_cardPayment.isChecked()) {
                    radio_cashPayment.setChecked(false);
                }
                if (radio_cashPayment.isChecked()) {
                    radio_cardPayment.setChecked(false);
                }
            }
        });


        btn_placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float allPrice = 0;
                final String combineName = edt_name.getText().toString().trim();

                String finalAmounts = "";
                finalAmounts = txt_targetAmt.getText().toString().trim();


                if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT) || preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {

                    if (tabLayout.getSelectedTabPosition() == 1) {
                        firstName = edt_name.getText().toString().trim();
                        lastName = "";
                        emailId = edt_email.getText().toString().trim();
                        isotherTimes = true;

                    } else {
                        firstName = edt_name.getText().toString().trim();
                        lastName = "";
                        emailId = edt_email.getText().toString().trim();


                    }


                } else {

                    if (tabLayout.getSelectedTabPosition() == 2) {

                        firstName = edt_name.getText().toString().trim();
                        lastName = "";
                        emailId = edt_email.getText().toString().trim();
                        isotherTimes = true;


                    }else {


                        firstName = edt_name.getText().toString().trim();
                        lastName = "";
                        emailId = edt_email.getText().toString().trim();


                    }
                }



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
                    String type_id = getSelectedProducts.get(i).getType_id();


                    float totalProductsPrice = Float.parseFloat(totalPrice);

                    allPrice += totalProductsPrice;




                    try {
                        JSONObject mainObject = new JSONObject();

                        mainObject.put("product_id", id);
                        mainObject.put("name", name);
                        mainObject.put("quantity", quantity);
                        mainObject.put("selling_price", price);
                        mainObject.put("type_id", type_id);
                        mainObject.put("item_total", totalPrice);


                        selectedProductArray.put(mainObject);





                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


                if (getSelectedProducts.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select atleast 1 product");
                } else if (finalAmounts.equalsIgnoreCase("$0.00") || finalAmounts.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select atleast 1 product");
                } else if (edt_name.getText().toString().trim().isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter receipt name");
                } else if (edt_email.getText().toString().trim().isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter receipt email");
                } else if (!C.INSTANCE.validEmail(edt_email.getText().toString().trim())) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter valid receipt email");
                } else if (edt_confirm_email.getText().toString().trim().isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Email not match");
                } else if (!edt_email.getText().toString().trim().matches(edt_confirm_email.getText().toString().trim())) {
                    C.INSTANCE.showToast(getApplicationContext(), "Email not match");
                } else {
                    if (checkedPaymentType == 1) {
                        if (campaignList.getCampaign().getRole_id().equalsIgnoreCase(C.ORGANIZATION)) {
                            organizationId = campaignList.getUserOrganization().getId();
                            fundspotId = campaignList.getUserFundspot().getId();
                        }

                        if (campaignList.getCampaign().getRole_id().equalsIgnoreCase(C.FUNDSPOT)) {
                            fundspotId = campaignList.getUserOrganization().getId();
                            organizationId = campaignList.getUserFundspot().getId();
                        }
                        dialog.show();
                        final Call<AppModel> addOrder = adminAPI.AddOrder(campaignList.getCampaign().getId(), selectedFundsUserId, preference.getTokenHash(), "4", firstName, lastName, emailId, member.getContact_info(), member.getLocation(), /*member.getCity().getName()*/ member.getCity_name(), member.getZip_code(), member.getState().getName(), String.valueOf(checkedPaymentType), String.valueOf(allPrice), preference.getUserID(), "0.0", "0.0", organizationId, fundspotId, selectedProductArray.toString(), "", "", "", "", "", "", "", "0", "1", "0");




















                        addOrder.enqueue(new Callback<AppModel>() {
                            @Override
                            public void onResponse(Call<AppModel> call, Response<AppModel> response) {

                                dialog.dismiss();
                                AppModel appModel = response.body();
                                if (addOrder != null) {
                                    if (appModel.isStatus()) {
                                        C.INSTANCE.showToast(getApplicationContext(), appModel.getMessage());

                                        Intent intent = new Intent(getApplicationContext(), Thankyou.class);
                                        intent.putExtra("org", campaignList.getUserOrganization().getTitle());
                                        intent.putExtra("fundspot", campaignList.getUserFundspot().getTitle());
                                        intent.putExtra("requestTimes", true);
                                        intent.putExtra("name", combineName);
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

                        if (campaignList.getCampaign().getRole_id().equalsIgnoreCase(C.ORGANIZATION)) {
                            organizationId = campaignList.getUserOrganization().getId();
                            fundspotId = campaignList.getUserFundspot().getId();
                        }

                        if (campaignList.getCampaign().getRole_id().equalsIgnoreCase(C.FUNDSPOT)) {
                            fundspotId = campaignList.getUserOrganization().getId();
                            organizationId = campaignList.getUserFundspot().getId();
                        }

                        if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {

                            if (tabLayout.getSelectedTabPosition() == 0) {
                                dialog.show();
                                final Call<BankCardResponse> bankCardResponse = adminAPI.BankCard(preference.getUserID(), preference.getTokenHash());

                                final float finalAllPrice = allPrice;
                                final int finalCheckedPaymentType = checkedPaymentType;
                                bankCardResponse.enqueue(new Callback<BankCardResponse>() {
                                    @Override
                                    public void onResponse(Call<BankCardResponse> call, Response<BankCardResponse> response) {
                                        dialog.dismiss();
                                        BankCardResponse cardResponse = response.body();



                                        if (cardResponse != null) {
                                            if (cardResponse.isStatus()) {
                                                Intent i = new Intent(getApplicationContext(), CardActivity.class);
                                                i.putExtra("selectedProductArray", selectedProductArray.toString());
                                                i.putExtra("firstname", firstName);
                                                i.putExtra("lastname", lastName);
                                                i.putExtra("email", emailId);
                                                i.putExtra("campaign_id", campaignList.getCampaign().getId());
                                                i.putExtra("mobile", member.getContact_info());
                                                i.putExtra("payment_address_1", member.getLocation());
                                                i.putExtra("organization_id", organizationId);
                                                i.putExtra("fundspot_id", fundspotId);
                                                i.putExtra("total", String.valueOf(finalAllPrice));
                                                i.putExtra("payment_city", member.getCity_name());
                                                i.putExtra("payment_postcode", member.getZip_code());
                                                i.putExtra("payment_state", member.getState().getName());
                                                i.putExtra("payment_method", "2");
                                                i.putExtra("save_card", "0");
                                                i.putExtra("on_behalf_of", on_behalf_of);
                                                i.putExtra("order_request", "0");
                                                i.putExtra("other_user", "0");
                                                i.putExtra("is_card_save", "1");
                                                i.putExtra("organization_name", "");
                                                i.putExtra("isOtherTimes", isotherTimes);
                                                i.putExtra("name", combineName);
                                                i.putExtra("newsFeedTimes", newsFeedTimes);
                                                i.putExtra("selectedUserID", selectedFundsUserId);

                                       i.putExtra("actionflag", "false");




                                                startActivity(i);
                                            } else {

                                                Intent i = new Intent(getApplicationContext(), CreateCardActivity.class);
                                                i.putExtra("actionflag", "true");
                                                i.putExtra("selectedProductArray", selectedProductArray.toString());
                                                i.putExtra("firstname", firstName);
                                                i.putExtra("lastname", lastName);
                                                i.putExtra("email", emailId);
                                                i.putExtra("campaign_id", campaignList.getCampaign().getId());
                                                i.putExtra("mobile", member.getContact_info());
                                                i.putExtra("payment_address_1", member.getLocation());
                                                i.putExtra("organization_id", organizationId);
                                                i.putExtra("fundspot_id", fundspotId);
                                                i.putExtra("total", String.valueOf(finalAllPrice));
                                                i.putExtra("payment_city", member.getCity_name());
                                                i.putExtra("payment_postcode", member.getZip_code());
                                                i.putExtra("payment_state", member.getState().getName());
                                                i.putExtra("payment_method", "2");
                                                i.putExtra("save_card", "0");
                                                i.putExtra("on_behalf_of", on_behalf_of);
                                                i.putExtra("order_request", "0");
                                                i.putExtra("other_user", "0");
                                                i.putExtra("is_card_save", "1");
                                                i.putExtra("organization_name", "");
                                                i.putExtra("isOtherTimes", isotherTimes);
                                                i.putExtra("name", combineName);
                                                i.putExtra("newsFeedTimes", newsFeedTimes);
                                                i.putExtra("selectedUserID", selectedFundsUserId);
















                                                startActivity(i);

                                            }
                                        } else {
                                            C.INSTANCE.defaultError(getApplicationContext());
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<BankCardResponse> call, Throwable t) {

                                        dialog.dismiss();
                                        C.INSTANCE.errorToast(getApplicationContext(), t);
                                    }
                                });

                            } else {

                                if (campaignList.getCampaign().getRole_id().equalsIgnoreCase(C.ORGANIZATION)) {
                                    organizationId = campaignList.getUserOrganization().getId();
                                    fundspotId = campaignList.getUserFundspot().getId();
                                }

                                if (campaignList.getCampaign().getRole_id().equalsIgnoreCase(C.FUNDSPOT)) {
                                    fundspotId = campaignList.getUserOrganization().getId();
                                    organizationId = campaignList.getUserFundspot().getId();
                                }


                                Intent i = new Intent(getApplicationContext(), CreateCardActivity.class);
                                i.putExtra("actionflag", "true");
                                i.putExtra("selectedProductArray", selectedProductArray.toString());
                                i.putExtra("firstname", firstName);
                                i.putExtra("lastname", lastName);
                                i.putExtra("email", emailId);
                                i.putExtra("campaign_id", campaignList.getCampaign().getId());
                                i.putExtra("mobile", member.getContact_info());
                                i.putExtra("payment_address_1", member.getLocation());
                                i.putExtra("organization_id", organizationId);
                                i.putExtra("fundspot_id", fundspotId);
                                i.putExtra("total", String.valueOf(allPrice));
                                i.putExtra("payment_city", member.getCity_name());
                                i.putExtra("payment_postcode", member.getZip_code());
                                i.putExtra("payment_state", member.getState().getName());
                                i.putExtra("payment_method", "2");
                                i.putExtra("save_card", "0");
                                i.putExtra("on_behalf_of", on_behalf_of);
                                i.putExtra("order_request", "0");
                                i.putExtra("other_user", "0");
                                i.putExtra("is_card_save", "1");
                                i.putExtra("organization_name", "");
                                i.putExtra("isSaveCard", true);
                                i.putExtra("isOtherTimes", isotherTimes);
                                i.putExtra("name", combineName);
                                i.putExtra("newsFeedTimes", newsFeedTimes);
                                i.putExtra("selectedUserID", selectedFundsUserId);
















                                startActivity(i);


                            }

                        } else {

                            if (campaignList.getCampaign().getRole_id().equalsIgnoreCase(C.ORGANIZATION)) {
                                organizationId = campaignList.getUserOrganization().getId();
                                fundspotId = campaignList.getUserFundspot().getId();
                            }

                            if (campaignList.getCampaign().getRole_id().equalsIgnoreCase(C.FUNDSPOT)) {
                                fundspotId = campaignList.getUserOrganization().getId();
                                organizationId = campaignList.getUserFundspot().getId();
                            }


                            Intent i = new Intent(getApplicationContext(), CreateCardActivity.class);
                            i.putExtra("actionflag", "true");
                            i.putExtra("selectedProductArray", selectedProductArray.toString());
                            i.putExtra("firstname", firstName);
                            i.putExtra("lastname", lastName);
                            i.putExtra("email", emailId);
                            i.putExtra("campaign_id", campaignList.getCampaign().getId());
                            i.putExtra("mobile", member.getContact_info());
                            i.putExtra("payment_address_1", member.getLocation());
                            i.putExtra("organization_id", organizationId);
                            i.putExtra("fundspot_id", fundspotId);
                            i.putExtra("total", String.valueOf(allPrice));
                            i.putExtra("payment_city", member.getCity_name());
                            i.putExtra("payment_postcode", member.getZip_code());
                            i.putExtra("payment_state", member.getState().getName());
                            i.putExtra("payment_method", "2");
                            i.putExtra("save_card", "0");
                            i.putExtra("on_behalf_of", on_behalf_of);
                            i.putExtra("order_request", "0");
                            i.putExtra("other_user", "0");
                            i.putExtra("is_card_save", "1");
                            i.putExtra("organization_name", "");
                            i.putExtra("isSaveCard", true);
                            i.putExtra("isOtherTimes", isotherTimes);
                            i.putExtra("name", combineName);
                            i.putExtra("newsFeedTimes", newsFeedTimes);
                            i.putExtra("selectedUserID", selectedFundsUserId);
















                            startActivity(i);


                        }


                    }
                }


            }


        });


    }


    public void me_data() {
        edt_name.setEnabled(false);
        edt_email.setEnabled(false);
        edt_confirm_email.setEnabled(false);
        txt_targetAmt.setEnabled(false);
        serch_user.setVisibility(View.GONE);
        radio_cashPayment.setVisibility(View.GONE);
        edt_name.setText(user.getTitle());
        edt_email.setText(user.getEmail_id());
        edt_confirm_email.setText(user.getEmail_id());
        //  txt_targetAmt.setText("$0.00");


    }

    public void fundit_user() {

        edt_name.setEnabled(false);
        edt_email.setEnabled(false);
        edt_confirm_email.setEnabled(false);
        txt_targetAmt.setEnabled(false);
        radio_cashPayment.setVisibility(View.VISIBLE);
        radio_cashPayment.setText("Send Request To");
        serch_user.setVisibility(View.VISIBLE);
        edt_name.setText("");
        edt_email.setText("");
        edt_confirm_email.setText("");
        //  txt_targetAmt.setText("$0.00");

//        serch_user.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(FinalOrderPlace.this, Search_fundituserActivity.class);
//                startActivityForResult(i, 1);
//            }
//        });


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 1) {
            if (data != null) {
                people = (GetSearchPeople.People) data.getSerializableExtra("id");
                //  txt_selectedname.setVisibility(View.VISIBLE);
                edt_name.setText(people.getFirst_name() + " " + people.getLast_name());
                //  txt_selectedname.setText("[" + people.getFirst_name() + "" + people.getLast_name() + "]");
                radio_cashPayment.setText("Send Request To " + "[" + people.getFirst_name() + " " + people.getLast_name() + "]");
                edt_email.setText(people.getEmail_id());
                edt_confirm_email.setText(people.getEmail_id());
                selectedFundsUserId = people.getUser_id();
                firstName = people.getFirst_name();
                lastName = people.getLast_name();
                emailId = people.getEmail_id();

            }
        }
    }

    public void other_user() {

        edt_name.setText("");
        edt_email.setText("");
        edt_confirm_email.setText("");

        edt_name.setEnabled(true);
        edt_email.setEnabled(true);
        edt_confirm_email.setEnabled(true);
        txt_targetAmt.setEnabled(false);
        radio_cashPayment.setVisibility(View.GONE);
        serch_user.setVisibility(View.GONE);
    }


    @Override
    public void UpdateTotalPrice(float totalPrice) {
        double allPrice = 0;
        for (int i = 0; i < productList.size(); i++) {
            String productstotalPrice = productList.get(i).getTotal_price();

            Float productWise = Float.parseFloat(productstotalPrice);

            allPrice += productWise;
        }



        txt_targetAmt.setText("$" + String.format("%.2f", allPrice));
    }

    public void getAddress(String fundspot_id) {

        dialog.show();

        Call<Address> addressCall = null;

        addressCall = adminAPI.GetAddress(fundspot_id);


        addressCall.enqueue(new Callback<Address>() {
            @Override
            public void onResponse(Call<Address> call, Response<Address> response) {
                dialog.dismiss();

                Address address_List = response.body();
                if (address_List != null) {
                    if (address_List.isStatus()) {

                        txt_address.setText(address_List.getData().getFundspot().getLocation() + '\n' + address_List.getData().getFundspot().getCity_name() + ", " + address_List.getData().getState().getState_code() + " " + address_List.getData().getFundspot().getZip_code());

                    } else {

                        C.INSTANCE.showToast(getApplicationContext(), address_List.getMessage());
                        // FOR_NOW_ITS_NOTHING
                    }


                }
            }

            @Override
            public void onFailure(Call<Address> call, Throwable t) {
                dialog.dismiss();
                C.INSTANCE.errorToast(getApplicationContext(), t);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        System.gc();
    }
}
