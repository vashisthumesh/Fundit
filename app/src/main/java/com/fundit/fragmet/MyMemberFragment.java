package com.fundit.fragmet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.fundit.AddMemberActivity;
import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.adapter.AccountMemberAdapter;
import com.fundit.adapter.MemberListAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.Member;
import com.fundit.model.MemberListResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NWSPL-17 on 01-Sep-17.
 */

public class MyMemberFragment extends Fragment {

    View view;

    AppPreference preference;
    CustomDialog dialog;
    AdminAPI adminAPI;

    ListView listMembers;

    List<Member> memberlist = new ArrayList<>();
    AccountMemberAdapter memberListAdapter;

    ImageView imgAdd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_mymember , container , false);


        preference = new AppPreference(getActivity());
        dialog = new CustomDialog(getActivity());
        adminAPI = ServiceGenerator.getAPIClass();


        fetchIDs();
        return view;
    }

    private void fetchIDs() {

        imgAdd = (ImageView) view.findViewById(R.id.imgAdd);

        listMembers = (ListView) view.findViewById(R.id.list_members);
        memberListAdapter = new AccountMemberAdapter(memberlist , getActivity());
        listMembers.setAdapter(memberListAdapter);

        dialog.show();
        Call<MemberListResponse> listResponse = adminAPI.MEMBER_LIST_RESPONSE_CALL(preference.getUserID() , preference.getTokenHash() , preference.getUserRoleID());
        listResponse.enqueue(new Callback<MemberListResponse>() {
            @Override
            public void onResponse(Call<MemberListResponse> call, Response<MemberListResponse> response) {
                dialog.dismiss();
                memberlist.clear();
                MemberListResponse memberListResponse = response.body();

                Log.e("response" , "-->" + new Gson().toJson(memberListResponse));

                if(memberListResponse!=null){
                    if(memberListResponse.isStatus()){
                        memberlist.addAll(memberListResponse.getData());
                    }else {
                        C.INSTANCE.showToast(getActivity(), memberListResponse.getMessage());
                    }
                }else {
                    C.INSTANCE.defaultError(getActivity());
                }

                memberListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MemberListResponse> call, Throwable t) {
                dialog.dismiss();
                C.INSTANCE.errorToast(getActivity() , t);
            }
        });


        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity() , AddMemberActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });




    }
}
