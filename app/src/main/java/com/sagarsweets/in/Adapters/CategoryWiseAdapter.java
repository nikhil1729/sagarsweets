package com.sagarsweets.in.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sagarsweets.in.ApiModel.TopCategoryDataModel;
import com.sagarsweets.in.ProductViewCategoryFragment;
import com.sagarsweets.in.R;
import com.sagarsweets.in.utils.CustomToast;

import java.util.List;

public class CategoryWiseAdapter
        extends RecyclerView.Adapter<CategoryWiseAdapter.ViewHolder> {

    Context context;
    List<TopCategoryDataModel> list;
    private PopularProductAdapter.CartUpdateListener cartUpdateListener;
    public CategoryWiseAdapter(Context context, List<TopCategoryDataModel> list,
                               PopularProductAdapter.CartUpdateListener listener) {
        this.context = context;
        this.list = list;
        this.cartUpdateListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_category_wise, parent, false);

        return new ViewHolder(view);
    }

    private int dpToPx(int i) {
        return (int) (i * Resources.getSystem().getDisplayMetrics().density);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TopCategoryDataModel category = list.get(position);

        // 🔴 Hide empty categories
        if (category.getProduct() == null
                || category.getProduct().isEmpty()) {

            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(
                    new RecyclerView.LayoutParams(0, 0)
            );
            return;
        }

        holder.itemView.setVisibility(View.VISIBLE);

        holder.tvCategoryName.setText(
                category.getCategory_name()
        );
        holder.tvViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CustomToast.success(context,String.valueOf(category.getId()));
                String categoryId = String.valueOf(category.getId());
                String categoryName = category.getCategory_name();
                openCategoryProduct(categoryId,categoryName);
            }
        });
        holder.rvProducts.setLayoutManager(
                new GridLayoutManager(
                        context,
                        1,
                        RecyclerView.HORIZONTAL,
                        false
                )
        );


        holder.rvProducts.setAdapter(
                new PopularProductAdapter(
                        context,
                        category.getProduct(),
                        true,
                        this.cartUpdateListener
                )
        );
    }

    private void openCategoryProduct(String categoryId, String categoryName) {
        if (!(context instanceof FragmentActivity)) return;
        FragmentActivity activity = (FragmentActivity) context;
        ProductViewCategoryFragment productViewCategoryFragment = new ProductViewCategoryFragment();
        // Optional: pass categoryId
        Bundle bundle = new Bundle();
        bundle.putString("category_id", categoryId);
        bundle.putString("category_name", categoryName);
        productViewCategoryFragment.setArguments(bundle);

        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, productViewCategoryFragment)
                .addToBackStack("product_details_By_top_category")
                .commit();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // 🔹 VIEW HOLDER
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCategoryName,tvViewAll;
        RecyclerView rvProducts;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvViewAll = itemView.findViewById(R.id.tvViewAll);
            rvProducts = itemView.findViewById(R.id.rvProducts);
        }
    }
}
