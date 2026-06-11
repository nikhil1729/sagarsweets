package com.sagarsweets.in.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sagarsweets.in.ApiControllers.SuperController;
import com.sagarsweets.in.ApiModel.CategoryModel;
import com.sagarsweets.in.ProductViewCategoryFragment;
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryId = model.getId();
                String categoryName = model.getName();
                openCategoryProduct(categoryId,categoryName);
            }
        });
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
                .addToBackStack("product_details_By_category")
                .commit();
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