package com.fundit.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fundit.FinalOrderPlace;
import com.fundit.R;
import com.fundit.ReadMessageActivity;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.AppModel;
import com.fundit.model.InboxMessagesResponse;
import com.google.gson.Gson;

import org.apache.http.client.cache.Resource;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NWSPL-17 on 29-Aug-17.
 */

public class InboxViewAdapter extends BaseAdapter {


    List<InboxMessagesResponse.MessageResponseData> messageResponseDatas = new ArrayList<>();
    Activity activity;

    LayoutInflater inflater = null;
    Date date;

    AppPreference preference;
    AdminAPI adminAPI;
    CustomDialog dialog;

    String messagedDate = "";

    public InboxViewAdapter(List<InboxMessagesResponse.MessageResponseData> messageResponseDatas, Activity activity) {
        this.messageResponseDatas = messageResponseDatas;
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();

    }

    @Override
    public int getCount() {
        return messageResponseDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return messageResponseDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        View view = inflater.inflate(R.layout.layout_message_view, parent, false);


        TextView txtSenderName = (TextView) view.findViewById(R.id.txt_senderName);
        TextView txtSubject = (TextView) view.findViewById(R.id.txt_subject);
        final TextView txtMessageDate = (TextView) view.findViewById(R.id.txt_date);

        LinearLayout layoutMain = (LinearLayout) view.findViewById(R.id.layout_main);

        if(messageResponseDatas.get(position).getInbox().getStatus().equals(1)){
            layoutMain.setBackground(activity.getDrawable(R.drawable.listviewbackground));
        }else {
            layoutMain.setBackground(activity.getDrawable(R.drawable.unreadmeeageborder));
        }


        String getReTitle = messageResponseDatas.get(position).getReceiverUser().getTitle();
        String getSendTitle = messageResponseDatas.get(position).getSenderUser().getTitle();
        String subject = messageResponseDatas.get(position).getInbox().getSubject();

        Log.e("reTitle", "-->" + getReTitle);
        Log.e("seTitle", "-->" + getSendTitle);
        Log.e("subject", "-->" + subject);


        txtSenderName.setText(getSendTitle);
        txtSubject.setText(subject);

        String getMessageDate = messageResponseDatas.get(position).getInbox().getCreated();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd k:mm:ss");
        try {
            date = simpleDateFormat.parse(getMessageDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txtMessageDate.setText(new SimpleDateFormat("MMM-dd").format(date));


        layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                messagedDate = txtMessageDate.getText().toString().trim();

                if (messageResponseDatas.get(position).getInbox().getStatus().equals(1)) {
                    SendUnreadNotification(position);
                } else {

                    Intent intent = new Intent(activity, ReadMessageActivity.class);
                    intent.putExtra("senderName", messageResponseDatas.get(position).getSenderUser().getTitle());
                    intent.putExtra("messages", messageResponseDatas.get(position).getInbox().getMsg());
                    intent.putExtra("subject", messageResponseDatas.get(position).getInbox().getSubject());
                    intent.putExtra("date", messagedDate);
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }
            }
        });


        return view;
    }

    private void SendUnreadNotification(final int position) {

        adminAPI = ServiceGenerator.getAPIClass();
        preference = new AppPreference(activity);
        dialog = new CustomDialog(activity);
        dialog.setCancelable(false);


        dialog.show();
        Call<AppModel> readMessage = adminAPI.UnreadMessage(preference.getUserID(), preference.getTokenHash(), messageResponseDatas.get(position).getInbox().getId());
        Log.e("parameters", "-->" + messageResponseDatas.get(position).getInbox().getId());
        readMessage.enqueue(new Callback<AppModel>() {
            @Override
            public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                dialog.dismiss();
                AppModel model = response.body();
                Log.e("response", "-->" + new Gson().toJson(model));
                if (model != null) {
                    if (model.isStatus()) {
                        Intent intent = new Intent(activity, ReadMessageActivity.class);
                        intent.putExtra("senderName", messageResponseDatas.get(position).getSenderUser().getTitle());
                        intent.putExtra("messages", messageResponseDatas.get(position).getInbox().getMsg());
                        intent.putExtra("subject", messageResponseDatas.get(position).getInbox().getSubject());
                        intent.putExtra("date", messagedDate);
                        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    } else {
                        C.INSTANCE.showToast(activity, model.getMessage());
                    }
                } else {
                    C.INSTANCE.defaultError(activity);
                }
            }
            @Override
            public void onFailure(Call<AppModel> call, Throwable t) {
                dialog.dismiss();
                C.INSTANCE.errorToast(activity, t);
            }
        });
    }
}
