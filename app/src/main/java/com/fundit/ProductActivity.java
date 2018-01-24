package com.fundit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fundit.a.W;
import com.fundit.fundspot.AddProductActivity;
import com.fundit.model.ProductListResponse;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductActivity extends AppCompatActivity {


    TextView txttitle,txt_sellprice,txt_desc,txt_fine;
    ImageView img_edit1;
    CircleImageView img_Pics;
    String name="";
    String price="";
    String Desc="";
    String fine="";
    String image="";
    Boolean myproduct=false;
    ProductListResponse.Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        txttitle= (TextView) findViewById(R.id.txttitle);
        txt_sellprice= (TextView) findViewById(R.id.txt_sellprice);
        txt_desc= (TextView) findViewById(R.id.txt_desc);
        txt_fine= (TextView) findViewById(R.id.txt_fine);
         img_Pics = (CircleImageView) findViewById(R.id.img_Pics);
        img_edit1= (ImageView) findViewById(R.id.img_edit1);



        final Intent i=getIntent();
      name= i.getStringExtra("name");
       price= i.getStringExtra("price");
       Desc= i.getStringExtra("Desc");
     fine=i.getStringExtra("fine");
        image=i.getStringExtra("image");
        myproduct=i.getBooleanExtra("myproduct",false);
        product = (ProductListResponse.Product) i.getSerializableExtra("product");



        if(myproduct == true)
        {
            img_edit1.setVisibility(View.VISIBLE);
        }
        else {
            img_edit1.setVisibility(View.GONE);
        }

        img_edit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this, AddProductActivity.class);
                intent.putExtra("editMode", true);
                intent.putExtra("product", product);
               startActivity(intent);
            }
        });

        setUpToolbar();
        txttitle.setText(name);
        txt_sellprice.setText("$" +String.format("%.2f",Double.parseDouble(price)));
        txt_desc.setText(Desc);
        txt_fine.setText(fine);

        String getImage = "";

        getImage = W.FILE_URL +  image;

        Picasso.with(ProductActivity.this)
                .load(getImage)
                .into(img_Pics);

    }
    private void setUpToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }
}
