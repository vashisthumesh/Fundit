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
import com.fundit.model.BankCardResponse;
import com.fundit.model.CampaignListResponse;
import com.fundit.model.Member;
import com.fundit.model.User;
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

public class CardPaymentActivity extends AppCompatActivity {

    AppPreference preference;
    CustomDialog dialog;
    AdminAPI adminAPI;


    CampaignListResponse.CampaignList campaignList;
    String selectedProductArray = "";

    Spinner spn_savedcard, spn_month, spn_year;

    ArrayList<String> cardName = new ArrayList<>();
    ArrayList<String> cardId = new ArrayList<>();

    ArrayAdapter savedCardAdapter;


    CreditCardEditText textview_credit_card;
    EditText edt_cvv;
    CheckBox chk_save;
    Button btn_continue;
    List<BankCardResponse.BankCardResponseData> bankCard = new ArrayList<>();
    ArrayList<String> months = new ArrayList<>();
    ArrayList<String> year = new ArrayList<>();

    ArrayAdapter<String> monthAdapter;
    ArrayAdapter<String> yearAdapter;

    String selectedCardId = "";
    String checked = "";
    String allPrice = "";

    User user = new User();
    Member member = new Member();

    String organizationId = "";
    String fundspotId = "";

    CreditCardPattern creditCardPattern;

    Boolean isCouponTimes = false;
    Boolean isOrderTimes = false;
    String orderId = "";

    String userId = "";
    String roleId = "";
    boolean isOtherUser = false;
    boolean isFundTimes = false;

    String orderRequest = "";
    String onBehalfOf = "";
    String otherUser = "";

    String firstName = "";
    String lastName = "";
    String emailId = "";
    String ownerId = "";

