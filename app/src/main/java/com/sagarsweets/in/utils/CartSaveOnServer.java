package com.sagarsweets.in.utils;

import android.util.Log;
import android.view.View;

import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.RemoveCartRequest;
import com.sagarsweets.in.ApiModel.UpdateCartRequest;
import com.sagarsweets.in.Session.CartItem;
import com.sagarsweets.in.Session.LoginSession;

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

        if (v != null) v.setEnabled(false);

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
                if (v != null) v.setEnabled(false);
                if (!response.isSuccessful()) {
                    // optional: retry or log
                    Log.d("CARTUPDATE","Success");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (v != null) v.setEnabled(false);
                t.printStackTrace();
            }
        });
    }

}
