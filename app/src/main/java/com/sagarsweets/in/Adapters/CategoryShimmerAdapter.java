package com.sagarsweets.in.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sagarsweets.in.R;

public class CategoryShimmerAdapter
        extends RecyclerView.Adapter<CategoryShimmerAdapter.ShimmerVH> {

    private int shimmerCount = 6;

    @NonNull
    @Override
    public ShimmerVH onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_shimmer, parent, false);
        return new ShimmerVH(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ShimmerVH holder, int position) {
        // No binding needed
    }

    @Override
    public int getItemCount() {
        return shimmerCount;
    }

    static class ShimmerVH extends RecyclerView.ViewHolder {
        ShimmerVH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
