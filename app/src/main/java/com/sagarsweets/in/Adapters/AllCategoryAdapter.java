package com.sagarsweets.in.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.sagarsweets.in.ApiControllers.SuperController;
import com.sagarsweets.in.ApiModel.AllCategoryModel;
import com.sagarsweets.in.ApiModel.AllSubCategoryModel;
import com.sagarsweets.in.ApiModel.CategoryModel;
import com.sagarsweets.in.R;

import java.util.List;

public class AllCategoryAdapter extends RecyclerView.Adapter<AllCategoryAdapter.ViewHolder>{
    private Context context;
    private List<AllCategoryModel> categoryList;
    private List<AllSubCategoryModel> allSubCategoryModels;
    private SubCategoryAdapter subCategoryAdapter;
    public AllCategoryAdapter(Context context, List<AllCategoryModel> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_all_category_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AllCategoryModel model = categoryList.get(position);

        holder.tvCategoryName.setText(model.getName());
        Log.d("tvCategoryName",model.getName());
        // Glide / Picasso
        Glide.with(context)
                .load(SuperController.base_url_images + model.getImage())
                .placeholder(R.drawable.category_placeholder)
                .into(holder.imgCategory);
        // AllSubCategoryModel allSubCategoryModel = new AllSubCategoryModel();
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        holder.rvSubCategory.setLayoutManager(layoutManager);

        List<AllSubCategoryModel> subCategoryList = model.getSubMenu();
        subCategoryAdapter = new SubCategoryAdapter(context, subCategoryList);
        holder.rvSubCategory.setAdapter(subCategoryAdapter);

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCategory;
        TextView tvCategoryName;
        //MaterialCardView card;
        RecyclerView rvSubCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCategory = itemView.findViewById(R.id.imgCategory);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            //card = itemView.findViewById(R.id.card);
            rvSubCategory = itemView.findViewById(R.id.rvSubCategory);
        }
    }
}
