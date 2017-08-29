package com.fundit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alihafizji.library.CreditCardEditText;
import com.alihafizji.library.CreditCardPatterns;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.W;
import com.fundit.apis.ServiceHandler;
import com.fundit.fragmet.MyCardsFragment;
import com.fundit.helper.CreditCardPattern;
import com.fundit.helper.CustomDialog;
import com.fundit.helper.DatePickerDialogWithTitle;
import com.fundit.model.Member;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MyCardDetailsActivity extends AppCompatActivity {

    CreditCardEditText textview_credit_card;
    Button btn_continue;

    EditText edtZip;

    CreditCardPattern creditCardPattern;

    Spinner spn_month , spn_year;

    ArrayList<String> months = new ArrayList<>();
    ArrayList<String> year = new ArrayList<>();

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card_details);

        Intent intent = getIntent();

        editMode= intent.getBooleanExtra("ediMode" , false);
        editedNumber = intent.getStringExtra("cardNumber");
        editedmonth = intent.getStringExtra("cardExpirymonth");
        editedyear = intent.getStringExtra("cardExpiryYear");
        editedZipcode = intent.getStringExtra("zipcode");
        editedId = intent.getStringExtra("id");


        preference = new AppPreference(getApplicationContext());
        dialog = new CustomDialog(getApplicationContext());

        creditCardPattern = new CreditCardPattern(getApplicationContext());

        try{
            member = new Gson().fromJson(preference.getMemberData() , Member.class);
        }catch (Exception e){
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


        actionTitle.setText("Save Card");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void fetchIds() {

        textview_credit_card = (CreditCardEditText) findViewById(R.id.textview_credit_card);
        textview_credit_card.setCreditCardEditTextListener(creditCardPattern);
        btn_continue = (Button) findViewById(R.id.btn_continue);

        spn_month = (Spinner) findViewById(R.id.spn_month);
        spn_year = (Spinner) findViewById(R.id.spn_year);

         monthAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_textview , months);
        spn_month.setAdapter(monthAdapter);

        yearAdapter = new ArrayAdapter<String>(getApplicationContext() , R.layout.spinner_textview , year);
        spn_year.setAdapter(yearAdapter);

        edtZip = (EditText) findViewById(R.id.edt_zip);
        edtZip.setText(member.getZip_code());


        if(editMode == true){

            textview_credit_card.setText(editedNumber);
            edtZip.setText(editedZipcode);
        }


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cardType = textview_credit_card.getTypeOfSelectedCreditCard().toString();
                String cardNumber = textview_credit_card.getText().toString();
                String getSpinnerMonth = spn_month.getSelectedItem().toString();
                String getSpinnerYear = spn_year.getSelectedItem().toString();
                String getZipCode = edtZip.getText().toString();
                Log.e("cardType" , "--->" + cardType);
                Log.e("cardNumber" , "--->" + cardNumber);
                Log.e("Month" , "--->" + getSpinnerMonth);
                Log.e("Year" , "--->" + getSpinnerYear);
                if(cardType.isEmpty()){
                    C.INSTANCE.showToast(getApplicationContext() , "Please enter proper card number");
                }else if(getSpinnerMonth.isEmpty()){
                    C.INSTANCE.showToast(getApplicationContext() , "Please select month");
                }else if(getSpinnerYear.isEmpty()){
                    C.INSTANCE.showToast(getApplicationContext() , "Please select year");
                }else {

                    if(editMode ==true){

                        new EditCard(cardType , cardNumber , getSpinnerMonth , getSpinnerYear , getZipCode).execute();
                    }
                    else {

                        new AddCard(cardType , cardNumber , getSpinnerMonth , getSpinnerYear , getZipCode).execute();
                    }


                }

            }
        });

        new GetMonthsAndYear().execute();
    }


    public class GetMonthsAndYear extends AsyncTask<Void , Void , String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{
                dialog = new CustomDialog(getApplicationContext());
                dialog.show();
                dialog.setCancelable(false);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... params) {

            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair(W.KEY_USERID , preference.getUserID()));
            pairs.add(new BasicNameValuePair(W.KEY_TOKEN , preference.getTokenHash()));


            String json = new ServiceHandler().makeServiceCall(W.BASE_URL + "User/b_card_month_year" , ServiceHandler.POST , pairs);


            Log.e("parameters" , "-->" +  pairs);
            Log.e("json" , json);

            return json;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            if(s.isEmpty()){

                C.INSTANCE.defaultError(getApplicationContext());
            }else {

                try {
                    JSONObject mainObject = new JSONObject(s);

                    boolean status = false;

                    status = mainObject.getBoolean("status");
                    if(status==true){

                        months.clear();
                        year.clear();
                        JSONObject dataObject = mainObject.getJSONObject("data");

                        JSONArray monthsArray = dataObject.getJSONArray("months");

                        for(int i=0 ; i<monthsArray.length();i++){
                            String getMonthsStrings = monthsArray.getString(i);
                            months.add(getMonthsStrings);
                            Log.e("monthsString" , "-->" + getMonthsStrings);

                        }

                        monthAdapter.notifyDataSetChanged();

                        JSONArray yearArray = dataObject.getJSONArray("years");
                        for(int j=0 ; j<yearArray.length();j++){

                            String getYearStrings = yearArray.getString(j);
                            year.add(getYearStrings);
                            Log.e("yearsString" , "-->" + getYearStrings);
                        }
                        yearAdapter.notifyDataSetChanged();


                        if(editMode==true){
                            checkForSelectedMonth();

                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public class AddCard extends AsyncTask<Void , Void , String>{

        String type = "";
        String number = "";
        String exMonth = "";
        String exYear = "";
        String zipCode = "";

        public AddCard(String type, String number, String exMonth, String exYear , String zipCode) {
            this.type = type;
            this.number = number;
            this.exMonth = exMonth;
            this.exYear = exYear;
            this.zipCode = zipCode;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{

                dialog = new CustomDialog(getApplicationContext()  ,"");
                dialog.show();
                dialog.setCancelable(false);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        @Override
        protected String doInBackground(Void... params) {


            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair(W.KEY_USERID , preference.getUserID()));
            pairs.add(new BasicNameValuePair(W.KEY_TOKEN , preference.getTokenHash()));
            pairs.add(new BasicNameValuePair("bcard_type" , type));
            pairs.add(new BasicNameValuePair("bcard_number" , number));
            pairs.add(new BasicNameValuePair("bexp_month" , exMonth));
            pairs.add(new BasicNameValuePair("bexp_year" , exYear));
            pairs.add(new BasicNameValuePair("zip_code" , zipCode));

            String json = new ServiceHandler().makeServiceCall(W.BASE_URL + "BankCard/app_add_card" , ServiceHandler.POST , pairs);

            Log.e("parameters" , "-->" + pairs);
            Log.e("json" , json);

            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();


            if(s.isEmpty()){
                C.INSTANCE.defaultError(getApplicationContext());

            }else{

                try {
                    JSONObject mainObject = new JSONObject(s);

                    boolean status = false;
                    String message = "";
                    status = mainObject.getBoolean("status");
                    message = mainObject.getString("message");

                    C.INSTANCE.showToast(getApplicationContext() , message);
                    if(status == true){
                        Intent intent = new Intent(getApplicationContext() , HomeActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }
    }
    public class EditCard extends AsyncTask<Void , Void , String>{

        String type = "";
        String number = "";
        String exMonth = "";
        String exYear = "";
        String zipCode = "";

        public EditCard(String type, String number, String exMonth, String exYear , String zipCode) {
            this.type = type;
            this.number = number;
            this.exMonth = exMonth;
            this.exYear = exYear;
            this.zipCode = zipCode;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{

                dialog = new CustomDialog(getApplicationContext()  ,"");
                dialog.show();
                dialog.setCancelable(false);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        @Override
        protected String doInBackground(Void... params) {


            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair(W.KEY_USERID , preference.getUserID()));
            pairs.add(new BasicNameValuePair(W.KEY_TOKEN , preference.getTokenHash()));
            pairs.add(new BasicNameValuePair("bcard_type" , type));
            pairs.add(new BasicNameValuePair("bcard_number" , number));
            pairs.add(new BasicNameValuePair("bexp_month" , exMonth));
            pairs.add(new BasicNameValuePair("bexp_year" , exYear));
            pairs.add(new BasicNameValuePair("zip_code" , zipCode));
            pairs.add(new BasicNameValuePair("id" , editedId));


            String json = new ServiceHandler().makeServiceCall(W.BASE_URL + "BankCard/app_edit_card" , ServiceHandler.POST , pairs);

            Log.e("parameters" , "-->" + pairs);
            Log.e("json" , json);

            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();


            if(s.isEmpty()){
                C.INSTANCE.defaultError(getApplicationContext());

            }else{

                try {
                    JSONObject mainObject = new JSONObject(s);

                    boolean status = false;
                    String message = "";
                    status = mainObject.getBoolean("status");
                    message = mainObject.getString("message");

                    C.INSTANCE.showToast(getApplicationContext() , message);
                    if(status == true){
                        editMode=false;
                        Intent intent = new Intent(getApplicationContext() , HomeActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }
    }



    private void checkForSelectedMonth() {
        int pos = 0;

        Log.e("getSelectedPductMonths" ,"-->" +  editedmonth);
        for (int i = 0; i < months.size(); i++) {
            if (months.get(i).equals(editedmonth)) {
                pos = i;
                break;
            }
        }




        //inEditModeFirstTime=false;

        spn_month.setSelection(pos);
        checkForSelectedYear();
    }

    private void checkForSelectedYear() {
        int pos = 0;
        Log.e("getSelectedPductyear" ,"-->" +  editedyear);
        for (int i = 0; i < year.size(); i++) {
            if (year.get(i).equals(editedyear)) {
                pos = i;
                break;
            }
        }



        spn_year.setSelection(pos);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        editMode = false;
    }
}
