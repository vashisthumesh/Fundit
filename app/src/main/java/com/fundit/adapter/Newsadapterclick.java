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
import com.fundit.model.News_model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by NWSPL6 on 1/22/2018.
 */

public class Newsadapterclick extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    List<News_model.Product> productList = new ArrayList<>();
    String imagePath = "";

    public Newsadapterclick( List<News_model.Product> productList ,Context context) {
        this.productList=productList;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
        View view = inflater.inflate(R.layout.news_layout_adapterlayout, parent, false);
        TextView txt_name= (TextView) view.findViewById(R.id.txtname);
        TextView selling_price= (TextView) view.findViewById(R.id.selling_price);
        TextView txt_comment= (TextView) view.findViewById(R.id.comment);
        CircleImageView img_productImage = (CircleImageView) view.findViewById(R.id.img_productImage);
        LinearLayout news_layout= (LinearLayout) view.findViewById(R.id.news_layout);

        txt_name.setText(productList.get(position).getName());



        selling_price.setText("Selling Price: $ "+ String.format("%.2f" , Double.parseDouble(productList.get(position).getPrice())));
        txt_comment.setText(productList.get(position).getDescription());





        imagePath = W.FILE_URL + productList.get(position).getImage();

        Picasso.with(context)
                .load(imagePath)
                .into(img_productImage);

        news_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, ProductActivity.class);
                i.putExtra("name", productList.get(position).getName());
                i.putExtra("price",productList.get(position).getPrice());
                i.putExtra("Desc",productList.get(position).getDescription());
                i.putExtra("fine",productList.get(position).getFine_print());
                i.putExtra("image",productList.get(position).getImage());
                i.putExtra("myproduct",false);
                i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });


        return view;

    }
}
