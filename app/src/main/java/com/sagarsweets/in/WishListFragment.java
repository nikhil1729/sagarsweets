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
import android.widget.Toast;

import com.sagarsweets.in.Adapters.PopularProductAdapter;
import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.ProductModel;
import com.sagarsweets.in.ApiModel.WishListProductResponse;
import com.sagarsweets.in.RoomDatabase.AppDatabase;
import com.sagarsweets.in.Session.LoginSession;
import com.sagarsweets.in.Session.PincodeSession;
import com.sagarsweets.in.Session.WishlistItem;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishListFragment extends Fragment {
    RecyclerView rvWishlist;
    LoginSession loginSession;
    PincodeSession pincodeSession;

    LinearLayout layoutEmpty;
    List<WishlistItem> wishlistItem;
    ImageView imgHeart;
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
        getWishListProducts();
        // Inflate the layout for this fragment
        return view;
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
        if(loginSession.isLoggedIn()){
            // get wish list from api

        }else{
            // get from room db
            AppDatabase db = AppDatabase.getInstance(getContext());

            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    wishlistItem = db.wishlistDao().getAllItems();
                    if (wishlistItem == null || wishlistItem.size() == 0) {
                        layoutEmpty.setVisibility(View.VISIBLE);
                        rvWishlist.setVisibility(View.GONE);

                    } else {
                        layoutEmpty.setVisibility(View.GONE);
                        rvWishlist.setVisibility(View.VISIBLE);
                        fetchDataFromApi();
                    }
                }
            });
        }
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
        ApiService apiService = LoginRetrofitClient
                .getClient()
                .create(ApiService.class);
        apiService.getwishlistdatanonlogin(wishlistItem).enqueue(new Callback<WishListProductResponse>() {
            @Override
            public void onResponse(Call<WishListProductResponse> call, Response<WishListProductResponse> response) {
                if (response.body() != null && response.body().isStatus()) {
                    List<ProductModel> productList = response.body().getResult();
                    rvWishlist.setLayoutManager(
                            new GridLayoutManager(getContext(), 2)
                    );
                    PopularProductAdapter adapter =
                            new PopularProductAdapter(getContext(), productList, false);
                    rvWishlist.setAdapter(adapter);
                }else{
                    Toast.makeText(getContext(),"Response body is null or false",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<WishListProductResponse> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initView(View view) {
        rvWishlist = view.findViewById(R.id.rvWishlist);
        layoutEmpty = view.findViewById(R.id.layoutEmpty);
        imgHeart = view.findViewById(R.id.imgEmptyWishlist);
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


}