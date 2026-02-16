package com.sagarsweets.in.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sagarsweets.in.ApiModel.SizeModel;
import com.sagarsweets.in.R;

import java.util.List;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.SizeVH> {

    private final List<SizeModel> sizeList;
    private final OnSizeClickListener listener;

    private int selectedPosition = RecyclerView.NO_POSITION;
    private int selectedSizeId = -1;
    public interface OnSizeClickListener {
        void onSizeClick(SizeModel size);
    }

    public SizeAdapter(List<SizeModel> sizeList, OnSizeClickListener listener) {
        this.sizeList = sizeList;
        this.listener = listener;
    }
    public SizeAdapter(
            List<SizeModel> sizeList,
            int selectedPosition,
            OnSizeClickListener listener
    ) {
        this.sizeList = sizeList;
        this.selectedPosition = selectedPosition;
        this.listener = listener;
    }

    public void setSelectedSize(int sizeId) {
        this.selectedSizeId = sizeId;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public SizeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {



        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_size, parent, false);

        return new SizeVH(view);
    }
    public void setSelectedSizeById(int sizeId) {
        for (int i = 0; i < sizeList.size(); i++) {
            if (sizeList.get(i).getId() == sizeId) {
                int previousPos = selectedPosition;
                selectedPosition = i;

                if (previousPos != RecyclerView.NO_POSITION) {
                    notifyItemChanged(previousPos);
                }
                notifyItemChanged(selectedPosition);
                break;
            }
        }
    }


    @Override
    public void onBindViewHolder(@NonNull SizeVH holder, int position) {

        SizeModel size = sizeList.get(position);

        holder.txtSize.setText(size.getTitle());
        holder.txtPrice.setText("₹" + size.getSellingPrice());

        // STOCK HANDLING
        Log.d("sizeadapter", size.getTitle()+"-"+String.valueOf(size.getStock()));
        if (size.getStock() <= 0) {
            holder.itemView.setAlpha(0.4f);
            holder.itemView.setEnabled(false);
        } else {
            holder.itemView.setAlpha(1f);
            holder.itemView.setEnabled(true);
        }
        // if cart selected
        if (size.getId() == selectedSizeId) {
            holder.itemView.setBackgroundResource(R.drawable.bg_size_selected);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bg_size_unselected);
        }

        // SELECTION UI
        if (position == selectedPosition) {
            holder.itemView.setBackgroundResource(R.drawable.bg_size_selected);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bg_size_unselected);
        }

        holder.itemView.setOnClickListener(v -> {
            int adapterPos = holder.getAdapterPosition();

            // VERY IMPORTANT CHECK
            if (adapterPos == RecyclerView.NO_POSITION) return;
            if (!v.isEnabled()) return;

            int previousPos = selectedPosition;
            selectedPosition = adapterPos;

            if (previousPos != RecyclerView.NO_POSITION) {
                notifyItemChanged(previousPos);
            }
            notifyItemChanged(selectedPosition);

            if (listener != null) {
                listener.onSizeClick(sizeList.get(adapterPos));
            }
        });
    }


    @Override
    public int getItemCount() {
        return sizeList.size();
    }

    static class SizeVH extends RecyclerView.ViewHolder {

        TextView txtSize, txtPrice;

        SizeVH(@NonNull View itemView) {
            super(itemView);
            txtSize = itemView.findViewById(R.id.txtSize);
            txtPrice = itemView.findViewById(R.id.txtPrice);
        }
    }
}

