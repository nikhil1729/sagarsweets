package com.sagarsweets.in.ApiInterface;



import com.sagarsweets.in.ApiModel.AboutResponse;
import com.sagarsweets.in.ApiModel.AddAddressRequest;
import com.sagarsweets.in.ApiModel.AddAddressResponse;
import com.sagarsweets.in.ApiModel.AllCategoryResponse;
import com.sagarsweets.in.ApiModel.CartSyncRequest;
import com.sagarsweets.in.ApiModel.CartSyncResponse;
import com.sagarsweets.in.ApiModel.CategoryProductRequest;
import com.sagarsweets.in.ApiModel.CategoryProductResponse;
import com.sagarsweets.in.ApiModel.CategoryResponse;
import com.sagarsweets.in.ApiModel.CheckoutRequest;
import com.sagarsweets.in.ApiModel.CheckoutResponse;
import com.sagarsweets.in.ApiModel.ContactUsFormRequest;
import com.sagarsweets.in.ApiModel.ContactUsFormResponse;
import com.sagarsweets.in.ApiModel.ContactUsResponse;
import com.sagarsweets.in.ApiModel.CouponRequest;
import com.sagarsweets.in.ApiModel.CouponResponse;
import com.sagarsweets.in.ApiModel.ForgetPasswordRequest;
import com.sagarsweets.in.ApiModel.GetUserAddressResponse;
import com.sagarsweets.in.ApiModel.LoginOtpRequest;
import com.sagarsweets.in.ApiModel.LoginRequest;
import com.sagarsweets.in.ApiModel.LoginResponse;
import com.sagarsweets.in.ApiModel.MyOrderRequest;
import com.sagarsweets.in.ApiModel.MyOrderResponse;
import com.sagarsweets.in.ApiModel.OtpResponse;
import com.sagarsweets.in.ApiModel.PapularProductHome;
import com.sagarsweets.in.ApiModel.PayonDeleveryOtpRequest;
import com.sagarsweets.in.ApiModel.PayonDeleveryOtpResponse;
import com.sagarsweets.in.ApiModel.PincodeRequest;
import com.sagarsweets.in.ApiModel.PincodeResponse;
import com.sagarsweets.in.ApiModel.PodVerifyOtpRequest;
import com.sagarsweets.in.ApiModel.PodVerifyOtpResponse;
import com.sagarsweets.in.ApiModel.PolicyResponse;
import com.sagarsweets.in.ApiModel.PopularProductResponse;
import com.sagarsweets.in.ApiModel.ProductDetailsModel;
import com.sagarsweets.in.ApiModel.ProductDetailsOfCartRequest;
import com.sagarsweets.in.ApiModel.ProductDetailsOfCartResponse;
import com.sagarsweets.in.ApiModel.ProductDetailsRequest;
import com.sagarsweets.in.ApiModel.ProductReviewRequest;
import com.sagarsweets.in.ApiModel.RazorpayRequest;
import com.sagarsweets.in.ApiModel.RegisterUserRequest;
import com.sagarsweets.in.ApiModel.RemoveCartRequest;
import com.sagarsweets.in.ApiModel.ReviewResponse;
import com.sagarsweets.in.ApiModel.SearchProductRequest;
import com.sagarsweets.in.ApiModel.SearchResponse;
import com.sagarsweets.in.ApiModel.SliderResponse;
import com.sagarsweets.in.ApiModel.StockRequest;
import com.sagarsweets.in.ApiModel.StockResponse;
import com.sagarsweets.in.ApiModel.TopCategoryRequest;
import com.sagarsweets.in.ApiModel.TopCategoryResponse;
import com.sagarsweets.in.ApiModel.UpdateCartRequest;
import com.sagarsweets.in.ApiModel.UserAddressRequest;
import com.sagarsweets.in.ApiModel.WishListByLoggedInUserRequest;
import com.sagarsweets.in.ApiModel.WishListByLoggedInUserResponse;
import com.sagarsweets.in.ApiModel.WishListProductResponse;
import com.sagarsweets.in.ApiModel.WishListRequest;
import com.sagarsweets.in.ApiModel.WishListResponse;
import com.sagarsweets.in.ApiModel.WishListSyncronizeRequest;
import com.sagarsweets.in.ApiModel.WishListSyncronizeResponse;
import com.sagarsweets.in.Session.WishlistItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("rest/default/savecontactus")
    Call<ContactUsFormResponse> saveContactUs(@Body ContactUsFormRequest contactUsFormRequest);

    @POST("rest/user/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("rest/user/getwishlistbyuserid")
    Call<WishListByLoggedInUserResponse> getWishListLoggedUser(@Body WishListByLoggedInUserRequest wishListByLoggedInUserRequest);
    @POST("rest/user/savewishlist")
    Call<WishListResponse> toggelWishList(@Body WishListRequest wishListRequest);

    @POST("rest/user/getwishlistdatanonguest")
    Call<WishListProductResponse> getwishlistdatanonlogin(@Body List<WishlistItem> wishlistItem);

    @POST("rest/user/mergewishlist")
    Call<WishListSyncronizeResponse>syncWishlist(@Body WishListSyncronizeRequest wishListSyncronizeRequest);
    @POST("rest/user/loginbyotp")
    Call<LoginResponse> loginUserByOtp(@Body LoginOtpRequest loginOtpRequest);

    @FormUrlEncoded
    @POST("rest/user/sendotp")
    Call<OtpResponse> sendOtp(
            @Field("mobile") String mobile
    );

    @POST("rest/user/registration")
    Call<OtpResponse> registerUser(@Body RegisterUserRequest registerUserRequest);

    @FormUrlEncoded
    @POST("rest/user/sendotpforreset")
    Call<OtpResponse>sendOtpForReset(
      @Field("mobile") String mobile,
      @Field("ip") String ip,
      @Field("device_info") String device_info
    );

    @POST("rest/user/forgetpassword")
    Call<OtpResponse> forgetPassword(@Body ForgetPasswordRequest forgetPasswordRequest);

    @GET("rest/homefragment/index")
    Call<SliderResponse> getSliderImages();

    @GET("rest/default/aboutus")
    Call<AboutResponse> getAboutUs();

    @GET("rest/default/privacy")
    Call<PolicyResponse> getPolicy();
    @GET("rest/default/contactus")
    Call<ContactUsResponse> getContactUs();


    @GET("rest/homefragment/getcategory")   // 👈 homefragment category
    Call<CategoryResponse> getCategories();

    @GET("rest/homefragment/getallcategory")   // 👈 homefragment category and sub category
    Call<AllCategoryResponse> getAllCategories();

    @POST("rest/homefragment/getlatestarrivals")
    Call<PopularProductResponse> getPopularProducts(@Body PapularProductHome papularProductHome);

    @POST("rest/product/getproductbycategoryid")
    Call<CategoryProductResponse> getCategoryProduct(@Body CategoryProductRequest categoryProductRequest);

    @POST("rest/default/searchproduct")
    Call<SearchResponse> getSearchProduct(@Body SearchProductRequest searchProductRequest);

    @POST("rest/homefragment/topcategory")
    Call<TopCategoryResponse> getTopCategory(@Body TopCategoryRequest topCategoryModel);


    @POST("rest/product/productdetails")
    Call<ProductDetailsModel> getProductDetails(@Body ProductDetailsRequest productDetailsRequest);

    @POST("rest/product/getreviewdetails")
    Call<ReviewResponse> getProductReview(@Body ProductReviewRequest productReviewRequest);

    @POST("rest/default/getpincodebypin")
    Call<PincodeResponse> getPincodeStatus(@Body PincodeRequest pincodeRequest);

    @POST("rest/user/getproductdetailsofcart")
    Call<ProductDetailsOfCartResponse> getProductDetailsOfCart(
            @Body ProductDetailsOfCartRequest productDetailsOfCartRequest);
    @GET("rest/default/searchsuggestions")
    Call<List<String>> getSuggestions(@Query("query") String query);

    @POST("rest/user/cartupdate")
    Call<Void> updateCart(@Body UpdateCartRequest updateCartRequest);

    @POST("rest/user/cartremove")
    Call<Void> removeCart(@Body RemoveCartRequest removeCartRequest);

    @POST("rest/user/syncfull")
    Call<CartSyncResponse> syncFullCart(@Body CartSyncRequest cartSyncRequest);
    /*Call<Void> updateCart(String user_id, int productId, int quantity, int sizeId, String device);*/

    @POST("rest/user/checkstock")
    Call<StockResponse> checkStock(@Body StockRequest request);

    @POST("rest/coupons/apply")
    Call<CouponResponse> couponApply(@Body CouponRequest couponRequest);
    @POST("rest/checkout/index")
    Call<CheckoutResponse> checkoutIndex(@Body CheckoutRequest checkoutRequest);

    @POST("rest/user/getuseraddress")
    Call<GetUserAddressResponse> getUserAddress(@Body UserAddressRequest userAddressRequest);
    @POST("rest/user/saveuseraddress")
    Call<AddAddressResponse> addAddressUser(@Body AddAddressRequest addAddressRequest);


    /* SEND OTP FOR POD ORDERS */
    @POST("rest/checkout/payondeliveryotp")
    Call<PayonDeleveryOtpResponse> getPayonDeliveryOtp(@Body PayonDeleveryOtpRequest payonDeleveryOtpRequest);
    @POST("rest/checkout/podotpconfirm")
    Call<PodVerifyOtpResponse> getVerifyPodOtp(@Body PodVerifyOtpRequest podVerifyOtpRequest);

    @POST("rest/checkout/razorpay")
    Call<PodVerifyOtpResponse> getVerifyRazorPay(@Body RazorpayRequest razorpayRequest);

    /*MYORDER API*/
    @POST("rest/order/myorder")
    Call<MyOrderResponse> getMyOrder(@Body MyOrderRequest myOrderRequest);
}
