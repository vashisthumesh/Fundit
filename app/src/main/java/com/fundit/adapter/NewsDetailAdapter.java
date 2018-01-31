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

import com.fundit.ProductActivity;
import com.fundit.R;
import com.fundit.a.W;
import com.fundit.model.MultipleProductResponse;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;




public class NewsDetailAdapter extends BaseAdapter {

    List<MultipleProductResponse> productResponses = new ArrayList<>();
    Context context;
    LayoutInflater inflater;

    String imagePath = "";

    public NewsDetailAdapter(List<MultipleProductResponse> productResponses, Context context) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.news_detail, parent, false);
        TextView txt_name= (TextView) view.findViewById(R.id.txtname);
        TextView selling_price= (TextView) view.findViewById(R.id.selling_price);
        TextView txt_comment= (TextView) view.findViewById(R.id.comment);
        CircleImageView img_productImage = (CircleImageView) view.findViewById(R.id.img_productImage);
        LinearLayout news_layout= (LinearLayout) view.findViewById(R.id.news_layout);



        txt_name.setText(productResponses.get(position).getName());
        //selling_price.setText("Selling Price:"+ productResponses.get(position).getPrice());
        selling_price.setText("Selling Price: $ "+ String.format("%.2f" , Double.parseDouble(productResponses.get(position).getPrice())));
        txt_comment.setText(productResponses.get(position).getDescription());





        imagePath = W.FILE_URL + productResponses.get(position).getImage();

        Picasso.with(context)
                .load(imagePath)
                .into(img_productImage);



        news_layout.setOnClickListener(new View.OnClickListener() {
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
