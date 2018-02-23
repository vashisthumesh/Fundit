package com.fundit;

import android.content.Intent;
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

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.adapter.OrderProductTimeAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.AdjustableListView;
import com.fundit.helper.CustomDialog;
import com.fundit.model.Address;
import com.fundit.model.BankCardResponse;
import com.fundit.model.GetSearchPeople;
import com.fundit.model.Member;
import com.fundit.model.News_model;
import com.fundit.model.NotificationCampaignModel;
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

public class NotificationTimesOrderActivity extends AppCompatActivity /*implements OrderProductTimeAdapter.OnClick*/ {

    AppPreference preference;
    AdminAPI adminAPI;
    public static TabLayout tabLayout;
    LinearLayout tab_layout, confirm_layout, fundraiser;
    ImageView serch_user;
    String Id = "";
    String flag = "false";
    int allPrice = 0;
    GetSearchPeople.People people;


    TextView txt_fundraiser, txt_partnerTitle, txt_partnerName, txt_targetAmt, txt_address;

    EditText edt_name, edt_email, edt_confirm_email;

    AdjustableListView list_products;

    RadioGroup radioGroup_paymentType;

    RadioButton radio_cardPayment ,  radio_cashPayment;

    Button btn_placeOrder;

    // CampaignListResponse.CampaignList campaignList;
  //  NotificationCampaignModel.CampaignData newslist;
    List<News_model.Product> productList = new ArrayList<>();

    User user = new User();
    Member member = new Member();

    //  List<MultipleProductResponse> productList = new ArrayList<>();
    OrderProductTimeAdapter productAdapter;

    CustomDialog dialog;

    JSONArray selectedProductArray = new JSONArray();


    String organizationId = "";
    String fundspotId = "";
    String org_name = "";


    String selectedFundsUserId = "";
    String firstName = "";
    String lastName = "";
    String emailId = "";

