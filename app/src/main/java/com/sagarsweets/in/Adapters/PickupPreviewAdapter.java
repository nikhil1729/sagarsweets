package com.sagarsweets.in.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sagarsweets.in.R;
import com.sagarsweets.in.Session.CartItem;

import java.util.List;

public class PickupPreviewAdapter extends RecyclerView.Adapter<PickupPreviewAdapter.ViewHolder> {

    private List<CartItem> list;

    public PickupPreviewAdapter(List<CartItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pickup_preview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem model = list.get(position);
        holder.tvName.setText(model.getProductName());
        if(model.getSizeId() != 0){
            holder.tvSizeTitle.setText(model.getSizeSelectedName());
        }

        holder.tvQty.setText("x" + model.getQuantity());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQty,tvSizeTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvSizeTitle = itemView.findViewById(R.id.tvSizeTitle);
        }
    }
}
