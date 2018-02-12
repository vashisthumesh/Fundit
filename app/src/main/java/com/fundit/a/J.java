package com.fundit.a;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.apis.ServiceHandler;
import com.fundit.helper.CustomDialog;
import com.fundit.model.MultipleProductResponse;
import com.fundit.model.NotificationCountModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nivida new on 17-Jul-17.
 */

public class J {
    public static final String ORGANIZATION = "2";
    public static final String FUNDSPOT = "3";
    public static final String GENERAL_MEMBER = "4";


    public void GetNotificationCountGlobal(String userId , String tokenHash , final AppPreference appPreference , Context context , Activity activity){

        AdminAPI adminAPI = ServiceGenerator.getAPIClass();
        final CustomDialog customDialog = new CustomDialog(activity , "");
        customDialog.show();
        Call<NotificationCountModel> countModelCall = adminAPI.NOTIFICATION_COUNT_MODEL_CALL(userId, tokenHash);
        countModelCall.enqueue(new Callback<NotificationCountModel>() {
            @Override
            public void onResponse(Call<NotificationCountModel> call, Response<NotificationCountModel> response) {
                customDialog.dismiss();

                NotificationCountModel countModel = response.body();
                if(countModel!=null){
                    if(countModel.isStatus()){

                        appPreference.setMessageCount(Integer.parseInt(countModel.getTotal_unread_msg()));
                        appPreference.setfundspot_product_count(Integer.parseInt(countModel.getFundspot_product_count()));
                       // appPreference.setRedeemer(countModel.get);



                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationCountModel> call, Throwable t) {
                customDialog.dismiss();

            }
        });










    }





}
