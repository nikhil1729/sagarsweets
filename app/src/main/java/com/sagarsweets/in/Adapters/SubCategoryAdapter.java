package com.sagarsweets.in.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.sagarsweets.in.ApiControllers.SuperController;
import com.sagarsweets.in.ApiModel.AllSubCategoryModel;
import com.sagarsweets.in.R;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryAdapter
        extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {

    private Context context;
    private List<AllSubCategoryModel> subCategoryList;



    public SubCategoryAdapter(Context context, List<AllSubCategoryModel> subCategoryList) {
        this.context = context;
        this.subCategoryList = (subCategoryList != null)
                ? subCategoryList
                : new ArrayList<>();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_all_sub_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AllSubCategoryModel model = subCategoryList.get(position);

        holder.tvName.setText(model.getName());

        Glide.with(context)
                .load(SuperController.base_url_images+model.getImage())
                .placeholder(R.drawable.category_placeholder)
                .into(holder.imgIcon);

        // Click handling
        holder.card.setOnClickListener(v -> {
            // TODO: Open product list / sub-category screen
            // Example:
            // openSubCategory(model.getId(), model.getName());
        });
    }

    @Override
    public int getItemCount() {
        //return subCategoryList.size();
        return subCategoryList == null ? 0 : subCategoryList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgIcon;
        TextView tvName;
        MaterialCardView card;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            card = (MaterialCardView) itemView;
            imgIcon = itemView.findViewById(R.id.imgSubCategory);
            tvName = itemView.findViewById(R.id.tvSubCategoryName);
        }
    }
}
