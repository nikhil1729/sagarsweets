package com.sagarsweets.in.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.CartSyncRequest;
import com.sagarsweets.in.ApiModel.CartSyncResponse;
import com.sagarsweets.in.ApiModel.RemoveCartRequest;
import com.sagarsweets.in.ApiModel.UpdateCartRequest;
import com.sagarsweets.in.RoomDatabase.AppDatabase;
import com.sagarsweets.in.Session.CartItem;
import com.sagarsweets.in.Session.LoginSession;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartSaveOnServer {

    public static void saveCartOnServer(CartItem model,
                                        View v,
                                        LoginSession loginSession,
                                        String device) {

        if (!loginSession.isLoggedIn()) return;

        if (v != null) v.setEnabled(false);

        ApiService apiService = LoginRetrofitClient
                .getClient()
                .create(ApiService.class);

        UpdateCartRequest request = new UpdateCartRequest(
                loginSession.getUserId(),
                model.getProductId(),
                model.getQuantity(),
                model.getSizeId(),
                device
        );

        apiService.updateCart(request).enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (v != null) v.setEnabled(true);

                if (response.isSuccessful()) {
                    Log.d("CARTUPDATE", "Success");
                } else {
                    Log.e("CARTUPDATE", "Failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                if (v != null) v.setEnabled(true);

                Log.e("CARTUPDATE", t.getMessage());
            }
        });
    }
    public static void cartRemoveFromServer(CartItem model,
                                            View v,
                                            LoginSession loginSession,
                                            String device){

        if (!loginSession.isLoggedIn()) return;

/*
        if (v != null) v.setEnabled(false);
*/

        ApiService apiService = LoginRetrofitClient
                .getClient()
                .create(ApiService.class);
        RemoveCartRequest removeCartRequest = new
                RemoveCartRequest(loginSession.getUserId(),
                model.getProductId(),
                model.getQuantity(),
                model.getSizeId(),
                device);
        apiService.removeCart(removeCartRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
/*
                if (v != null) v.setEnabled(false);
*/
                if (response.isSuccessful()) {
                    // optional: retry or log
                    Log.d("CARTUPDATE","Success");
                }else{
                    Log.d("CARTUPDATE","Failed");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
/*
                if (v != null) v.setEnabled(false);
*/
                Log.e("CARTUPDATE", t.getMessage());
                t.printStackTrace();
            }
        });
    }
//7388114240 phool mandi varanasi rajkumar mama
    public static void syncFullCart(
            List<CartItem> localCart,
            LoginSession loginSession,
            String deviceInfo, Context context) {

        if (!loginSession.isLoggedIn()) return;

        String userId = loginSession.getUserId();

        CartSyncRequest request =
                new CartSyncRequest(userId, deviceInfo, localCart);
        ApiService apiService = LoginRetrofitClient
                .getClient()
                .create(ApiService.class);
        apiService.syncFullCart(request).enqueue(new Callback<CartSyncResponse>() {
            @Override
            public void onResponse(Call<CartSyncResponse> call, Response<CartSyncResponse> response) {
                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().getStatus()) {

                    List<CartItem> serverCart =
                            response.body().getServerCart();

                    updateLocalCart(context, serverCart);
                }
            }

            @Override
            public void onFailure(Call<CartSyncResponse> call, Throwable t) {
                Log.d("CART_SYNC", "Sync failed: " + t.getMessage());
            }
        });
    }


    private static void updateLocalCart(
            Context context,
            List<CartItem> serverCart) {

        Executors.newSingleThreadExecutor().execute(() -> {

            AppDatabase db =
                    AppDatabase.getInstance(context);

            int userId = Integer.parseInt(
                    new LoginSession(context).getUserId());

            db.cartDao().clearAllCartByUser(userId);
            db.cartDao().clearAllCartByUser(0);// clear cart list for guest user
            if (serverCart != null && !serverCart.isEmpty()) {
                db.cartDao().insertAll(serverCart);
            }

            Log.d("CART_SYNC", "Local cart updated from server");
        });
    }


}
