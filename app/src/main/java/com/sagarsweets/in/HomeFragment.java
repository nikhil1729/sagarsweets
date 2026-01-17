package com.sagarsweets.in;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import com.sagarsweets.in.ApiControllers.OtpRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
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

        loadSlider();
        // Inflate the layout for this fragment
        return view;
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