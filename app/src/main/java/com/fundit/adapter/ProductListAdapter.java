package com.fundit.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fundit.R;
import com.fundit.a.C;
import com.fundit.a.W;
import com.fundit.apis.AdminAPI;
import com.fundit.model.ProductListResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Nivida new on 20-Jul-17.
 */

public class ProductListAdapter extends BaseAdapter {

    List<ProductListResponse.Product> productList = new ArrayList<>();
    Activity activity;
    LayoutInflater inflater;
    AdminAPI adminAPI;
    OnProductClick onProductClick;
    boolean selectOnly=false;

    public ProductListAdapter(List<ProductListResponse.Product> productList, Activity activity) {
        this.productList = productList;
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
    }

    public ProductListAdapter(List<ProductListResponse.Product> productList, Activity activity, boolean selectOnly){
        this(productList,activity);
        this.selectOnly = selectOnly;
    }

    public void setOnProductClick(OnProductClick onProductClick) {
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

        LinearLayout layout_options=(LinearLayout) view.findViewById(R.id.layout_options);

        if(selectOnly){
            layout_options.setVisibility(View.GONE);
        }else{
            checkedProducts.setVisibility(View.GONE);
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

        return view;
    }

    public interface OnProductClick {
        void setOnProductEditClickListener(int position, String productID);

        void setOnProductDeleteClickListener(int position, String productID);

        void setOnProductSelectedListener(int position, String productID);

    }
}
