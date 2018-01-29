package com.fundit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.alihafizji.library.CreditCardEditText;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CreditCardPattern;
import com.fundit.model.BankCardResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardActivity extends AppCompatActivity {
    Spinner spn_savedcard;
    ArrayAdapter savedCardAdapter;
    CreditCardPattern creditCardPattern;
    EditText edt_cvv;
    AdminAPI adminAPI;
    AppPreference preference;
    ArrayList<String> cardName = new ArrayList<>();
    ArrayList<String> cardId = new ArrayList<>();
    List<BankCardResponse.BankCardResponseData> bankCard = new ArrayList<>();
    CreditCardEditText textview_credit_card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        adminAPI = ServiceGenerator.getAPIClass();
        preference = new AppPreference(getApplicationContext());
        creditCardPattern = new CreditCardPattern(getApplicationContext());
        fetchIds();
    }

    public void fetchIds()
    {

        spn_savedcard = (Spinner) findViewById(R.id.spn_savedcard);
        textview_credit_card = (CreditCardEditText) findViewById(R.id.textview_credit_card);
        textview_credit_card.setCreditCardEditTextListener(creditCardPattern);
        edt_cvv = (EditText) findViewById(R.id.edt_cvv);

        savedCardAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_textview, cardName);
        spn_savedcard.setAdapter(savedCardAdapter);


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
                        //    cardId.add(cardResponse.getData().get(i).getId().toString());

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

                } else {


                    textview_credit_card.setText(bankCard.get(position - 1).getBcard_number());



                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

}
