package com.sagarsweets.in.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.sagarsweets.in.ApiModel.CancelProductRequest;
import com.sagarsweets.in.ApiModel.CancelProductResponse;
import com.sagarsweets.in.ApiModel.CancellationWindowRequest;
import com.sagarsweets.in.ApiModel.CancellationWindowResponse;
import com.sagarsweets.in.ApiModel.MyOrderDetailsRequest;
import com.sagarsweets.in.ApiModel.MyOrderDetailsResponse;
import com.sagarsweets.in.ApiModel.OrderData;
import com.sagarsweets.in.ApiModel.TimelineItem;
import com.sagarsweets.in.ApiModel.TrackOrderRequest;
import com.sagarsweets.in.ApiModel.TrackOrderResponse;
import com.sagarsweets.in.R;
import com.sagarsweets.in.utils.ButtonLoaderUtil;
import com.sagarsweets.in.utils.CustomToast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    private Context context;
    private List<OrderData> list;
    private String userId;
    private String device;
    private String txnId;
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
        this.txnId = item.getTxn_id();
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
           // holder.txtDeliveryType.setBackgroundResource(R.drawable.bg_delivery_type_home);
            holder.txtDeliveryType.setTextColor(Color.parseColor("#4CAF50"));
        }else{
            holder.txtDeliveryType.setText("PICKUP STORE");
            holder.txtDeliveryType.setTextColor(Color.parseColor("#2196F3"));
            //holder.txtDeliveryType.setBackgroundResource(R.drawable.bg_delivery_type_pickup);
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
                ButtonLoaderUtil.showLoading(holder.btnCancel,holder.progressCancel);
                CancellationWindowResponse cancellationWindowResponse = new CancellationWindowResponse(userId, item.getTxn_id(), device);
                apiService.getCancellationWindow(cancellationWindowResponse).enqueue(new Callback<CancellationWindowRequest>() {
                    @Override
                    public void onResponse(Call<CancellationWindowRequest> call, Response<CancellationWindowRequest> response) {
                        ButtonLoaderUtil.hideLoading(holder.btnCancel,holder.progressCancel,"");
                        if(response.body() != null && response.body().isStatus()){
                            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                            View sheetView = LayoutInflater.from(context).inflate(R.layout.bottom_cancel_order, null);
                            Spinner spinnerCancelReason = sheetView.findViewById(R.id.spinnerCancelReason);
                            TextView tvRefundAmount = sheetView.findViewById(R.id.tvRefundAmount);
                            TextView tvMessage = sheetView.findViewById(R.id.tvMessage);
                            Button btnKeepOrder = sheetView.findViewById(R.id.btnKeepOrder);
                            ProgressBar progressCancelOrder = sheetView.findViewById(R.id.progressCancelOrder);
                            EditText edtCancelNote = sheetView.findViewById(R.id.edtCancelNote);
                            Button btnCancelOrder = sheetView.findViewById(R.id.btnCancelOrder);
                            tvRefundAmount.setText("₹"+response.body().getRefundAmount());
                            tvMessage.setText(response.body().getMessage());
                            List<String> reasonList = new ArrayList<>();

                            reasonList.add("Select Cancel Reason");

                            reasonList.addAll(response.body().getReason().values());

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    context,
                                    android.R.layout.simple_spinner_item,
                                    reasonList
                            );

                            adapter.setDropDownViewResource(
                                    android.R.layout.simple_spinner_dropdown_item
                            );
                            btnCancelOrder.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(spinnerCancelReason.getSelectedItemPosition() == 0){
                                        TextView errorText = (TextView) spinnerCancelReason.getSelectedView();
                                        if (errorText != null) {
                                            errorText.setError("Please select reason");
                                            errorText.setTextColor(Color.RED);
                                            errorText.setText("Please select reason");
                                        }
                                        return;
                                    }

                                    String reson_value = spinnerCancelReason.getSelectedItem().toString();
                                    String cancleNote = edtCancelNote.getText().toString().trim();
                                    if(reson_value.equalsIgnoreCase("other issue")){
                                        if(cancleNote.isEmpty()){
                                            edtCancelNote.setError("If other issue is selected then enter additional note");
                                            edtCancelNote.setFocusable(true);
                                            return;
                                        }
                                    }
                                    ButtonLoaderUtil.showLoading(btnCancelOrder,progressCancelOrder);
                                    CancelProductRequest cancelProductRequest = new CancelProductRequest(userId,device, item.getTxn_id(), reson_value,cancleNote);
                                    apiService.cancelProduct(cancelProductRequest).enqueue(new Callback<CancelProductResponse>() {
                                        @Override
                                        public void onResponse(Call<CancelProductResponse> call, Response<CancelProductResponse> response) {
                                            ButtonLoaderUtil.hideLoading(btnCancelOrder,progressCancelOrder,"Cancel Order");
                                            if(response.body().getStatus()){
                                                CustomToast.success(context,response.body().getMessage());
                                                bottomSheetDialog.dismiss();
                                            }else{
                                                CustomToast.error(context,response.body().getMessage());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<CancelProductResponse> call, Throwable t) {
                                            ButtonLoaderUtil.hideLoading(btnCancelOrder,progressCancelOrder,"Cancel Order");
                                            CustomToast.error(context,t.getMessage());
                                        }
                                    });

                                }
                            });
                            btnKeepOrder.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    bottomSheetDialog.dismiss();
                                }
                            });
                            spinnerCancelReason.setAdapter(adapter);

                            bottomSheetDialog.setContentView(sheetView);
                            bottomSheetDialog.setCanceledOnTouchOutside(false);
                            bottomSheetDialog.show();
                        }else{
                            showCancleDeliveryDetails(response);

                            //CustomToast.error(context,response.body().getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<CancellationWindowRequest> call, Throwable t) {
                        ButtonLoaderUtil.hideLoading(holder.btnCancel,holder.progressCancel,"");
                        CustomToast.error(context,t.getMessage());
                    }
                });

            }
        });
        //holder.btnTrack.setVisibility(View.VISIBLE);
        holder.btnTrack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                BottomSheetDialog bottomSheetDialog =
                        new BottomSheetDialog(context);
                View sheetView =
                        LayoutInflater.from(context)
                                .inflate(
                                        R.layout.botton_track_order,
                                        null
                                );
                bottomSheetDialog.setContentView(
                        sheetView
                );
                // Views

                LinearLayout layoutCancelled =
                        sheetView.findViewById(
                                R.id.layoutCancelled
                        );

                LinearLayout layoutWaiting =
                        sheetView.findViewById(
                                R.id.layoutWaiting
                        );

                LinearLayout layoutTracking =
                        sheetView.findViewById(
                                R.id.layoutTracking
                        );

                TextView txtCancelled =
                        sheetView.findViewById(
                                R.id.txtCancelled
                        );

                TextView txtWaiting =
                        sheetView.findViewById(
                                R.id.txtWaiting
                        );

                TextView txtCurrentStatus =
                        sheetView.findViewById(
                                R.id.txtCurrentStatus
                        );

                RecyclerView rvTimeline =
                        sheetView.findViewById(
                                R.id.rvTimeline
                        );

                rvTimeline.setLayoutManager(
                        new LinearLayoutManager(
                                context
                        )
                );
                bottomSheetDialog.show();
                TrackOrderRequest request = new TrackOrderRequest(userId,item.getTxn_id(),device);
                apiService.trackMyOrder(request).enqueue(new Callback<TrackOrderResponse>() {
                    @Override
                    public void onResponse(Call<TrackOrderResponse> call, Response<TrackOrderResponse> response) {
                        if (response.isSuccessful()
                                && response.body() != null) {
                            TrackOrderResponse data =
                                    response.body();

                            layoutCancelled.setVisibility(
                                    View.GONE);

                            layoutWaiting.setVisibility(
                                    View.GONE);

                            layoutTracking.setVisibility(
                                    View.GONE);

                            if ("cancelled".equals(response.body().getStatus())) {

                                // show cancel screen
                                layoutCancelled.setVisibility(
                                        View.VISIBLE
                                );

                                txtCancelled.setText(
                                        data.getTimeline()
                                                .get(0)
                                                .getMessage()
                                );

                            } else if (response.body().getTimeline() == null
                                    || response.body().getTimeline().isEmpty()) {
                                // Not Confirmed
                                layoutWaiting.setVisibility(
                                        View.VISIBLE
                                );

                                txtWaiting.setText(
                                        Html.fromHtml(
                                                data.getCurrentStatus()
                                        )
                                );

                            } else {

                                // show tracking timeline
                                layoutTracking.setVisibility(
                                        View.VISIBLE
                                );

                                txtCurrentStatus.setText(
                                        Html.fromHtml(
                                                data.getCurrentStatus()
                                        )
                                );

                                TrackAdapter adapter =
                                        new TrackAdapter(
                                                data.getTimeline()
                                        );

                                rvTimeline.setAdapter(
                                        adapter
                                );
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<TrackOrderResponse> call, Throwable t) {
                        CustomToast.error(context,t.getMessage());
                    }
                });


                /* BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                View sheetView = LayoutInflater.from(context).inflate(R.layout.botton_track_order, null);
                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show(); */
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
                        ButtonLoaderUtil.hideLoading(holder.btnViewDetails,holder.progressDetails,"");
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
                            MaterialButton btnInvoice = sheetView.findViewById(R.id.btnInvoice);
                            ProgressBar progressInvoice = sheetView.findViewById(R.id.progressInvoice);

                            MyOrderDetailsResponse.Result result = response.body().getResult();
                            ///  setting data in views
                            String totalProductAmount =result.getTotalProductAmount();
                            Float subTotal = 0f;
                            if(totalProductAmount.equals("")){
                                subTotal = 0f;
                            }else{
                                subTotal = Float.valueOf(result.getTotalProductAmount());
                            }

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
                            btnInvoice.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    JSONObject json = new JSONObject();
                                    try {
                                        ButtonLoaderUtil.showLoading(btnInvoice,progressInvoice);
                                        json.put("txn_id", result.getTxnId());
                                        RequestBody requestBody =
                                                RequestBody.create(
                                                        json.toString(),
                                                        MediaType.parse("application/json")
                                                );
                                        apiService.downloadInvoice(requestBody).enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                ButtonLoaderUtil.hideLoading(btnInvoice,progressInvoice,"Invoice");
                                                if(response.isSuccessful()
                                                        && response.body()!=null){

                                                    savePdf(
                                                            response.body(),
                                                            "invoice#"+result.getTxnId()+".pdf"
                                                    );

                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                ButtonLoaderUtil.hideLoading(btnInvoice,progressInvoice,"Invoice");
                                                CustomToast.error(context,t.getMessage());
                                            }
                                        });
                                    } catch (JSONException e) {
                                        //throw new RuntimeException(e);
                                        ButtonLoaderUtil.hideLoading(btnInvoice,progressInvoice,"Invoice");
                                        CustomToast.show(context,e.getMessage(),0);
                                    }

                                    //CustomToast.show(context,result.getTxnId(),0);
                                }
                            });
                            // product item
                            RecyclerView recyclerProducts = sheetView.findViewById(R.id.recyclerProducts);
                            recyclerProducts.setLayoutManager(new LinearLayoutManager(context));
                            OrderProductsAdapter adapter =
                                    new OrderProductsAdapter(context, response.body().getResult().getProductDetails());
                            recyclerProducts.setAdapter(adapter);
                            bottomSheetDialog.setContentView(sheetView);
                            bottomSheetDialog.show();
                        }else{
                            CustomToast.error(context,response.body().getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<MyOrderDetailsResponse> call, Throwable t) {
                        CustomToast.error(context,t.getMessage());
                    }
                });

            }
        });
    }

    private void showCancleDeliveryDetails(Response<CancellationWindowRequest> response) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View sheetView = LayoutInflater.from(context).inflate(R.layout.layout_order_cancelled, null);
        if(!response.body().isStatus()){
            TextView tvOrderStatus = sheetView.findViewById(R.id.tvOrderStatus);
            ImageButton btnClose   = sheetView.findViewById(R.id.btnClose);
            TextView tvMessage = sheetView.findViewById(R.id.tvMessage);
            TextView tvReason = sheetView.findViewById(R.id.tvReason);
            TextView tvReturnDate = sheetView.findViewById(R.id.tvReturnDate);
            TextView tvCancelledBy = sheetView.findViewById(R.id.tvCancelledBy);
            TextView tvRefundAmount = sheetView.findViewById(R.id.tvRefundAmount);
            TextView tvNote = sheetView.findViewById(R.id.tvNote);
            Button btnDismiss = sheetView.findViewById(R.id.btnDismiss);
            TextView tvDescription = sheetView.findViewById(R.id.tvDescription);
            // setting response data
            tvOrderStatus.setText(response.body().getOrderStatus());
            tvMessage.setText(response.body().getMessage());
            tvReason.setText(response.body().getData().getCustomerReason());
            tvReturnDate.setText("Return Date : "+response.body().getData().getReturnDate());
            tvCancelledBy.setText("Cancelled By : "+response.body().getData().getCancelledBy().toUpperCase());
            tvRefundAmount.setText("₹"+response.body().getData().getApproveReturnAmt());
            String description = response.body().getData().getDescription();
            if(description == null){
                tvDescription.setVisibility(View.GONE);
            }else{
                tvDescription.setText(description);
            }
            String refund_status = response.body().getData().getRefundStatus();
            if(refund_status == null){
                tvNote.setText("Your refund request is currently under review. Please wait some time or contact support with your transaction ID.");
            }else{
                String refund_date = response.body().getData().getRefundDatetime();
                String refund_id = response.body().getData().getRefundId();
                String note = "Your refund has been processed successfully on "
                        + refund_date
                        + ". Refund ID: "
                        + refund_id;
                tvNote.setText(note);
            }
            btnDismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                }
            });
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                }
            });
            bottomSheetDialog.setContentView(sheetView);
            bottomSheetDialog.setCanceledOnTouchOutside(false);
            bottomSheetDialog.show();

        }else{
            CustomToast.error(context,"Somthing went wrong.please try after some time.");
        }

    }



    private void savePdf(
            ResponseBody body,
            String fileName
    ){

        try{

            File dir =
                    context.getExternalFilesDir(
                            Environment.DIRECTORY_DOWNLOADS
                    );

            if(dir == null){
                CustomToast.error(
                        context,
                        "Download directory unavailable"
                );
                return;
            }

            File file =
                    new File(
                            dir,
                            fileName
                    );

            InputStream input =
                    body.byteStream();

            OutputStream output =
                    new FileOutputStream(
                            file
                    );

            byte[] buffer =
                    new byte[8192];

            int read;

            while(
                    (read =
                            input.read(
                                    buffer
                            )
                    ) != -1
            ){

                output.write(
                        buffer,
                        0,
                        read
                );

            }

            output.flush();

            output.close();

            input.close();

            CustomToast.success(
                    context,
                    "Invoice downloaded"
            );

            Intent intent =
                    new Intent(
                            Intent.ACTION_VIEW
                    );

            Uri uri =
                    androidx.core.content
                            .FileProvider
                            .getUriForFile(
                                    context,
                                    context.getPackageName()
                                            + ".provider",
                                    file
                            );

            intent.setDataAndType(
                    uri,
                    "application/pdf"
            );

            intent.addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            );

            context.startActivity(
                    intent
            );

        }

        catch(Exception e){

            CustomToast.error(
                    context,
                    e.getMessage()
            );

        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTxnId, txtDate, txtAmount, txtDeliverySlot, txtAddress,txtDeliveryDate,
                txtDeliveryType,txtCoupon,txtItemCount,txtStatus;
        ImageView imgProduct;
        ProgressBar progressDetails,progressCancel;
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
            progressCancel = itemView.findViewById(R.id.progressCancel);
        }
    }
}