package com.sagarsweets.in.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sagarsweets.in.ApiModel.Item;
import com.sagarsweets.in.R;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {

    private List<Item> itemList;

    public OrderItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_product, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Item item = itemList.get(position);

        holder.txtName.setText(item.getName());

        String size = item.getSize();
        if(size == null || "0".equals(size)){
            holder.txtSize.setVisibility(View.GONE);
        }else{
            holder.txtSize.setVisibility(View.VISIBLE);
            holder.txtSize.setText("Size: " + size);
        }
        holder.txtQuantity.setText("Qty: " + item.getQuantity());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtSize, txtQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtSize = itemView.findViewById(R.id.txtSize);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
        }
    }
}
