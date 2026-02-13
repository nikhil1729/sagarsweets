package com.sagarsweets.in.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.WishListRequest;
import com.sagarsweets.in.ApiModel.WishListResponse;
import com.sagarsweets.in.R;
import com.sagarsweets.in.RoomDatabase.AppDatabase;
import com.sagarsweets.in.Session.WishlistItem;

import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishListClicked {

    public static void clicked(Context context, String userId, String pId, ImageView imgWishlist){
        String device = DeviceInfo.getDeviceString(context);
        WishListRequest wishListRequest = new WishListRequest(userId,pId,device);
        ApiService apiService = LoginRetrofitClient
                .getClient()
                .create(ApiService.class);
        apiService.toggelWishList(wishListRequest).enqueue(new Callback<WishListResponse>() {
            @Override
            public void onResponse(Call<WishListResponse> call, Response<WishListResponse> response) {
                if(response.body().getStatus()){
                    if(response.body().getAction().equals("added")){
                        imgWishlist.setImageResource(R.drawable.ic_heart_filled);
                    }else{
                        imgWishlist.setImageResource(R.drawable.ic_heart_outline);
                    }
                    imgWishlist.setScaleX(0.8f);
                    imgWishlist.setScaleY(0.8f);
                    imgWishlist.animate().scaleX(1f).scaleY(1f).setDuration(200);
                }else{
                    Toast.makeText(context,response.body().getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<WishListResponse> call, Throwable t) {
                Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void clickNonLogin(Context context,String pId,ImageView imgWishlist){
        AppDatabase db = AppDatabase.getInstance(context);

        int productId = Integer.parseInt(pId);

        Executors.newSingleThreadExecutor().execute(() -> {

            if (db.wishlistDao().isExists(productId)) {

                db.wishlistDao().deleteById(productId);

                ((Activity) context).runOnUiThread(() ->
                        imgWishlist.setImageResource(R.drawable.ic_heart_outline)
                );

            } else {

                WishlistItem item = new WishlistItem(
                        productId
                );

                db.wishlistDao().insert(item);

                ((Activity) context).runOnUiThread(() ->
                        imgWishlist.setImageResource(R.drawable.ic_heart_filled)
                );
            }

        });

    }
}
