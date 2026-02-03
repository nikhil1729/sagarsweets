package com.sagarsweets.in;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.sagarsweets.in.Adapters.PopularProductAdapter;
import com.sagarsweets.in.Adapters.PopularProductShimmerAdapter;
import com.sagarsweets.in.Adapters.SubCategorySpinnerAdapter;
import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.CategoryModel;
import com.sagarsweets.in.ApiModel.CategoryProductRequest;
import com.sagarsweets.in.ApiModel.CategoryProductResponse;
import com.sagarsweets.in.ApiModel.ProductModel;
import com.sagarsweets.in.Session.LoginSession;
import com.sagarsweets.in.Session.PincodeSession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductViewCategoryFragment extends Fragment {

    String categoryId,categoryName ;
    TextView tvCategoryName;
    LinearLayout layoutSort,layoutFilter;
    RecyclerView rvProducts,rvShimmer;
    ShimmerFrameLayout shimmerLayout;
    CategoryProductRequest categoryProductRequest;
    String user_id, pincode;
    Integer pageSize,pageNumber;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    String sort;

    LoginSession loginSession;
    PincodeSession pincodeSession;
    List<ProductModel> productList = new ArrayList<>();
    PopularProductAdapter adapter;
    List<CategoryModel> subCategoryList;
    String filterSubCategoryId;
    String filterMin, filterMax;
    String filterRating;
    TextView tvFilterBadge;
    Integer filterCount = 0;

    public ProductViewCategoryFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.categoryId = getArguments().getString("category_id", "");
            this.categoryName = getArguments().getString("category_name","");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_view_category, container, false);
        tvCategoryName = view.findViewById(R.id.tvCategoryName);
        layoutSort = view.findViewById(R.id.layoutSort);
        layoutFilter = view.findViewById(R.id.layoutFilter);
        rvProducts = view.findViewById(R.id.rvProducts);
        rvShimmer = view.findViewById(R.id.rvShimmer);
        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        tvFilterBadge = view.findViewById(R.id.tvFilterBadge);

        tvCategoryName.setText(this.categoryName);
        loginSession = new LoginSession(getContext());
        pincodeSession = new PincodeSession(getContext());
        if(loginSession.isLoggedIn()){
            user_id = loginSession.getUserId();
        }
        if(pincodeSession.hasPincode()){
            pincode = pincodeSession.getPincode();
        }
        pageSize = 10;
        pageNumber = 1;
        sort ="";
        filterSubCategoryId = "";
        filterMax ="";
        filterMin = "";
        filterRating = "";
        layoutSort.setOnClickListener(v -> {
            // Open bottom sheet or dialog
            showSortDialog();
        });
        layoutFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvProducts.setLayoutManager(gridLayoutManager);
        adapter = new PopularProductAdapter(getContext(), productList,false);
        rvProducts.setAdapter(adapter);
        loadProduct();
        rvProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManager =
                        (GridLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 2) {
                        loadProduct();
                    }
                }
            }
        });



        // Inflate the layout for this fragment
        return view;
    }



    private void loadProduct() {
        if (isLoading) return;
        filterCount =0;
        if(!filterSubCategoryId.isEmpty()){
            filterCount++;
        }
        if(!filterMax.isEmpty()){
            filterCount++;
        }
        if(!filterMin.isEmpty()){
            filterCount++;
        }
        Log.d("filterRating","hhhhhh"+filterRating);
        if(!filterRating.isEmpty() ){

            if(!filterRating.equals("0.0")){
                Log.d("filterRating","asas-"+filterRating);
                filterCount++;
            }

        }
        if(filterCount > 0 ){
            tvFilterBadge.setVisibility(View.VISIBLE);
            tvFilterBadge.setText(filterCount.toString());
        }else{
            tvFilterBadge.setVisibility(View.GONE);
        }

        isLoading = true;
        String actual_category_id;
        if (pageNumber == 1) showShimmer();

        categoryProductRequest = new CategoryProductRequest(
                categoryId, pincode, pageSize, pageNumber, sort,
                user_id, getContext(),
                filterSubCategoryId,filterMax,filterMin,filterRating
        );

        ApiService apiService = LoginRetrofitClient
                .getClient()
                .create(ApiService.class);

        apiService.getCategoryProduct(categoryProductRequest)
                .enqueue(new Callback<CategoryProductResponse>() {

                    @Override
                    public void onResponse(Call<CategoryProductResponse> call,
                                           Response<CategoryProductResponse> response) {

                        isLoading = false;
                        hideShimmer();

                        if (response.isSuccessful() && response.body() != null) {

                            List<ProductModel> newItems = response.body().getItems();
                            subCategoryList = response.body().getSubCategory(); // here get sub category
                            if (pageNumber == 1) {
                                productList.clear();
                            }
                            if (newItems == null || newItems.isEmpty()) {
                                isLastPage = true;
                                adapter.setShowEndMessage(true);
                            }
                            if (newItems != null && !newItems.isEmpty()) {
                                productList.addAll(newItems);
                                adapter.notifyDataSetChanged();
                                pageNumber++; // 🔥 IMPORTANT
                            } else {
                                isLastPage = true;
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<CategoryProductResponse> call, Throwable t) {
                        isLoading = false;
                        hideShimmer();
                    }
                });
    }


    private void showFilterDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_filter, null);

        MaterialAutoCompleteTextView spinnerSubCategory =
                view.findViewById(R.id.spinnerSubCategory);
        Button btnClear = view.findViewById(R.id.btnClear);
        Button btnApply = view.findViewById(R.id.btnApply);
        EditText etMinPrice = view.findViewById(R.id.etMinPrice);
        EditText etMaxPrice = view.findViewById(R.id.etMaxPrice);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        TextInputLayout tilSubcategory = view.findViewById(R.id.tilSubcategory);
        etMaxPrice.setText(filterMax);
        etMinPrice.setText(filterMin);
        // 🔥 CREATE NEW LIST EVERY TIME
        List<CategoryModel> spinnerList = new ArrayList<>();

        // Add default item ONCE
        spinnerList.add(new CategoryModel("0", "All"));
        Log.d("spinnerListNikhil", "spinner size = "+String.valueOf(subCategoryList.size()));
        if(subCategoryList.size() == 0){
            tilSubcategory.setVisibility(View.GONE);
        }
        if (subCategoryList != null) {
            spinnerList.addAll(subCategoryList);
        }

        ArrayAdapter<CategoryModel> adapter =
                new ArrayAdapter<>(requireContext(),
                        R.layout.item_spinner,
                        spinnerList);

        spinnerSubCategory.setAdapter(adapter);
        // if any filter is selected then it shoild be select after reopen of bottmsheet
        // Default
        spinnerSubCategory.setText(spinnerList.get(0).getName(), false);

        if (!TextUtils.isEmpty(filterSubCategoryId)) {
            for (CategoryModel model : spinnerList) {
                if (model.getId().equals(filterSubCategoryId)) {
                    spinnerSubCategory.setText(model.getName(), false);
                    break;
                }
            }
        }
        //  RESTORE rating
        if (!TextUtils.isEmpty(filterRating)) {
            try {
                ratingBar.setRating(Float.parseFloat(filterRating));
            } catch (Exception e) {
                ratingBar.setRating(0f);
            }
        } else {
            ratingBar.setRating(0f);
        }
        spinnerSubCategory.setOnItemClickListener((parent, v, position, id) -> {
            CategoryModel selected = (CategoryModel) parent.getItemAtPosition(position);

            if (!"0".equals(selected.getId())) {
                filterSubCategoryId = selected.getId();
            } else {
                filterSubCategoryId = "";
            }
        });

        btnClear.setOnClickListener(v -> {

            // Reset filter values
            filterSubCategoryId = "";
            filterMin = "";
            filterMax = "";
            filterRating = "";
            filterCount = 0;
            // Reset UI
            spinnerSubCategory.setText(spinnerList.get(0).getName(), false);
           // spinnerSubCategory.setText("Select Sub Category", false);
            etMinPrice.setText("");
            etMaxPrice.setText("");
            ratingBar.setRating(0f);

            // Reset pagination
            pageNumber = 1;
            isLastPage = false;

            // Reload products without filter
            loadProduct();

            dialog.dismiss();
        });

        btnApply.setOnClickListener(v -> {
            filterMax = etMaxPrice.getText().toString().trim();
            filterMin = etMinPrice.getText().toString().trim();
            filterRating = String.valueOf(ratingBar.getRating());

            if (!filterMin.isEmpty() && !filterMax.isEmpty()) {
                Integer max = Integer.valueOf(filterMax);
                Integer min = Integer.valueOf(filterMin);
                if (min > max) {
                    etMaxPrice.setError("Max price must be greater than min price.");
                    etMinPrice.setError("Min price must be less than max price.");
                    return;
                }
            }
            pageNumber = 1;
            isLastPage = false;
            loadProduct();

            dialog.dismiss();
        });

        dialog.setContentView(view);
        dialog.show();
    }

    private void showSortDialog() {

        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View view = getLayoutInflater().inflate(R.layout.bottomsheet_sort, null);
        dialog.setContentView(view);

        view.findViewById(R.id.sortPopularity).setOnClickListener(v -> {
            applySorting("popular");
            dialog.dismiss();
        });

        view.findViewById(R.id.sortNewest).setOnClickListener(v -> {
            applySorting("newest");
            dialog.dismiss();
        });

        view.findViewById(R.id.sortLowHigh).setOnClickListener(v -> {
            applySorting("price_low");
            dialog.dismiss();
        });

        view.findViewById(R.id.sortHighLow).setOnClickListener(v -> {
            applySorting("price_high");
            dialog.dismiss();
        });

        view.findViewById(R.id.sortRating).setOnClickListener(v -> {
            applySorting("rating");
            dialog.dismiss();
        });

        dialog.show();
    }

    private void applySorting(String sortType) {
        // price_low,price_high,rating,newest,popular
        switch (sortType) {
            case "popular":
                // API param: sort=popular
                sort = "popular";
                break;

            case "newest":
                // ORDER BY product.id DESC
                sort = "newest";
                break;

            case "price_low":
                // ORDER BY product_prices.selling_price ASC
                sort = "price_low";
                break;

            case "price_high":
                // ORDER BY product_prices.selling_price DESC
                sort = "price_high";
                break;

            case "rating":
                // ORDER BY avg_rating DESC
                sort= "rating";
                break;
        }

        pageNumber = 1;
        isLastPage = false;
        loadProduct();
        // reload products
       // fetchProducts(sortType);
    }

    private void showShimmer() {
        if (shimmerLayout == null) return;

        shimmerLayout.setVisibility(View.VISIBLE);
        rvProducts.setVisibility(View.GONE);

        rvShimmer.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvShimmer.setAdapter(new PopularProductShimmerAdapter());
        shimmerLayout.startShimmer();
    }


    private void hideShimmer() {
        shimmerLayout.stopShimmer();
        shimmerLayout.setVisibility(View.GONE);
        rvProducts.setVisibility(View.VISIBLE);
    }


}