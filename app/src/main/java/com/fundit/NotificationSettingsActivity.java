package com.fundit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.adapter.NotificationSettingsAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.NotificationSettingsModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationSettingsActivity extends AppCompatActivity {


    ListView listSettings;

    AdminAPI adminAPI;
    AppPreference preference;
    CustomDialog customDialog;

    List<NotificationSettingsModel.NotificationData> notificationData = new ArrayList<>();
    NotificationSettingsAdapter settingsAdapter;

    String[] Org_Title = {"Member Join Requests", "Member Join Approvals", "Campaign Requests", "Campiagn Kickoff", "Campaign End", "Coupon Order Placed", "Fundspot/Organization Followed Activity"};

    String[] Fund_Title = {"Member Join Requests", "Member Join Approvals", "Campaign Requests", "Campiagn Kickoff", "Campaign End", "Coupon Order Placed", "Fundspot/Organization Followed Activity"};

    String[] Gm_Title = {"Member Join Requests", "Member Join Approvals", "Campiagn Kickoff", "Campaign End", "Fundspot/Organization Followed Activity", "Coupon Send/Recieved", "Coupon Expiration Warning", "Coupon Requests"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);

        adminAPI = ServiceGenerator.getAPIClass();
        preference = new AppPreference(getApplicationContext());
        customDialog = new CustomDialog(NotificationSettingsActivity.this, "");
        customDialog.setCancelable(false);


        fetchIds();
    }

    private void fetchIds() {


        listSettings = (ListView) findViewById(R.id.list_settings);
        settingsAdapter = new NotificationSettingsAdapter(notificationData, getApplicationContext());
        listSettings.setAdapter(settingsAdapter);

        GetAllSettingsList();
    }

    private void GetAllSettingsList() {
        customDialog.show();
        Call<NotificationSettingsModel> notificationSettingsModelCall = adminAPI.GetAllSettings(preference.getUserID(), preference.getUserRoleID());
        notificationSettingsModelCall.enqueue(new Callback<NotificationSettingsModel>() {
            @Override
            public void onResponse(Call<NotificationSettingsModel> call, Response<NotificationSettingsModel> response) {
                customDialog.dismiss();
                notificationData.clear();
                NotificationSettingsModel notificationSettingsModel = response.body();

                if (notificationSettingsModel != null) {
                    if (notificationSettingsModel.isStatus()) {
                        notificationData.addAll(notificationSettingsModel.getData());
                        for (int i = 0; i < notificationData.size(); i++) {
                            if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {
                                notificationData.get(i).setName(Org_Title[i]);
                            } else if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
                                notificationData.get(i).setName(Fund_Title[i]);
                            } else if (preference.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                                notificationData.get(i).setName(Gm_Title[i]);
                            }
                        }

                        settingsAdapter.notifyDataSetChanged();

                    } else {
                        C.INSTANCE.showToast(getApplicationContext(), notificationSettingsModel.getMessage());
                    }
                } else {
                    C.INSTANCE.defaultError(getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<NotificationSettingsModel> call, Throwable t) {
                customDialog.dismiss();
                C.INSTANCE.errorToast(getApplicationContext(), t);
            }
        });
    }
}
