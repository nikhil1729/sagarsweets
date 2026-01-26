package com.sagarsweets.in;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.sagarsweets.in.Adapters.ImageAdapter;
import com.sagarsweets.in.Adapters.PopularProductAdapter;
import com.sagarsweets.in.Adapters.SizeAdapter;
import com.sagarsweets.in.Adapters.SpecificationAdapter;
import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.ProductDetailsModel;
import com.sagarsweets.in.ApiModel.ProductDetailsRequest;
import com.sagarsweets.in.ApiModel.ProductModel;
import com.sagarsweets.in.ApiModel.ProductReviewRequest;
import com.sagarsweets.in.ApiModel.ReviewModel;
import com.sagarsweets.in.ApiModel.ReviewResponse;
import com.sagarsweets.in.ApiModel.SizeModel;
import com.sagarsweets.in.Session.LoginSession;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductDetailsFragment extends Fragment {

    Integer productId;
    String pincode;
    String userId;
    TextView txtBrand,
            txtTitle,
            txtSellingPrice,
            txtMrp,
            txtDiscount,
            txtDelivery,
            txtDescription,
            txtStockStatus,
            tvSizeTitle,
            txtReviewCount;
    RatingBar ratingBar;
    RecyclerView rvImages,
            rvSizes,
            rvSpecification,
            rvRelatedProducts;
    Button btnAddToCart;
    ShimmerFrameLayout shimmerLayout;
    View contentLayout;
    LoginSession loginSession;
    public ProductDetailsFragment() {
        // Required empty public constructor
        //this.productId = "12";

        this.userId = "";

        this.pincode = "274204";

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.productId = getArguments().getInt("product_id", 0);
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);
        initViews(view);
        setPincodeAndUserId();
        loadProductDetails(productId,view);
        getReviewBottomSheet();
        return view;
    }

    private void setPincodeAndUserId() {
        loginSession = new LoginSession(getContext());
        this.userId = "";
        if(loginSession.isLoggedIn()){
            this.userId = loginSession.getUserId();
        }

        pincode = "274204";
    }

    private void getReviewBottomSheet() {
        txtReviewCount.setOnClickListener(v -> {
            ProductReviewRequest productReviewRequest = new ProductReviewRequest(productId,userId,getContext());
            ApiService apiService  = LoginRetrofitClient
                    .getClient()
                    .create(ApiService.class);
            apiService.getProductReview(productReviewRequest).enqueue(new Callback<ReviewResponse>() {
                @Override
                public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                    if (response.isSuccessful() && response.body().isStatus()) {
                        List<ReviewModel> reviewList = response.body().getData();
                        ReviewsBottomSheetFragment fragment = new ReviewsBottomSheetFragment();
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("review_status", response.body().isStatus());
                        bundle.putSerializable("review_list", (Serializable) reviewList);
                        fragment.setArguments(bundle);
                        fragment.show(getParentFragmentManager(), "ReviewsBottomSheet");
                    }else{
                        ReviewsBottomSheetFragment fragment = new ReviewsBottomSheetFragment();
                        List<ReviewModel> reviewList = response.body().getData();
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("review_status", response.body().isStatus());
                        bundle.putSerializable("review_list", (Serializable) reviewList);
                        fragment.setArguments(bundle);
                        fragment.show(getParentFragmentManager(), "ReviewsBottomSheet");
                    }
                }

                @Override
                public void onFailure(Call<ReviewResponse> call, Throwable t) {
                    Log.d("failed_review", t.getMessage());
                }
            });

        });

    }

    private void initViews(View view) {
        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        contentLayout = view.findViewById(R.id.contentLayout);

        txtBrand = view.findViewById(R.id.txtBrand);
        txtTitle = view.findViewById(R.id.txtTitle);
        txtSellingPrice = view.findViewById(R.id.txtSellingPrice);
        txtMrp = view.findViewById(R.id.txtMrp);
        txtDiscount = view.findViewById(R.id.txtDiscount);
        txtDelivery = view.findViewById(R.id.txtDelivery);
        txtDescription = view.findViewById(R.id.txtDescription);
        //txtSelectedSize = view.findViewById(R.id.txtSelectedSize);

        ratingBar = view.findViewById(R.id.ratingBar);

        rvImages = view.findViewById(R.id.rvImages);
        rvSizes = view.findViewById(R.id.rvSizes);
        tvSizeTitle = view.findViewById(R.id.tvSizeTitle);
        rvSpecification = view.findViewById(R.id.rvSpecification);
        rvRelatedProducts = view.findViewById(R.id.rvRelatedProducts);
        btnAddToCart = view.findViewById(R.id.btnAddToCart);
        txtStockStatus = view.findViewById(R.id.txtStockStatus);
        txtReviewCount = view.findViewById(R.id.txtReviewCount);
    }


    private void loadProductDetails(Integer productId, View view) {
        shimmerLayout.startShimmer();
        shimmerLayout.setVisibility(View.VISIBLE);
        contentLayout.setVisibility(View.GONE);
        ProductDetailsRequest productDetailsRequest =
                new ProductDetailsRequest(productId,pincode,userId,getContext());
        //ApiService apiService = ApiClient.getClient().create(ApiService.class);
        ApiService apiService  = LoginRetrofitClient
                .getClient()
                .create(ApiService.class);
        apiService.getProductDetails(productDetailsRequest).enqueue(new Callback<ProductDetailsModel>() {
            @Override
            public void onResponse(Call<ProductDetailsModel> call, Response<ProductDetailsModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("testnikhil","here");
                    shimmerLayout.stopShimmer();
                    shimmerLayout.setVisibility(View.GONE);
                    contentLayout.setVisibility(View.VISIBLE);
                    btnAddToCart.setVisibility(View.VISIBLE);
                    ProductDetailsModel product = response.body();
                    // -------- BASIC DATA --------
                    String brand = product.getBrandName();

                    txtBrand.setText(brand != null ? brand : "N/A");
                    txtTitle.setText(product.getProductTitle());

                    // stock checking
                    Integer stock = product.getStock();
                    if (stock > 0) {
                        txtStockStatus.setText("IN STOCK");
                        txtStockStatus.setBackgroundResource(R.drawable.bg_stock_in);
                        btnAddToCart.setEnabled(true);
                        //itemView.setAlpha(1f);
                    } else {
                        txtStockStatus.setText("OUT OF STOCK");
                        txtStockStatus.setBackgroundResource(R.drawable.bg_stock_out);
                        btnAddToCart.setEnabled(false);
                        //itemView.setAlpha(0.6f);
                    }
                    // rating bar
                    ratingBar.setRating(product.getRating());
                    txtReviewCount.setText("("+ product.getRatingCount()+" reviews)");
                    txtSellingPrice.setText("₹" + product.getSellingPrice());
                    txtMrp.setText("₹" + product.getMrp());
                    txtDiscount.setText(Math.round(product.getDiscountPercentage()) + "% OFF");

// HTML DELIVERY TEXT
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        txtDelivery.setText(
                                Html.fromHtml(product.getExpectedDay(), Html.FROM_HTML_MODE_LEGACY)
                        );
                    }

// HTML DESCRIPTION
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        txtDescription.setText(
                                Html.fromHtml(product.getDescription(), Html.FROM_HTML_MODE_LEGACY)
                        );
                    }

// -------- IMAGES --------
                    ImageAdapter imageAdapter =
                            new ImageAdapter(product.getImages(), product.getDefaultImage());
                    rvImages.setAdapter(imageAdapter);

                    // -------- SIZE SELECTION --------
                    List<SizeModel> sizes = product.getSizeList();
                    if (sizes != null && !sizes.isEmpty()) {

                        rvSizes.setVisibility(View.VISIBLE);

                        rvSizes.setLayoutManager(
                                new LinearLayoutManager(
                                        view.getContext(),
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                )
                        );

                        SizeAdapter sizeAdapter = new SizeAdapter(sizes, size -> {
                           updatePriceAndStock(size);
                        });
                        rvSizes.setAdapter(sizeAdapter);

                    } else {
                        tvSizeTitle.setVisibility(View.GONE);
                        rvSizes.setVisibility(View.GONE);
                    }

                    // -------- SPECIFICATIONS --------
                    if (product.getSpecification() != null) {
                        Log.d("SPEC_DEBUG", "Specification size: " + product.getSpecification().size());

                        SpecificationAdapter specificationAdapter =
                                new SpecificationAdapter(product.getSpecification());
                        rvSpecification.setLayoutManager(new LinearLayoutManager(getContext()));
                        rvSpecification.setHasFixedSize(true);
                        rvSpecification.setAdapter(specificationAdapter);
                    } else {
                        Log.e("SPEC_DEBUG", "Specification is NULL");
                    }


//                    SpecificationAdapter specificationAdapter =
  //                          new SpecificationAdapter(product.getSpecification());

    //                Log.d("specification", String.valueOf(product.getSpecification()));
      //              rvSpecification.setAdapter(specificationAdapter);

                    // -------- RELATED PRODUCTS --------
                    List<ProductModel> productList = product.getRelatedProduct();
                    rvRelatedProducts.setLayoutManager(
                            new GridLayoutManager(getContext(), 2)
                    );

                    PopularProductAdapter adapter =
                                new PopularProductAdapter(getContext(), productList,false);

                    rvRelatedProducts.setAdapter(adapter);

                }


            }

            @Override
            public void onFailure(Call<ProductDetailsModel> call, Throwable t) {
                Log.d("errornikhil",t.getMessage());
            }
        });



    }

    private void updatePriceAndStock(SizeModel size) {
        txtSellingPrice.setText("₹" + size.getSellingPrice());
        txtMrp.setText("₹" + size.getMrp());

        double mrp = Double.parseDouble(size.getMrp());
        double sellingPrice = Double.parseDouble(size.getSellingPrice());

        double discountPercent = ((mrp - sellingPrice) / mrp) * 100;
        int discount = (int) Math.round(discountPercent);


        txtDiscount.setText(discount+"% OFF");
        Log.d("stock_change", String.valueOf(size.getStock()));
        if (size.getStock() > 0) {
            txtStockStatus.setText("IN STOCK");
            txtStockStatus.setBackgroundResource(R.drawable.bg_stock_in);
            btnAddToCart.setEnabled(true);

            //itemView.setAlpha(1f);
        } else {
            txtStockStatus.setText("OUT OF STOCK");
            txtStockStatus.setBackgroundResource(R.drawable.bg_stock_out);
            btnAddToCart.setEnabled(false);
            //itemView.setAlpha(0.6f);
        }
    }

}