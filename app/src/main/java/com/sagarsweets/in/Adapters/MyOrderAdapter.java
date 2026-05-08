package com.sagarsweets.in.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sagarsweets.in.ApiControllers.SuperController;
import com.sagarsweets.in.ApiModel.OrderData;
import com.sagarsweets.in.R;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    private Context context;
    private List<OrderData> list;

    public MyOrderAdapter(Context context, List<OrderData> list) {
        this.context = context;
        this.list = list;
    }

    public void addData(List<OrderData> newList){
        list.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        OrderData item = list.get(position);

        holder.txtTxnId.setText(item.getTxn_id());
        holder.txtDate.setText(item.getOrdered_date());
        holder.txtAmount.setText("₹" + item.getTotal_product_amount());
        holder.txtDeliverySlot.setText("Delivery Time Slot:"+item.getDelivery_time_slot());
        holder.txtDeliveryDate.setText("Selected Delivery Date:"+item.getDelivery_date());
        holder.txtAddress.setText(item.getShort_address());
        holder.txtItemCount.setText("Product: "+item.getItem_count()+" item(s)");
        holder.txtStatus.setText(item.getOrder_status());

        if(item.getOrder_status() == null || item.getOrder_status().isEmpty() ){
            holder.txtStatus.setVisibility(View.GONE);
        }
        // Load image
        Glide.with(context)
                .load(SuperController.base_url_images+item.getImage())
                .placeholder(R.drawable.ic_product_placeholder)
                .into(holder.imgProduct);
        if("HOME_DELIVERY".equals(item.getDelivery_type())){
            holder.txtDeliveryType.setText("HOME DELIVERY");
            holder.txtDeliveryType.setBackgroundResource(R.drawable.bg_delivery_type_home);
        }else{
            holder.txtDeliveryType.setText("PICKUP STORE");
            holder.txtDeliveryType.setBackgroundResource(R.drawable.bg_delivery_type_pickup);
        }
        if(item.getCoupon_code() != null && !item.getCoupon_code().isEmpty()){
            holder.txtCoupon.setVisibility(View.VISIBLE);
            holder.txtCoupon.setText("Coupon: " + item.getCoupon_code() + " Applied");
        }else{
            holder.txtCoupon.setVisibility(View.GONE);
        }

        holder.btnViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                View sheetView = LayoutInflater.from(context).inflate(R.layout.bottomsheet_order_details, null);
                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTxnId, txtDate, txtAmount, txtDeliverySlot, txtAddress,txtDeliveryDate,
                txtDeliveryType,txtCoupon,txtItemCount,txtStatus;
        ImageView imgProduct;

        Button btnViewDetails;
        public ViewHolder(View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imgProduct);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtItemCount = itemView.findViewById(R.id.txtItemCount);
            txtTxnId = itemView.findViewById(R.id.txtTxnId);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtDeliverySlot = itemView.findViewById(R.id.txtDeliverySlot);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtDeliveryDate = itemView.findViewById(R.id.txtDeliveryDate);
            txtDeliveryType = itemView.findViewById(R.id.txtDeliveryType);
            txtCoupon = itemView.findViewById(R.id.txtCoupon);
        }
    }
}