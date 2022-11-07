package com.tomiprasetyo.goodmart.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;
import com.tomiprasetyo.goodmart.R;
import com.tomiprasetyo.goodmart.databinding.ActivityProductDetailsBinding;
import com.tomiprasetyo.goodmart.model.Product;

public class ProductDetailsActivity extends AppCompatActivity {

    TextView productDescription;

    ActivityProductDetailsBinding binding;
    Product currentProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String name = getIntent().getStringExtra("name");
        String image = getIntent().getStringExtra("image");
        int id = getIntent().getIntExtra("id",0);
        int stock= getIntent().getIntExtra("stock",5);
        double price = getIntent().getDoubleExtra("price",0);
        double discount = getIntent().getDoubleExtra("discount",0);
        String status = getIntent().getStringExtra("status");
        String category = getIntent().getStringExtra("category");

        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productDescription = findViewById(R.id.productDescription);
        productDescription.setText(status);

        binding.productPrice.setText("Price: Rp " + price);

        Glide.with(this).load(image).into(binding.productImage);

        currentProduct = new Product(name,image,status,category,price,discount,stock,id);


        Cart cart = TinyCartHelper.getCart();


        binding.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.addItem(currentProduct,1);
                binding.addToCartBtn.setEnabled(false);
                binding.addToCartBtn.setText("Product added in cart");
                Toast.makeText(ProductDetailsActivity.this, "Product added", Toast.LENGTH_SHORT).show();
            }
        });


    }


}