package com.sagarsweets.in.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sagarsweets.in.ApiModel.TopCategoryDataModel;
import com.sagarsweets.in.R;

import java.util.List;

public class CategoryWiseAdapter
        extends RecyclerView.Adapter<CategoryWiseAdapter.ViewHolder> {

    Context context;
    List<TopCategoryDataModel> list;

    public CategoryWiseAdapter(Context context, List<TopCategoryDataModel> list) {
        this.context = context;
        this.list = list;
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
                        true
                )
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // 🔹 VIEW HOLDER
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCategoryName;
        RecyclerView rvProducts;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            rvProducts = itemView.findViewById(R.id.rvProducts);
        }
    }
}
