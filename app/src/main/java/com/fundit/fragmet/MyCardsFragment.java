package com.fundit.fragmet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.fundit.MyCardDetailsActivity;
import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.adapter.MemberCardDetailsAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.BankCardResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NWSPL-17 on 25-Aug-17.
 */

public class MyCardsFragment extends Fragment {


    View view;
    AdminAPI adminAPI;
    CustomDialog dialog;
    AppPreference preference;


    MemberCardDetailsAdapter cardDetailsAdapter;
    ListView list_cards;
    TextView addNewCard;

    Context context;

    List<BankCardResponse.BankCardResponseData> bankCard = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cards, container, false);

        adminAPI = ServiceGenerator.getAPIClass();
        dialog = new CustomDialog(getActivity());
        preference = new AppPreference(getActivity());


        fetchIDs();


        return view;
    }

    private void fetchIDs() {

        list_cards = (ListView) view.findViewById(R.id.list_cardsdetails);
        addNewCard = (TextView) view.findViewById(R.id.txt_addCards);

        cardDetailsAdapter = new MemberCardDetailsAdapter(bankCard, getActivity());
        list_cards.setAdapter(cardDetailsAdapter);


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
                        bankCard.addAll(cardResponse.getData());
                    } else {
                        C.INSTANCE.showToast(getActivity(), cardResponse.getMessage());
                    }
                } else {
                    C.INSTANCE.defaultError(getActivity());
                }
                cardDetailsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<BankCardResponse> call, Throwable t) {

                dialog.dismiss();
                C.INSTANCE.errorToast(getActivity(), t);
            }
        });

        addNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyCardDetailsActivity.class);
                startActivity(intent);
            }
        });

    }
}
