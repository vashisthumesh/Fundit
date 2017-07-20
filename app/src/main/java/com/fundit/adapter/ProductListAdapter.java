package com.fundit.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fundit.R;
import com.fundit.model.ProductListResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nivida new on 20-Jul-17.
 */

public class ProductListAdapter extends BaseAdapter {

    List<ProductListResponse.Product> productList = new ArrayList<>();
    Activity activity;
    LayoutInflater inflater;

    public ProductListAdapter(List<ProductListResponse.Product> productList, Activity activity) {
        this.productList = productList;
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
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
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.layout_product_item, parent, false);

        TextView txt_productName = (TextView) view.findViewById(R.id.txt_productName);
        TextView txt_price = (TextView) view.findViewById(R.id.txt_price);
        TextView txt_productDescription = (TextView) view.findViewById(R.id.txt_productDescription);

        ImageView img_productImage = (ImageView) view.findViewById(R.id.img_productImage);
        ImageView img_edit = (ImageView) view.findViewById(R.id.img_edit);
        ImageView img_delete = (ImageView) view.findViewById(R.id.img_delete);


        return view;
    }
}
