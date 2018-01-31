package com.fundit.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fundit.ProductActivity;
import com.fundit.R;
import com.fundit.a.W;
import com.fundit.model.MultipleProductResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by NWSPL-17 on 09-Aug-17.
 */

public class GetAllProductsAdapter extends BaseAdapter {

    List<MultipleProductResponse> productResponses = new ArrayList<>();
    Context context;
    LayoutInflater inflater;

    String imagePath = "";


    public GetAllProductsAdapter(List<MultipleProductResponse> productResponses, Context context) {
        this.productResponses = productResponses;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return productResponses.size();
    }

    @Override
    public Object getItem(int position) {
        return productResponses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.layout_product_item, parent, false);

        LinearLayout product_layout= (LinearLayout) view.findViewById(R.id.product_layout);
        TextView txt_productName = (TextView) view.findViewById(R.id.txt_productName);
        TextView txt_price = (TextView) view.findViewById(R.id.txt_price);
        TextView txt_type = (TextView) view.findViewById(R.id.txt_type);
        TextView txt_typ = (TextView) view.findViewById(R.id.txt_typ);
        txt_type.setVisibility(View.GONE);
        txt_typ.setVisibility(View.GONE);
        TextView txt_productDescription = (TextView) view.findViewById(R.id.txt_productDescription);

        CircleImageView img_productImage = (CircleImageView) view.findViewById(R.id.img_productImage);

        LinearLayout layout_options = (LinearLayout) view.findViewById(R.id.layout_options);

        layout_options.setVisibility(View.GONE);


        txt_productName.setText(productResponses.get(position).getName());
        txt_price.setText("$" + String.format("%.2f" , Double.parseDouble(productResponses.get(position).getPrice())));
        txt_productDescription.setText(productResponses.get(position).getDescription());

        imagePath = W.FILE_URL + productResponses.get(position).getImage();

        Picasso.with(context)
                .load(imagePath)
                .into(img_productImage);

        product_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, ProductActivity.class);
                i.putExtra("name", productResponses.get(position).getName());
                i.putExtra("price",productResponses.get(position).getPrice());
                i.putExtra("Desc",productResponses.get(position).getDescription());
                i.putExtra("fine",productResponses.get(position).getFine_print());
                i.putExtra("image",productResponses.get(position).getImage());
                i.putExtra("myproduct",false);
                i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });


        return view;
    }
}
