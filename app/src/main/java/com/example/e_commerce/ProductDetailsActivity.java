package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.e_commerce.Model.Products;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductDetailsActivity extends AppCompatActivity {

    private ImageView productImage1,productImage2,productImage3,productImage4,productImage5,productImage6;
    private TextView productPrice, productDescription, productName;
    private String productID = "";
    private String CategoryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pid");
        CategoryName=getIntent().getStringExtra("category");

        productImage1 = (ImageView) findViewById(R.id.product_image1);
        productImage2 = (ImageView) findViewById(R.id.product_image2);
        productImage3 = (ImageView) findViewById(R.id.product_image3);
        productImage4 = (ImageView) findViewById(R.id.product_image4);
        productImage5 = (ImageView) findViewById(R.id.product_image5);
        productImage6 = (ImageView) findViewById(R.id.product_image6);

        productName = (TextView) findViewById(R.id.product_name_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);

        getProductDetails(productID);
    }

    private void getProductDetails(String productID)
    {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(CategoryName);

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    Products products = dataSnapshot.getValue(Products.class);

                    productName.setText(products.getPname());
                    productPrice.setText( "Price - "+products.getPrice()+" ₹");
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage1()).into(productImage1);
                    if(products.getImage2()!=null)
                        Picasso.get().load(products.getImage2()).into(productImage2);
                    else
                        productImage2.setVisibility(View.GONE);

                    if(products.getImage3()!=null)
                        Picasso.get().load(products.getImage3()).into(productImage3);
                    else
                        productImage3.setVisibility(View.GONE);

                    if(products.getImage4()!=null)
                        Picasso.get().load(products.getImage4()).into(productImage4);
                    else
                        productImage4.setVisibility(View.GONE);

                    if(products.getImage5()!=null)
                        Picasso.get().load(products.getImage5()).into(productImage5);
                    else
                        productImage5.setVisibility(View.GONE);

                    if(products.getImage6()!=null)
                        Picasso.get().load(products.getImage6()).into(productImage6);
                    else
                        productImage6.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}