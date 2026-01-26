package com.sagarsweets.in.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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

import java.util.List;

public class PopularProductAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ProductModel> productList;
    private boolean categoryWise;
    private boolean showEndMessage = false;

    private static final int TYPE_PRODUCT = 1;
    private static final int TYPE_LOADER  = 2;
    private static final int TYPE_END     = 3;

    public PopularProductAdapter(Context context,
                                 List<ProductModel> productList,
                                 boolean categoryWise) {
        this.context = context;
        this.productList = productList;
        this.categoryWise = categoryWise;
    }

    // --------------------------------------------------
    // VIEW HOLDER CREATION
    // --------------------------------------------------

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_LOADER) {
            View v = inflater.inflate(R.layout.item_load_more_shimmer, parent, false);
            return new LoaderVH(v);
        }

        if (viewType == TYPE_END) {
            View v = inflater.inflate(R.layout.item_no_more_products, parent, false);
            return new EndVH(v);
        }

        View v = inflater.inflate(R.layout.item_popular_product, parent, false);
        return new ProductVH(v);
    }

    // --------------------------------------------------
    // BIND
    // --------------------------------------------------

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof LoaderVH || holder instanceof EndVH) {
            return;
        }

        ProductVH vh = (ProductVH) holder;
        ProductModel product = productList.get(position);

        // ---------- WIDTH (CATEGORY WISE) ----------
        if (categoryWise) {
            DisplayMetrics metrics =
                    vh.itemView.getResources().getDisplayMetrics();
            int itemWidth = (int) (metrics.widthPixels * 0.45);

            RecyclerView.LayoutParams params =
                    (RecyclerView.LayoutParams) vh.itemView.getLayoutParams();
            params.width = itemWidth;
            params.rightMargin = dpToPx(12);
            vh.itemView.setLayoutParams(params);
        } else {
            RecyclerView.LayoutParams params =
                    (RecyclerView.LayoutParams) vh.itemView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            vh.itemView.setLayoutParams(params);
        }

        // ---------- IMAGE ----------
        Glide.with(context)
                .load(SuperController.base_url_images + product.getImagePath())
                .placeholder(R.drawable.category_placeholder)
                .error(R.drawable.category_error)
                .into(vh.imgProduct);

        // ---------- TEXT ----------
        vh.tvProductName.setText(product.getProductName());
        vh.tvSalePrice.setText("₹" + product.getSellingPrice());
        vh.tvPrice.setText("₹" + product.getMrp());
        vh.ratingBar.setRating(product.getRating());
        vh.tvRatingCount.setText("(" + product.getRatingCount() + ")");

        // ---------- STOCK ----------
        if (product.getStock() != null && product.getStock() == 0) {
            vh.tvStockStatus.setText("OUT OF STOCK");
            vh.tvStockStatus.setBackgroundResource(R.drawable.bg_stock_out);
            vh.ivAddToCart.setEnabled(false);
            vh.itemView.setAlpha(0.6f);
        } else {
            vh.tvStockStatus.setText("IN STOCK");
            vh.tvStockStatus.setBackgroundResource(R.drawable.bg_stock_in);
            vh.ivAddToCart.setEnabled(true);
            vh.itemView.setAlpha(1f);
        }

        // ---------- SIZE LIST ----------
        List<SizeModel> sizes = product.getSizeList();
        if (sizes != null && !sizes.isEmpty()) {
            vh.rvSizes.setVisibility(View.VISIBLE);
            vh.rvSizes.setLayoutManager(
                    new LinearLayoutManager(context,
                            LinearLayoutManager.HORIZONTAL, false));

            SizeAdapter adapter =
                    new SizeAdapter(sizes, size -> updatePriceAndStock(vh, size));
            vh.rvSizes.setAdapter(adapter);
        } else {
            vh.rvSizes.setVisibility(View.GONE);
        }

        // ---------- CLICK ----------
        vh.itemView.setOnClickListener(v ->
                openProductDetails(product.getId()));
    }

    // --------------------------------------------------
    // ITEM COUNT / TYPE
    // --------------------------------------------------

    @Override
    public int getItemCount() {
        int count = productList == null ? 0 : productList.size();
        return showEndMessage ? count + 1 : count;
    }

    @Override
    public int getItemViewType(int position) {

        if (position < productList.size()
                && productList.get(position) == null) {
            return TYPE_LOADER;
        }

        if (position == productList.size() && showEndMessage) {
            return TYPE_END;
        }

        return TYPE_PRODUCT;
    }

    // --------------------------------------------------
    // HELPERS
    // --------------------------------------------------

    public void addLoader() {
        productList.add(null);
        notifyItemInserted(productList.size() - 1);
    }

    public void removeLoader() {
        int pos = productList.size() - 1;
        if (pos >= 0 && productList.get(pos) == null) {
            productList.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    public void setShowEndMessage(boolean show) {
        showEndMessage = show;
        notifyDataSetChanged();
    }

    private void updatePriceAndStock(ProductVH vh, SizeModel size) {
        vh.tvSalePrice.setText("₹" + size.getSellingPrice());
        vh.tvPrice.setText("₹" + size.getMrp());

        if (size.getStock() > 0) {
            vh.tvStockStatus.setText("IN STOCK");
            vh.tvStockStatus.setBackgroundResource(R.drawable.bg_stock_in);
            vh.ivAddToCart.setEnabled(true);
            vh.itemView.setAlpha(1f);
        } else {
            vh.tvStockStatus.setText("OUT OF STOCK");
            vh.tvStockStatus.setBackgroundResource(R.drawable.bg_stock_out);
            vh.ivAddToCart.setEnabled(false);
            vh.itemView.setAlpha(0.6f);
        }
    }

    private void openProductDetails(Integer productId) {
        if (!(context instanceof FragmentActivity)) return;

        FragmentActivity activity = (FragmentActivity) context;
        ProductDetailsFragment fragment = new ProductDetailsFragment();

        Bundle b = new Bundle();
        b.putInt("product_id", productId);
        fragment.setArguments(b);

        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("product_details")
                .commit();
    }

    private int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem()
                .getDisplayMetrics().density);
    }

    // --------------------------------------------------
    // VIEW HOLDERS
    // --------------------------------------------------

    public static class ProductVH extends RecyclerView.ViewHolder {

        ImageView imgProduct, ivAddToCart;
        TextView tvProductName, tvSalePrice, tvPrice,
                tvRatingCount, tvStockStatus;
        RatingBar ratingBar;
        RecyclerView rvSizes;

        public ProductVH(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            ivAddToCart = itemView.findViewById(R.id.ivAddToCart);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvSalePrice = itemView.findViewById(R.id.tvSalePrice);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRatingCount = itemView.findViewById(R.id.tvRatingCount);
            tvStockStatus = itemView.findViewById(R.id.tvStockStatus);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            rvSizes = itemView.findViewById(R.id.rvSizes);
        }
    }

    public static class LoaderVH extends RecyclerView.ViewHolder {
        public LoaderVH(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class EndVH extends RecyclerView.ViewHolder {
        public EndVH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
