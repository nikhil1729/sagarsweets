package com.sagarsweets.in.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sagarsweets.in.ApiModel.Address;
import com.sagarsweets.in.R;
import com.sagarsweets.in.utils.AddressFormatter;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    List<Address> list;
    OnAddressSelect listener;

    public interface OnAddressSelect{
        void onAddressSelected(Address address);
    }

    public AddressAdapter(List<Address> list, OnAddressSelect listener){
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_address,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Address model = list.get(position);

        holder.type.setText(model.getAddressType());

        holder.shortAddress.setText(AddressFormatter.formatDeliveryAddressSingle(model));
        //holder.fullAddress.setText(model.getFullName());
        if(model.getIsDefault() == 1){
            holder.defaultBadge.setVisibility(View.VISIBLE);
        }else{
            holder.defaultBadge.setVisibility(View.GONE);
        }

        holder.btnSelectAddress.setOnClickListener(v -> {
            listener.onAddressSelected(model);
        });

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView type,shortAddress,fullAddress,defaultBadge;
        Button btnSelectAddress;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            defaultBadge = itemView.findViewById(R.id.defaultBadge);
            type = itemView.findViewById(R.id.addressType);
            shortAddress = itemView.findViewById(R.id.shortAddress);
            btnSelectAddress = itemView.findViewById(R.id.btnSelectAddress);
        }
    }
}
