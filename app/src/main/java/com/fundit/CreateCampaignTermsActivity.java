package com.fundit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.AppModel;
import com.fundit.model.CampaignListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCampaignTermsActivity extends AppCompatActivity {

    TextView txt_partnerLabel, txt_itemLabel;
    EditText edt_partner, edt_itemName, edt_price, edt_split, edt_campaignDuration, edt_maxLimitCoupon, edt_couponExpireDay, edt_finePrint;
    RadioGroup rg_productType;
    RadioButton rdo_typeItem, rdo_typeGiftCard;
    CheckBox chk_infinite;
    Button btn_accept, btn_message, btn_decline;

    AppPreference preference;
    CampaignListResponse.CampaignList campaignList;
    AdminAPI adminAPI;
    CustomDialog dialog;

    int REQUEST_START_CAMPAIGN = 369;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_campaign_terms);

        adminAPI = ServiceGenerator.getAPIClass();
        dialog = new CustomDialog(this);
        preference = new AppPreference(this);

        Intent intent = getIntent();

        campaignList = (CampaignListResponse.CampaignList) intent.getSerializableExtra("campaignItem");

        fetchIDs();
    }

    private void fetchIDs() {
        txt_partnerLabel = (TextView) findViewById(R.id.txt_partnerLabel);
        txt_itemLabel = (TextView) findViewById(R.id.txt_itemLabel);

        edt_partner = (EditText) findViewById(R.id.edt_partner);
        edt_itemName = (EditText) findViewById(R.id.edt_itemName);
        edt_price = (EditText) findViewById(R.id.edt_price);
        edt_split = (EditText) findViewById(R.id.edt_split);
        edt_campaignDuration = (EditText) findViewById(R.id.edt_campaignDuration);
        edt_maxLimitCoupon = (EditText) findViewById(R.id.edt_maxLimitCoupon);
        edt_couponExpireDay = (EditText) findViewById(R.id.edt_couponExpireDay);
        edt_finePrint = (EditText) findViewById(R.id.edt_finePrint);

        rg_productType = (RadioGroup) findViewById(R.id.rg_productType);

        rdo_typeItem = (RadioButton) findViewById(R.id.rdo_typeItem);
        rdo_typeGiftCard = (RadioButton) findViewById(R.id.rdo_typeGiftCard);

        chk_infinite = (CheckBox) findViewById(R.id.chk_infinite);

        btn_accept = (Button) findViewById(R.id.btn_accept);
        btn_message = (Button) findViewById(R.id.btn_message);
        btn_decline = (Button) findViewById(R.id.btn_decline);


        if (preference.getUserRoleID().equals(C.ORGANIZATION)) {
            txt_partnerLabel.setText("Fundspot Partner");
            edt_partner.setText(campaignList.getUserFundspot().getFundspot().getTitle());
        } else if (preference.getUserRoleID().equals(C.FUNDSPOT)) {
            txt_partnerLabel.setText("Organization Partner");
            edt_partner.setText(campaignList.getUserOrganization().getOrganization().getTitle());
        }

        if (campaignList.getProduct().getType_id().equals(C.TYPE_PRODUCT)) {
            txt_itemLabel.setText("Item being sold");
            rdo_typeGiftCard.setChecked(true);
        } else if (campaignList.getProduct().getType_id().equals(C.TYPE_GIFTCARD)) {
            txt_itemLabel.setText("Gift card being sold");
            rdo_typeItem.setChecked(true);
        }

        edt_itemName.setText(campaignList.getProduct().getName());
        edt_price.setText(campaignList.getCampaign().getPrice());
        edt_split.setText(campaignList.getCampaign().getFundspot_percent() + " / " + campaignList.getCampaign().getOrganization_percent());
        edt_campaignDuration.setText(campaignList.getCampaign().getCampaign_duration());
        edt_maxLimitCoupon.setText(campaignList.getCampaign().getMax_limit_of_coupons());
        edt_couponExpireDay.setText(campaignList.getCampaign().getCoupon_expire_day());
        edt_finePrint.setText(campaignList.getCampaign().getFine_print());

        if (campaignList.getCampaign().getCampaign_duration().equals("0")) {
            chk_infinite.setChecked(true);
        }

        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                Call<AppModel> cancelCampaign = adminAPI.cancelCampaign(preference.getUserID(), preference.getTokenHash(),campaignList.getCampaign().getId(), preference.getUserRoleID(), "3");


                Log.e("Parameters" , "" + preference.getUserID() + "--" + preference.getTokenHash() + "--" + preference.getUserRoleID() + "--" + campaignList.getCampaign().getId());

                cancelCampaign.enqueue(new Callback<AppModel>() {
                    @Override
                    public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                        dialog.dismiss();
                        AppModel appModel = response.body();
                        if (appModel != null) {
                            if (appModel.isStatus()) {
                                C.INSTANCE.showToast(getApplicationContext(), appModel.getMessage());
                                setResult(RESULT_OK);
                                onBackPressed();
                            } else {
                                C.INSTANCE.showToast(getApplicationContext(), appModel.getMessage());
                            }
                        } else {
                            C.INSTANCE.defaultError(getApplicationContext());
                        }
                    }

                    @Override
                    public void onFailure(Call<AppModel> call, Throwable t) {
                        dialog.dismiss();
                        C.INSTANCE.errorToast(getApplicationContext() , t);
                    }
                });
            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in3=new Intent(getApplicationContext(),CreateCampaignTermsNextActivity.class);
                in3.putExtra("campaignList",campaignList);
                startActivityForResult(in3,REQUEST_START_CAMPAIGN);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_START_CAMPAIGN && resultCode==RESULT_OK){
            setResult(RESULT_OK);
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
