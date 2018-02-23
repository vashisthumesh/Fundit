package com.fundit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.J;
import com.fundit.adapter.SendMessageAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.Member;
import com.fundit.model.MemberListResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendMessageActivity extends AppCompatActivity {

    AutoCompleteTextView searchUsers ;
    ImageView search ;
    ListView listUsers;

    AppPreference preference ;
    CustomDialog dialog;
    AdminAPI adminAPI;

    List<Member> memberList = new ArrayList<>();
    ArrayList<String> memberNames = new ArrayList<>();
    ArrayAdapter<String> memberArrayAdapter;

    String selectedPersonId = "";
    String selectedPersonName = "";

    SendMessageAdapter messageAdapter;
    boolean isSearchTimes = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);


        preference = new AppPreference(getApplicationContext());
        dialog = new CustomDialog(this);
        adminAPI = ServiceGenerator.getAPIClass();

        setupToolBar();
        fetchIds();




    }

    private void setupToolBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionTitle.setText("New Message");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void fetchIds() {

        searchUsers = (AutoCompleteTextView) findViewById(R.id.auto_searchUser);
        search = (ImageView) findViewById(R.id.img_search);
        listUsers = (ListView) findViewById(R.id.list_users);


        memberArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_textview, memberNames);
        searchUsers.setAdapter(memberArrayAdapter);

        messageAdapter = new SendMessageAdapter(memberList , getApplicationContext() , false);
        listUsers.setAdapter(messageAdapter);




        /*searchUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                List<Member> searchedMembers = new ArrayList<>();
                selectedPersonName = searchUsers.getText().toString().trim();
                for (int i = 0 ; i < memberList.size() ; i++){
                    String getName = memberList.get(i).getFirst_name() + " " + memberList.get(i).getLast_name();
                    if(selectedPersonName.equalsIgnoreCase(getName)){
                        selectedPersonId = memberList.get(i).getUser_id();
                        searchedMembers.add(memberList.get(i));
                        break;
                    }
                }
                Log.e("getId" , "--->" + selectedPersonId);
                Log.e("getSelectedName" , "--->" + selectedPersonName);
                Log.e("size" , "--->" + searchedMembers.size());




            }
        });*/

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getText = searchUsers.getText().toString();

                SearchMembers(getText);


            }
        });


        searchUsers.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                String getText = searchUsers.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    SearchMembers(getText);
                    return true;
                }
                return false;
            }
        });

        searchUsers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String findName = "";
                findName = searchUsers.getText().toString().trim();

                if (findName.isEmpty()) {

                } else {
                    isSearchTimes = true;
                    SearchMembers(findName);
                }


            }
        });







    }

    private void SearchMembers(String getText) {

        if(isSearchTimes==false){
            dialog.show();
        }

        Call<MemberListResponse> memberListResponseCall = adminAPI.getMembersMessageTime(preference.getUserID(),getText);
        Log.e("messageTimesPara" , "-->" + preference.getUserID() + "-->" + getText);
        memberListResponseCall.enqueue(new Callback<MemberListResponse>() {
            @Override
            public void onResponse(Call<MemberListResponse> call, Response<MemberListResponse> response) {
                dialog.dismiss();
                memberList.clear();
                memberNames.clear();
                MemberListResponse memberListResponse = response.body();
                if (memberListResponse != null) {
                    if (memberListResponse.isStatus()) {
                        memberList.addAll(memberListResponse.getData());
                        isSearchTimes = false;
                      //  memberNames.addAll(memberListResponse.getNames());
                    }
                } else {
                    C.INSTANCE.defaultError(getApplicationContext());
                }

                Log.e("Size", "--" + memberNames.size());
                //  memberArrayAdapter.notifyDataSetChanged();
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MemberListResponse> call, Throwable t) {
                dialog.dismiss();
                C.INSTANCE.errorToast(getApplicationContext(), t);
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        System.gc();
        searchUsers.setText("");
        memberList.clear();
        messageAdapter.notifyDataSetChanged();
        J.GetNotificationCountGlobal(preference.getUserID(), preference.getTokenHash(), preference, getApplicationContext(), this);
    }



}
