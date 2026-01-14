package com.sagarsweets.in.ApiInterface;



import com.sagarsweets.in.ApiModel.LoginRequest;
import com.sagarsweets.in.ApiModel.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
public interface ApiService {
    @POST("rest/user/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);
}
