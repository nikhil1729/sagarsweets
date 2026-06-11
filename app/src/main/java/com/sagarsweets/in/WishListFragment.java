package com.sagarsweets.in;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.sagarsweets.in.Adapters.PopularProductAdapter;
import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.ProductModel;
import com.sagarsweets.in.ApiModel.WishListByLoggedInUserRequest;
import com.sagarsweets.in.ApiModel.WishListByLoggedInUserResponse;
import com.sagarsweets.in.ApiModel.WishListProductResponse;
import com.sagarsweets.in.RoomDatabase.AppDatabase;
import com.sagarsweets.in.Session.LoginSession;
import com.sagarsweets.in.Session.PincodeSession;
import com.sagarsweets.in.Session.WishlistItem;
import com.sagarsweets.in.utils.CustomToast;
import com.sagarsweets.in.utils.DeviceInfo;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishListFragment extends Fragment
        implements PopularProductAdapter.CartUpdateListener{
    RecyclerView rvWishlist;
    LoginSession loginSession;
    PincodeSession pincodeSession;

    LinearLayout layoutEmpty;
    List<WishlistItem> wishlistItem;
    ImageView imgHeart;
    TextView tvMessage;
    ShimmerFrameLayout shimmer;
    Button btnStartShopping;
    private AnimatorSet heartBeatAnimator;
    public WishListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wish_list, container, false);
        initView(view);
        startButtonClicked();
        if(loginSession.isLoggedIn()){
            getWishListProductsByUser();
        }else{
            getWishListProducts();
        }

        // Inflate the layout for this fragment
        return view;
    }

    private void getWishListProductsByUser() {
        shimmer.startShimmer();
        shimmer.setVisibility(View.VISIBLE);
        rvWishlist.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.GONE);
        ApiService apiService = LoginRetrofitClient
                .getClient()
                .create(ApiService.class);
        WishListByLoggedInUserRequest wishListByLoggedInUserRequest =
                new WishListByLoggedInUserRequest(loginSession.getUserId(),
                        pincodeSession.getPincode(), DeviceInfo.getDeviceString(getContext()));
        Call<WishListByLoggedInUserResponse> call =
                apiService.getWishListLoggedUser(wishListByLoggedInUserRequest);
        call.enqueue(new Callback<WishListByLoggedInUserResponse>() {
            @Override
            public void onResponse(Call<WishListByLoggedInUserResponse> call, Response<WishListByLoggedInUserResponse> response) {
                if (response.body() != null && response.body().isStatus()) {
                    shimmer.stopShimmer();
                    shimmer.setVisibility(View.GONE);

                    List<ProductModel> productList = response.body().getResult();
                    if(productList.isEmpty()){
                         layoutEmpty.setVisibility(View.VISIBLE);
                         return;
                    }
                    rvWishlist.setVisibility(View.VISIBLE);
                    rvWishlist.setLayoutManager(
                            new GridLayoutManager(getContext(), 2)
                    );
                    PopularProductAdapter adapter =
                            new PopularProductAdapter(getContext(), productList,
                                    false,WishListFragment.this);
                    rvWishlist.setAdapter(adapter);
                }else{
                    shimmer.stopShimmer();
                    shimmer.setVisibility(View.GONE);
                    rvWishlist.setVisibility(View.GONE);
                    layoutEmpty.setVisibility(View.VISIBLE);
                    tvMessage.setText(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<WishListByLoggedInUserResponse> call, Throwable t) {
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                rvWishlist.setVisibility(View.GONE);
                layoutEmpty.setVisibility(View.VISIBLE);
                tvMessage.setText(t.getMessage());
            }
        });


    }

    private void startButtonClicked() {
        btnStartShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment homeFragment = new HomeFragment();
                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, homeFragment)
                        .addToBackStack("home_fragment")
                        .commit();
            }
        });
    }


    private void getWishListProducts() {

        AppDatabase db = AppDatabase.getInstance(getContext());

        // Start shimmer on MAIN thread
        shimmer.startShimmer();
        shimmer.setVisibility(View.VISIBLE);
        rvWishlist.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.GONE);

        Executors.newSingleThreadExecutor().execute(() -> {

            wishlistItem = db.wishlistDao().getAllItems();

            // Switch back to MAIN thread for UI updates
            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    Log.d("WISHLISTITEM", String.valueOf(wishlistItem.isEmpty()));
                    if (wishlistItem == null || wishlistItem.isEmpty()) {

                        shimmer.stopShimmer();
                        shimmer.setVisibility(View.GONE);
                        layoutEmpty.setVisibility(View.VISIBLE);
                        rvWishlist.setVisibility(View.GONE);
                        Log.d("WISHLISTITEM","hi");
                    } else {

                        fetchDataFromApi(); // safe (Retrofit callback runs on main thread)
                    }

                });
            }
        });
    }




    private void dhakDhakHeart() {

        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(imgHeart, "scaleX", 1f, 1.2f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(imgHeart, "scaleY", 1f, 1.2f);

        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(imgHeart, "scaleX", 1.2f, 1f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(imgHeart, "scaleY", 1.2f, 1f);

        scaleUpX.setDuration(150);
        scaleUpY.setDuration(150);

        scaleDownX.setDuration(150);
        scaleDownY.setDuration(150);

        heartBeatAnimator = new AnimatorSet();
        heartBeatAnimator.play(scaleUpX).with(scaleUpY);
        heartBeatAnimator.play(scaleDownX).with(scaleDownY).after(scaleUpX);

        heartBeatAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        heartBeatAnimator.setStartDelay(400);
        heartBeatAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                heartBeatAnimator.start(); // loop manually for natural pause
            }
        });

        heartBeatAnimator.start();
    }


    private void fetchDataFromApi() {

        //layoutEmpty.setVisibility(View.GONE);
        ApiService apiService = LoginRetrofitClient
                .getClient()
                .create(ApiService.class);
        apiService.getwishlistdatanonlogin(wishlistItem).enqueue(new Callback<WishListProductResponse>() {
            @Override
            public void onResponse(Call<WishListProductResponse> call, Response<WishListProductResponse> response) {
                if (response.body() != null && response.body().isStatus()) {
                    shimmer.stopShimmer();
                    shimmer.setVisibility(View.GONE);

                    List<ProductModel> productList = response.body().getResult();
                    if(productList.isEmpty()){
                        layoutEmpty.setVisibility(View.VISIBLE);
                        return;
                    }
                    rvWishlist.setVisibility(View.VISIBLE);
                    rvWishlist.setLayoutManager(
                            new GridLayoutManager(getContext(), 2)
                    );
                    PopularProductAdapter adapter =
                            new PopularProductAdapter(getContext(), productList,
                                    false,WishListFragment.this);
                    rvWishlist.setAdapter(adapter);
                }else{
                    CustomToast.error(getContext(),"Response body is null or false");
                }
            }

            @Override
            public void onFailure(Call<WishListProductResponse> call, Throwable t) {
                if (isAdded()) {
                    shimmer.stopShimmer();
                    shimmer.setVisibility(View.GONE);

                    if (wishlistItem == null || wishlistItem.isEmpty()) {
                        layoutEmpty.setVisibility(View.VISIBLE);
                    }
                }
                CustomToast.error(getContext(),t.getMessage());

            }
        });
    }

    private void initView(View view) {
        shimmer = view.findViewById(R.id.shimmerLayout);
        rvWishlist = view.findViewById(R.id.rvWishlist);
        layoutEmpty = view.findViewById(R.id.layoutEmpty);
        imgHeart = view.findViewById(R.id.imgEmptyWishlist);
        tvMessage = view.findViewById(R.id.tvMessage);
        dhakDhakHeart();
        btnStartShopping = view.findViewById(R.id.btnStartShopping);
        loginSession = new LoginSession(getContext());
        pincodeSession = new PincodeSession(getContext());

    }

    @Override
    public void onPause() {
        super.onPause();
        if (heartBeatAnimator != null) {
            heartBeatAnimator.cancel();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (shimmer != null) {
            shimmer.stopShimmer();
        }

        if (heartBeatAnimator != null) {
            heartBeatAnimator.cancel();
        }
    }

    @Override
    public void onCartUpdated() {

        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).onCartUpdated();
        }
    }


}