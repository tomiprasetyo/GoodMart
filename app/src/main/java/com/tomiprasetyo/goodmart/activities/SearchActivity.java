package com.tomiprasetyo.goodmart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tomiprasetyo.goodmart.R;
import com.tomiprasetyo.goodmart.adapters.ProductAdapter;
import com.tomiprasetyo.goodmart.databinding.ActivitySearchBinding;
import com.tomiprasetyo.goodmart.model.Product;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding binding;
    ProductAdapter productAdapter;
    ArrayList<Product> products;
    DatabaseReference database;
    RecyclerView productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this, products);


        String query = getIntent().getStringExtra("query");

        getSupportActionBar().setTitle("Searching for "+ "\""+query+"\"");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getProducts(query);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.productList.setLayoutManager(layoutManager);
        binding.productList.setAdapter(productAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    void getProducts(String query) {

        //Firebase for fetching data. and searching data...
        productList = findViewById(R.id.productList);
        database = FirebaseDatabase.getInstance().getReference("goodmart");
        productList.setHasFixedSize(true);
        productList.setLayoutManager(new LinearLayoutManager(this));
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        productList.setLayoutManager((layoutManager));


        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this,products);
        productList.setAdapter(productAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Product product = dataSnapshot.getValue(Product.class);
                    if(product.getName().toLowerCase().contains(query.toLowerCase()))
                        products.add(product);
                }
                if(products.size() == 0)
                    Toast.makeText(SearchActivity.this, "Oops! No Product found...", Toast.LENGTH_SHORT).show();
                else Toast.makeText(SearchActivity.this, products.size()+" Similar Product found", Toast.LENGTH_SHORT).show();

                productAdapter.notifyDataSetChanged();
                //Log.i("Saim",products.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
}