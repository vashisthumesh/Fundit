package com.fundit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class CreateCampaignTermsActivity extends AppCompatActivity {

    TextView txt_partnerLabel, txt_itemLabel;
    EditText edt_partner, edt_itemName, edt_price, edt_split, edt_campaignDuration, edt_maxLimitCoupon, edt_couponExpireDay, edt_finePrint;
    RadioGroup rg_productType;
    RadioButton rdo_typeItem, rdo_typeGiftCard;
    CheckBox chk_infinite;
    Button btn_accept, btn_message, btn_decline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_campaign_terms);
    }
}
