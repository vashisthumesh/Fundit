package com.fundit;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.alihafizji.library.CreditCardEditText;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.W;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.apis.ServiceHandler;
import com.fundit.helper.CreditCardPattern;
import com.fundit.helper.CustomDialog;
import com.fundit.model.AppModel;
import com.fundit.model.AreaItem;
import com.fundit.model.AreaResponse;
import com.fundit.model.CompleteOrderModel;
import com.fundit.model.Member;
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

public class CreateCardActivity extends AppCompatActivity {

    String firstName = "";
    String lastName = "";
    String email = "";
    String campaign_id = "";
    String mobile = "";
    String organization_id = "";
    String fundspot_id = "";
    String total = "";
    String payment_city = "";
    String payment_postcode = "";
    String payment_state = "";
    String payment_address_1 = "";
    String selectedProductArray = "";
    String auth_cust_paymnet_profile_id = "";
    String customerProfileId = "";
    String card_Id = "";
    String cvv = "";
    String save_card = "";
    String on_behalf_of = "";
    String order_request = "";
    String other_user = "";
    String is_card_save = "0";
    String payment_method = "";
    String organization_name = "";
    String combineName = "";
    String orderId = "";
    String selectedFundsUserId="";
    boolean SaveCard = false ;
    boolean newsFeedTimes = false ;
    boolean isotherTimes = false;
    boolean isCouponTimes = false ;
    String actionflag="false";


    CreditCardEditText textview_credit_card;
    Button btn_continue;
    EditText edt_firstname, edt_lastname, edt_address, edt_city , edt_cvv_number;
    TextView title_text;
    Spinner spn_state;

    EditText edtZip;
    ArrayList<String> stateNames = new ArrayList<>();
    ArrayList<AreaItem> stateItems = new ArrayList<>();
    CreditCardPattern creditCardPattern;

    Spinner spn_month, spn_year;

    ArrayList<String> months = new ArrayList<>();
    ArrayList<String> year = new ArrayList<>();
    ArrayAdapter<String> stateAdapter;

    AppPreference preference;
    CustomDialog dialog;

    ArrayAdapter<String> monthAdapter;
    ArrayAdapter<String> yearAdapter;

    Member member = new Member();

    boolean editMode = false;
    String editedNumber = "";
    String editedmonth = "";
    String editedyear = "";
    String editedZipcode = "";
    String editedId = "";
    AdminAPI adminAPI;