    TextView txt_titleSavedCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment);

        Intent intent = getIntent();
        campaignList = (CampaignListResponse.CampaignList) intent.getSerializableExtra("details");
        selectedProductArray = intent.getStringExtra("productArray");
        allPrice = intent.getStringExtra("allPrice");
        isCouponTimes = intent.getBooleanExtra("isCouponTimes", false);
        orderId = intent.getStringExtra("orderId");
        userId = intent.getStringExtra("selectedFundUserId");
        isOtherUser = intent.getBooleanExtra("otherUser", false);
        firstName = intent.getStringExtra("firstName");
        lastName = intent.getStringExtra("lastName");
        emailId = intent.getStringExtra("emailId");
        isOrderTimes = intent.getBooleanExtra("orderTimes" , false);
        isFundTimes = intent.getBooleanExtra("fundTimes" , false);


        Log.e("userId", "--->" + userId);

        preference = new AppPreference(getApplicationContext());
        dialog = new CustomDialog(this);

        adminAPI = ServiceGenerator.getAPIClass();

        try {

            user = new Gson().fromJson(preference.getUserData(), User.class);
            member = new Gson().fromJson(preference.getMemberData(), Member.class);


        } catch (Exception e) {

            Log.e("Exception", e.getMessage());
        }

        creditCardPattern = new CreditCardPattern(getApplicationContext());


        setToolBar();
        fetchIds();


    }

    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        actionTitle.setText("Place Order");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void fetchIds() {

        txt_titleSavedCard = (TextView) findViewById(R.id.txt_titleSavedCard);

        spn_savedcard = (Spinner) findViewById(R.id.spn_savedcard);
        spn_month = (Spinner) findViewById(R.id.spn_month);
        spn_year = (Spinner) findViewById(R.id.spn_year);

        textview_credit_card = (CreditCardEditText) findViewById(R.id.textview_credit_card);
        textview_credit_card.setCreditCardEditTextListener(creditCardPattern);

        edt_cvv = (EditText) findViewById(R.id.edt_cvv);
        chk_save = (CheckBox) findViewById(R.id.chk_save);
        btn_continue = (Button) findViewById(R.id.btn_continue);

        savedCardAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_textview, cardName);
        spn_savedcard.setAdapter(savedCardAdapter);


        monthAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_textview, months);
        spn_month.setAdapter(monthAdapter);

        yearAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_textview, year);
        spn_year.setAdapter(yearAdapter);


        if(isFundTimes){
            spn_savedcard.setVisibility(View.GONE);
            chk_save.setVisibility(View.GONE);
            txt_titleSavedCard.setVisibility(View.GONE);
        }



        // dialog.show();
        final Call<BankCardResponse> bankCardResponse = adminAPI.BankCard(preference.getUserID(), preference.getTokenHash());
        Log.e("parameters", "-->" + preference.getUserID() + "-->" + preference.getTokenHash());
        bankCardResponse.enqueue(new Callback<BankCardResponse>() {
            @Override
            public void onResponse(Call<BankCardResponse> call, Response<BankCardResponse> response) {
                // dialog.dismiss();
                cardName.add("Select Card");
                cardId.add("0");

                BankCardResponse cardResponse = response.body();

                Log.e("getData", "-->" + new Gson().toJson(cardResponse));

                if (cardResponse != null) {
                    if (cardResponse.isStatus()) {
                        for (int i = 0; i < cardResponse.getData().size(); i++) {

                            cardName.add(cardResponse.getData().get(i).getBcard_type().toString());
                            cardId.add(cardResponse.getData().get(i).getId().toString());

                            Log.e("cardId", "" + cardId.get(i));

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
                if (position == 0) {
                    textview_credit_card.setText("");
                    edt_cvv.setText("");
                    spn_month.setSelection(0);
                    spn_year.setSelection(0);
                    chk_save.setVisibility(View.VISIBLE);
                } else {

                    chk_save.setVisibility(View.GONE);
                    textview_credit_card.setText(bankCard.get(position - 1).getBcard_number());
                    String getSelectedItemMonth = bankCard.get(position - 1).getBexp_month();
                    String getSelectedItemYear = bankCard.get(position - 1).getBexp_year();

                    checkForSelectedMonth(getSelectedItemMonth, getSelectedItemYear);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cardType = textview_credit_card.getTypeOfSelectedCreditCard().toString();
                String cardNumber = textview_credit_card.getText().toString();
                String getSpinnerMonth = spn_month.getSelectedItem().toString();
                String getSpinnerYear = spn_year.getSelectedItem().toString();
                String cvv = edt_cvv.getText().toString();

                if (isCouponTimes == false) {

                    if (userId.isEmpty()) {

                        userId = preference.getUserID();
                        roleId = preference.getUserRoleID();

                        onBehalfOf = "0";
                        orderRequest = "0";

                        if (isOtherUser) {
                            otherUser = "1";
                        } else {
                            otherUser = "0";
                            firstName = user.getFirst_name();
                            lastName = user.getLast_name();
                            emailId = member.getContact_info_email();
                        }
                    } else {
                        roleId = "4";
                        onBehalfOf = "1";
                        orderRequest = "0";
                        otherUser = "0";
                        /*firstName = user.getFirst_name();
                        lastName = user.getLast_name();
                        emailId = member.getContact_info_email();
*/
                    }

                }

                if (chk_save.isChecked()) {
                    checked = "1";
                }


                if (spn_savedcard.getSelectedItemPosition() != 0) {
                    selectedCardId = bankCard.get(0).getId();
                }


                if (isCouponTimes == false) {

                    if (campaignList.getCampaign().getRole_id().equalsIgnoreCase(C.ORGANIZATION)) {
                        organizationId = campaignList.getUserOrganization().getId();
                        fundspotId = campaignList.getUserFundspot().getId();
                    }

                    if (campaignList.getCampaign().getRole_id().equalsIgnoreCase(C.FUNDSPOT)) {
                        fundspotId = campaignList.getUserOrganization().getId();
                        organizationId = campaignList.getUserFundspot().getId();
                    }
                }

                if (cardType.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter proper card number");
                } else if (getSpinnerMonth.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select month");
                } else if (getSpinnerYear.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select year");
                } else if (cvv.isEmpty()) {

                    C.INSTANCE.showToast(getApplicationContext(), "Please enter cvv ");
                } else {
                    dialog.show();

                    Call<AppModel> addOrder = null;

                    if (isCouponTimes == true) {
                        addOrder = adminAPI.AcceptCoupon(orderId, preference.getUserID(), "1", selectedCardId, cardNumber, cardType, cvv, getSpinnerMonth, getSpinnerYear, checked);

                    } else {

                        addOrder = adminAPI.AddCardOrder(userId, roleId, preference.getTokenHash(), campaignList.getCampaign().getId(), firstName , lastName , emailId, member.getContact_info(), member.getLocation(), member.getCity().getName(), member.getZip_code(), member.getState().getName(), "2", allPrice, preference.getUserID(), "0.0", "0.0", organizationId, fundspotId, selectedProductArray, cardNumber, cardType, getSpinnerMonth, getSpinnerYear, cvv, selectedCardId, checked, onBehalfOf, orderRequest, otherUser);

                        Log.e("check", "-->" + onBehalfOf + "-->" + orderRequest + "--->" + otherUser + "--->" +userId +"--->" + firstName + "--->"+ lastName + "--->" + preference.getUserID());

                    }
                    addOrder.enqueue(new Callback<AppModel>() {
                        @Override
                        public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                            dialog.dismiss();
                            AppModel appModel = response.body();
                            if (appModel != null) {
                                if (appModel.isStatus()) {
                                    C.INSTANCE.showToast(getApplicationContext(), appModel.getMessage());
                                    if (isCouponTimes) {
                                        Intent intent = new Intent(getApplicationContext(), Thankyou.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("selectedUserId" , "");
                                        intent.putExtra("isCouponTimes" , isCouponTimes);
                                        startActivity(intent);
                                    }else if(isOrderTimes){
                                        Intent intent = new Intent(getApplicationContext(), Thankyou.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("isOtherUser" , isOtherUser);
                                        intent.putExtra("name" , firstName);
                                        intent.putExtra("selectedUserId" , userId);
                                        intent.putExtra("org", campaignList.getUserOrganization().getTitle());
                                        intent.putExtra("fundspot", campaignList.getUserFundspot().getTitle());
                                        intent.putExtra("fundTimes" , isFundTimes);
                                        startActivity(intent);
                                    }
                                    else {
                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
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

        new GetMonthsAndYear().execute();
    }

    private void checkForSelectedMonth(String getSelectedItemMonth, String getSelectedItemYear) {
        int pos = 0;

        Log.e("getSelectedPductMonths", "-->" + getSelectedItemMonth);
        for (int i = 0; i < months.size(); i++) {
            if (months.get(i).equals(getSelectedItemMonth)) {
                pos = i;
                break;
            }
        }


        //inEditModeFirstTime=false;

        spn_month.setSelection(pos);
        checkForSelectedYear(getSelectedItemYear);

    }

    private void checkForSelectedYear(String getSelectedItemYear) {

        int pos = 0;
        Log.e("getSelectedPductyear", "-->" + getSelectedItemYear);
        for (int i = 0; i < year.size(); i++) {
            if (year.get(i).equals(getSelectedItemYear)) {
                pos = i;
                break;
            }
        }


        spn_year.setSelection(pos);


    }


    public class GetMonthsAndYear extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
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


}
