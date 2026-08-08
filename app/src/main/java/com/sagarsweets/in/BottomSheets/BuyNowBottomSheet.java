package com.sagarsweets.in.BottomSheets;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiControllers.SuperController;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiInterface.PaymentCallback;
import com.sagarsweets.in.ApiModel.FastCheckoutOrderRequest;
import com.sagarsweets.in.ApiModel.FastCheckoutRequest;
import com.sagarsweets.in.ApiModel.FastCheckoutResponse;
import com.sagarsweets.in.ApiModel.PodVerifyOtpResponse;
import com.sagarsweets.in.ApiModel.RazorpayRequest;
import com.sagarsweets.in.HomeActivity;
import com.sagarsweets.in.OrderReceivedFragment;
import com.sagarsweets.in.R;
import com.sagarsweets.in.Session.LoginSession;
import com.sagarsweets.in.utils.CustomToast;
import com.sagarsweets.in.utils.DeviceInfo;
import com.sagarsweets.in.utils.LiveLocationHelper;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyNowBottomSheet extends BottomSheetDialogFragment implements PaymentCallback {
    private ShimmerFrameLayout shimmerLayout;
    private View contentLayout;
    private View layoutError;
    private View btnRetry,btnErrorClose;
    Chip chipOnline;
    ApiService apiService;
    ImageView btnClose;
    ImageView imgProduct;
    TextView tvProductName;
    TextView tvSelectedSize;
    TextView tvPrice;
    ImageView btnMinus;
    TextView tvQuantity;
    ImageView btnPlus;
    TextView tvSelectedAddress;
    TextView tvDeliveryCharge;
    TextInputEditText etCoupon;
    MaterialButton btnApplyCoupon;
    TextView tvGrandTotal;
    TextView tvCouponDiscount;
    MaterialButton btnBuyNow;
    private int productId;
    Integer addressId;
    private String productName;
    Integer max_stock;
    Integer min_quantity =1;
    Double unitPrice;
    Double shippingCharge = 0.0;
    Double coupon_discount = 0.0;
    Double total = 0.0;
    private String productImage;
    String paymentGatewayKey,paymentGatewaySalt;
    private String device,latitude,longitude,paymentId;
    private String sizeId;
    private String sizeName;


    Context context;
    LoginSession loginSession;
    private LiveLocationHelper locationHelper;
    private String coupon_name;
    private String delivery_date;
    private String delivery_time;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.bottomsheet_buy_now,
                container,
                false
        );

        initViews(view);
        locationFunctions(); // getting here location's lng and lat
        loadFastCheckoutUi(); // loading ui data from server
        HomeActivity.paymentCallback = this; // setting interface callback for payment
        sendFastCheckout(); // open razorpay
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer quantity = Integer.parseInt(tvQuantity.getText().toString().trim());
                if(quantity <= min_quantity){
                    CustomToast.warning(context,"Minimum quantity");
                    return;
                }
                quantity -=1;
                tvQuantity.setText(String.valueOf(quantity));
                calculatePrice(quantity,unitPrice,shippingCharge,coupon_discount);
            }
        });
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer quantity = Integer.parseInt(tvQuantity.getText().toString().trim());
                if(quantity >= max_stock){
                    CustomToast.warning(context,"Max Stock");
                    return;
                }
                quantity +=1;
                tvQuantity.setText(String.valueOf(quantity));
                calculatePrice(quantity,unitPrice,shippingCharge,coupon_discount);
            }
        });

        btnClose.setOnClickListener(v -> {
            v.animate()
                    .rotation(90)
                    .setDuration(120)
                    .withEndAction(this::dismiss)
                    .start();
        });
        btnErrorClose.setOnClickListener(v -> {
            dismiss();
        });

        return view;
    }

    private void locationFunctions() {
        locationHelper = new LiveLocationHelper(context);
        locationHelper.start(
                new LiveLocationHelper.LocationListener() {
                    @Override
                    public void onLocationChanged(
                            double lat,
                            double lng,
                            float accuracy
                    ) {
                        latitude = String.valueOf(lat);
                        longitude = String.valueOf(lng);

                    }
                    @Override
                    public void onError( String error) {
                        longitude = null;
                        latitude = null;
                        CustomToast.error(context,error);
                    }
                }
        );

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationHelper != null) {
            locationHelper.stop();
        }
    }
    private void sendFastCheckout() {
        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validate variables first then send for
                if(loginSession.isLoggedIn()){
                    String userId = loginSession.getUserId();
                    String product_id = String.valueOf(productId);
                    String size_id = sizeId;
                    String quantity_product = tvQuantity.getText().toString().trim();
                    String address_id = String.valueOf(addressId);

                    openRazorpay(total);
                }else{
                    CustomToast.warning(context,"Login required for fast checkout");
                }
            }
        });
    }

    private void openRazorpay(Double amount) {
        try {
            com.razorpay.Checkout checkout = new com.razorpay.Checkout();
            checkout.setKeyID(paymentGatewayKey); // key is coming from database

            JSONObject options = new JSONObject();

            options.put("name", "Sagar Sweets");
            options.put("description", "Fast Checkout Order Payment");
            options.put("currency", "INR");

            options.put("amount", Math.round(amount*100));

            JSONObject prefill = new JSONObject();
            prefill.put("email", loginSession.getEmail());
            prefill.put("contact", loginSession.getMobile());

            options.put("prefill", prefill);

            checkout.open(requireActivity(), options);
        } catch (Exception e) {
            CustomToast.error(context,e.getMessage());
        }

    }

    private void loadFastCheckoutUi() {
        // add shimmer
        showLoading();
        FastCheckoutRequest fastCheckoutRequest = new FastCheckoutRequest(loginSession.getUserId(),
                String.valueOf(productId),String.valueOf(sizeId),device,latitude,longitude);
        apiService.fastCheckoutIndex(fastCheckoutRequest).enqueue(new Callback<FastCheckoutResponse>() {
            @Override
            public void onResponse(Call<FastCheckoutResponse> call, Response<FastCheckoutResponse> response) {
                if(response.body() != null && response.body().getName() != null){
                    setProductDetails(response.body());
                    showContent();
                }else{
                    showError();
                }

            }

            @Override
            public void onFailure(Call<FastCheckoutResponse> call, Throwable t) {
                showError();
            }
        });
    }

    private void setProductDetails(FastCheckoutResponse body) {
        tvProductName.setText(productName);
        if(sizeId == "0" || sizeId == null){
            tvSelectedSize.setVisibility(View.GONE);
        }else{
            tvSelectedSize.setVisibility(View.VISIBLE);
            tvSelectedSize.setText(sizeName);
        }
        tvPrice.setText("₹"+body.getSelling_price());
        tvSelectedAddress.setText(body.getDefault_address());
        shippingCharge = Double.valueOf(body.getDelivery_charge());
        tvDeliveryCharge.setText("₹"+shippingCharge);
        addressId = body.getAddress_id();
        max_stock = body.getStock();
        tvQuantity.setText(String.valueOf(min_quantity));
        unitPrice = Double.valueOf(body.getSelling_price());
        paymentGatewayKey = body.getPaymentGatewayKey();
        paymentGatewaySalt = body.getPaymentGatewaySalt();// add in server
        calculatePrice(min_quantity,unitPrice,shippingCharge,coupon_discount);
        delivery_date = body.getDeliveryDate();
        delivery_time = body.getDeliveryTimeSlot();
        Glide.with(requireContext())
                .load(SuperController.base_url_images +productImage)
                .into(imgProduct);
    }

    private void calculatePrice(Integer quantity, Double unitPrice, Double shippingCharge, Double couponDiscount) {
        total = (quantity * unitPrice) + shippingCharge - couponDiscount;
        tvGrandTotal.setText("₹"+String.valueOf(total));
    }

    private void initViews(View view) {
        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        contentLayout = view.findViewById(R.id.contentLayout);
        btnClose = view.findViewById(R.id.btnClose);
        tvProductName = view.findViewById(R.id.tvProductName);
        tvSelectedSize = view.findViewById(R.id.tvSelectedSize);
        tvPrice = view.findViewById(R.id.tvPrice);
        imgProduct = view.findViewById(R.id.imgProduct);
        tvSelectedAddress = view.findViewById(R.id.tvSelectedAddress);
        tvDeliveryCharge = view.findViewById(R.id.tvDeliveryCharge);
        etCoupon = view.findViewById(R.id.etCoupon);
        btnApplyCoupon = view.findViewById(R.id.btnApplyCoupon);
        tvCouponDiscount = view.findViewById(R.id.tvCouponDiscount);
        tvGrandTotal = view.findViewById(R.id.tvGrandTotal);
        btnPlus = view.findViewById(R.id.btnPlus);
        btnMinus = view.findViewById(R.id.btnMinus);
        tvQuantity = view.findViewById(R.id.tvQuantity);

        loginSession = new LoginSession(context);
        layoutError = view.findViewById(R.id.layoutError);
        btnRetry = view.findViewById(R.id.btnRetry);
        btnErrorClose = view.findViewById(R.id.btnErrorClose);
        btnBuyNow = view.findViewById(R.id.btnBuyNow);

        coupon_name = "";
        
        btnRetry.setOnClickListener(v -> {
            loadFastCheckoutUi();
        });

        chipOnline = view.findViewById(R.id.chipOnline);
        chipOnline.setChecked(true);

        apiService = LoginRetrofitClient
                .getClient()
                .create(ApiService.class);
    }

    public void setContext(Context context){
        this.context = context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            productId = getArguments().getInt("product_id");

            productName =
                    getArguments().getString("product_name");

            productImage =
                    getArguments().getString("product_image");

            sizeId =
                    getArguments().getString("size_id");

            sizeName =
                    getArguments().getString("size_name");
        }
    }

    private void showLoading() {

        layoutError.setVisibility(View.GONE);

        contentLayout.setVisibility(View.INVISIBLE);

        shimmerLayout.setVisibility(View.VISIBLE);
        shimmerLayout.startShimmer();
    }

    private void showContent() {

        shimmerLayout.stopShimmer();
        shimmerLayout.setVisibility(View.GONE);

        layoutError.setVisibility(View.GONE);

        contentLayout.setVisibility(View.VISIBLE);
    }

    private void showError() {

        shimmerLayout.stopShimmer();
        shimmerLayout.setVisibility(View.GONE);

        contentLayout.setVisibility(View.GONE);

        layoutError.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPaymentSuccess(String paymentId) {
        CustomToast.success(context,paymentId);
        FastCheckoutOrderRequest request = new
            FastCheckoutOrderRequest(
            loginSession.getUserId(),
            String.valueOf(productId),
            sizeId,
            tvQuantity.getText().toString().trim(),
            addressId,
            paymentId,
            longitude,
            latitude,
            DeviceInfo.getDeviceString(context),
            "HOME_DELIVERY",
            coupon_name,
            delivery_date,
            delivery_time
        );
        apiService.fastCheckoutSuccess(request).enqueue(new Callback<PodVerifyOtpResponse>() {
            @Override
            public void onResponse(Call<PodVerifyOtpResponse> call, Response<PodVerifyOtpResponse> response) {
                if(response.body() != null && response.body().getStatus()){
                    OrderReceivedFragment orderReceivedFragment = new OrderReceivedFragment();
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    Bundle bundle = new Bundle();
                    bundle.putString("order_data", json);
                    //bundle.putSerializable("resultData", (Serializable) res);
                    orderReceivedFragment.setArguments(bundle);

                    requireActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, orderReceivedFragment)
                            .commit();
                    dismiss();
                }
            }

            @Override
            public void onFailure(Call<PodVerifyOtpResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onPaymentError(int code, String response) {
        CustomToast.error(getContext(),"Payment Failed: " + response);
    }


}