    CheckBox chk_save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);

        Intent i = getIntent();

        isCouponTimes = i.getBooleanExtra("isCouponTimes" , false);


        if(isCouponTimes){
            orderId = i.getStringExtra("orderId");
        }else {

            selectedProductArray = i.getStringExtra("selectedProductArray");
            firstName = i.getStringExtra("firstname");
            lastName = i.getStringExtra("lastname");
            email = i.getStringExtra("email");
            campaign_id = i.getStringExtra("campaign_id");
            mobile = i.getStringExtra("mobile");
            organization_id = i.getStringExtra("organization_id");
            fundspot_id = i.getStringExtra("fundspot_id");
            total = i.getStringExtra("total");
            payment_city = i.getStringExtra("payment_city");
            payment_postcode = i.getStringExtra("payment_postcode");
            payment_state = i.getStringExtra("payment_state");
            payment_address_1 = i.getStringExtra("payment_address_1");
            payment_method = i.getStringExtra("payment_method");
            save_card = i.getStringExtra("save_card");
            on_behalf_of = i.getStringExtra("on_behalf_of");
            order_request = i.getStringExtra("order_request");
            other_user = i.getStringExtra("other_user");
            is_card_save = i.getStringExtra("is_card_save");
            combineName = i.getStringExtra("name");
            organization_name = i.getStringExtra("organization_name");
            SaveCard = i.getBooleanExtra("isSaveCard", false);
            newsFeedTimes = i.getBooleanExtra("newsFeedTimes", false);
            isotherTimes = i.getBooleanExtra("isOtherTimes", false);
            actionflag=i.getStringExtra("actionflag");
            selectedFundsUserId = i.getStringExtra("selectedUserID");
            Log.e("isother4", "-->" + isotherTimes);
        }
        adminAPI = ServiceGenerator.getAPIClass();

        preference = new AppPreference(getApplicationContext());
        dialog = new CustomDialog(getApplicationContext());

        creditCardPattern = new CreditCardPattern(getApplicationContext());

        try {
            member = new Gson().fromJson(preference.getMemberData(), Member.class);
        } catch (Exception e) {
            e.printStackTrace();
        }


        setUpToolBar();
        fetchIds();


    }

    private void setUpToolBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if(actionflag.equalsIgnoreCase("true"))
        {
            actionTitle.setText("Place Order");
        }
        else {
            actionTitle.setText("Save Card");
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void fetchIds() {

        title_text= (TextView) findViewById(R.id.title_text);
        textview_credit_card = (CreditCardEditText) findViewById(R.id.textview_credit_card);
        textview_credit_card.setCreditCardEditTextListener(creditCardPattern);
        btn_continue = (Button) findViewById(R.id.btn_continue);

        spn_month = (Spinner) findViewById(R.id.spn_month);
        spn_year = (Spinner) findViewById(R.id.spn_year);
        spn_state = (Spinner) findViewById(R.id.sp_state);
        edt_firstname = (EditText) findViewById(R.id.edt_firstname);
        edt_lastname = (EditText) findViewById(R.id.edt_lastname);
        edt_city = (EditText) findViewById(R.id.edt_city);
        edt_address = (EditText) findViewById(R.id.edt_address);
        edt_cvv_number = (EditText) findViewById(R.id.edt_cvv_number);

        chk_save = (CheckBox) findViewById(R.id.chk_save);

        stateAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, stateNames);
        spn_state.setAdapter(stateAdapter);
        monthAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_textview, months);
        spn_month.setAdapter(monthAdapter);

        yearAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_textview, year);
        spn_year.setAdapter(yearAdapter);

        edtZip = (EditText) findViewById(R.id.edt_zip);
        edtZip.setText(member.getZip_code());



        if(SaveCard==true){
            chk_save.setVisibility(View.GONE);
        }
        if(actionflag.equalsIgnoreCase("true"))
        {
            title_text.setText("Payment");
        }


        if (editMode == true) {

            textview_credit_card.setText(editedNumber);
            edtZip.setText(editedZipcode);
        }

        spn_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {
                    // clearCities();
                } else {
                    // loadCities(stateItems.get(position).getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //  dialog.show();
        Call<AreaResponse> stateCall = adminAPI.getStateList("1");
        stateCall.enqueue(new Callback<AreaResponse>() {
            @Override
            public void onResponse(Call<AreaResponse> call, Response<AreaResponse> response) {
                //  dialog.dismiss();


                AreaResponse areaResponse = response.body();
                if (areaResponse != null) {
                    stateNames.clear();
                    stateItems.clear();
                    stateNames.add("Select State");
                    if (areaResponse.isStatus()) {
                        stateItems.addAll(areaResponse.getData());
                        stateNames.addAll(areaResponse.getNameList());
                    } else {
                        C.INSTANCE.showToast(getApplicationContext(), areaResponse.getMessage());
                    }
                }

                stateAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<AreaResponse> call, Throwable t) {
                dialog.dismiss();

            }
        });
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstname = edt_firstname.getText().toString().trim();
                String lastname = edt_lastname.getText().toString().trim();
                String address = edt_address.getText().toString().trim();
                String city = edt_city.getText().toString().trim();
                String state = spn_state.getSelectedItem().toString();
                String cardType = "";
                cvv = edt_cvv_number.getText().toString().trim();

                String cardNumber = textview_credit_card.getText().toString().replace("-", "");
                String getSpinnerMonth = spn_month.getSelectedItem().toString();
                String getSpinnerYear = spn_year.getSelectedItem().toString();
                String getZipCode = edtZip.getText().toString();

                if (chk_save.isChecked() == true) {
                    is_card_save = "1";
                } else {
                    is_card_save = "0";
                }


                try {
                    cardType = textview_credit_card.getTypeOfSelectedCreditCard().toString();

                } catch (Exception e) {
                    cardType = "";
                }


                Log.e("cardType", "--->" + cardType);
                Log.e("cardNumber", "--->" + cardNumber);
                Log.e("Month", "--->" + getSpinnerMonth);
                Log.e("Year", "--->" + getSpinnerYear);


                int statePosition = spn_state.getSelectedItemPosition();
                if (firstname.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter valid first name");
                } else if (lastname.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter valid last name");
                } else if (cardType.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter valid card number");
                } else if (address.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter address");
                } else if (city.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter city");
                } else if (state.isEmpty() || statePosition == 0) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please Select state");
                } else if (getSpinnerMonth.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select month");
                } else if (getSpinnerYear.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select year");
                } else {
                    new AddCard(firstname, lastname, address, city, state, cardType, cardNumber, getSpinnerMonth, getSpinnerYear, getZipCode).execute();


                }

            }
        });

        new GetMonthsAndYear().execute();
    }


    public class GetMonthsAndYear extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                dialog = new CustomDialog(getApplicationContext());
                dialog.show();
                dialog.setCancelable(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... params) {

            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair(W.KEY_USERID, preference.getUserID()));
            pairs.add(new BasicNameValuePair(W.KEY_TOKEN, preference.getTokenHash()));


            String json = new ServiceHandler().makeServiceCall(W.BASE_URL + "User/b_card_month_year", ServiceHandler.POST, pairs);


            Log.e("parameters", "-->" + pairs);
            Log.e("json", json);

            return json;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            if (s.isEmpty()) {

                C.INSTANCE.defaultError(getApplicationContext());
            } else {

                try {
                    JSONObject mainObject = new JSONObject(s);

                    boolean status = false;

                    status = mainObject.getBoolean("status");
                    if (status == true) {

                        months.clear();
                        year.clear();
                        JSONObject dataObject = mainObject.getJSONObject("data");

                        JSONArray monthsArray = dataObject.getJSONArray("months");

                        for (int i = 0; i < monthsArray.length(); i++) {
                            String getMonthsStrings = monthsArray.getString(i);
                            months.add(getMonthsStrings);
                            Log.e("monthsString", "-->" + getMonthsStrings);

                        }

                        monthAdapter.notifyDataSetChanged();

                        JSONArray yearArray = dataObject.getJSONArray("years");
                        for (int j = 0; j < yearArray.length(); j++) {

                            String getYearStrings = yearArray.getString(j);
                            year.add(getYearStrings);
                            Log.e("yearsString", "-->" + getYearStrings);
                        }
                        yearAdapter.notifyDataSetChanged();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public class AddCard extends AsyncTask<Void, Void, String> {

        String type = "";
        String number = "";
        String exMonth = "";
        String exYear = "";
        String zipCode = "";
        String firstname = "";
        String lastname = "";
        String address = "";
        String city = "";
        String state = "";

        public AddCard(String firstname, String lastname, String address, String city, String state, String type, String number, String exMonth, String exYear, String zipCode) {
            this.firstname = firstname;
            this.lastname = lastname;
            this.address = address;
            this.city = city;
            this.state = state;
            this.type = type;
            this.number = number;
            this.exMonth = exMonth;
            this.exYear = exYear;
            this.zipCode = zipCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {

                dialog = new CustomDialog(CreateCardActivity.this, "");
                dialog.show();
                dialog.setCancelable(false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... params) {


            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("first_name", firstname));
            pairs.add(new BasicNameValuePair("last_name", lastname));
            pairs.add(new BasicNameValuePair("address", address));
            pairs.add(new BasicNameValuePair("city", city));
            pairs.add(new BasicNameValuePair("state", state));
            pairs.add(new BasicNameValuePair("country", ""));
            pairs.add(new BasicNameValuePair("phone", ""));
            pairs.add(new BasicNameValuePair("is_card_save", is_card_save));
            pairs.add(new BasicNameValuePair(W.KEY_USERID, preference.getUserID()));
            pairs.add(new BasicNameValuePair(W.KEY_TOKEN, preference.getTokenHash()));
            pairs.add(new BasicNameValuePair("bcard_type", type));
            pairs.add(new BasicNameValuePair("bcard_number", number));
            pairs.add(new BasicNameValuePair("bexp_month", exMonth));
            pairs.add(new BasicNameValuePair("bexp_year", exYear));
            pairs.add(new BasicNameValuePair("zip_code", zipCode));

            String json = new ServiceHandler().makeServiceCall(W.BASE_URL + "BankCard/app_add_card", ServiceHandler.POST, pairs);

            Log.e("parameters", "-->" + pairs);
            Log.e("json", json);

            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();


            if (s.isEmpty()) {
                C.INSTANCE.defaultError(getApplicationContext());

            } else {

                try {
                    JSONObject mainObject = new JSONObject(s);

                    boolean status = false;
                    String message = "";
                    status = mainObject.getBoolean("status");
                    message = mainObject.getString("message");

                  //  C.INSTANCE.showToast(getApplicationContext(), message);
                    if (status == true) {

                        JSONObject object = mainObject.getJSONObject("data");

                        auth_cust_paymnet_profile_id = object.getString("auth_cust_paymnet_profile_id");
                        customerProfileId = object.getString("profile_id");
                        card_Id = object.getString("card_id");

                        AddOrder();
                        /*Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);*/
                    }else {
                        C.INSTANCE.showToast(getApplicationContext(), message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }

        private void AddOrder() {

            dialog.show();

            if (isCouponTimes) {

                Call<CompleteOrderModel> couponRequest = adminAPI.CouponAccpet(orderId, preference.getUserID(), "1", card_Id, cvv, auth_cust_paymnet_profile_id, customerProfileId, is_card_save, is_card_save);
                couponRequest.enqueue(new Callback<CompleteOrderModel>() {
                    @Override
                    public void onResponse(Call<CompleteOrderModel> call, Response<CompleteOrderModel> response) {

                        CompleteOrderModel appModel = response.body();
                        if (appModel != null) {
                            if (appModel.isStatus()) {
                                Intent i = new Intent(getApplicationContext(), Thankyou.class);
                                i.putExtra("isCouponTimes", isCouponTimes);
                                i.putExtra("campaignName", appModel.getData().getCampaign_name());
                                i.putExtra("name", appModel.getData().getCustomer_name());
                                i.putExtra("expiryDate", appModel.getData().getExpiry_date());
                                i.putExtra("fundspot", appModel.getData().getFundspot_name());
                                i.putExtra("org", appModel.getData().getOrganization_name());
                                i.putExtra("total", appModel.getData().getTotal());
                                startActivity(i);


                            } else {
                                C.INSTANCE.showToast(getApplicationContext(), appModel.getMessage());
                            }
                        } else {
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
                Log.e("auth_cust_paymnet", "-->" + auth_cust_paymnet_profile_id);
                Log.e("is_card_saveEDEDEDEDE", "-->" + is_card_save);


                Log.e("customerProfileId", "-->" + customerProfileId);


                addOrder.enqueue(new Callback<CompleteOrderModel>() {
                    @Override
                    public void onResponse(Call<CompleteOrderModel> call, Response<CompleteOrderModel> response) {
                        dialog.dismiss();
                        CompleteOrderModel appModel = response.body();
                        Log.e("model", "-->" + new Gson().toJson(appModel));
                        if (appModel != null) {
                            if (appModel.isStatus()) {


                                Intent i = new Intent(getApplicationContext(), Thankyou.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

                                Log.e("isother5", "-->" + isotherTimes);

                                //i.putExtra("org", organization_name);
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
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.gc();
    }

}
