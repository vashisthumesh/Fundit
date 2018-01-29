package com.fundit.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fundit.MyCardDetailsActivity;
import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.AppModel;
import com.fundit.model.BankCardResponse;
import com.google.zxing.common.StringUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NWSPL-17 on 25-Aug-17.
 */

public class MemberCardDetailsAdapter extends BaseAdapter {

    List<BankCardResponse.BankCardResponseData> bankCardResponseDatas = new ArrayList<>();
    Activity activity;
    AdminAPI adminAPI;
    AppPreference preference;
    LayoutInflater inflater = null;
    CustomDialog dialog;
    public MemberCardDetailsAdapter(List<BankCardResponse.BankCardResponseData> bankCardResponseDatas, Activity activity) {
        this.bankCardResponseDatas = bankCardResponseDatas;
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return bankCardResponseDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return bankCardResponseDatas.get(position);
    }

    @Override

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        View view = inflater.inflate(R.layout.layout_card_details, parent, false);
        adminAPI = ServiceGenerator.getAPIClass();
        dialog = new CustomDialog(activity);
        preference = new AppPreference(activity);
        final TextView txtCardName = (TextView) view.findViewById(R.id.txt_cards);
        final ImageView img_edit = (ImageView) view.findViewById(R.id.img_edit);

        String getCardType = bankCardResponseDatas.get(position).getBcard_type();
        String getCardnumber = bankCardResponseDatas.get(position).getBcard_number();
        final String card_id=bankCardResponseDatas.get(position).getCard_id();
        final String auth_cust_paymnet_profile_id=bankCardResponseDatas.get(position).getAuth_cust_paymnet_profile_id();
        final String customerProfileId=bankCardResponseDatas.get(position).getCustomerProfileId();

        int i = 0;
        StringBuffer temp = new StringBuffer();
        while (i < (getCardnumber.length())) {
            if (i > getCardnumber.length() - 5) {
                temp.append(getCardnumber.charAt(i));
            } else {
                temp.append("X");
            }
            i++;
        }

        txtCardName.setText(getCardType + " " + temp);
       img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            Call<AppModel> delete_card_detail = adminAPI.Delete_Carddetail(preference.getUserID(),card_id,auth_cust_paymnet_profile_id,customerProfileId);
            delete_card_detail.enqueue(new Callback<AppModel>() {
                @Override
                public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                    dialog.dismiss();
                    AppModel appModel=response.body();
                    if (appModel != null) {
                        if (appModel.isStatus()) {

                        } else {

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

//                Intent intent = new Intent(activity, MyCardDetailsActivity.class);
//                intent.putExtra("cardNumber" , bankCardResponseDatas.get(position).getBcard_number());
//                intent.putExtra("cardExpirymonth" , bankCardResponseDatas.get(position).getBexp_month());
//                intent.putExtra("cardExpiryYear" , bankCardResponseDatas.get(position).getBexp_year());
//                intent.putExtra("zipcode" , bankCardResponseDatas.get(position).getZip_code());
//                intent.putExtra("id" , bankCardResponseDatas.get(position).getId());
//                intent.putExtra("ediMode" , true);
//
//                Log.e("isitAvalaible" , "--->" );
//
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                activity.startActivity(intent);
            }
        });

        return view;
    }


}
