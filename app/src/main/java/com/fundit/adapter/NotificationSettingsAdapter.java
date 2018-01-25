package com.fundit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.fundit.R;
import com.fundit.model.NotificationSettingsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nivida6 on 25-01-2018.
 */


public class NotificationSettingsAdapter extends BaseAdapter {

    List<NotificationSettingsModel.NotificationData> notificationData = new ArrayList<>();
    Context context ;
    LayoutInflater inflater;


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
    public View getView(int i, View view, ViewGroup viewGroup) {

        View rootView = inflater.inflate(R.layout.settinglayout, viewGroup, false);

        TextView textNotificationName = (TextView) rootView.findViewById(R.id.txt_setting);
        Switch switchOnOff = (Switch) rootView.findViewById(R.id.switch_setting);


        textNotificationName.setText(notificationData.get(i).getName());

        if(notificationData.get(i).getStatus().equalsIgnoreCase("1")){
            switchOnOff.setChecked(true);
        }else {
            switchOnOff.setChecked(false);
        }

        return rootView;

    }
}
