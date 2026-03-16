package com.sagarsweets.in;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.card.MaterialCardView;
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
import com.sagarsweets.in.RoomDatabase.AppDatabase;
import com.sagarsweets.in.Session.CartItem;
import com.sagarsweets.in.Session.LoginSession;
import com.sagarsweets.in.Session.PincodeSession;
import com.sagarsweets.in.utils.CartSaveOnServer;
import com.sagarsweets.in.utils.DeviceInfo;
import com.sagarsweets.in.utils.WishListClicked;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductDetailsFragment extends Fragment
        implements PopularProductAdapter.CartUpdateListener{

    Integer productId;
    String pincode;
    int userId;
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
    ImageView imgWishlist;
    LoginSession loginSession;
    PincodeSession pincodeSession;
    List<SizeModel> sizes;
    int selectedSizeId;
    String selectedSizeName;
    Double selectedSizePrice;
    Double selectedSizeMrp;
    private boolean sizeSelected;
    ProductDetailsModel product;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private PopularProductAdapter.CartUpdateListener cartUpdateListener;
    AppDatabase db;
    MaterialCardView layoutStickyCart;
    ImageView btnPlus, btnMinus;
    TextView txtQuantity,txtStickyPrice,txtStickyStock;
    int currentQuantity;
    SizeAdapter sizeAdapter;
    //CardView cardDeliveryAddress;
    public ProductDetailsFragment() {
        // Required empty public constructor
        //this.productId = "12";
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
        loginSession = new LoginSession(getContext());
        if(loginSession.isLoggedIn()){
            this.userId = Integer.parseInt(loginSession.getUserId());
        }else{
            this.userId = 0;
        }
        pincodeSession = new PincodeSession(getContext());
        if(pincodeSession.hasPincode()){
            this.pincode = pincodeSession.getPincode();
        }else{
            this.pincode = "";
        }

        setPincodeAndUserId();
        loadProductDetails(productId,view);
        getReviewBottomSheet();
        clickedFunction();
        return view;
    }

    private void clickedFunction() {
        stepperButtonClicked(); // + and - button
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double sellingPrice = product.getSellingPrice();
                double mrp = product.getMrp();
                currentQuantity = 1;
                int selecteSize = 0;
                String sizeName = "NA";
                Long updatedAt = System.currentTimeMillis();
                Boolean isSync = false;
                if (sizes != null && !sizes.isEmpty()) {
                    // size is available
                    Log.d("DEBUG_CART","Selected Size ID-"+selectedSizeId);

                    Log.d("DEBUG_CART","cart clicked-size is available");
                    if (!sizeSelected) {
                        showSizeSelected();
                        return;
                    }
                    sellingPrice = selectedSizePrice;
                    mrp = selectedSizeMrp;
                    selecteSize = selectedSizeId;
                    sizeName = selectedSizeName;
                    int availableStock = getAvailableStock(product,selectedSizeId);
                    if (availableStock <= 0) {
                        Toast.makeText(getContext(), "Out of stock", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    addedToCart(product,selecteSize,currentQuantity,sizeName,v,sellingPrice,mrp);
                }else{

                    Log.d("DEBUG_CART","cart clicked-size is not available");
                    addedToCart(product,0,currentQuantity,"NA",v, sellingPrice, mrp);
                }
                //addedToCart(product,selecteSize,quantity,sizeName,v); // ✅ MAIN CALL
            }
        });
    }

    private void stepperButtonClicked() {

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuantity > 1) {
                    currentQuantity--;
                    double sellingPrice = product.getSellingPrice();
                    double mrp = product.getMrp();
                    if(sizeSelected){
                        sellingPrice = selectedSizePrice;
                        mrp = selectedSizeMrp;
                    }
                    txtQuantity.setText(String.valueOf(currentQuantity));
                    addedToCart(product, selectedSizeId,currentQuantity, selectedSizeName,v, sellingPrice, mrp);
                }else{
                    currentQuantity =0;
                    removeFromCart(productId,selectedSizeId);
                }
            }
        });
        btnPlus.setOnClickListener(v -> {
            int availableStock = getAvailableStock(product, selectedSizeId);
            if (currentQuantity >= availableStock) {
                Toast.makeText(getContext(), "Maximum stock reached", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d("currentQuantity","curr quan-"+currentQuantity);
            double sellingPrice = product.getSellingPrice();
            double mrp = product.getMrp();
            if(sizeSelected){
                sellingPrice = selectedSizePrice;
                mrp = selectedSizeMrp;
            }
            currentQuantity++;
            Log.d("currentQuantity","curr quan-"+currentQuantity);
            txtQuantity.setText(String.valueOf(currentQuantity));
            addedToCart(product, selectedSizeId,currentQuantity, selectedSizeName,v, sellingPrice, mrp);
        });
    }

    private int getAvailableStock(ProductDetailsModel product, int selectedSizeId) {
        List<SizeModel> sizes = product.getSizeList();
        Log.d("STOCKCHECK", "Selected Size-"+String.valueOf(selectedSizeId));
        // ✅ If size-based product
        if (sizes != null && !sizes.isEmpty()) {
            for (SizeModel size : sizes) {
                if (size.getId() == selectedSizeId) {
                    Integer stock = size.getStock();
                    Log.d("STOCKCHECK", String.valueOf(stock));
                    return stock == null ? 0 : size.getStock();
                }
            }
            return 0; // size not found
        }

        // ✅ Non-size product
        return product.getStock() == null ? 0 : product.getStock();
    }

    private void addedToCart(ProductDetailsModel product,
                             int selectedSizeId,
                             int quantity,
                             String sizeSelectedName,
                             View v, double sellingPrice, double mrp) {

        executor.execute(() -> {

            int productId = product.getId();
            int userId = loginSession.isLoggedIn()
                    ? Integer.parseInt(loginSession.getUserId())
                    : 0;

            CartItem existingItem =
                    db.cartDao().checkItem(productId, userId);

            CartItem itemToSync;

            if (existingItem != null) {
                existingItem.setPrice(sellingPrice);
                existingItem.setMrp(mrp);
                existingItem.setQuantity(quantity);
                existingItem.setUpdatedAt(System.currentTimeMillis());
                existingItem.setSynced(false);

                db.cartDao().update(existingItem);
                itemToSync = existingItem;

            } else {

                CartItem newItem = new CartItem(
                        productId,
                        product.getProductTitle(),
                        product.getDefaultImage(),
                        sellingPrice,
                        mrp,
                        quantity,
                        selectedSizeId,
                        userId,
                        sizeSelectedName,
                        System.currentTimeMillis(),
                        false
                );

                db.cartDao().insert(newItem);
                itemToSync = newItem;
            }

            // 🔥 Switch to Main Thread for UI + API call
            requireActivity().runOnUiThread(() -> {

                // show stepper
                txtQuantity.setText(String.valueOf(itemToSync.getQuantity()));
                Integer totalPriceSticky = (int)( quantity * sellingPrice );
                txtStickyPrice.setText(String.valueOf(DeviceInfo.rupeeSymbol()+totalPriceSticky));

                btnAddToCart.setVisibility(View.GONE);
                layoutStickyCart.setVisibility(View.VISIBLE);
                int totalStock = getAvailableStock(product,selectedSizeId);
                int pendingStock = totalStock - quantity;
                txtStickyStock.setText("Remaining-"+(int)pendingStock);
                // now call API
                CartSaveOnServer.saveCartOnServer(
                        itemToSync,
                        v,
                        loginSession,
                        DeviceInfo.getDeviceString(getContext())
                );
            });
        });
    }



    private void showSizeSelected() {
        Context context = requireContext();
        DeviceInfo.vibratMobile(context);
        // Shake animation
        rvSizes.animate()
                .translationX(20)
                .setDuration(50)
                .withEndAction(() ->
                        rvSizes.animate()
                                .translationX(-20)
                                .setDuration(50)
                                .withEndAction(() ->
                                        rvSizes.animate()
                                                .translationX(0)
                                                .setDuration(50)
                                )
                );
    }


    private void setPincodeAndUserId() {
        loginSession = new LoginSession(getContext());
        this.userId = 0;
        if(loginSession.isLoggedIn()){
            this.userId = Integer.parseInt(loginSession.getUserId());
        }

        pincode = pincodeSession.getPincode();
    }

    private void getReviewBottomSheet() {
        txtReviewCount.setOnClickListener(v -> {
            ProductReviewRequest productReviewRequest = new ProductReviewRequest(productId,
                    String.valueOf(userId),
                    getContext());
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
        imgWishlist = view.findViewById(R.id.imgWishlist);
        layoutStickyCart = view.findViewById(R.id.layoutStickyCart);
        btnPlus = view.findViewById(R.id.btnPlus);
        btnMinus = view.findViewById(R.id.btnMinus);
        txtQuantity = view.findViewById(R.id.txtQuantity);
        txtStickyPrice = view.findViewById(R.id.txtStickyPrice);
        txtStickyStock = view.findViewById(R.id.txtStickyStock);
        // cardDeliveryAddress = view.findViewById(R.id.cardDeliveryAddress);
        db = AppDatabase.getInstance(getContext());

        sizeSelected = false;
        selectedSizeId = 0;
        selectedSizeName = "NA";
        selectedSizePrice = 0.0;
        selectedSizeMrp = 0.0;
    }


    private void loadProductDetails(Integer productId, View view) {
        shimmerLayout.startShimmer();
        shimmerLayout.setVisibility(View.VISIBLE);
        contentLayout.setVisibility(View.GONE);
        ProductDetailsRequest productDetailsRequest =
                new ProductDetailsRequest(productId,pincode,String.valueOf(userId),getContext());
        //ApiService apiService = ApiClient.getClient().create(ApiService.class);
        ApiService apiService  = LoginRetrofitClient
                .getClient()
                .create(ApiService.class);
        apiService.getProductDetails(productDetailsRequest).enqueue(new Callback<ProductDetailsModel>() {
            @Override
            public void onResponse(Call<ProductDetailsModel> call, Response<ProductDetailsModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    shimmerLayout.stopShimmer();
                    shimmerLayout.setVisibility(View.GONE);
                    contentLayout.setVisibility(View.VISIBLE);
                    btnAddToCart.setVisibility(View.VISIBLE);
                    product = response.body();
                    // -------- BASIC DATA --------
                    String brand = product.getBrandName();

                    txtBrand.setText(brand != null ? brand : "N/A");
                    txtTitle.setText(product.getProductTitle());

                    // stock checking
                    Integer stock = product.getStock();
                    Log.d("STOCKCHECK", String.valueOf(stock));
                    if (stock != null && stock > 0) {
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
                    Float rating = product.getRating();
                    ratingBar.setRating(rating != null ? rating : 0f);
                    // review
                    Integer reviewCount = product.getRatingCount();
                    // mrp sell price and discount
                    txtReviewCount.setText("(" + (reviewCount != null ? reviewCount : 0) + " reviews)");
                    Double selling = Double.valueOf(product.getSellingPrice());
                    txtSellingPrice.setText(DeviceInfo.rupeeSymbol() + (int)(selling != null ? selling : 0));
                    Double mrp = Double.valueOf(product.getMrp());
                    txtMrp.setText(DeviceInfo.rupeeSymbol() + (int)(mrp != null ? mrp : 0));
                    Double discount = product.getDiscountPercentage();
                    int dis = discount != null ? (int) Math.round(discount) : 0;
                    txtDiscount.setText(dis + "% OFF");
                    // end mrp sell price and discount

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
                    sizes = product.getSizeList();
                    if (sizes != null && !sizes.isEmpty()) {

                        rvSizes.setVisibility(View.VISIBLE);

                        rvSizes.setLayoutManager(
                                new LinearLayoutManager(
                                        view.getContext(),
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                )
                        );

                        sizeAdapter = new SizeAdapter(sizes, size -> {
                           updatePriceAndStock(product,size);
                        });

                        /*CHECK PRODUCT SIZE IS IN CART*/
                        int productId = product.getId();
                        int userId = loginSession.isLoggedIn()
                                ? Integer.parseInt(loginSession.getUserId())
                                : 0;
                        executor.execute(() -> {
                            CartItem existingItem =
                                    db.cartDao().checkItem(productId, userId);
                            if (existingItem != null) {
                                int sizeId = existingItem.getSizeId();   // ✅ SAFE NOW
                                selectedSizeId = sizeId;
                                currentQuantity = existingItem.getQuantity();
                                selectedSizePrice = existingItem.getPrice();
                                double mrpSize = (int)existingItem.getMrp();
                                double sellPriceSize = (int)existingItem.getPrice();
                                requireActivity().runOnUiThread(() -> {
                                    currentQuantity = existingItem.getQuantity();
                                    txtQuantity.setText(String.valueOf(existingItem.getQuantity()));
                                    btnAddToCart.setVisibility(View.GONE);
                                    layoutStickyCart.setVisibility(View.VISIBLE);

                                    if (sizeId != 0 && sizeAdapter != null) {
                                        sizeAdapter.setSelectedSizeById(sizeId);
                                        txtSellingPrice.setText(DeviceInfo.rupeeSymbol() + (int)sellPriceSize);
                                        txtMrp.setText(DeviceInfo.rupeeSymbol() + (int)mrpSize);
                                        int discountSize = calculateDiscount(mrpSize,sellPriceSize);
                                        txtDiscount.setText(discountSize + "% OFF");
                                    }

                                });
                                double totalPrice = existingItem.getQuantity() * existingItem.getPrice();
                                int totalStock = getAvailableStock(product,selectedSizeId);
                                int pendigStock = totalStock - existingItem.getQuantity();
                                txtStickyPrice.setText(DeviceInfo.rupeeSymbol() + (int)totalPrice);
                                txtStickyStock.setText("Remaining-"+pendigStock);
                            }
                        });
                        /*CHECK CART IS END*/
                        rvSizes.setAdapter(sizeAdapter);

                    } else {
                        tvSizeTitle.setVisibility(View.GONE);
                        rvSizes.setVisibility(View.GONE);
                        /*Function is use for if product is available in cart
                         * then hide add cart button*/
                        checkProductInCart(product);
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

                    /*WISH LIST */
                    Log.e("WISHLIST_DEBUG", "Wish list is "+String.valueOf(product.getWishListedMain()));
                    if (Boolean.TRUE.equals(product.getWishListedMain())) {
                        imgWishlist.setImageResource(R.drawable.ic_heart_filled);
                    }else{
                        imgWishlist.setImageResource(R.drawable.ic_heart_outline);
                    }
                    imgWishlist.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            wishListCkicked(productId);
                        }
                    });
                    /*WISH LIST END*/

                    // -------- RELATED PRODUCTS --------
                    List<ProductModel> productList = product.getRelatedProduct();
                    rvRelatedProducts.setLayoutManager(
                            new GridLayoutManager(getContext(), 2)
                    );

                    PopularProductAdapter adapter =
                                new PopularProductAdapter(
                                        getContext(), productList,
                                        false,ProductDetailsFragment.this);

                    rvRelatedProducts.setAdapter(adapter);

                }


            }

            @Override
            public void onFailure(Call<ProductDetailsModel> call, Throwable t) {
                Log.d("errornikhil",t.getMessage());
            }
        });



    }

    private void checkProductInCart(ProductDetailsModel productDetailsModel) {
        int userId = loginSession.isLoggedIn()
                ? Integer.parseInt(loginSession.getUserId())
                : 0;
        executor.execute(() -> {
            CartItem existingItem =
                    db.cartDao().checkItem(productDetailsModel.getId(), userId);
            if (existingItem != null) {
                txtQuantity.setText(String.valueOf(existingItem.getQuantity()));
                btnAddToCart.setVisibility(View.GONE);
                layoutStickyCart.setVisibility(View.VISIBLE);
                currentQuantity = existingItem.getQuantity();
                double price = currentQuantity * productDetailsModel.getSellingPrice();
                int pendingStock = getAvailableStock(productDetailsModel,0)-currentQuantity;
                txtStickyStock.setText("Pending Stock "+pendingStock);
                txtStickyPrice.setText(DeviceInfo.rupeeSymbol()+ (int) price);
            }else{
                currentQuantity = 0;
                txtQuantity.setText("0");
                btnAddToCart.setVisibility(View.VISIBLE);
                layoutStickyCart.setVisibility(View.GONE);
            }
        });
    }

    private int calculateDiscount(double mrp, double sellingPrice) {
        if (mrp <= 0) return 0;   // safety check

        double discount = ((mrp - sellingPrice) / mrp) * 100;
        return (int) Math.round(discount);
    }

    private void wishListCkicked(Integer pId) {
        Toast.makeText(getContext(),"Product id = "+String.valueOf(pId),Toast.LENGTH_LONG).show();
        if(loginSession.isLoggedIn()){
            // save in api
            WishListClicked.clicked(getContext(),loginSession.getUserId(),String.valueOf(pId),imgWishlist);
        }else{
            // save in local
        }
    }

    private void updatePriceAndStock(ProductDetailsModel product, SizeModel size) {

        if (size == null) return;

        String selling = size.getSellingPrice();
        String mrpStr = size.getMrp();

        double sellingPrice = 0;
        double mrp = 0;

        try {
            sellingPrice = selling != null ? Double.parseDouble(selling) : 0;
            mrp = mrpStr != null ? Double.parseDouble(mrpStr) : 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        txtSellingPrice.setText(DeviceInfo.rupeeSymbol() + (int)sellingPrice);
        txtMrp.setText(DeviceInfo.rupeeSymbol() + (int)mrp);
        int discountSize = calculateDiscount(mrp,sellingPrice);
        txtDiscount.setText(discountSize + "% OFF");
        sizeSelected = true;
        selectedSizeId = size.getId() != null ? size.getId() : 0;
        selectedSizeName = size.getTitle() != null ? size.getTitle() : "NA";
        selectedSizePrice = sellingPrice;
        selectedSizeMrp = mrp;



        Integer stockObj = size.getStock();
        int stock = stockObj != null ? stockObj : 0;

        if (stock > 0) {
            txtStockStatus.setText("IN STOCK");
            txtStockStatus.setBackgroundResource(R.drawable.bg_stock_in);
            btnAddToCart.setEnabled(true);
        } else {
            txtStockStatus.setText("OUT OF STOCK");
            txtStockStatus.setBackgroundResource(R.drawable.bg_stock_out);
            btnAddToCart.setEnabled(false);
        }

        removeFromCart(productId,selectedSizeId);
    }

    private void removeFromCart(int productId, int sizeId) {
        int userId = loginSession.isLoggedIn()
                ? Integer.parseInt(loginSession.getUserId())
                : 0;
        // ✅ Database work in background if any size will select
        executor.execute(() -> {

            CartItem existingItem = db.cartDao().checkItem(productId, userId);
            if(existingItem != null){
                CartSaveOnServer.cartRemoveFromServer(existingItem,null,
                        loginSession,DeviceInfo.getDeviceString(getContext()));
                currentQuantity =0;
            }

            db.cartDao().deleteCartByProductId(userId, productId);
        });
        btnAddToCart.setVisibility(View.VISIBLE);
        layoutStickyCart.setVisibility(View.GONE);

    }
    @Override
    public void onCartUpdated() {

        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).onCartUpdated();
        }
    }
}

