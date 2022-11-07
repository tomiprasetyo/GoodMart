package com.tomiprasetyo.goodmart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;
import com.tomiprasetyo.goodmart.R;
import com.tomiprasetyo.goodmart.adapters.CartAdapter;
import com.tomiprasetyo.goodmart.databinding.ActivityCheckoutBinding;
import com.tomiprasetyo.goodmart.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    ActivityCheckoutBinding binding;
    CartAdapter adapter;
    ArrayList<Product> products;
    double totalPrice = 0;
    final int tax = 7;
    ProgressDialog progressDialog;
    Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Processing...");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        products = new ArrayList<>();

        cart = TinyCartHelper.getCart();
        String str = "";
        int i=1;
        FirebaseFirestore dbroot = FirebaseFirestore.getInstance();


        for(Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()) {
            Product product = (Product) item.getKey();
            int quantity = item.getValue();
            product.setQuantity(quantity);

            products.add(product);

            str+=i+". "+product.getName()+'\n';
            Log.i("goodmart",str);
            i++;

            HashMap hashMap = new HashMap();
            hashMap.put("Products",str);

            String email = user.getEmail();
            dbroot.collection("goodmart").document(email+"orderedProduct")
                    .set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                        }
                    });
        }

        adapter = new CartAdapter(this, products, new CartAdapter.CartListener() {
            @Override
            public void onQuantityChanged() {
                binding.subtotal.setText(String.format("Rp %.2f",cart.getTotalPrice())+"৳");
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        binding.cartList.setLayoutManager(layoutManager);
        binding.cartList.addItemDecoration(itemDecoration);
        binding.cartList.setAdapter(adapter);

        binding.subtotal.setText(String.format("Rp %.2f",cart.getTotalPrice()));

        totalPrice = (cart.getTotalPrice().doubleValue() * tax / 100) + cart.getTotalPrice().doubleValue();
        binding.total.setText("Rp " + totalPrice);

        String taxText = tax + "%";
        binding.tax.setText(taxText);

        //Data fetching from firestore....

        if (user != null) {
            String email = user.getEmail();
            dbroot.collection("goodmart").document(email)
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                String name = documentSnapshot.getString("Name");
                                String phone = documentSnapshot.getString("Phone");
                                String address = documentSnapshot.getString("Address");

                                binding.nameBox.setText(name);
                                binding.phoneBox.setText(phone);
                                binding.addressBox.setText(address);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CheckoutActivity.this, "No Such Data!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        binding.checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.nameBox.getText().toString().isEmpty() || binding.phoneBox.getText().toString().isEmpty() || binding.addressBox.getText().toString().isEmpty()){
                    Toast.makeText(CheckoutActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                }
                else processOrder();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Check Out");
    }

    int i=0;
    int val = (int)(Math.random()*(1000000-100000)+10000000);

    void processOrder() {
//        progressDialog.show();
//        progressDialog.dismiss();
        new AlertDialog.Builder(CheckoutActivity.this)
                .setTitle("Order Successful")
                .setCancelable(false)
                .setMessage("Your order number is: " + val)
                .setPositiveButton("Pay Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(CheckoutActivity.this, PaymentActivity.class);
                        intent.putExtra("orderCode", ""+val);
                        intent.putExtra("total",totalPrice);
                        startActivity(intent);
                    }
                }).show();}

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}