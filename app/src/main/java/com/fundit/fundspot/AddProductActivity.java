package com.fundit.fundspot;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.helper.FilePath;
import com.fundit.model.AppModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {

    private static final int IMAGE_REQUEST = 155;
    EditText edt_productName, edt_description, edt_price, edt_fundSplit, edt_organizationSplit, edt_campaignDuration, edt_couponExpireDay, edt_maxLimitCoupon;

    ImageView img_productImage;
    Button btn_cancel, btn_addProduct;

    RadioGroup rg_productType;

    AdminAPI adminAPI;
    AppPreference preference;
    CustomDialog dialog;

    String imagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        dialog = new CustomDialog(this);
        preference = new AppPreference(this);
        adminAPI = ServiceGenerator.getAPIClass();

        fetchIDs();
    }

    private void fetchIDs() {
        edt_productName = (EditText) findViewById(R.id.edt_productName);
        edt_description = (EditText) findViewById(R.id.edt_description);
        edt_price = (EditText) findViewById(R.id.edt_price);
        edt_fundSplit = (EditText) findViewById(R.id.edt_fundSplit);
        edt_organizationSplit = (EditText) findViewById(R.id.edt_organizationSplit);
        edt_campaignDuration = (EditText) findViewById(R.id.edt_campaignDuration);
        edt_couponExpireDay = (EditText) findViewById(R.id.edt_couponExpireDay);
        edt_maxLimitCoupon = (EditText) findViewById(R.id.edt_maxLimitCoupon);

        img_productImage = (ImageView) findViewById(R.id.img_productImage);
        rg_productType = (RadioGroup) findViewById(R.id.rg_productType);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_addProduct = (Button) findViewById(R.id.btn_addProduct);

        img_productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_REQUEST);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edt_productName.getText().toString().trim();
                String description = edt_description.getText().toString().trim();
                String price = edt_price.getText().toString().trim();
                String fundSplit = edt_fundSplit.getText().toString().trim();
                String orgSplit = edt_organizationSplit.getText().toString().trim();
                String campaignDuration = edt_campaignDuration.getText().toString().trim();
                String maxLimitCoupon = edt_maxLimitCoupon.getText().toString().trim();
                String couponExpiryDays = edt_couponExpireDay.getText().toString().trim();

                float productPrice = Float.parseFloat(price.isEmpty() ? "0" : price);
                float fundSplitPer = Float.parseFloat(fundSplit.isEmpty() ? "0" : fundSplit);
                float orgSplitPer = Float.parseFloat(orgSplit.isEmpty() ? "0" : orgSplit);
                int campDuration = Integer.parseInt(campaignDuration.isEmpty() ? "0" : campaignDuration);
                int maxLimitCouponNum = Integer.parseInt(maxLimitCoupon.isEmpty() ? "0" : maxLimitCoupon);
                int couponExpiryDaysNum = Integer.parseInt(couponExpiryDays.isEmpty() ? "0" : couponExpiryDays);

                Log.e("percent", fundSplitPer + " - " + orgSplitPer);

                if (name.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter product name");
                } else if (description.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter product description");
                } else if (productPrice < 1) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter product price min. $1");
                } else if (fundSplitPer < 1) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter FundSpot split min. 1%");
                } else if (orgSplitPer < 1) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter Organization split min. 1%");
                } else if (campDuration < 1) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter campaign duration min. 1 day");
                } else if (maxLimitCouponNum < 1) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter max. coupon limit at least 1");
                } else if (couponExpiryDaysNum < 1) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please enter coupon expiry days min. 1");
                } else if (imagePath == null || imagePath.isEmpty()) {
                    C.INSTANCE.showToast(getApplicationContext(), "Please select product image");
                } else {

                    int checkedTypeID = rg_productType.getCheckedRadioButtonId();
                    String typeID = checkedTypeID == R.id.rdo_typeItem ? C.TYPE_PRODUCT : C.TYPE_GIFTCARD;

                    dialog.show();
                    Call<AppModel> addProductCall = adminAPI.addMyProduct(preference.getUserID(), preference.getTokenHash(), typeID, name, description, price, fundSplit, orgSplit, campaignDuration, maxLimitCoupon, couponExpiryDays, ServiceGenerator.prepareFilePart("image", imagePath));
                    addProductCall.enqueue(new Callback<AppModel>() {
                        @Override
                        public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                            dialog.dismiss();
                            AppModel model = response.body();
                            if (model != null) {
                                C.INSTANCE.showToast(getApplicationContext(), model.getMessage());
                                if (model.isStatus()) {
                                    setResult(RESULT_OK);
                                    onBackPressed();
                                }
                            } else {
                                C.INSTANCE.defaultError(getApplicationContext());
                            }
                        }

                        @Override
                        public void onFailure(Call<AppModel> call, Throwable t) {
                            dialog.dismiss();
                            C.INSTANCE.errorToast(getApplicationContext(), t);
                        }
                    });
                }

            }
        });

        edt_fundSplit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String fundSplit = edt_fundSplit.getText().toString().trim();

                if (fundSplit.isEmpty()) {
                    edt_organizationSplit.setText("100");
                } else {
                    float fSplit = Float.parseFloat(fundSplit);

                    if (fSplit > 100) {
                        edt_fundSplit.setText("99");
                        fSplit = 99;
                    }

                    float orgSplit = 100 - fSplit;
                    edt_organizationSplit.setText(String.format(Locale.getDefault(), "%.2f", orgSplit));
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                Uri dataUri = data.getData();

                Log.e("URI", dataUri.toString());

                imagePath = FilePath.getPath(this, dataUri);
                Picasso.with(this)
                        .load(new File(imagePath))
                        .into(img_productImage);
            } else {
                C.INSTANCE.showToast(getApplication(), "Sorry, image not found");
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
