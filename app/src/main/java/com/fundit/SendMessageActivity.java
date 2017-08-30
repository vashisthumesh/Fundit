package com.fundit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
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



        dialog.show();
        Call<MemberListResponse> memberListResponseCall = adminAPI.getAllMemberList(preference.getUserID(), preference.getTokenHash(), preference.getUserRoleID(), preference.getUserID(), null);
        memberListResponseCall.enqueue(new Callback<MemberListResponse>() {
            @Override
            public void onResponse(Call<MemberListResponse> call, Response<MemberListResponse> response) {
                dialog.dismiss();
                MemberListResponse memberListResponse = response.body();
                if (memberListResponse != null) {
                    if (memberListResponse.isStatus()) {
                        memberList.addAll(memberListResponse.getData());
                        memberNames.addAll(memberListResponse.getNames());
                    }
                } else {
                    C.INSTANCE.defaultError(getApplicationContext());
                }

                Log.e("Size", "--" + memberNames.size());
                memberArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MemberListResponse> call, Throwable t) {
                dialog.dismiss();
                C.INSTANCE.errorToast(getApplicationContext(), t);
            }
        });


        searchUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

                messageAdapter = new SendMessageAdapter(searchedMembers , getApplicationContext());
                listUsers.setAdapter(messageAdapter);
                messageAdapter.notifyDataSetChanged();

            }
        });








    }
}
