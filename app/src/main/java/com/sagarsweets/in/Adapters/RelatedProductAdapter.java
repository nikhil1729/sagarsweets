package com.sagarsweets.in.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sagarsweets.in.ApiControllers.SuperController;
import com.sagarsweets.in.ApiModel.RelatedProductModel;
import com.sagarsweets.in.R;

import java.util.List;

public class RelatedProductAdapter
        extends RecyclerView.Adapter<RelatedProductAdapter.RelatedVH> {

    private final List<RelatedProductModel> productList;

    public RelatedProductAdapter(List<RelatedProductModel> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public RelatedVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_popular_product, parent, false);
        return new RelatedVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedVH holder, int position) {
        RelatedProductModel product = productList.get(position);

        holder.txtName.setText(product.getProductName());
        holder.txtSalePrice.setText("₹" + product.getSellingPrice());
        holder.ratingBar.setRating(product.getRating());
        String imageUrl = SuperController.base_url_images+product.getImagePath();
        Log.d("image_Url", imageUrl);
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_product_placeholder)
                .into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class RelatedVH extends RecyclerView.ViewHolder {

        ImageView imgProduct;
        TextView txtName, txtSalePrice;
        RatingBar ratingBar;

        RelatedVH(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.tvProductName);
            txtSalePrice = itemView.findViewById(R.id.tvSalePrice);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}

