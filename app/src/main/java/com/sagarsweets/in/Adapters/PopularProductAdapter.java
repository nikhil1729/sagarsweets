package com.sagarsweets.in.Adapters;

import static androidx.core.util.TypedValueCompat.dpToPx;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sagarsweets.in.ApiControllers.SuperController;
import com.sagarsweets.in.ApiModel.ProductModel;
import com.sagarsweets.in.ApiModel.SizeModel;
import com.sagarsweets.in.ProductDetailsFragment;
import com.sagarsweets.in.R;
import com.sagarsweets.in.RegisterFragment;

import java.util.ArrayList;
import java.util.List;

public class PopularProductAdapter
        extends RecyclerView.Adapter<PopularProductAdapter.ProductVH> {

    private Context context;
    private List<ProductModel> productList;
    Boolean category_wise;
    public PopularProductAdapter(Context context, List<ProductModel> productList) {
        this.context = context;
        this.productList = productList;
        this.category_wise = false;
    }
    public PopularProductAdapter(Context context, List<ProductModel> productList,Boolean category_wise) {
        this.context = context;
        this.productList = productList;
        this.category_wise = category_wise;
    }

    @NonNull
    @Override
    public ProductVH onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_popular_product, parent, false);

        return new ProductVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductVH holder, int position) {
        if(category_wise){
            // 🟢 SET PRODUCT WIDTH = 75% OF SCREEN
            DisplayMetrics metrics =
                    holder.itemView.getContext()
                            .getResources()
                            .getDisplayMetrics();

            int screenWidth = metrics.widthPixels;
            int itemWidth = (int) (screenWidth * 0.45); // ⭐ 1.5 view

            RecyclerView.LayoutParams params =
                    (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();

            params.width = itemWidth;
            params.rightMargin = dpToPx(12);

            holder.itemView.setLayoutParams(params);
        }else{
            // NORMAL GRID / FULL WIDTH
            RecyclerView.LayoutParams params =
                    (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            holder.itemView.setLayoutParams(params);
        }
        ProductModel product = productList.get(position);

        Glide.with(context)
                .load(SuperController.base_url_images + product.getImagePath())
                .placeholder(R.drawable.category_placeholder)
                .error(R.drawable.category_error)
                .into(holder.imgProduct);
        holder.tvProductName.setText(product.getProductName());
        holder.tvSalePrice.setText("₹" + product.getSellingPrice());
        holder.tvPrice.setText("₹" + product.getMrp());
        holder.ratingBar.setRating(product.getRating());
        holder.tvRatingCount.setText("(" + product.getRatingCount() + ")");
        Log.d("stock", String.valueOf(product.getStock() ));
        holder.tvProductName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("product_clicked", String.valueOf(product.getId()));
            }
        });
        holder.imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("product_clicked", String.valueOf(product.getId()));
                Integer product_id = product.getId();
                openProductDetails(product_id);
            }
        });
        holder.imgWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("product_clicked","withlist clicked");
            }
        });
        if(product.getStock() !=null){
            Log.d("stock", "if !null");
            if(product.getStock() == 0){
                Log.d("stock", "if == 0");
                holder.tvStockStatus.setText("OUT OF STOCK");
                holder.tvStockStatus.setBackgroundResource(R.drawable.bg_stock_out);
                holder.ivAddToCart.setEnabled(false);
                holder.ivBuyNow.setEnabled(false);
                holder.itemView.setAlpha(0.6f);
            }else{
                Log.d("stock","else");
                holder.tvStockStatus.setText("IN STOCK");
                holder.tvStockStatus.setBackgroundResource(R.drawable.bg_stock_in);
                holder.ivAddToCart.setEnabled(true);
                holder.ivBuyNow.setEnabled(true);
                holder.itemView.setAlpha(1f);
            }
        }
        List<SizeModel> sizeList = product.getSizeList();

        // -------- SIZE HANDLING --------
        List<SizeModel> sizes = product.getSizeList();

        if (sizes != null && !sizes.isEmpty()) {

            holder.rvSizes.setVisibility(View.VISIBLE);

            holder.rvSizes.setLayoutManager(
                    new LinearLayoutManager(
                            holder.itemView.getContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                    )
            );

            SizeAdapter sizeAdapter = new SizeAdapter(sizeList, size -> {
                updatePriceAndStock(holder, size);
            });
            holder.rvSizes.setAdapter(sizeAdapter);

        } else {
            holder.rvSizes.setVisibility(View.GONE);
        }


    }

    private int dpToPx(int i) {
        return (int) (i * Resources.getSystem().getDisplayMetrics().density);
    }


    private void openProductDetails(Integer productId) {

        if (!(context instanceof FragmentActivity)) return;

        FragmentActivity activity = (FragmentActivity) context;

        ProductDetailsFragment fragment = new ProductDetailsFragment();

        // Optional: pass productId
        Bundle bundle = new Bundle();
        bundle.putInt("product_id", productId);
        fragment.setArguments(bundle);

        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("product_details")
                .commit();
    }


    private void updatePriceAndStock(ProductVH holder, SizeModel size) {

        holder.tvSalePrice.setText("₹" + size.getSellingPrice());
        holder.tvPrice.setText("₹" + size.getMrp());

        if (size.getStock() > 0) {
            holder.tvStockStatus.setText("IN STOCK");
            holder.tvStockStatus.setBackgroundResource(R.drawable.bg_stock_in);
            holder.ivAddToCart.setEnabled(true);
            holder.itemView.setAlpha(1f);
        } else {
            holder.tvStockStatus.setText("OUT OF STOCK");
            holder.tvStockStatus.setBackgroundResource(R.drawable.bg_stock_out);
            holder.ivAddToCart.setEnabled(false);
            holder.itemView.setAlpha(0.6f);
        }
    }


    private void updatePrice(ProductVH holder, SizeModel size) {
        //holder.tvSalePrice.setText("₹" + size.getSelling_price());
        //holder.tvPrice.setText("₹" + size.getMrp());
    }

    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }

    public class ProductVH extends RecyclerView.ViewHolder {

        ImageView imgProduct, imgWishlist, ivAddToCart, ivBuyNow;
        TextView tvProductName, tvSalePrice, tvPrice, tvRatingCount, tvStockStatus;
        RatingBar ratingBar;
        RecyclerView rvSizes;



        public ProductVH(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imgProduct);
            imgWishlist = itemView.findViewById(R.id.imgWishlist);
            ivAddToCart = itemView.findViewById(R.id.ivAddToCart);
            ivBuyNow = itemView.findViewById(R.id.ivBuyNow);

            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvSalePrice = itemView.findViewById(R.id.tvSalePrice);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRatingCount = itemView.findViewById(R.id.tvRatingCount);
            tvStockStatus = itemView.findViewById(R.id.tvStockStatus);

            ratingBar = itemView.findViewById(R.id.ratingBar);
            rvSizes = itemView.findViewById(R.id.rvSizes);
        }
    }

}
