package com.sagarsweets.in.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiControllers.SuperController;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.MyOrderDetailsRequest;
import com.sagarsweets.in.ApiModel.MyOrderDetailsResponse;
import com.sagarsweets.in.ApiModel.OrderData;
import com.sagarsweets.in.R;
import com.sagarsweets.in.utils.ButtonLoaderUtil;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    private Context context;
    private List<OrderData> list;
    private String userId;
    private String device;
    ApiService apiService;
    public MyOrderAdapter(Context context, List<OrderData> list,String user_id,String device) {
        this.context = context;
        this.list = list;
        this.userId = user_id;
        this.device = device;
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

        apiService = LoginRetrofitClient
                .getClient()
                .create(ApiService.class);
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

        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                View sheetView = LayoutInflater.from(context).inflate(R.layout.bottom_cancel_order, null);
                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show();
            }
        });
        //holder.btnTrack.setVisibility(View.VISIBLE);
        holder.btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                View sheetView = LayoutInflater.from(context).inflate(R.layout.botton_track_order, null);
                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show();
            }
        });

        holder.btnViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonLoaderUtil.showLoading(holder.btnViewDetails,holder.progressDetails);
                MyOrderDetailsRequest myOrderDetailsRequest = new MyOrderDetailsRequest(userId, item.getTxn_id(), device);
                apiService.getOrderDetails(myOrderDetailsRequest).enqueue(new Callback<MyOrderDetailsResponse>() {
                    @Override
                    public void onResponse(Call<MyOrderDetailsResponse> call, Response<MyOrderDetailsResponse> response) {
                        ButtonLoaderUtil.hideLoading(holder.btnViewDetails,holder.progressDetails,"Details");
                        if(response.body()!=null && response.body().isStatus()){
                            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                            View sheetView = LayoutInflater.from(context).inflate(R.layout.bottomsheet_order_details, null);
                            // set data in bottomsheet view
                            TextView tvOrderStatus = sheetView.findViewById(R.id.tvOrderStatus);
                            TextView tvOrderDate   = sheetView.findViewById(R.id.tvOrderDate);
                            TextView tvOrderId = sheetView.findViewById(R.id.tvOrderId);
                            TextView tvCustomerName = sheetView.findViewById(R.id.tvCustomerName);
                            TextView tvCustomerMobile = sheetView.findViewById(R.id.tvCustomerMobile);
                            TextView tvCustomerAddress = sheetView.findViewById(R.id.tvCustomerAddress);
                            TextView tvSubtotalAmount = sheetView.findViewById(R.id.tvSubtotalAmount);
                            TextView tvDeliveryAmount = sheetView.findViewById(R.id.tvDeliveryAmount);
                            TextView tvCouponAmount = sheetView.findViewById(R.id.tvCouponAmount);
                            TextView tvCouponCode = sheetView.findViewById(R.id.tvCouponCode);
                            TextView tvTotalAmount = sheetView.findViewById(R.id.tvTotalAmount);
                            MyOrderDetailsResponse.Result result = response.body().getResult();
                            ///  setting data in views
                            Float subTotal = Float.valueOf(result.getTotalProductAmount());
                            Float delivery = Float.valueOf(result.getDeliveryCharge());
                            Float couponAmount = Float.valueOf(result.getCouponDiscount());
                            Float total = (subTotal+delivery)-couponAmount;

                            tvOrderId.setText("Order ID : "+result.getTxnId().toUpperCase());
                            tvOrderStatus.setText(result.getCurrentStatus());
                            tvOrderDate.setText(result.getOrderedTime());
                            tvCustomerName.setText(result.getCustomerName().toUpperCase());
                            tvCustomerMobile.setText(result.getMobileNumber());
                            tvCustomerAddress.setText(result.getAddress());
                            tvSubtotalAmount.setText("₹"+String.valueOf(subTotal));
                            tvDeliveryAmount.setText("₹"+String.valueOf(delivery));
                            tvCouponAmount.setText("₹"+String.valueOf(couponAmount));
                            tvTotalAmount.setText("₹"+String.valueOf(total));
                            if(result.getCouponCode() != null ){
                                tvCouponCode.setText("Code: "+result.getCouponCode().toUpperCase());
                            }else{
                                tvCouponCode.setVisibility(View.GONE);
                            }
                            // product item
                            RecyclerView recyclerProducts = sheetView.findViewById(R.id.recyclerProducts);
                            recyclerProducts.setLayoutManager(new LinearLayoutManager(context));
                            OrderProductsAdapter adapter =
                                    new OrderProductsAdapter(context, response.body().getResult().getProductDetails());
                            recyclerProducts.setAdapter(adapter);
                            bottomSheetDialog.setContentView(sheetView);
                            bottomSheetDialog.show();
                        }else{
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MyOrderDetailsResponse> call, Throwable t) {

                    }
                });

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
        ProgressBar progressDetails;
        MaterialButton btnViewDetails,btnCancel,btnTrack;
        public ViewHolder(View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imgProduct);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
            btnCancel = itemView.findViewById(R.id.btnCancle);
            btnTrack = itemView.findViewById(R.id.btnTrack);
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
            progressDetails = itemView.findViewById(R.id.progressDetails);
        }
    }
}