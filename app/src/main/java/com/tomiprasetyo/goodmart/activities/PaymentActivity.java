package com.tomiprasetyo.goodmart.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tomiprasetyo.goodmart.R;
import com.tomiprasetyo.goodmart.databinding.ActivityPaymentBinding;

public class PaymentActivity extends AppCompatActivity {

    ActivityPaymentBinding binding;
    ProgressDialog CODp;

    double total;
    String orderCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        orderCode = getIntent().getStringExtra("orderCode");
        total = getIntent().getDoubleExtra("total", 1);

        binding.COD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processCOD();
            }
        });

        binding.bkash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processGopay();
            }
        });

        binding.nagad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processShopeepay();
            }
        });

        binding.rocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processDana();
            }
        });

        binding.order.setText("Your Order Number is: " + orderCode);

        getSupportActionBar().setTitle("Payment Method");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    void processCOD(){
        new AlertDialog.Builder(this)
                .setTitle("Cash on Delivery")
                .setCancelable(false)
                .setMessage("Your products will be shipped within 24 hours. " +
                        "You can pay Rp " + total + " to the courier when you recieve the products at your doorstep.")
                .setPositiveButton("Confirm Order", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(PaymentActivity.this,HomeActivity.class);
                        Toast.makeText(PaymentActivity.this, "Order Confirmed", Toast.LENGTH_SHORT).show();
                        intent.putExtra("check","placed");
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }
    void processGopay(){
        new AlertDialog.Builder(this)
                .setTitle("Gopay")
                .setCancelable(false)
                .setMessage("Place your payment of Rp " + total + " through Gopay to 01608504435 within 2 hours with this Order no. Your products will then be shipped within 24 hours. " +
                        "If you do not pay within 2 hours, your order will be cancelled.")
                .setPositiveButton("Confirm Order", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(PaymentActivity.this,HomeActivity.class);
                        Toast.makeText(PaymentActivity.this, "Order Confirmed", Toast.LENGTH_SHORT).show();
                        intent.putExtra("check","placed");
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).
                show();
    }
    void processShopeepay(){
        new AlertDialog.Builder(this)
                .setTitle("Shopeepay")
                .setCancelable(false)
                .setMessage("Place your payment of Rp " + total + " through Shopeepay to 01804879621 within 2 hours with this Order no. Your products will then be shipped within 24 hours. " +
                        "If you do not pay within 2 hours, your order will be cancelled.")
                .setPositiveButton("Confirm Order", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(PaymentActivity.this,HomeActivity.class);
                        Toast.makeText(PaymentActivity.this, "Order Confirmed", Toast.LENGTH_SHORT).show();
                        intent.putExtra("check","placed");
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }
    void processDana(){
        new AlertDialog.Builder(this)
                .setTitle("Dana")
                .setCancelable(false)
                .setMessage("Place your payment of Rp " + total + " through Dana to 01754317077 within 2 hours with this Order no. Your products will then be shipped within 24 hours. " +
                        "If you do not pay within 2 hours, your order will be cancelled.")
                .setPositiveButton("Confirm Order", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(PaymentActivity.this,HomeActivity.class);
                        Toast.makeText(PaymentActivity.this, "Order Confirmed", Toast.LENGTH_SHORT).show();
                        intent.putExtra("check","placed");
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).
                show();
    }

    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}