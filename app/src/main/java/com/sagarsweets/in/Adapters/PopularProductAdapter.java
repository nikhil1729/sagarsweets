package com.sagarsweets.in.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import com.sagarsweets.in.ApiModel.ProductModel;
import com.sagarsweets.in.R;

import java.util.List;

public class PopularProductAdapter
        extends RecyclerView.Adapter<PopularProductAdapter.ProductViewHolder> {

    private Context context;
    private List<ProductModel> productList;

    public PopularProductAdapter(Context context, List<ProductModel> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_popular_product, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ProductViewHolder holder, int position) {

        ProductModel product = productList.get(position);

        holder.tvProductName.setText(product.getProductName());
        holder.tvSalePrice.setText("₹" + product.getSalePrice());
        holder.tvPrice.setText("₹" + product.getPrice());

        holder.ratingBar.setRating(product.getRating());
        holder.tvRatingCount.setText("(" + product.getRatingCount() + ")");
        // Strike through original price
        holder.tvPrice.setPaintFlags(
                holder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
        );

        // Load image
        String image_path = SuperController.base_url_images;
        Glide.with(context)
                .load(image_path + product.getImagePath())
                .placeholder(R.drawable.slider_placeholder)
                .error(R.drawable.slider_error)
                .into(holder.imgProduct);

        // Wishlist icon
        if (product.getIsWishlist() == 1) {
            holder.imgWishlist.setImageResource(R.drawable.ic_heart_filled);
        } else {
            holder.imgWishlist.setImageResource(R.drawable.ic_heart_outline);
        }

        // Click → Product Details
        holder.itemView.setOnClickListener(v -> {
            //Intent intent = new Intent(context, ProductDetailsActivity.class);
            //intent.putExtra("product_id", product.getId());
            //context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduct, imgWishlist;
        TextView tvProductName, tvPrice, tvSalePrice;
        RatingBar ratingBar;
        TextView tvRatingCount;
        ImageView ivAddToCart, ivBuyNow;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imgProduct);
            imgWishlist = itemView.findViewById(R.id.imgWishlist);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvSalePrice = itemView.findViewById(R.id.tvSalePrice);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvRatingCount = itemView.findViewById(R.id.tvRatingCount);
            ivAddToCart = itemView.findViewById(R.id.ivAddToCart);
            ivBuyNow = itemView.findViewById(R.id.ivBuyNow);

        }
    }
}
