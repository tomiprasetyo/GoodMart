package com.tomiprasetyo.goodmart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;
import com.tomiprasetyo.goodmart.R;
import com.tomiprasetyo.goodmart.adapters.CartAdapter;
import com.tomiprasetyo.goodmart.databinding.ActivityCartBinding;
import com.tomiprasetyo.goodmart.model.Product;

import java.util.ArrayList;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding binding;
    CartAdapter adapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        products = new ArrayList<>();

        Cart cart = TinyCartHelper.getCart();


        for(Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()) {
            Product product = (Product) item.getKey();
            int quantity = item.getValue();
            product.setQuantity(quantity);

            products.add(product);
        }

        adapter = new CartAdapter(this, products, new CartAdapter.CartListener() {
            @Override
            public void onQuantityChanged() {
                binding.subtotal.setText(String.format("Rp %.2f",cart.getTotalPrice()));
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        binding.cartList.setLayoutManager(layoutManager);
        binding.cartList.addItemDecoration(itemDecoration);
        binding.cartList.setAdapter(adapter);



        binding.subtotal.setText(String.format("Rp %.2f",cart.getTotalPrice()));

        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cart.getAllItemsWithQty().entrySet().size() !=0 )
                    startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
                else {
                    binding.continueBtn.setText("Oops! Empty Cart");
                    binding.continueBtn.setEnabled(false);
                    Toast.makeText(CartActivity.this, "Please, add Product to continue!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Shopping Cart");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.cart);


        ///Delete porduct form cart....

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Product deleteProduct=products.get(viewHolder.getAdapterPosition());
                int position = viewHolder.getAdapterPosition();
                products.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                cart.removeItem(deleteProduct);

                binding.subtotal.setText(String.format("Rp %.2f",cart.getTotalPrice()));
                Toast.makeText(CartActivity.this, "Product Deleted", Toast.LENGTH_SHORT).show();

                if(cart.getAllItemsWithQty().entrySet().size() !=0 ) {
                    BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.cart);
                    badgeDrawable.setVisible(true);
                    badgeDrawable.setNumber(cart.getAllItemsWithQty().entrySet().size());
                }
            }
        }).attachToRecyclerView(binding.cartList);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.cart:
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });



        if(cart.getAllItemsWithQty().entrySet().size() !=0 ) {
            BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.cart);
            badgeDrawable.setVisible(true);
            badgeDrawable.setNumber(cart.getAllItemsWithQty().entrySet().size());
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}