package com.fundit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.fundit.R;
import com.fundit.a.C;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.model.AppModel;
import com.fundit.model.NotificationSettingsModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nivida6 on 25-01-2018.
 */


public class NotificationSettingsAdapter extends BaseAdapter {

    List<NotificationSettingsModel.NotificationData> notificationData = new ArrayList<>();
    Context context;
    LayoutInflater inflater;
    AdminAPI adminAPI;


    public NotificationSettingsAdapter(List<NotificationSettingsModel.NotificationData> notificationData, Context context) {
        this.notificationData = notificationData;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return notificationData.size();
    }

    @Override
    public Object getItem(int i) {
        return notificationData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        View rootView = inflater.inflate(R.layout.settinglayout, viewGroup, false);

        TextView textNotificationName = (TextView) rootView.findViewById(R.id.txt_setting);
        final Switch switchOnOff = (Switch) rootView.findViewById(R.id.switch_setting);


        textNotificationName.setText(notificationData.get(i).getName());

        if (notificationData.get(i).getStatus().equalsIgnoreCase("1")) {
            switchOnOff.setChecked(true);
            switchOnOff.setTrackDrawable(context.getResources().getDrawable(R.drawable.custom_track));
        } else {
            switchOnOff.setChecked(false);
            switchOnOff.setTrackDrawable(context.getResources().getDrawable(R.drawable.white_track));
        }

        switchOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                String id = notificationData.get(i).getId();
                String status = "";


                if (b == true) {
                    status = "1";
                    switchOnOff.setTrackDrawable(context.getResources().getDrawable(R.drawable.custom_track));
                    ChangeNotificationSettings(id, status, i);

                } else {
                    status = "0";
                    switchOnOff.setTrackDrawable(context.getResources().getDrawable(R.drawable.white_track));
                    ChangeNotificationSettings(id, status, i);

                }


            }
        });

        return rootView;

    }

    private void ChangeNotificationSettings(String id, final String status, final int i) {

        adminAPI = ServiceGenerator.getAPIClass();

        Call<AppModel> appModelCall = adminAPI.UpdateSettings(id, status);
        appModelCall.enqueue(new Callback<AppModel>() {
            @Override
            public void onResponse(Call<AppModel> call, Response<AppModel> response) {

                AppModel appModel = response.body();

                if (appModel != null) {
                    if (appModel.isStatus() == false) {
                        if (status.equalsIgnoreCase("1")) {
                            notificationData.get(i).setStatus("0");
                        } else if (status.equalsIgnoreCase("0")) {
                            notificationData.get(i).setStatus("1");
                        }

                        notifyDataSetChanged();
                    }
                } else {
                    C.INSTANCE.defaultError(context);
                }


            }

            @Override
            public void onFailure(Call<AppModel> call, Throwable t) {
                C.INSTANCE.errorToast(context, t);

            }
        });


    }
}
