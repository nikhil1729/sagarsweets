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

import com.bumptech.glide.Glide;
import com.sagarsweets.in.ApiControllers.SuperController;
import com.sagarsweets.in.ApiModel.CategoryModel;
import com.sagarsweets.in.R;

import java.util.List;

public class CategoryAdapter
        extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<CategoryModel> categoryList;



    public CategoryAdapter(Context context, List<CategoryModel> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull CategoryViewHolder holder, int position) {

        CategoryModel model = categoryList.get(position);

        holder.txtName.setText(model.getName());
        Log.d("loadimage", SuperController.base_url_images + model.getImage());
        Glide.with(context)
                .load(SuperController.base_url_images + model.getImage())
                .placeholder(R.drawable.category_placeholder)
                .error(R.drawable.category_error)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView txtName;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgCategory);
            txtName = itemView.findViewById(R.id.txtCategoryName);
        }
    }
}