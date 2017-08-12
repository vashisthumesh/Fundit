package com.fundit.organization;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.W;
import com.fundit.adapter.ProductListAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.apis.StringConverterFactory;
import com.fundit.helper.CustomDialog;
import com.fundit.model.ProductListResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FundspotProductListActivity extends AppCompatActivity {

    ListView list_products;
    TextView txt_fundspotName;

    String fundSpotID = null;
    String fundspotName = "";
    String selectedOrganizationID = "";

    AdminAPI adminAPI;
    AppPreference preference;
    List<ProductListResponse.Product> productList = new ArrayList<>();
    ProductsListAdapter productListAdapter;

    CheckBox chk_items, chk_giftCard;

    CustomDialog dialog;

    Button btn_select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fundspot_product_list);

        adminAPI = ServiceGenerator.getAPIClass();
        preference = new AppPreference(this);
        dialog = new CustomDialog(this);

        Intent intent = getIntent();
        fundSpotID = intent.getStringExtra("fundspotID");

        fundspotName = intent.getStringExtra("fundspotName");


        selectedOrganizationID = intent.getStringExtra("organizationID");

        Log.e("GetName" , fundspotName);
        fetchIDs();
        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionTitle.setText("Select Product");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void fetchIDs() {
        txt_fundspotName = (TextView) findViewById(R.id.txt_fundspotName);
        list_products = (ListView) findViewById(R.id.list_products);
        productListAdapter = new ProductsListAdapter(productList, this, true);
        list_products.setAdapter(productListAdapter);
        chk_items = (CheckBox) findViewById(R.id.chk_items);
        chk_giftCard = (CheckBox) findViewById(R.id.chk_giftCard);
        txt_fundspotName.setText(fundspotName);

        btn_select = (Button) findViewById(R.id.btn_select);


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

        /*list_products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent();
                intent.putExtra("fundspotName", fundspotName);
                intent.putExtra("fundspotID", fundSpotID);
                intent.putExtra("product", productList.get(position));
                setResult(RESULT_OK, intent);
                onBackPressed();
            }
        });
*/
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> selectedProductsId = new ArrayList<>();

                List<ProductListResponse.Product> selectedProducts = productListAdapter.getSelectedProducts();

                if (selectedProducts.size() > 0) {

                    for (int i = 0; i < selectedProducts.size(); i++) {
                        Log.e("getProductsDetails", "" + selectedProducts.get(i).getId());

                        selectedProductsId.add(selectedProducts.get(i).getId());
                    }

                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("ProductsId" , selectedProductsId);
                    intent.putExtra("fundspotName", fundspotName);
                    intent.putExtra("organizationID" , selectedOrganizationID);
                    intent.putExtra("fundspotID", fundSpotID);
                    setResult(RESULT_OK , intent);
                    onBackPressed();
                } else {

                    C.INSTANCE.showToast(getApplicationContext(), "Please Select Products");

                }


            }
        });


    }

    private void listProductAfterChecks() {
        if (chk_items.isChecked() && chk_giftCard.isChecked()) {
            listOutProducts(null);
        } else if (chk_items.isChecked() && !chk_giftCard.isChecked()) {
            listOutProducts(C.TYPE_PRODUCT);
        } else if (!chk_items.isChecked() && chk_giftCard.isChecked()) {
            listOutProducts(C.TYPE_GIFTCARD);
        } else {
            productList.clear();
            productListAdapter.notifyDataSetChanged();
            C.INSTANCE.showToast(getApplicationContext(), "Please select any type to list products");
        }
    }

    private void listOutProducts(String typeID) {
        dialog.show();
        productList.clear();
        productListAdapter.notifyDataSetChanged();
        Call<ProductListResponse> productCall = adminAPI.fundSpotProducts(preference.getUserID(), preference.getTokenHash(), fundSpotID, typeID);
        productCall.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                dialog.dismiss();
                ProductListResponse listResponse = response.body();
                if (listResponse != null) {
                    if (listResponse.isStatus()) {
                        productList.addAll(listResponse.getData());
                    } else {
                        C.INSTANCE.showToast(getApplicationContext(), listResponse.getMessage());
                    }
                } else {
                    C.INSTANCE.defaultError(getApplicationContext());
                }

                productListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                dialog.dismiss();
                C.INSTANCE.errorToast(getApplicationContext(), t);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public class ProductsListAdapter extends BaseAdapter {

        List<ProductListResponse.Product> productList = new ArrayList<>();
        Activity activity;
        LayoutInflater inflater;
        AdminAPI adminAPI;
        com.fundit.adapter.ProductListAdapter.OnProductClick onProductClick;
        boolean selectOnly = false;

        public ProductsListAdapter(List<ProductListResponse.Product> productList, Activity activity) {
            this.productList = productList;
            this.activity = activity;
            this.inflater = activity.getLayoutInflater();
        }

        public ProductsListAdapter(List<ProductListResponse.Product> productList, Activity activity, boolean selectOnly) {
            this(productList, activity);
            this.selectOnly = selectOnly;
        }

        public void setOnProductClick(com.fundit.adapter.ProductListAdapter.OnProductClick onProductClick) {
            this.onProductClick = onProductClick;
        }

        @Override
        public int getCount() {
            return productList.size();
        }

        @Override
        public Object getItem(int position) {
            return productList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View view = inflater.inflate(R.layout.layout_product_item, parent, false);

            TextView txt_productName = (TextView) view.findViewById(R.id.txt_productName);
            TextView txt_price = (TextView) view.findViewById(R.id.txt_price);
            TextView txt_type = (TextView) view.findViewById(R.id.txt_type);
            TextView txt_productDescription = (TextView) view.findViewById(R.id.txt_productDescription);

            CircleImageView img_productImage = (CircleImageView) view.findViewById(R.id.img_productImage);
            ImageView img_edit = (ImageView) view.findViewById(R.id.img_edit);
            ImageView img_delete = (ImageView) view.findViewById(R.id.img_delete);
            CheckBox checkedProducts = (CheckBox) view.findViewById(R.id.check_product);

            LinearLayout layout_options = (LinearLayout) view.findViewById(R.id.layout_options);

            if (selectOnly) {
                //  layout_options.setVisibility(View.GONE);
                img_delete.setVisibility(View.GONE);
                img_edit.setVisibility(View.GONE);
            }

            txt_productName.setText(productList.get(position).getName());
            txt_productDescription.setText(productList.get(position).getDescription());
            txt_price.setText("$" + productList.get(position).getPrice());

            if (productList.get(position).getType_id().equals(C.TYPE_PRODUCT)) {
                txt_type.setText("Product Item");
            } else {
                txt_type.setText("Gift Card Value");
            }

            Picasso.with(activity)
                    .load(W.FILE_URL + productList.get(position).getImage())
                    .into(img_productImage);

            img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onProductClick != null) {
                        onProductClick.setOnProductDeleteClickListener(position, productList.get(position).getId());
                    }
                }
            });

            img_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onProductClick != null) {
                        onProductClick.setOnProductEditClickListener(position, productList.get(position).getId());
                    }
                }
            });


            checkedProducts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {

                        productList.get(position).setChecked(isChecked);

                        String id = productList.get(position).getId().toString().trim();

                        Log.e("getProductId", "-->" + id);

                    }


                    productList.get(position).setChecked(isChecked);


                }
            });


            return view;
        }

        public List<ProductListResponse.Product> getSelectedProducts() {

            List<ProductListResponse.Product> selctedProducts = new ArrayList<>();

            for (int i = 0; i < productList.size(); i++) {

                if (productList.get(i).isChecked()) {
                    selctedProducts.add(productList.get(i));
                }
            }


            return selctedProducts;
        }
    }

}
