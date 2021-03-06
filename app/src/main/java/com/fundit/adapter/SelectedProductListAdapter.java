package com.fundit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fundit.Bean.GetProductsFundspotBean;
import com.fundit.ProductActivity;
import com.fundit.R;
import com.fundit.a.W;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by NWSPL-17 on 08-Aug-17.
 */

public class SelectedProductListAdapter extends BaseAdapter {


    List<GetProductsFundspotBean> productsFundspotBeen = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public SelectedProductListAdapter(List<GetProductsFundspotBean> productsFundspotBeen, Context context) {
        this.productsFundspotBeen = productsFundspotBeen;
        this.context = context;

        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return productsFundspotBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return productsFundspotBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        View view = inflater.inflate(R.layout.layout_product_item , parent , false);

        LinearLayout product_layout= (LinearLayout) view.findViewById(R.id.product_layout);
        TextView txt_productName = (TextView) view.findViewById(R.id.txt_productName);
        TextView txt_price = (TextView) view.findViewById(R.id.txt_price);
        TextView txt_type = (TextView) view.findViewById(R.id.txt_type);
        TextView txt_typ = (TextView) view.findViewById(R.id.txt_typ);
        txt_type.setVisibility(View.GONE);
        txt_typ.setVisibility(View.GONE);
        TextView txt_productDescription = (TextView) view.findViewById(R.id.txt_productDescription);

        CircleImageView img_productImage = (CircleImageView) view.findViewById(R.id.img_productImage);

        LinearLayout layout_options=(LinearLayout) view.findViewById(R.id.layout_options);

        layout_options.setVisibility(View.GONE);



        txt_productName.setText(productsFundspotBeen.get(position).getName());
        txt_price.setText("$"+String.format("%.2f",Double.parseDouble(productsFundspotBeen.get(position).getPrice())));
        txt_productDescription.setText(productsFundspotBeen.get(position).getProductDescription());


        String imagePath = W.FILE_URL + productsFundspotBeen.get(position).getImage();


        Picasso.with(context)
                .load(imagePath)
                .into(img_productImage);

        product_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(context, ProductActivity.class);
                i.putExtra("name",productsFundspotBeen.get(position).getName());
                i.putExtra("price",productsFundspotBeen.get(position).getPrice());
                i.putExtra("Desc",productsFundspotBeen.get(position).getProductDescription());
                i.putExtra("fine",productsFundspotBeen.get(position).getFine_print());
                i.putExtra("image", productsFundspotBeen.get(position).getImage());
                i.putExtra("myproduct",false);
                i.putExtra("isCampaignTimes" , true);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);


            }
        });




        return view;
    }
}
