package com.fundit.fragmet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.fundit.R;
import com.fundit.SendMessageActivity;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.adapter.InboxViewAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.apis.StringConverterFactory;
import com.fundit.helper.CustomDialog;
import com.fundit.model.InboxMessagesResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NWSPL-17 on 29-Aug-17.
 */

public class InboxFragment extends Fragment {

    View view;
    AppPreference preference;
    CustomDialog dialog;
    AdminAPI adminAPI;

    AutoCompleteTextView edtSearch;
    ImageView imgEdit;
    Button btnSearch;
    ListView listMessages;

    List<InboxMessagesResponse.MessageResponseData> messageResponseDatas = new ArrayList<>();
    InboxViewAdapter inboxViewAdapter;

    ArrayList<String> getAllSubject = new ArrayList<>();
    ArrayList<String> getAllSubjectIds = new ArrayList<>();
    ArrayAdapter<String> subjectAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_inbox, container, false);

        preference = new AppPreference(getActivity());
        dialog = new CustomDialog(getActivity());
        dialog.setCancelable(false);
        adminAPI = ServiceGenerator.getAPIClass();


        fetchIds();

        return view;
    }

    private void fetchIds() {
        edtSearch = (AutoCompleteTextView) view.findViewById(R.id.auto_searchMessage);
        imgEdit = (ImageView) view.findViewById(R.id.img_edit);
        btnSearch = (Button) view.findViewById(R.id.btn_search);
        listMessages = (ListView) view.findViewById(R.id.list_messages);
        inboxViewAdapter = new InboxViewAdapter(messageResponseDatas, getActivity());
        listMessages.setAdapter(inboxViewAdapter);

        subjectAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, getAllSubject);
        edtSearch.setAdapter(subjectAdapter);


        dialog.show();
        final Call<InboxMessagesResponse> inboxMessagesResponseCall = adminAPI.GetInboxMessage(preference.getUserID(), preference.getTokenHash());
        inboxMessagesResponseCall.enqueue(new Callback<InboxMessagesResponse>() {
            @Override
            public void onResponse(Call<InboxMessagesResponse> call, Response<InboxMessagesResponse> response) {
                dialog.dismiss();
                Log.e("response", "-->" + new Gson().toJson(response.body()));
                InboxMessagesResponse messagesResponse = response.body();
                Log.e("response1", "-->" + new Gson().toJson(messagesResponse));

                if (messagesResponse != null) {
                    if (messagesResponse.isStatus()) {
                        messageResponseDatas.addAll(messagesResponse.getResponseDataList());
                       // getAllSubject.addAll(messagesResponse.getSubjects());
                    } else {
                        C.INSTANCE.showToast(getActivity(), messagesResponse.getMessage());
                    }
                } else {
                    C.INSTANCE.defaultError(getActivity());
                }

                inboxViewAdapter.notifyDataSetChanged();
             //   subjectAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<InboxMessagesResponse> call, Throwable t) {
                dialog.dismiss();
                C.INSTANCE.errorToast(getActivity(), t);
            }
        });

        /*
        edtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                List<InboxMessagesResponse.MessageResponseData> searchSubjects = new ArrayList<>();
                String searchedSubject = "";
                searchedSubject = edtSearch.getText().toString();

                Log.e("searchedSubject" , "-->" + searchedSubject);

                 for(int i =0 ; i <messageResponseDatas.size();i++){
                     if(searchedSubject.equalsIgnoreCase(messageResponseDatas.get(i).getInbox().getSubject())){
                         Log.e("searchedSubject2" , "-->" + messageResponseDatas.get(i).getInbox().getSubject());
                         searchSubjects.add(messageResponseDatas.get(i));
                         break;
                     }
                 }
                 inboxViewAdapter.notifyDataSetChanged();
            }
        });*/



        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , SendMessageActivity.class);
                startActivity(intent);
            }
        });


    }
}