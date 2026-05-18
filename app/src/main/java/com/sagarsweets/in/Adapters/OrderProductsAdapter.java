package com.sagarsweets.in.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sagarsweets.in.ApiModel.MyOrderDetailsResponse;
import com.sagarsweets.in.R;

import java.util.List;

public class OrderProductsAdapter extends RecyclerView.Adapter<OrderProductsAdapter.ViewHolder> {

    Context context;
    List<MyOrderDetailsResponse.ProductDetail> productList;

    public OrderProductsAdapter(Context context, List<MyOrderDetailsResponse.ProductDetail> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_order_product, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MyOrderDetailsResponse.ProductDetail product = productList.get(position);
        String size = "";
        if(!product.getProductSize().isEmpty()){
            size = "("+product.getProductSize()+")";
        }
        Log.d("P_SIZE",product.getProductSize());
        holder.tvProductName.setText(product.getProductName() + size);
        holder.tvProductQty.setText("Qty : " + product.getQuantity());
        holder.tvProductPrice.setText("₹" + product.getPrice());

        // Glide/Picasso image load
        // Glide.with(context).load(product.getImage()).into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduct;
        TextView tvProductName, tvProductQty, tvProductPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.txtName);
            tvProductQty = itemView.findViewById(R.id.txtQuantity);
            tvProductPrice = itemView.findViewById(R.id.txtSize);
        }
    }
}