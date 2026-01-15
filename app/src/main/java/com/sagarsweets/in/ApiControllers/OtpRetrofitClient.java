package com.sagarsweets.in.ApiControllers;

import com.sagarsweets.in.ApiInterface.ApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OtpRetrofitClient {
    private static Retrofit retrofit = null;

    public static ApiService getApiService() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(SuperController.base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}
