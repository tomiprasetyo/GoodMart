package com.tomiprasetyo.goodmart.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tomiprasetyo.goodmart.R;
import com.tomiprasetyo.goodmart.activities.ProductDetailsActivity;
import com.tomiprasetyo.goodmart.databinding.ItemProductBinding;
import com.tomiprasetyo.goodmart.model.Product;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{

    Context context;
    ArrayList<Product> products;

    public ProductAdapter(Context context,ArrayList<Product> products){
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        Glide.with(context).load(product.getImage()).into(holder.binding.image);
        holder.binding.label.setText(product.getName());
        holder.binding.price.setText("৳ "+ product.getPrice());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("name",product.getName());
                intent.putExtra("image",product.getImage());
                intent.putExtra("id",product.getId());
                intent.putExtra("price",product.getPrice());
                intent.putExtra("status",product.getStatus());
                intent.putExtra("category",product.getCategory());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        ItemProductBinding binding;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemProductBinding.bind(itemView);
        }
    }
}
