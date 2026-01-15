package com.sagarsweets.in.ApiInterface;



import com.sagarsweets.in.ApiModel.LoginRequest;
import com.sagarsweets.in.ApiModel.LoginResponse;
import com.sagarsweets.in.ApiModel.OtpResponse;
import com.sagarsweets.in.ApiModel.RegisterUserRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
public interface ApiService {
    @POST("rest/user/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);


    @FormUrlEncoded
    @POST("rest/user/sendotp")
    Call<OtpResponse> sendOtp(
            @Field("mobile") String mobile
    );

    @POST("rest/user/registration")
    Call<OtpResponse> registerUser(@Body RegisterUserRequest registerUserRequest);

}
