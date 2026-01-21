package com.sagarsweets.in.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sagarsweets.in.ApiModel.SpecificationModel;
import com.sagarsweets.in.R;

import java.util.List;

public class SpecificationAdapter
        extends RecyclerView.Adapter<SpecificationAdapter.SpecVH> {

    private final List<SpecificationModel> specList;

    public SpecificationAdapter(List<SpecificationModel> specList) {
        Log.d("specification ","constractor");
        this.specList = specList;
    }

    @NonNull
    @Override
    public SpecVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_specification, parent, false);
        return new SpecVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecVH holder, int position) {
        SpecificationModel spec = specList.get(position);
        Log.d("rvSpecification","herebbbbbbb");
        holder.txtAttribute.setText(spec.getAttribute());
        holder.txtValue.setText(spec.getValue());
    }

    @Override
    public int getItemCount() {
        return specList.size();
    }

    static class SpecVH extends RecyclerView.ViewHolder {
        TextView txtAttribute, txtValue;

        SpecVH(@NonNull View itemView) {
            super(itemView);
            txtAttribute = itemView.findViewById(R.id.txtAttribute);
            txtValue = itemView.findViewById(R.id.txtValue);
        }
    }
}

