package com.fundit.fragmet;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.adapter.ProductListAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.fundspot.AddProductActivity;
import com.fundit.helper.CustomDialog;
import com.fundit.model.AppModel;
import com.fundit.model.ProductListResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProductsFragment extends Fragment {

    ListView list_products;
    FloatingActionButton btn_floating_add;
    List<ProductListResponse.Product> productList = new ArrayList<>();
    ProductListAdapter productListAdapter;
    AdminAPI adminAPI;
    AppPreference preference;
    CustomDialog dialog;

    View view;
    int PRODUCT_REQUEST = 136;

    public MyProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_products, container, false);
        adminAPI = ServiceGenerator.getAPIClass();
        preference = new AppPreference(getContext());
        dialog = new CustomDialog(getActivity());

        if(preference.isSkiped()){
            Intent intent = new Intent(getContext(), AddProductActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, PRODUCT_REQUEST);

        }



        fetchIDs();

        return view;
    }

    private void fetchIDs() {
        list_products = (ListView) view.findViewById(R.id.list_products);
        btn_floating_add = (FloatingActionButton) view.findViewById(R.id.btn_floating_add);

        productListAdapter = new ProductListAdapter(productList, getActivity());
        productListAdapter.setOnProductClick(new ProductListAdapter.OnProductClick() {
            @Override
            public void setOnProductEditClickListener(int position, String productID) {
                Intent intent = new Intent(getContext(), AddProductActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("editMode", true);
                intent.putExtra("product", productList.get(position));
                startActivityForResult(intent, PRODUCT_REQUEST);
            }

            @Override
            public void setOnProductDeleteClickListener(int position, String productID) {
                askToDeleteProduct(position);
            }

            @Override
            public void setOnProductSelectedListener(int position, String productID) {

            }
        });
        list_products.setAdapter(productListAdapter);
        getProductList();
        btn_floating_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddProductActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, PRODUCT_REQUEST);
            }
        });
    }

    private void askToDeleteProduct(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm to delete");
        builder.setMessage("Are you sure to delete this product?");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                deleteProduct(position);
            }
        });
        AlertDialog dialogB = builder.create();
        dialogB.show();
    }

    private void deleteProduct(final int position) {
        dialog.show();
        Call<AppModel> modelCall = adminAPI.deleteProduct(preference.getUserID(), preference.getTokenHash(), productList.get(position).getId());
        modelCall.enqueue(new Callback<AppModel>() {
            @Override
            public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                dialog.dismiss();
                AppModel model = response.body();
                if (model != null) {
                    C.INSTANCE.showToast(getContext(), model.getMessage());
                    if (model.isStatus()) {
                        productList.remove(position);
                        productListAdapter.notifyDataSetChanged();
                    }
                } else {
                    C.INSTANCE.defaultError(getContext());
                }
            }

            @Override
            public void onFailure(Call<AppModel> call, Throwable t) {
                dialog.dismiss();
                C.INSTANCE.errorToast(getContext(), t);
            }
        });
    }

    private void getProductList() {
        dialog.show();
        Call<ProductListResponse> productListCall = adminAPI.getProductList(preference.getUserID(), preference.getTokenHash());
        productListCall.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                dialog.dismiss();
                productList.clear();
                productListAdapter.notifyDataSetChanged();
                ProductListResponse productListResponse = response.body();
                if (productListResponse != null) {
                    productList.addAll(productListResponse.getData());
                } else {
                    C.INSTANCE.defaultError(getContext());
                }

                productListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                dialog.dismiss();
                C.INSTANCE.errorToast(getContext(), t);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PRODUCT_REQUEST && resultCode == RESULT_OK) {
            getProductList();
        }
    }
}
