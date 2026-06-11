package com.sagarsweets.in;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.sagarsweets.in.Adapters.AllCategoryAdapter;
import com.sagarsweets.in.ApiControllers.OtpRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.AllCategoryModel;
import com.sagarsweets.in.ApiModel.AllCategoryResponse;
import com.sagarsweets.in.ApiModel.CategoryModel;
import com.sagarsweets.in.utils.CustomToast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewCategoryFragment extends Fragment {

    private RecyclerView rvCategory;
    private ShimmerFrameLayout shimmerLayout;
    private AllCategoryAdapter categoryAdapter;

    public ViewCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_category, container, false);

        rvCategory = view.findViewById(R.id.rvCategories);
        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        // 1 columns grid
        rvCategory.setLayoutManager(new GridLayoutManager(getContext(), 1));
        //rvCategory.setHasFixedSize(true);

        loadAllCategory();

        return view;
    }

    private void loadAllCategory() {
        ApiService apiService = OtpRetrofitClient.getApiService();

        apiService.getAllCategories().enqueue(new Callback<AllCategoryResponse>() {
            @Override
            public void onResponse(
                    @NonNull Call<AllCategoryResponse> call,
                    @NonNull Response<AllCategoryResponse> response
            ) {

                if (response.isSuccessful() && response.body() != null) {

                    shimmerLayout.stopShimmer();

                    shimmerLayout.setVisibility(GONE);

                    rvCategory.setVisibility(VISIBLE);
                    AllCategoryResponse categoryResponse = response.body();

                    if (categoryResponse.isStatus()
                            && categoryResponse.getData() != null
                            && !categoryResponse.getData().isEmpty()) {

                        List<AllCategoryModel> categoryList = categoryResponse.getData();

                        categoryAdapter = new AllCategoryAdapter(getContext(), categoryList);
                        rvCategory.setAdapter(categoryAdapter);

                    } else {
                        CustomToast.error(getContext(),"Category not found");

                    }

                } else {
                    CustomToast.error(getContext(),"Something went wrong");

                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<AllCategoryResponse> call,
                    @NonNull Throwable t
            ) {
                Log.e("CategoryAPI", "Error: " + t.getMessage());
                CustomToast.error(getContext(),t.getMessage());
            }
        });
    }
}
