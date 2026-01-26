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
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.sagarsweets.in.Adapters.ApiSliderAdapter;
import com.sagarsweets.in.Adapters.CategoryAdapter;
import com.sagarsweets.in.Adapters.CategoryShimmerAdapter;
import com.sagarsweets.in.Adapters.CategoryWiseAdapter;
import com.sagarsweets.in.Adapters.PopularProductAdapter;
import com.sagarsweets.in.Adapters.PopularProductShimmerAdapter;
import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiControllers.OtpRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.CategoryModel;
import com.sagarsweets.in.ApiModel.CategoryResponse;
import com.sagarsweets.in.ApiModel.PapularProductHome;
import com.sagarsweets.in.ApiModel.PopularProductResponse;
import com.sagarsweets.in.ApiModel.ProductModel;
import com.sagarsweets.in.ApiModel.SliderModel;
import com.sagarsweets.in.ApiModel.SliderResponse;
import com.sagarsweets.in.ApiModel.TopCategoryDataModel;
import com.sagarsweets.in.ApiModel.TopCategoryRequest;
import com.sagarsweets.in.ApiModel.TopCategoryResponse;
import com.sagarsweets.in.Session.LoginSession;
import com.sagarsweets.in.Session.PincodeSession;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private ViewPager2 viewPagerSlider;

    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private Runnable sliderRunnable;
    TabLayout tabDots ;
    LoginSession loginSession;
    PincodeSession pincodeSession;
    String userId;
    String pincode;
    RecyclerView rvCategories,rvProducts;
    TextView tvViewAllCategory;
    RecyclerView rvCategoryWiseProducts;
    CategoryWiseAdapter categoryWiseAdapter;
    List<TopCategoryResponse> categoryWiseList;
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
        loginSession = new LoginSession(getContext());
        pincodeSession = new PincodeSession(getContext());
        tvViewAllCategory = view.findViewById(R.id.tvViewAllCategory);
        rvCategoryWiseProducts = view.findViewById(R.id.rvCategoryWiseProducts);

        userId = "";
        pincode = "";
        if(pincodeSession.hasPincode()){
            pincode = pincodeSession.getPincode();
        }

        tvViewAllCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewCategoryFragment viewCategoryFragment = new ViewCategoryFragment();
                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, viewCategoryFragment)
                        .addToBackStack("view_all_category")
                        .commit();
            }
        });
        if(loginSession.isLoggedIn()){
            userId = loginSession.getUserId();
        }
        rvCategories.setLayoutManager(
                new LinearLayoutManager(
                        getContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false));
        loadSlider();
        loadCategories();
        loadPopularProducts();
        loadTopCategoryProducts();
        // Inflate the layout for this fragment
        return view;
    }

    private void loadTopCategoryProducts() {
        rvCategoryWiseProducts.setLayoutManager(
                new LinearLayoutManager(getContext())
        );
        rvCategoryWiseProducts.setNestedScrollingEnabled(false);


        TopCategoryRequest topCategoryModel = new TopCategoryRequest(pincode,userId);
        ApiService apiService = LoginRetrofitClient
                .getClient()
                .create(ApiService.class);
        apiService.getTopCategory(topCategoryModel).enqueue(new Callback<TopCategoryResponse>() {
            @Override
            public void onResponse(Call<TopCategoryResponse> call, Response<TopCategoryResponse> response) {
                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().isStatus()) {
                    List<TopCategoryDataModel> topCategoryDataModels =
                            response.body().getData();

                    Log.d("topcategory",
                            "Category count = " + topCategoryDataModels.size());
                    categoryWiseAdapter =
                            new CategoryWiseAdapter(
                                    requireContext(),
                                    topCategoryDataModels
                            );

                    rvCategoryWiseProducts.setAdapter(categoryWiseAdapter);

                }
            }

            @Override
            public void onFailure(Call<TopCategoryResponse> call, Throwable t) {

            }
        });
    }

    private void loadPopularProducts() {
        rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvProducts.setAdapter(new PopularProductShimmerAdapter());
        PapularProductHome papularProductHome =
                new PapularProductHome(pincode,userId,getContext());
        ApiService apiService  = LoginRetrofitClient
                .getClient()
                .create(ApiService.class);
        apiService.getPopularProducts(papularProductHome).enqueue(new Callback<PopularProductResponse>() {
            @Override
            public void onResponse(Call<PopularProductResponse> call,
                                   Response<PopularProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ProductModel> productList = response.body().getResult();
                    Log.d("here","sasasaaaa");
                    rvProducts.setLayoutManager(
                            new GridLayoutManager(getContext(), 2)
                    );
                    PopularProductAdapter adapter =
                            new PopularProductAdapter(getContext(), productList,false);
                    rvProducts.setAdapter(adapter);
                }else{
                    Log.d("here","elseeeeee");
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