    boolean isotherTimes = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_order_place);

        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");

       /* newslist = (NotificationCampaignModel.CampaignData) intent.getSerializableExtra("details");
        preference = new AppPreference(getApplicationContext());
        adminAPI = ServiceGenerator.getAPIClass();
        dialog = new CustomDialog(NotificationTimesOrderActivity.this);

        Id = intent.getStringExtra("id");


        try {

            user = new Gson().fromJson(preference.getUserData(), User.class);
            Log.e("user", "--->" + user);
            member = new Gson().fromJson(preference.getMemberData(), Member.class);


        } catch (Exception e) {

            Log.e("Exception", e.getMessage());
        }


        if (newslist.getNews_Campaign().getRole_id().equalsIgnoreCase("2")) {
            getAddress(newslist.getReceiveUser().getId());
        } else if (newslist.getNews_Campaign().getRole_id().equalsIgnoreCase("3")) {
            getAddress(newslist.getCreateUser().getId());
        }
        setupToolbar();
        fetchIDs();



*/


    }


  /*  private void setupToolbar() {
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
        txt_address = (TextView) findViewById(R.id.txt_address);

        fundraiser = (LinearLayout) findViewById(R.id.fundraiser);

        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_confirm_email = (EditText) findViewById(R.id.edt_confirm_email);

        list_products = (AdjustableListView) findViewById(R.id.list_products);

        radioGroup_paymentType = (RadioGroup) findViewById(R.id.radioGroup_paymentType);

        radio_cardPayment = (RadioButton) findViewById(R.id.radio_cardPayment);
        radio_cashPayment = (RadioButton) findViewById(R.id.radio_cashPayment);


        btn_placeOrder = (Button) findViewById(R.id.btn_placeOrder);

        tab_layout = (LinearLayout) findViewById(R.id.tab_layout);
        tabLayout = (TabLayout) findViewById(R.id.tabs_layout);
        confirm_layout = (LinearLayout) findViewById(R.id.confirm_layout);

        serch_user = (ImageView) findViewById(R.id.serch_user);


        edt_name.setEnabled(false);
        edt_email.setEnabled(false);
        edt_confirm_email.setEnabled(false);
        txt_targetAmt.setEnabled(false);

        *//*if (flag.equalsIgnoreCase("true")) {
            btn_placeOrder.setText("Continue");
            tabLayout.setVisibility(View.GONE);
            confirm_layout.setVisibility(View.GONE);
        } else {
            tabLayout.setVisibility(View.VISIBLE);
            confirm_layout.setVisibility(View.VISIBLE);
        }*//*

        if (newslist.getNews_Campaign().getTitle().equalsIgnoreCase("") || newslist.getNews_Campaign().getTitle() == null) {
            fundraiser.setVisibility(View.GONE);
        } else {
            fundraiser.setVisibility(View.VISIBLE);
            txt_fundraiser.setText("Fundraiser:" + newslist.getNews_Campaign().getTitle());
        }


        //  txt_targetAmt.setText(" $" +String.format("%.2f", Double.parseDouble( campaignList.getCampaign().getTarget_amount())));

        productAdapter = new OrderProductTimeAdapter(productList, getApplicationContext(), NotificationTimesOrderActivity.this);
        list_products.setAdapter(productAdapter);
        for (int i = 0; i < newslist.getCampaignProduct().size(); i++) {

            productList.add(newslist.getCampaignProduct().get(i));
            productAdapter.notifyDataSetChanged();

        }

        Log.e("testKeval", "--->test" + C.FUNDSPOT);
        Log.e("test", "test");

        txt_partnerTitle.setVisibility(View.VISIBLE);
        if (newslist.getNews_Campaign().getRole_id().equalsIgnoreCase("2")) {

            fundspotId = newslist.getReceiveUser().getId();
            organizationId = newslist.getCreateUser().getId();
            org_name = newslist.getCreateUser().getTitle();

            txt_partnerName.setText(newslist.getReceiveUser().getTitle());


        } else if (newslist.getNews_Campaign().getRole_id().equalsIgnoreCase("3")) {
            fundspotId = newslist.getCreateUser().getId();
            organizationId = newslist.getReceiveUser().getId();
            txt_partnerName.setText(newslist.getCreateUser().getTitle());
            org_name = newslist.getReceiveUser().getTitle();

        }


        if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
            if (member.getSeller().equalsIgnoreCase("1")) {
                tab_layout.setVisibility(View.VISIBLE);
                //   confirm_layout.setVisibility(View.VISIBLE);


                me_data();

                tabLayout.addTab(tabLayout.newTab().setText("Me"));
                tabLayout.addTab(tabLayout.newTab().setText("Fundit User"));
                tabLayout.addTab(tabLayout.newTab().setText("other"));


                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if (tab.getPosition() == 0) {
                            me_data();


                        } else if (tab.getPosition() == 1) {
                            fundit_user();

                        } else if (tab.getPosition() == 2) {
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
                txt_targetAmt.setText("$0.00");
            }


        }

        if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {


                tab_layout.setVisibility(View.VISIBLE);
                confirm_layout.setVisibility(View.VISIBLE);


                //me_data();

                // tabLayout.addTab(tabLayout.newTab().setText("Me"));
                tabLayout.addTab(tabLayout.newTab().setText("Fundit User"));
                tabLayout.addTab(tabLayout.newTab().setText("Other"));


                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if (tab.getPosition() == 0) {
                            fundit_user();


                        } else if (tab.getPosition() == 1) {
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


        }

        if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {

            if (member.getSeller().equalsIgnoreCase("1")) {
                tab_layout.setVisibility(View.VISIBLE);
                confirm_layout.setVisibility(View.VISIBLE);


                me_data();

                // tabLayout.addTab(tabLayout.newTab().setText("Me"));
                tabLayout.addTab(tabLayout.newTab().setText("Fundit User"));
                tabLayout.addTab(tabLayout.newTab().setText("other"));


                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if (tab.getPosition() == 0) {
                            fundit_user();


                        } else if (tab.getPosition() == 1) {
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
                serch_user.setVisibility(View.GONE);
                edt_name.setText(user.getTitle());
                edt_email.setText(user.getEmail_id());
                edt_confirm_email.setText(user.getEmail_id());
                txt_targetAmt.setText("$0.00");
            }

        }

        //edt_name.setText(user.getTitle());
        //edt_email.setText(user.getEmail_id());

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

                final List<News_model.Product> getSelectedProducts = productAdapter.getProducts();


                for (int i = 0; i < getSelectedProducts.size(); i++) {
                    String name = getSelectedProducts.get(i).getName();
                    String price = getSelectedProducts.get(i).getPrice();
                    String totalPrice = getSelectedProducts.get(i).getTotal_price();
                    String quantity = String.valueOf(getSelectedProducts.get(i).getQty());
                    String id = getSelectedProducts.get(i).getProduct_id();
                    String type_id = getSelectedProducts.get(i).getType_id();


                    float totalProductsPrice = Float.parseFloat(totalPrice);

                    allPrice += totalProductsPrice;

                    Log.e("totalPrice", "" + allPrice);


                    try {

                        JSONObject mainObject = new JSONObject();

                        mainObject.put("product_id", id);
                        mainObject.put("name", name);
                        mainObject.put("quantity", quantity);
                        mainObject.put("selling_price", price);
                        mainObject.put("type_id", type_id);
                        mainObject.put("item_total", totalPrice);

                        selectedProductArray.put(mainObject);


                        Log.e("selectedProducts", "---->" + selectedProductArray);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


                if (getSelectedProducts.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please Select Product");
                }else if(txt_targetAmt.getText().toString().trim().equalsIgnoreCase("$0.00")){
                    C.INSTANCE.showToast(getApplicationContext(), "Please Select minimum 1 quantity");
                } else {


                    dialog.show();
                    final Call<BankCardResponse> bankCardResponse = adminAPI.BankCard(preference.getUserID(), preference.getTokenHash());
                    Log.e("parameters", "-->" + preference.getUserID() + "-->" + preference.getTokenHash());
                    bankCardResponse.enqueue(new Callback<BankCardResponse>() {
                        @Override
                        public void onResponse(Call<BankCardResponse> call, Response<BankCardResponse> response) {
                            dialog.dismiss();
                            BankCardResponse cardResponse = response.body();

                            Log.e("getData", "-->" + new Gson().toJson(cardResponse));

                            if (cardResponse != null) {
                                if (cardResponse.isStatus()) {
                                    Intent i = new Intent(getApplicationContext(), CardActivity.class);
                                    i.putExtra("selectedProductArray", selectedProductArray.toString());
                                    i.putExtra("firstname", user.getFirst_name());
                                    i.putExtra("lastname", user.getLast_name());
                                    i.putExtra("email", user.getEmail_id());
                                    i.putExtra("campaign_id", newslist.getNews_Campaign().getId());
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
                                    i.putExtra("on_behalf_of", "0");
                                    i.putExtra("order_request", "0");
                                    i.putExtra("other_user", "0");
                                    i.putExtra("is_card_save", "1");
                                    i.putExtra("organization_name", org_name);
                                    i.putExtra("newsFeedTimes" , true);
                                    i.putExtra("actionflag","true");
                                    i.putExtra("selectedUserID", selectedFundsUserId);

                                    startActivity(i);
                                } else {

                                    Intent i = new Intent(getApplicationContext(), CreateCardActivity.class);
                                    i.putExtra("actionflag","true");
                                    i.putExtra("selectedProductArray", selectedProductArray.toString());
                                    i.putExtra("firstname", user.getFirst_name());
                                    i.putExtra("lastname", user.getLast_name());
                                    i.putExtra("email", user.getEmail_id());
                                    i.putExtra("campaign_id", newslist.getNews_Campaign().getId());
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
                                    i.putExtra("on_behalf_of", "0");
                                    i.putExtra("order_request", "0");
                                    i.putExtra("other_user", "0");
                                    i.putExtra("is_card_save", "1");
                                    i.putExtra("organization_name", org_name);
                                    i.putExtra("newsFeedTimes" , true);
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
        edt_name.setText(user.getTitle());
        edt_email.setText(user.getEmail_id());
        edt_confirm_email.setText(user.getEmail_id());
        txt_targetAmt.setText("$0.00");


    }

    public void fundit_user() {

        edt_name.setEnabled(false);
        edt_email.setEnabled(false);
        edt_confirm_email.setEnabled(false);
        txt_targetAmt.setEnabled(false);
        serch_user.setVisibility(View.VISIBLE);
        edt_name.setText("");
        edt_email.setText("");
        edt_confirm_email.setText("");
        txt_targetAmt.setText("$0.00");


        serch_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NotificationTimesOrderActivity.this, Search_fundituserActivity.class);
                startActivityForResult(i, 1);
            }
        });


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 1) {
            if (data != null) {
                people = (GetSearchPeople.People) data.getSerializableExtra("id");

                edt_name.setText(people.getFirst_name() + "" + people.getLast_name());
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
        edt_name.setEnabled(true);
        edt_email.setEnabled(true);
        edt_confirm_email.setEnabled(true);
        txt_targetAmt.setEnabled(false);
        serch_user.setVisibility(View.GONE);
    }


    public void getAddress(String fundspot_id) {

        dialog.show();

        Call<Address> addressCall = null;

        addressCall = adminAPI.GetAddress(fundspot_id);
        Log.e("userid", "--->" + fundspot_id);

        addressCall.enqueue(new Callback<Address>() {
            @Override
            public void onResponse(Call<Address> call, Response<Address> response) {
                dialog.dismiss();

                Address address_List = response.body();
                if (address_List != null) {
                    if (address_List.isStatus()) {

                        txt_address.setText(address_List.getData().getFundspot().getLocation()+ '\n' + address_List.getData().getFundspot().getCity_name() + "," + address_List.getData().getState().getState_code() + " " + address_List.getData().getFundspot().getZip_code());

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
    public void UpdateTotalPrice(float totalPrice) {
        Log.e("price", "-->" + totalPrice);
        txt_targetAmt.setText("$" + String.valueOf(totalPrice));
    }
*/

    @Override
    protected void onResume() {
        super.onResume();
        System.gc();
    }


}
