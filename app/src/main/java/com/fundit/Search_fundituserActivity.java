package com.fundit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.adapter.GetSearchAdapter;
import com.fundit.adapter.PeopleSerchAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.GetSearchPeople;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search_fundituserActivity extends AppCompatActivity implements PeopleSerchAdapter.updateinterface{

    EditText edt_search_user;
    ImageView search_user;
    ListView list_search;
    AdminAPI adminAPI;
    AppPreference preferences;
    List<GetSearchPeople.People> people_search=new ArrayList<>();
    CustomDialog dialog;
    PeopleSerchAdapter searchAdapter;
    boolean fundspotSelected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_fundituser);
        adminAPI = ServiceGenerator.getAPIClass();
        preferences = new AppPreference(getApplicationContext());
        dialog = new CustomDialog(getApplicationContext());

            FetchIds();

    }

    private void  FetchIds()
    {
        edt_search_user= (EditText) findViewById(R.id.edt_search_user);
        search_user= (ImageView) findViewById(R.id.search_user);
        list_search= (ListView) findViewById(R.id.list_search);


        edt_search_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchKey = edt_search_user.getText().toString().trim();
                if (searchKey.length() >= 3) {
                    SEARCH_PEOPLE(searchKey);
                }
            }
        });

        search_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String getSearchTermed = edt_search_user.getText().toString();

                if(getSearchTermed.isEmpty())
                    C.INSTANCE.showToast(getApplicationContext() , "Please enter name");
                else if(getSearchTermed.length() <3)
                    C.INSTANCE.showToast(getApplicationContext() , "Please enter mininum 3 keuwords");
                else {
                    SEARCH_PEOPLE(getSearchTermed);
                }


            }
        });

    }

    private void SEARCH_PEOPLE(String title) {
        Call<GetSearchPeople> searchCall = adminAPI.getsearchpeople(preferences.getUserID(), title, preferences.getUserRoleID());
        searchCall.enqueue(new Callback<GetSearchPeople>() {
            @Override
            public void onResponse(Call<GetSearchPeople> call, Response<GetSearchPeople> response) {
                people_search.clear();
                dialog.dismiss();
                GetSearchPeople listResponse = response.body();
                if (listResponse != null) {
                    if (listResponse.isStatus()) {
                        Log.e("true","true");

                        people_search.addAll(listResponse.getData());
                    }
                }
                else {
                    C.INSTANCE.defaultError(getApplicationContext());
                }
                searchAdapter = new PeopleSerchAdapter(people_search,getApplicationContext(),Search_fundituserActivity.this,Search_fundituserActivity.this);
                list_search.setAdapter(searchAdapter);

                searchAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<GetSearchPeople> call, Throwable t) {
                dialog.dismiss();
                C.INSTANCE.errorToast(getApplicationContext(), t);
            }
        });
    }


    @Override
    public void updateintent(GetSearchPeople.People position) {
        Intent intent=new Intent();
        intent.putExtra("id",position);
        setResult(1,intent);
         finish();



    }
}
