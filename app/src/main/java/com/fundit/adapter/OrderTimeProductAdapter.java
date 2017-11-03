package com.fundit.adapter;

import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fundit.CartItemClickListener;
import com.fundit.R;
import com.fundit.a.W;
import com.fundit.model.MultipleProductResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by NWSPL-17 on 17-Aug-17.
 */

public class OrderTimeProductAdapter extends BaseAdapter {

    List<MultipleProductResponse> productResponses = new ArrayList<>();
    Context context;
    LayoutInflater inflater;
    OnClick onClick;

    String imagePath = "";



    public OrderTimeProductAdapter(List<MultipleProductResponse> productResponses, Context context , OnClick onClick) {
        this.productResponses = productResponses;
        this.context = context;
        this.onClick = onClick;
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
        View view = inflater.inflate(R.layout.layout_ordertime_list, parent, false);


        TextView txt_productName = (TextView) view.findViewById(R.id.txt_productName);
        TextView txt_price = (TextView) view.findViewById(R.id.txt_price);
        TextView txt_type = (TextView) view.findViewById(R.id.txt_type);
        TextView txt_typ = (TextView) view.findViewById(R.id.txt_typ);
        final ImageView img_plus = (ImageView) view.findViewById(R.id.img_plus);
        final ImageView img_minus = (ImageView) view.findViewById(R.id.img_minus);
        final EditText edt_qty = (EditText) view.findViewById(R.id.edt_qty);
        CheckBox check_product = (CheckBox) view.findViewById(R.id.check_product);
        txt_type.setVisibility(View.GONE);
        txt_typ.setVisibility(View.GONE);
        TextView txt_productDescription = (TextView) view.findViewById(R.id.txt_productDescription);

        CircleImageView img_productImage = (CircleImageView) view.findViewById(R.id.img_productImage);


        txt_productName.setText(productResponses.get(position).getName());
        txt_price.setText(productResponses.get(position).getPrice());
        txt_productDescription.setText(productResponses.get(position).getDescription());

        imagePath = W.FILE_URL + productResponses.get(position).getImage();

        Picasso.with(context)
                .load(imagePath)
                .into(img_productImage);


        img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currentQty = edt_qty.getText().toString().trim();
                edt_qty.setCursorVisible(false);

                currentQty = currentQty.isEmpty() ? "0" : currentQty;
                int Quantity = Integer.parseInt(currentQty);

                Quantity++;

                productResponses.get(position).setQty(Quantity);
                edt_qty.setText(String.valueOf(Quantity));


            }
        });

        img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String currentQty = edt_qty.getText().toString().trim();
                currentQty = currentQty.isEmpty() ? "0" : currentQty;
                edt_qty.setCursorVisible(false);
                int quantity = Integer.parseInt(currentQty);

                if (quantity >= 1) {
                    quantity = quantity - 1;
                    productResponses.get(position).setQty(quantity);
                    edt_qty.setText(String.valueOf(quantity));
                }


            }
        });


        edt_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String qty = edt_qty.getText().toString().trim();

                if(!qty.isEmpty()){
                    int quantity=Integer.parseInt(qty);

                    if(quantity>0){
                        productResponses.get(position).setQty(Integer.parseInt(qty));
                        float totalPrice = Integer.parseInt(productResponses.get(position).getPrice()) *
                                productResponses.get(position).getQty();

                        productResponses.get(position).setTotal_price(String.valueOf(totalPrice));

                        onClick.UpdateTotalPrice(totalPrice);

                    }

                }

            }


        });


        check_product.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    img_plus.setEnabled(true);
                    img_minus.setEnabled(true);
                    edt_qty.setText("1");
                    productResponses.get(position).setQty(1);
                    edt_qty.setEnabled(true);
                    img_plus.setClickable(true);
                    img_minus.setClickable(true);


                } else {

                    edt_qty.setText("");
                    edt_qty.setHint("0");
                    productResponses.get(position).setQty(0);
                    edt_qty.setEnabled(false);
                    img_plus.setClickable(false);
                    img_minus.setClickable(false);


                }

                productResponses.get(position).setChecked(isChecked);
            }
        });


        return view;
    }

    public List<MultipleProductResponse> getProducts() {

        List<MultipleProductResponse> getSelectedProducts = new ArrayList<>();
        for (int i = 0; i < productResponses.size(); i++) {

            if (productResponses.get(i).isChecked()) {

                getSelectedProducts.add(productResponses.get(i));


            }

        }

        return getSelectedProducts;


    }


    public interface OnClick{

        void UpdateTotalPrice(float totalPrice);
    }



}
