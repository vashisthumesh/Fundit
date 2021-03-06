package com.fundit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.alihafizji.library.CreditCardEditText;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.J;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CreditCardPattern;
import com.fundit.helper.CustomDialog;
import com.fundit.model.AppModel;
import com.fundit.model.BankCardResponse;
import com.fundit.model.CompleteOrderModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardActivity extends AppCompatActivity {

    String firstName="";
    String lastName="";
    String email="";
    String campaign_id="";
    String mobile="";
    String organization_id="";
    String fundspot_id="";
    String total="";
    String payment_city="";
    String payment_postcode="";
    String payment_state="";
    String payment_address_1="";
    String selectedProductArray="";
    String auth_cust_paymnet_profile_id="";
    String customerProfileId="";
    String card_Id="";
    String cvv="";
    String save_card="";
    String on_behalf_of="";
    String order_request="";
    String other_user="";
    String is_card_save="";
    String payment_method="";
    String organization_name="";
    String combineName = "";
    String orderId = "";
    String actionflag = "";
    String selectedFundsUserId="";
    boolean newsFeedTimes = false;
    boolean isotherTimes = false;
    boolean isCouponTimes = false ;

    CustomDialog dialog;

    Spinner spn_savedcard;
    ArrayAdapter savedCardAdapter;
    TextView txt_othercard;
    EditText edt_cvv;
    AdminAPI adminAPI;
    AppPreference preference;
    ArrayList<String> cardName = new ArrayList<>();
    ArrayList<String> cardId = new ArrayList<>();
    List<BankCardResponse.BankCardResponseData> bankCard = new ArrayList<>();

    Button btn_continue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
            Intent i=getIntent();

        isCouponTimes = i.getBooleanExtra("isCouponTimes" , false);
        if(isCouponTimes==true){
            orderId = i.getStringExtra("orderId");
        }else {
            selectedProductArray= i.getStringExtra("selectedProductArray");
            firstName= i.getStringExtra("firstname");
            lastName= i.getStringExtra("lastname");
            email= i.getStringExtra("email");
            campaign_id= i.getStringExtra("campaign_id");
            mobile=  i.getStringExtra("mobile");
            organization_id=  i.getStringExtra("organization_id");
            fundspot_id=  i.getStringExtra("fundspot_id");
            total=   i.getStringExtra("total");
            payment_city=i.getStringExtra("payment_city");
            payment_postcode=i.getStringExtra("payment_postcode");
            payment_state=i.getStringExtra("payment_state");
            payment_address_1=i.getStringExtra("payment_address_1");
            payment_method= i.getStringExtra("payment_method");
            save_card=i.getStringExtra("save_card");
            on_behalf_of= i.getStringExtra("on_behalf_of");
            order_request= i.getStringExtra("order_request");
            other_user=i.getStringExtra("other_user");
            is_card_save=i.getStringExtra("is_card_save");
            combineName = i.getStringExtra("name");
            organization_name=i.getStringExtra("organization_name");
            newsFeedTimes=i.getBooleanExtra("newsFeedTimes" , false);
            isotherTimes=i.getBooleanExtra("isOtherTimes" , false);
            actionflag = i.getStringExtra("actionflag");
            selectedFundsUserId = i.getStringExtra("selectedUserID");
        }



        Log.e("isother" ,"-->" +  isotherTimes);
        dialog = new CustomDialog(CardActivity.this);

        adminAPI = ServiceGenerator.getAPIClass();
        preference = new AppPreference(getApplicationContext());

        fetchIds();
        setupToolbar();
    }

    public void fetchIds()
    {

        txt_othercard= (TextView) findViewById(R.id.txt_othercard);
        spn_savedcard = (Spinner) findViewById(R.id.spn_savedcard);

        edt_cvv = (EditText) findViewById(R.id.edt_cvv);
        btn_continue= (Button) findViewById(R.id.btn_continue);

        savedCardAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_textview, cardName);
        spn_savedcard.setAdapter(savedCardAdapter);


        final Call<BankCardResponse> bankCardResponse = adminAPI.BankCard(preference.getUserID(), preference.getTokenHash());
        Log.e("parameters", "-->" + preference.getUserID() + "-->" + preference.getTokenHash());
        bankCardResponse.enqueue(new Callback<BankCardResponse>() {
            @Override
            public void onResponse(Call<BankCardResponse> call, Response<BankCardResponse> response) {
                // dialog.dismiss();
//                cardName.add("Select Card");
//                cardId.add("0");

                BankCardResponse cardResponse = response.body();

                Log.e("getData", "-->" + new Gson().toJson(cardResponse));

                if (cardResponse != null) {
                    if (cardResponse.isStatus()) {
                        for (int i = 0; i < cardResponse.getData().size(); i++) {

                            cardName.add(cardResponse.getData().get(i).getBcard_type().toString() +""+ cardResponse.getData().get(i).getBcard_number());
                        //    cardId.add(cardResponse.getData().get(i).getId().toString());

                         //   Log.e("cardId", "" + cardId.get(i));

                            bankCard.addAll(cardResponse.getData());

                        }
                    } else {
                        C.INSTANCE.showToast(getApplicationContext(), cardResponse.getMessage());
                    }
                } else {
                    C.INSTANCE.defaultError(getApplicationContext());
                }
                savedCardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<BankCardResponse> call, Throwable t) {
                // dialog.dismiss();
                C.INSTANCE.errorToast(getApplicationContext(), t);
            }
        });

        spn_savedcard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                      auth_cust_paymnet_profile_id=bankCard.get(position).getAuth_cust_paymnet_profile_id();
                      customerProfileId=bankCard.get(position ).getCustomerProfileId();
                      card_Id=bankCard.get(position).getCard_id();




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        txt_othercard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),CreateCardActivity.class);
                if(isCouponTimes==true){
                    i.putExtra("isCouponTimes" , true);
                    i.putExtra("orderId" , orderId);
                }else {
                    i.putExtra("selectedProductArray",selectedProductArray.toString());
                    i.putExtra("firstname",firstName);
                    i.putExtra("lastname",lastName);
                    i.putExtra("email",email);
                    i.putExtra("campaign_id",campaign_id);
                    i.putExtra("mobile",mobile);
                    i.putExtra("payment_address_1",payment_address_1);
                    i.putExtra("organization_id",organization_id);
                    i.putExtra("fundspot_id",fundspot_id);
                    i.putExtra("total",total);
                    i.putExtra("payment_city",payment_city);
                    i.putExtra("payment_postcode",payment_postcode);
                    i.putExtra("payment_state",payment_state);
                    i.putExtra("payment_method",payment_method);
                    i.putExtra("save_card",save_card);
                    i.putExtra("on_behalf_of",on_behalf_of);
                    i.putExtra("order_request",order_request);
                    i.putExtra("other_user",other_user);
                    i.putExtra("is_card_save","0");
                    i.putExtra("organization_name",organization_name);
                    i.putExtra("newsFeedTimes" , newsFeedTimes);
                    i.putExtra("isOtherTimes" , isotherTimes);
                    i.putExtra("email",email);
                    i.putExtra("name" , combineName);
                    i.putExtra("actionflag","true");
                    i.putExtra("newsFeedTimes" , newsFeedTimes);
                    i.putExtra("selectedUserID" , selectedFundsUserId);

                    Log.e("isother" ,"-->" +  isotherTimes);

                }
                startActivity(i);
            }
        });


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cvv = edt_cvv.getText().toString();

                if (cvv.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please Enter CVV Number");
                } else {
                    dialog.show();

                    if (isCouponTimes) {

                        Call<CompleteOrderModel> couponRequest = adminAPI.CouponAccpet(orderId , preference.getUserID() , "1" , card_Id , cvv , auth_cust_paymnet_profile_id , customerProfileId , is_card_save , is_card_save);
                        couponRequest.enqueue(new Callback<CompleteOrderModel>() {
                            @Override
                            public void onResponse(Call<CompleteOrderModel> call, Response<CompleteOrderModel> response) {

                                CompleteOrderModel appModel = response.body();
                                if(appModel!=null){
                                    if(appModel.isStatus()){
                                        Intent i = new Intent(getApplicationContext(), Thankyou.class);
                                        i.putExtra("isCouponTimes", isCouponTimes);
                                        i.putExtra("campaignName", appModel.getData().getCampaign_name());
                                        i.putExtra("name", appModel.getData().getCustomer_name());
                                        i.putExtra("expiryDate", appModel.getData().getExpiry_date());
                                        i.putExtra("fundspot", appModel.getData().getFundspot_name());
                                        i.putExtra("org", appModel.getData().getOrganization_name());
                                        i.putExtra("total", appModel.getData().getTotal());
                                        startActivity(i);





                                    }else {
                                        C.INSTANCE.showToast(getApplicationContext() , appModel.getMessage());
                                    }
                                }else {
                                    C.INSTANCE.defaultError(getApplicationContext());
                                }


                            }

                            @Override
                            public void onFailure(Call<CompleteOrderModel> call, Throwable t) {

                            }
                        });






                    } else {

                        String sendUserId = "";
                        if(selectedFundsUserId.isEmpty()){
                            sendUserId=preference.getUserID();
                        }else {
                            sendUserId = selectedFundsUserId;
                        }

                        Call<CompleteOrderModel> addOrder = null;

                        addOrder = adminAPI.CompleteOrder(sendUserId, preference.getUserRoleID(), preference.getTokenHash(), campaign_id, firstName, lastName, email, mobile, payment_address_1, payment_city, payment_postcode, payment_state, payment_method, total, preference.getUserID(), "", "", organization_id, fundspot_id, selectedProductArray, auth_cust_paymnet_profile_id, customerProfileId, cvv, card_Id, save_card, on_behalf_of, order_request, other_user, is_card_save);


                        Log.e("userid", "--->" + preference.getUserID());
                        Log.e("roleid", "--->" + preference.getUserRoleID());
                        Log.e("token", "--->" + preference.getTokenHash());
                        Log.e("campaign_id", "-->" + campaign_id);
                        Log.e("firstName", "--->" + firstName);
                        Log.e("lastName", "--->" + lastName);
                        Log.e("email", "--->" + email);
                        Log.e("mobile", "-->" + mobile);
                        Log.e("payment_address_1", "--->" + payment_address_1);
                        Log.e("payment_city", "--->" + payment_city);
                        Log.e("payment_postcode", "--->" + payment_postcode);
                        Log.e("payment_state", "-->" + payment_state);
                        Log.e("payment_method", "--->" + payment_method);
                        Log.e("total", "--->" + total);
                        Log.e("organization_id", "--->" + organization_id);
                        Log.e("fundspot_id", "-->" + fundspot_id);
                        Log.e("selectedProductArray", "-->" + selectedProductArray);
                        Log.e("card_Id","--->"+card_Id);
                        Log.e("auth_cust_paymnet", "-->" + auth_cust_paymnet_profile_id);


                        Log.e("customerProfileId", "-->" + customerProfileId);


                        addOrder.enqueue(new Callback<CompleteOrderModel>() {
                            @Override
                            public void onResponse(Call<CompleteOrderModel> call, Response<CompleteOrderModel> response) {
                                dialog.dismiss();
                                CompleteOrderModel appModel = response.body();
                                if (appModel != null) {
                                    if (appModel.isStatus()) {


                                        Intent i = new Intent(getApplicationContext(), Thankyou.class);
                                        i.putExtra("campaignName", appModel.getData().getCampaign_name());
                                        i.putExtra("name", appModel.getData().getCustomer_name());
                                        i.putExtra("expiryDate", appModel.getData().getExpiry_date());
                                        i.putExtra("fundspot", appModel.getData().getFundspot_name());
                                        i.putExtra("org", appModel.getData().getOrganization_name());
                                        i.putExtra("total", appModel.getData().getTotal());
                                        i.putExtra("newsFeedTimes", newsFeedTimes);
                                        i.putExtra("isOtherTimes", isotherTimes);
                                        i.putExtra("email", email);
                                        i.putExtra("name", combineName);

                                        Log.e("isother", "-->" + isotherTimes);

                                        // i.putExtra("org",organization_name);
                                        startActivity(i);

                                    }

                                } else {

                                    C.INSTANCE.defaultError(getApplicationContext());
                                }

                            }

                            @Override
                            public void onFailure(Call<CompleteOrderModel> call, Throwable t) {
                                dialog.dismiss();
                                C.INSTANCE.errorToast(getApplicationContext(), t);

                            }
                        });
                        Log.e("fundspot_id", "-->" + fundspot_id);
                    }
                }
            }
        });




    }


    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionTitle.setText("");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.gc();
        J.GetNotificationCountGlobal(preference.getUserID() , preference.getTokenHash() , preference , getApplicationContext() , this);
    }



}
