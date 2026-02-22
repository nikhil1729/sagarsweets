package com.sagarsweets.in;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sagarsweets.in.Adapters.CartAdapter;
import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.RemoveCartRequest;
import com.sagarsweets.in.ApiModel.StockRequest;
import com.sagarsweets.in.ApiModel.StockResponse;
import com.sagarsweets.in.ApiModel.UpdateCartRequest;
import com.sagarsweets.in.RoomDatabase.AppDatabase;
import com.sagarsweets.in.Session.CartItem;
import com.sagarsweets.in.Session.LoginSession;
import com.sagarsweets.in.utils.CartSaveOnServer;
import com.sagarsweets.in.utils.DeviceInfo;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFragment extends Fragment {

    RecyclerView rvCart;
    LinearLayout layoutEmpty;
    EditText etCoupon;
    TextView txtCouponError,txtAppliedCoupon;
    LinearLayout layoutCouponApplied;
    TextView txtSubtotal,txtDelivery,txtDiscount,txtGrandTotal;
    Button btnCheckout;
    TextView btnApplyCoupon,btnRemoveCoupon,tvItemCount;
    CardView cardItemCount;

    CartAdapter adapter;
    AppDatabase db;
    private final Executor executor = Executors.newSingleThreadExecutor();
    LoginSession loginSession;
    String device;
    public CartFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        initViews(view);
        //getCartProductDetailsApi();
        setCart();
        syncronizeCart();
        //clearCartAllItem();
        // Inflate the layout for this fragment
        return view;
    }

    private void syncronizeCart() {
        if(loginSession.isLoggedIn()){
            Executors.newSingleThreadExecutor().execute(() -> {

                int userId = Integer.parseInt(loginSession.getUserId());

                List<CartItem> localCart = db.cartDao().getCartItemsList(userId);

                CartSaveOnServer.syncFullCart(
                        localCart,
                        loginSession,
                        DeviceInfo.getDeviceString(getContext()),
                        getContext()
                );

            });
            //CartSaveOnServer.syncFullCart();
        }
    }



    private void setCart() {


        int userId = loginSession.isLoggedIn()
                ? Integer.parseInt(loginSession.getUserId())
                : 0;

        adapter = new CartAdapter(getContext(), new CartAdapter.CartItemListener() {

            @Override
            public void onQuantityChanged(CartItem model, View v) {
                checkStockFromApi(model, v);

                /* executor.execute(() -> {

                    db.cartDao().update(model);
                });
                // 🔥 If logged in → update server
                if (loginSession.isLoggedIn()) {
                    updateCartOnServer(model,v);
                } */
            }

            @Override
            public void onItemRemoved(CartItem model, View v) {

                executor.execute(() -> {
                    db.cartDao().deleteCart(model);
                });
                if (loginSession.isLoggedIn()) {
                    removeCartFromServer(model,v);
                }
            }
        });

        rvCart.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCart.setAdapter(adapter);

        // 🔥 Observe LiveData
        db.cartDao().getCartItems(userId)
                .observe(getViewLifecycleOwner(), cartList -> {

                    if (cartList != null && !cartList.isEmpty()) {

                        adapter.setList(cartList);

                        layoutEmpty.setVisibility(View.GONE);
                        rvCart.setVisibility(View.VISIBLE);

                        tvItemCount.setText(cartList.size() + " Items in Cart");

                        cardItemCount.setVisibility(View.VISIBLE);
                        calculateTotal(cartList);

                    } else {

                        layoutEmpty.setVisibility(View.VISIBLE);
                        rvCart.setVisibility(View.GONE);

                        txtSubtotal.setText("₹0");
                        txtGrandTotal.setText("₹0");
                        cardItemCount.setVisibility(View.GONE);
                    }
                });
    }

    private void checkStockFromApi(CartItem model, View v) {

        ApiService apiService =
                LoginRetrofitClient.getClient().create(ApiService.class);

        StockRequest request =
                new StockRequest(model.getProductId(), model.getSizeId());

        apiService.checkStock(request).enqueue(new Callback<StockResponse>() {

            @Override
            public void onResponse(Call<StockResponse> call,
                                   Response<StockResponse> response) {

                if (!response.isSuccessful() || response.body() == null) return;

                Integer availableStock = (Integer) response.body().getStock();
                if (availableStock == null) availableStock = 0;

                int finalStock = availableStock;

                executor.execute(() -> {

                    if (model.getQuantity() > finalStock) {

                        requireActivity().runOnUiThread(() -> {
                            model.setQuantity(finalStock);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), "Adjusted to max stock.", Toast.LENGTH_SHORT).show();
                            Log.d("STOCK", "Adjusted to max stock");
                        });

                        return;
                    }

                    // ✅ safe to update DB
                    db.cartDao().update(model);

                    if (loginSession.isLoggedIn()) {
                        requireActivity().runOnUiThread(() ->
                                updateCartOnServer(model, v));
                    }
                });
            }

            @Override
            public void onFailure(Call<StockResponse> call, Throwable t) {
                Log.e("STOCK_API", t.getMessage());
            }
        });
    }

    private void removeCartFromServer(CartItem model, View v) {
        // util static function for remove product in cart
        CartSaveOnServer.cartRemoveFromServer(model,v,loginSession,device);
    }

    private void updateCartOnServer(CartItem model, View v) {
        // util static function for saving product in cart
        CartSaveOnServer.saveCartOnServer(model,v,loginSession,device);
    }

    private void calculateTotal(List<CartItem> cartList) {
        double subtotal = 0;

        for (CartItem item : cartList) {
            subtotal += item.getPrice() * item.getQuantity();
        }

        double delivery = subtotal > 500 ? 0 : 40;   // Free delivery above 500
        double discount = 0; // Later connect coupon

        double grandTotal = subtotal + delivery - discount;

        txtSubtotal.setText("₹" + subtotal);
        txtDelivery.setText("₹" + delivery);
        txtDiscount.setText("- ₹" + discount);
        txtGrandTotal.setText("₹" + grandTotal);
    }


    private void initViews(View view) {
        rvCart = view.findViewById(R.id.rvCart);
        layoutEmpty = view.findViewById(R.id.layoutEmpty);
        etCoupon = view.findViewById(R.id.etCoupon);
        txtCouponError = view.findViewById(R.id.txtCouponError);
        txtAppliedCoupon = view.findViewById(R.id.txtAppliedCoupon);
        layoutCouponApplied = view.findViewById(R.id.layoutCouponApplied);
        txtSubtotal = view.findViewById(R.id.txtSubtotal);
        txtDelivery = view.findViewById(R.id.txtDelivery);
        txtDiscount = view.findViewById(R.id.txtDiscount);
        txtGrandTotal = view.findViewById(R.id.txtGrandTotal);
        btnCheckout = view.findViewById(R.id.btnCheckout);
        btnApplyCoupon = view.findViewById(R.id.btnApplyCoupon);
        btnRemoveCoupon = view.findViewById(R.id.btnRemoveCoupon);
        cardItemCount = view.findViewById(R.id.cardItemCount);
        tvItemCount = view.findViewById(R.id.tvItemCount);
        db = AppDatabase.getInstance(getContext());
        loginSession = new LoginSession(getContext());
        device = DeviceInfo.getDeviceString(getContext());
    }

    private void clearCartAllItem() {
        LoginSession loginSession = new LoginSession(getContext());
        int userId = loginSession.isLoggedIn()
                ? Integer.parseInt(loginSession.getUserId())
                : 0;

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase.getInstance(getContext())
                    .cartDao()
                    .clearAllCartByUser(userId);
        });
    }


}