package com.sagarsweets.in;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sagarsweets.in.Adapters.ApiSliderAdapter;
import com.sagarsweets.in.Adapters.CategoryAdapter;
import com.sagarsweets.in.Adapters.CategoryShimmerAdapter;
import com.sagarsweets.in.Adapters.PopularProductAdapter;
import com.sagarsweets.in.Adapters.PopularProductShimmerAdapter;
import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiControllers.OtpRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.CategoryModel;
import com.sagarsweets.in.ApiModel.CategoryResponse;
import com.sagarsweets.in.ApiModel.LoginRequest;
import com.sagarsweets.in.ApiModel.PapularProductHome;
import com.sagarsweets.in.ApiModel.PopularProductResponse;
import com.sagarsweets.in.ApiModel.ProductModel;
import com.sagarsweets.in.ApiModel.SliderModel;
import com.sagarsweets.in.ApiModel.SliderResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private ViewPager2 viewPagerSlider;

    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private Runnable sliderRunnable;
    TabLayout tabDots ;

    RecyclerView rvCategories,rvProducts;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sliderRunnable != null) {
            sliderHandler.postDelayed(sliderRunnable, 3000);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        tabDots = view.findViewById(R.id.tabDots);// dot for slider
        viewPagerSlider = view.findViewById(R.id.viewPagerSlider);
        rvCategories = view.findViewById(R.id.rvCategories);
        rvProducts = view.findViewById(R.id.rvProducts);
        rvCategories.setLayoutManager(
                new LinearLayoutManager(
                        getContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false));
        loadSlider();
        loadCategories();
        loadPopularProducts();
        // Inflate the layout for this fragment
        return view;
    }

    private void loadPopularProducts() {
        rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvProducts.setAdapter(new PopularProductShimmerAdapter());

        PapularProductHome papularProductHome = new PapularProductHome("274204","");
        ApiService apiService  = LoginRetrofitClient
                .getClient()
                .create(ApiService.class);
        apiService.getPopularProducts(papularProductHome).enqueue(new Callback<PopularProductResponse>() {
            @Override
            public void onResponse(Call<PopularProductResponse> call,
                                   Response<PopularProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ProductModel> productList = response.body().getResult();

                    rvProducts.setLayoutManager(
                            new GridLayoutManager(getContext(), 2)
                    );

                    PopularProductAdapter adapter =
                            new PopularProductAdapter(getContext(), productList);

                    rvProducts.setAdapter(adapter);
                }else{

                }



            }

            @Override
            public void onFailure(Call<PopularProductResponse> call, Throwable t) {
                Log.d("papular",t.getMessage());
            }
        });

    }


    private void loadCategories() {
        rvCategories.setAdapter(new CategoryShimmerAdapter());

        ApiService apiService = OtpRetrofitClient.getApiService();

        apiService.getCategories().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call,
                                   Response<CategoryResponse> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().isStatus()) {

                    List<CategoryModel> categoryList =
                            response.body().getData();

                    Log.d("CATEGORY_COUNT", String.valueOf(categoryList.size()));

                    // TODO: set adapter to RecyclerView
                    // CategoryAdapter adapter = new CategoryAdapter(getContext(), categoryList);
                    // rvCategories.setAdapter(adapter);
                    CategoryAdapter adapter =
                            new CategoryAdapter(getContext(), categoryList);
                    rvCategories.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Log.e("CATEGORY_API_ERROR", t.getMessage());
            }
        });
    }

    private void loadSlider() {
        ApiService apiService = OtpRetrofitClient.getApiService();
        apiService.getSliderImages().enqueue(new Callback<SliderResponse>() {
            @Override
            public void onResponse(Call<SliderResponse> call, Response<SliderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SliderModel> sliderList = response.body().getData();

                    ApiSliderAdapter adapter =
                            new ApiSliderAdapter(getContext(), sliderList);

                    viewPagerSlider.setAdapter(adapter);
                    viewPagerSlider.setOffscreenPageLimit(3);
                    viewPagerSlider.setNestedScrollingEnabled(false);

                    startAutoScroll(sliderList.size());

                    viewPagerSlider.setNestedScrollingEnabled(false);
                    viewPagerSlider.setOffscreenPageLimit(3);
                    viewPagerSlider.setPageTransformer((page, position) -> {
                        float scale = 0.85f + (1 - Math.abs(position)) * 0.15f;
                        page.setScaleX(scale);
                        page.setScaleY(scale);

                        page.setAlpha(0.5f + (1 - Math.abs(position)) * 0.5f);
                    });

                }
            }

            @Override
            public void onFailure(Call<SliderResponse> call, Throwable t) {
                Log.e("SLIDER_API", t.getMessage());
            }
        });
    }

    private void startAutoScroll(int pageCount) {
        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                if (pageCount <= 1) return;

                int currentItem = viewPagerSlider.getCurrentItem();
                int nextItem = (currentItem + 1) % pageCount;

                viewPagerSlider.setCurrentItem(nextItem, true);
                sliderHandler.postDelayed(this, 3000); // 3 seconds
            }
        };

        sliderHandler.postDelayed(sliderRunnable, 3000);
    }

}