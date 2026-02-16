package com.sagarsweets.in;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.sagarsweets.in.Adapters.PopularProductAdapter;
import com.sagarsweets.in.Adapters.PopularProductShimmerAdapter;
import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.CategoryDetails;
import com.sagarsweets.in.ApiModel.CategoryModel;
import com.sagarsweets.in.ApiModel.ProductModel;
import com.sagarsweets.in.ApiModel.SearchProductRequest;
import com.sagarsweets.in.ApiModel.SearchResponse;
import com.sagarsweets.in.Session.LoginSession;
import com.sagarsweets.in.Session.PincodeSession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment
        implements PopularProductAdapter.CartUpdateListener{

    private String searchQuery = "";
    private String pincode = "";
    private String userId = "";

    private int pageSize = 5;
    private int pageNumber = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private String sort = "newest";
    private String filterCategoryId = "";
    private String filterMin = "";
    private String filterMax = "";
    private String filterRating = "";

    private int filterCount = 0;

    private RecyclerView rvProducts, rvShimmer;
    private ShimmerFrameLayout shimmerLayout;
    private LinearLayout layoutSort, layoutFilter;
    private TextView tvFilterBadge;

    private PopularProductAdapter adapter;
    private final List<ProductModel> productList = new ArrayList<>();
    private List<CategoryDetails> allCategory = new ArrayList<>();

    public static SearchFragment newInstance(String query) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString("query", query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchQuery = getArguments().getString("query", "");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initView(view);
        initSession();

        GridLayoutManager manager = new GridLayoutManager(requireContext(), 2);
        rvProducts.setLayoutManager(manager);

        adapter = new PopularProductAdapter(requireContext(), productList,
                false,SearchFragment.this);
        rvProducts.setAdapter(adapter);

        loadSearch();
        setupPagination();

        layoutSort.setOnClickListener(v -> openSortBottomSheet());
        layoutFilter.setOnClickListener(v -> openFilterBottomSheet());

        return view;
    }

    private void initView(View view) {
        layoutSort = view.findViewById(R.id.layoutSort);
        layoutFilter = view.findViewById(R.id.layoutFilter);
        rvProducts = view.findViewById(R.id.rvProducts);
        rvShimmer = view.findViewById(R.id.rvShimmer);
        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        tvFilterBadge = view.findViewById(R.id.tvFilterBadge);
    }

    private void initSession() {
        PincodeSession pincodeSession = new PincodeSession(requireContext());
        if (pincodeSession.hasPincode()) {
            pincode = pincodeSession.getPincode();
        }

        LoginSession loginSession = new LoginSession(requireContext());
        if (loginSession.isLoggedIn()) {
            userId = loginSession.getUserId();
        }
    }

    private void setupPagination() {
        rvProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                GridLayoutManager lm = (GridLayoutManager) recyclerView.getLayoutManager();
                if (lm == null) return;

                int visible = lm.getChildCount();
                int total = lm.getItemCount();
                int firstVisible = lm.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visible + firstVisible) >= total && firstVisible >= 0 && total >= pageSize) {
                        loadSearch();
                    }
                }
            }
        });
    }

    private void loadSearch() {
        if (isLoading) return;
        isLoading = true;
        filterCount =0;
        if(!filterCategoryId.isEmpty()){
            filterCount++;
        }
        if(!filterMax.isEmpty()){
            filterCount++;
        }
        if(!filterMin.isEmpty()){
            filterCount++;
        }
        if(!filterRating.isEmpty() ){

            if(!filterRating.equals("0.0")){
                Log.d("filterRating","asas-"+filterRating);
                filterCount++;
            }

        }
        if(filterCount > 0 ){
            tvFilterBadge.setVisibility(View.VISIBLE);
            tvFilterBadge.setText(String.valueOf(filterCount));
        }else{
            tvFilterBadge.setVisibility(View.GONE);
        }
        if (pageNumber == 1) showShimmer();

        SearchProductRequest request = new SearchProductRequest(
                searchQuery,
                pincode,
                sort,
                userId,
                filterCategoryId,
                filterMax,
                filterMin,
                filterRating,
                pageSize,
                pageNumber,
                requireContext()
        );

        ApiService apiService = LoginRetrofitClient.getClient().create(ApiService.class);
        apiService.getSearchProduct(request).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<SearchResponse> call,
                                   @NonNull Response<SearchResponse> response) {

                isLoading = false;
                hideShimmer();

                if (!response.isSuccessful() || response.body() == null) return;

                if (pageNumber == 1) {
                    productList.clear();
                    adapter.setShowEndMessage(false);
                }

                List<ProductModel> items = response.body().getItems();
                allCategory = response.body().getAllCategory();

                if (items == null || items.isEmpty()) {
                    isLastPage = true;
                    adapter.setShowEndMessage(true);
                    return;
                }

                productList.addAll(items);
                adapter.notifyDataSetChanged();
                pageNumber++;
            }

            @Override
            public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
                isLoading = false;
                hideShimmer();
                Log.e("SearchFragment", "API Error", t);
            }
        });
    }

    private void showShimmer() {
        shimmerLayout.setVisibility(View.VISIBLE);
        rvProducts.setVisibility(View.GONE);
        rvShimmer.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        rvShimmer.setAdapter(new PopularProductShimmerAdapter());
        shimmerLayout.startShimmer();
    }

    private void hideShimmer() {
        shimmerLayout.stopShimmer();
        shimmerLayout.setVisibility(View.GONE);
        rvProducts.setVisibility(View.VISIBLE);
    }

    /* ---------------- SORT ---------------- */

    private void openSortBottomSheet() {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View view = getLayoutInflater().inflate(R.layout.bottomsheet_sort, null);
        dialog.setContentView(view);

        view.findViewById(R.id.sortPopularity).setOnClickListener(v -> applySort("popular", dialog));
        view.findViewById(R.id.sortNewest).setOnClickListener(v -> applySort("newest", dialog));
        view.findViewById(R.id.sortLowHigh).setOnClickListener(v -> applySort("price_low", dialog));
        view.findViewById(R.id.sortHighLow).setOnClickListener(v -> applySort("price_high", dialog));
        view.findViewById(R.id.sortRating).setOnClickListener(v -> applySort("rating", dialog));

        dialog.show();
    }

    private void applySort(String type, BottomSheetDialog dialog) {
        sort = type;
        pageNumber = 1;
        isLastPage = false;
        loadSearch();
        dialog.dismiss();
    }

    /* ---------------- FILTER ---------------- */

    private void openFilterBottomSheet() {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_filter, null);

        MaterialAutoCompleteTextView spinner = view.findViewById(R.id.spinnerSubCategory);
        EditText etMin = view.findViewById(R.id.etMinPrice);
        EditText etMax = view.findViewById(R.id.etMaxPrice);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        TextInputLayout tilSub = view.findViewById(R.id.tilSubcategory);
        // setting filtered value
        etMax.setText(filterMax);
        etMin.setText(filterMin);
        Button btnClear = view.findViewById(R.id.btnClear);
        Button btnApply = view.findViewById(R.id.btnApply);

        List<CategoryDetails> list = new ArrayList<>();
        list.add(new CategoryDetails("0", "All"));

        if (allCategory != null && !allCategory.isEmpty()) {
            list.addAll(allCategory);
        } else {
            tilSub.setVisibility(View.GONE);
        }

        ArrayAdapter<CategoryDetails> adapter =
                new ArrayAdapter<>(requireContext(), R.layout.item_spinner, list);
        spinner.setAdapter(adapter);
        // if any filter is selected then it shoild be select after reopen of bottmsheet
        // Default
        spinner.setText(list.get(0).getName(), false);
        if (!TextUtils.isEmpty(filterCategoryId)) {
            for (CategoryDetails model : list) {
                String id = String.valueOf(model.getId());
                if (id.equals(filterCategoryId)) {
                    spinner.setText(model.getName(), false);
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

        spinner.setOnItemClickListener((p, v, pos, id) -> {
            CategoryDetails c = (CategoryDetails) p.getItemAtPosition(pos);
            //filterCategoryId = "0".equals(c.getId()) ? "" : String.valueOf(c.getId());
            String selected = String.valueOf(c.getId());
            if (!"0".equals(selected)) {
                filterCategoryId = String.valueOf(c.getId());
            } else {
                filterCategoryId = "";
            }
            Log.d("filterCategoryId","category - "+filterCategoryId);
        });

        btnClear.setOnClickListener(v -> {
            filterCategoryId = filterMin = filterMax = filterRating = "";
            filterCount = 0;
            pageNumber = 1;
            isLastPage = false;

            // Reset UI
            spinner.setText(list.get(0).getName(), false);
            // spinnerSubCategory.setText("Select Sub Category", false);
            etMax.setText("");
            etMin.setText("");
            ratingBar.setRating(0f);

            loadSearch();
            dialog.dismiss();
        });

        btnApply.setOnClickListener(v -> {
            filterMin = etMin.getText().toString().trim();
            filterMax = etMax.getText().toString().trim();
            filterRating = ratingBar.getRating() > 0 ? String.valueOf(ratingBar.getRating()) : "";

            pageNumber = 1;
            isLastPage = false;
            loadSearch();
            dialog.dismiss();
        });

        dialog.setContentView(view);
        dialog.show();
    }
    @Override
    public void onCartUpdated() {

        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).onCartUpdated();
        }
    }
}
