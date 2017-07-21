package com.fundit.organization;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.adapter.ProductListAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.ProductListResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FundspotProductListActivity extends AppCompatActivity {

    ListView list_products;
    TextView txt_fundspotName;

    String fundSpotID=null;
    String fundspotName="";

    AdminAPI adminAPI;
    AppPreference preference;
    List<ProductListResponse.Product> productList=new ArrayList<>();
    ProductListAdapter productListAdapter;

    CheckBox chk_items, chk_giftCard;

    CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fundspot_product_list);

        adminAPI= ServiceGenerator.getAPIClass();
        preference=new AppPreference(this);
        dialog=new CustomDialog(this);

        Intent intent=getIntent();
        fundSpotID = intent.getStringExtra("fundspotID");
        fundspotName = intent.getStringExtra("fundspotName");

        fetchIDs();
    }

    private void fetchIDs() {
        txt_fundspotName = (TextView) findViewById(R.id.txt_fundspotName);
        list_products = (ListView) findViewById(R.id.list_products);
        productListAdapter = new ProductListAdapter(productList,this,true);
        list_products.setAdapter(productListAdapter);
        chk_items = (CheckBox) findViewById(R.id.chk_items);
        chk_giftCard = (CheckBox) findViewById(R.id.chk_giftCard);
        txt_fundspotName.setText(fundspotName);

        listOutProducts(null);

        chk_items.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                listProductAfterChecks();
            }
        });

        chk_giftCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                listProductAfterChecks();
            }
        });

        list_products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent=new Intent();
                intent.putExtra("fundspotName",fundspotName);
                intent.putExtra("fundspotID",fundSpotID);
                intent.putExtra("product",productList.get(position));
                setResult(RESULT_OK,intent);
                onBackPressed();
            }
        });
    }

    private void listProductAfterChecks() {
        if(chk_items.isChecked() && chk_giftCard.isChecked()){
            listOutProducts(null);
        }
        else if(chk_items.isChecked() && !chk_giftCard.isChecked()){
            listOutProducts(C.TYPE_PRODUCT);
        }
        else if(!chk_items.isChecked() && chk_giftCard.isChecked()){
            listOutProducts(C.TYPE_GIFTCARD);
        }
        else {
            productList.clear();
            productListAdapter.notifyDataSetChanged();
            C.INSTANCE.showToast(getApplicationContext(),"Please select any type to list products");
        }
    }

    private void listOutProducts(String typeID){
        dialog.show();
        productList.clear();
        productListAdapter.notifyDataSetChanged();
        Call<ProductListResponse> productCall=adminAPI.fundSpotProducts(preference.getUserID(),preference.getTokenHash(),fundSpotID,typeID);
        productCall.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                dialog.dismiss();
                ProductListResponse listResponse=response.body();
                if(listResponse!=null){
                    if(listResponse.isStatus()){
                        productList.addAll(listResponse.getData());
                    }
                    else {
                        C.INSTANCE.showToast(getApplicationContext(),listResponse.getMessage());
                    }
                }
                else{
                    C.INSTANCE.defaultError(getApplicationContext());
                }

                productListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                dialog.dismiss();
                C.INSTANCE.errorToast(getApplicationContext(),t);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
