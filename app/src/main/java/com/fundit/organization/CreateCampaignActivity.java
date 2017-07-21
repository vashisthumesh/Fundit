package com.fundit.organization;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.model.ProductListResponse;

import java.util.ArrayList;
import java.util.Locale;

public class CreateCampaignActivity extends AppCompatActivity {

    TextView txt_browseFundspot;
    AutoCompleteTextView auto_searchFundspot;
    ArrayList<String> fundSpotNames = new ArrayList<>();

    AdminAPI adminAPI;
    AppPreference preference;

    String selectedFundSpotName = null;
    String selectedFundSpotID = null;
    ProductListResponse.Product product = null;

    RadioButton rdo_typeItem, rdo_typeGiftCard;
    EditText edt_itemName,edt_couponCost,edt_organizationSplit,edt_fundSplit,edt_maxLimitCoupon,edt_campaignDuration,edt_couponExpireDay,edt_finePrint;
    CheckBox chk_indefinite;

    int REQUEST_PRODUCT = 369;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_campaign);

        adminAPI= ServiceGenerator.getAPIClass();
        preference = new AppPreference(this);

        fetchIDs();
    }

    private void fetchIDs() {
        txt_browseFundspot=(TextView) findViewById(R.id.txt_browseFundspot);
        auto_searchFundspot=(AutoCompleteTextView) findViewById(R.id.auto_searchFundspot);

        txt_browseFundspot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),FundspotListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent,REQUEST_PRODUCT);
            }
        });

        edt_itemName = (EditText) findViewById(R.id.edt_itemName);
        edt_couponCost = (EditText) findViewById(R.id.edt_couponCost);
        edt_organizationSplit = (EditText) findViewById(R.id.edt_organizationSplit);
        edt_fundSplit = (EditText) findViewById(R.id.edt_fundSplit);
        edt_maxLimitCoupon = (EditText) findViewById(R.id.edt_maxLimitCoupon);
        edt_campaignDuration = (EditText) findViewById(R.id.edt_campaignDuration);
        edt_couponExpireDay = (EditText) findViewById(R.id.edt_couponExpireDay);
        edt_finePrint = (EditText) findViewById(R.id.edt_finePrint);

        rdo_typeItem = (RadioButton) findViewById(R.id.rdo_typeItem);
        rdo_typeGiftCard = (RadioButton) findViewById(R.id.rdo_typeGiftCard);

        chk_indefinite = (CheckBox) findViewById(R.id.chk_indefinite);

        edt_organizationSplit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String fundSplit = edt_organizationSplit.getText().toString().trim();

                if (fundSplit.isEmpty()) {
                    edt_fundSplit.setText("100");
                } else {
                    float fSplit = Float.parseFloat(fundSplit);

                    if (fSplit > 100) {
                        edt_organizationSplit.setText("99");
                        fSplit = 99;
                    }

                    float orgSplit = 100 - fSplit;
                    edt_fundSplit.setText(String.format(Locale.getDefault(), "%.2f", orgSplit));
                }
            }
        });

        auto_searchFundspot.setThreshold(1);

        auto_searchFundspot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_PRODUCT && resultCode == RESULT_OK && data!=null){
            selectedFundSpotName = data.getStringExtra("fundspotName");
            selectedFundSpotID = data.getStringExtra("fundspotID");
            product = (ProductListResponse.Product) data.getSerializableExtra("product");
            fillupSelectedData();
        }
    }

    private void fillupSelectedData() {
        auto_searchFundspot.setText(selectedFundSpotName);
        edt_itemName.setText(product.getName());
        edt_couponCost.setText(product.getPrice());
        edt_organizationSplit.setText(product.getOrganization_percent());
        edt_campaignDuration.setText(product.getCampaign_duration());
        edt_maxLimitCoupon.setText(product.getMax_limit_of_coupons());
        edt_finePrint.setText(product.getFine_print());
        edt_couponExpireDay.setText(product.getCoupon_expire_day());

        if(product.getType_id().equals(C.TYPE_GIFTCARD)){
            rdo_typeGiftCard.setChecked(true);
        }
        else {
            rdo_typeItem.setChecked(true);
        }
    }
}
