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
import com.fundit.model.BankCardResponse;
import com.google.zxing.common.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NWSPL-17 on 25-Aug-17.
 */

public class MemberCardDetailsAdapter extends BaseAdapter {

    List<BankCardResponse.BankCardResponseData> bankCardResponseDatas = new ArrayList<>();
    Activity activity;

    LayoutInflater inflater = null;

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

        final TextView txtCardName = (TextView) view.findViewById(R.id.txt_cards);
        final ImageView img_edit = (ImageView) view.findViewById(R.id.img_edit);

        String getCardType = bankCardResponseDatas.get(position).getBcard_type();
        String getCardnumber = bankCardResponseDatas.get(position).getBcard_number();

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
//        img_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
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
//            }
//        });

        return view;
    }


}
