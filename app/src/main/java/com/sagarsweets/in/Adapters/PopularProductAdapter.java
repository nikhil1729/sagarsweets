package com.sagarsweets.in.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.badge.BadgeDrawable;
import com.sagarsweets.in.ApiControllers.SuperController;
import com.sagarsweets.in.ApiModel.ProductModel;
import com.sagarsweets.in.ApiModel.SizeModel;
import com.sagarsweets.in.HomeActivity;
import com.sagarsweets.in.ProductDetailsFragment;
import com.sagarsweets.in.R;
import com.sagarsweets.in.RoomDatabase.AppDatabase;
import com.sagarsweets.in.Session.CartItem;
import com.sagarsweets.in.Session.LoginSession;
import com.sagarsweets.in.utils.WishListClicked;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PopularProductAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Executor executor = Executors.newSingleThreadExecutor();

    private Context context;
    private List<ProductModel> productList;

    private boolean categoryWise;
    private boolean showEndMessage = false;

    private static final int TYPE_PRODUCT = 1;
    private static final int TYPE_LOADER  = 2;
    private static final int TYPE_END     = 3;
    BadgeDrawable badge;

    AppDatabase db;
    LoginSession loginSession;
    private CartUpdateListener cartUpdateListener; //9532549374

    public PopularProductAdapter(Context context,
                                 List<ProductModel> productList,
                                 boolean categoryWise,
                                 CartUpdateListener listener) {
        this.context = context;
        this.productList = productList;
        this.categoryWise = categoryWise;
        this.cartUpdateListener = listener;
        db = AppDatabase.getInstance(context);
        loginSession = new LoginSession(context);
    }

    // --------------------------------------------------
    // VIEW HOLDER CREATION
    // --------------------------------------------------

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_LOADER) {
            View v = inflater.inflate(R.layout.item_load_more_shimmer, parent, false);
            return new LoaderVH(v);
        }

        if (viewType == TYPE_END) {
            View v = inflater.inflate(R.layout.item_no_more_products, parent, false);
            return new EndVH(v);
        }

        View v = inflater.inflate(R.layout.item_popular_product, parent, false);
        return new ProductVH(v);
    }

    // --------------------------------------------------
    // BIND
    // --------------------------------------------------

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof LoaderVH || holder instanceof EndVH) {
            return;
        }

        ProductVH vh = (ProductVH) holder;
        ProductModel product = productList.get(position);
        // RESET UI FIRST (VERY IMPORTANT)
        vh.layoutCartSection.setVisibility(View.GONE);
        vh.frIvCart.setVisibility(View.VISIBLE);
        vh.tvQuantity.setText("0");
        vh.quantity = 0;
        vh.sizeSelected = false;
        vh.selectedSizeId = 0;

        // ---------- WIDTH (CATEGORY WISE) ----------
        if (categoryWise) {
            DisplayMetrics metrics =
                    vh.itemView.getResources().getDisplayMetrics();
            int itemWidth = (int) (metrics.widthPixels * 0.45);

            RecyclerView.LayoutParams params =
                    (RecyclerView.LayoutParams) vh.itemView.getLayoutParams();
            params.width = itemWidth;
            params.rightMargin = dpToPx(12);
            vh.itemView.setLayoutParams(params);
        } else {
            RecyclerView.LayoutParams params =
                    (RecyclerView.LayoutParams) vh.itemView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            vh.itemView.setLayoutParams(params);
        }

        // ---------- IMAGE ----------
        Glide.with(context)
                .load(SuperController.base_url_images + product.getImagePath())
                .placeholder(R.drawable.category_placeholder)
                .error(R.drawable.category_error)
                .into(vh.imgProduct);

        // ---------- TEXT ----------
        vh.vhProductId = product.getId();
        vh.tvProductName.setText(product.getProductName());
        vh.tvSalePrice.setText("₹" + product.getSellingPrice());
        vh.tvPrice.setText("₹" + product.getMrp());
        vh.ratingBar.setRating(product.getRating());
        vh.tvRatingCount.setText("(" + product.getRatingCount() + ")");

        // ---------- STOCK ----------
        if (product.getStock() != null && product.getStock() == 0) {
            vh.tvStockStatus.setText("OUT OF STOCK");
            vh.tvStockStatus.setBackgroundResource(R.drawable.bg_stock_out);
            vh.ivAddToCart.setEnabled(false);
            vh.itemView.setAlpha(0.6f);
        } else {
            vh.tvStockStatus.setText("IN STOCK");
            vh.tvStockStatus.setBackgroundResource(R.drawable.bg_stock_in);
            vh.ivAddToCart.setEnabled(true);
            vh.itemView.setAlpha(1f);
        }

        // ---------- SIZE LIST ----------
        List<SizeModel> sizes = product.getSizeList();

        if (sizes != null && !sizes.isEmpty()) {

            vh.rvSizes.setVisibility(View.VISIBLE);
            vh.rvSizes.setLayoutManager(
                    new LinearLayoutManager(context,
                            LinearLayoutManager.HORIZONTAL, false));

            vh.sizeAdapter = new SizeAdapter(
                    sizes,
                    size -> updatePriceAndStock(vh, size)
            );

            vh.rvSizes.setAdapter(vh.sizeAdapter);

        } else {
            vh.rvSizes.setVisibility(View.GONE);
        }

        // ---------- WISH LIST CHECK --------
        LoginSession loginSession = new LoginSession(context);
        if(loginSession.isLoggedIn()){
            if(product.getWIshListed()){
                vh.imgWishlist.setImageResource(R.drawable.ic_heart_filled);
            }else{
                vh.imgWishlist.setImageResource(R.drawable.ic_heart_outline);
            }
        }else{
            int currentProductId = product.getId();

            executor.execute(() -> {

                boolean exists = db.wishlistDao().isExists(currentProductId);

                ((FragmentActivity) context).runOnUiThread(() -> {

                    if (vh.getAdapterPosition() == RecyclerView.NO_POSITION)
                        return;

                    if (product.getId() != currentProductId)
                        return;

                    vh.imgWishlist.setImageResource(
                            exists ? R.drawable.ic_heart_filled
                                    : R.drawable.ic_heart_outline
                    );
                });
            });

        }

        // -------- FUNCTION FOR CHECK PRODUCT IS IN CART -------//
        checkProductInCart(vh,product);

        // ---------- CLICK ----------
        vh.tvProductName.setOnClickListener(v ->
                openProductDetails(product.getId()));
        vh.imgWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wishListSaved(product.getId(),vh);
            }
        });
        vh.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vh.quantity > 1) {

                    vh.quantity--;
                    vh.tvQuantity.setText(String.valueOf(vh.quantity));

                    addedToCart(product,vh.selectedSizeId,vh.quantity);

                } else {

                    vh.quantity = 0;

                    vh.layoutCartSection.setVisibility(View.GONE);
                    vh.frIvCart.setVisibility(View.VISIBLE);
                    addedToCart(product,vh.selectedSizeId,vh.quantity);
                    removeFromCart(product);
                }
            }
        });
        vh.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vh.quantity++;
                vh.tvQuantity.setText(String.valueOf(vh.quantity));

                addedToCart(product, vh.selectedSizeId,vh.quantity);
                animateAddToCart(vh.imgProduct);
            }
        });

        vh.ivAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<SizeModel> sizes = product.getSizeList();
                Log.d("DEBUG_CART", "Size-"+String.valueOf(sizes.size()));
                if (sizes != null && !sizes.isEmpty()) {
                    // size is available
                    Log.d("DEBUG_CART","Selected Size ID-"+vh.selectedSizeId);

                    Log.d("DEBUG_CART","cart clicked-size is available");
                    if (!vh.sizeSelected) {
                        showSizeSelected(vh);
                        return;
                    }
                    cartControlVisible(vh,product,vh.selectedSizeId);
                    //addedToCart(product,vh.selectedSizeId);
                }else{
                    // size is not available
                    Log.d("DEBUG_CART","cart clicked-size is not available");
                    cartControlVisible(vh,product,0);
                    //addedToCart(product,0);
                }
                //animateAddToCart(vh.imgProduct);
            }
        });
        // ------------- CLICK END ----------------
    }

    private void checkProductInCart(ProductVH vh, ProductModel product) {
        executor.execute(() -> {

            int productId = product.getId();
            int userId = loginSession.isLoggedIn()
                    ? Integer.parseInt(loginSession.getUserId())
                    : 0;

            CartItem existingItem = db.cartDao().checkItem(productId, userId);

            if (existingItem != null) {

                int count = existingItem.getQuantity();
                int sizeId = existingItem.getSizeId();

                ((FragmentActivity) context).runOnUiThread(() -> {

                    vh.frIvCart.setVisibility(View.GONE);
                    vh.layoutCartSection.setVisibility(View.VISIBLE);
                    vh.tvQuantity.setText(String.valueOf(count));
                    vh.quantity = count;

                    if (sizeId != 0 && vh.sizeAdapter != null) {

                        vh.sizeSelected = true;
                        vh.selectedSizeId = sizeId;

                        vh.sizeAdapter.setSelectedSizeById(sizeId);
                    }

                });

            }
        });
    }


    private void removeFromCart(ProductModel product) {
        executor.execute(() -> {
            int productId = product.getId();
            int userId = loginSession.isLoggedIn() ? Integer.parseInt(loginSession.getUserId()) : 0;
            db.cartDao().deleteItem(productId,userId);
        });
    }

    private void cartControlVisible(ProductVH vh, ProductModel product, int selectedSizeId) {
        vh.layoutCartSection.setVisibility(View.VISIBLE);
        vh.frIvCart.setVisibility(View.GONE);
        vh.quantity = 1;
        vh.tvQuantity.setText("1");
        addedToCart(product, selectedSizeId, vh.quantity);
        animateAddToCart(vh.imgProduct);
    }

    private void showSizeSelected(ProductVH vh) {
            vh.rvSizes.animate()
                    .translationX(20)
                    .setDuration(50)
                    .withEndAction(() ->
                            vh.rvSizes.animate()
                                    .translationX(-20)
                                    .setDuration(50)
                                    .withEndAction(() ->
                                            vh.rvSizes.animate()
                                                    .translationX(0)
                                                    .setDuration(50)
                                    )
                    );
            return;
    }

    // animation function while adding in cart
    private void animateAddToCart(ImageView imageView) {

        Context context = imageView.getContext();

        if (!(context instanceof HomeActivity)) return;

        HomeActivity activity = (HomeActivity) context;

        int[] startLocation = new int[2];
        imageView.getLocationOnScreen(startLocation);

        ImageView animView = new ImageView(context);
        animView.setImageDrawable(imageView.getDrawable());

        ViewGroup root = (ViewGroup) activity.getWindow().getDecorView();

        ViewGroup.LayoutParams params =
                new ViewGroup.LayoutParams(imageView.getWidth(), imageView.getHeight());

        animView.setLayoutParams(params);
        animView.setX(startLocation[0]);
        animView.setY(startLocation[1]);

        root.addView(animView);

        // Get cart position
        int[] cartLocation = new int[2];
        activity.findViewById(R.id.action_cart)
                .getLocationOnScreen(cartLocation);

        float endX = cartLocation[0]-150; // adjust if needed
        float endY = cartLocation[1];

        animView.animate()
                .x(endX)
                .y(endY)
                .scaleX(0.2f)
                .scaleY(0.2f)
                .alpha(0.5f)
                .setDuration(600)
                .withEndAction(() -> root.removeView(animView))
                .start();
    }


    private void addedToCart(ProductModel product,int selectedSizeId,int quantity) {
        executor.execute(() -> {
            int productId = product.getId();
            int userId = loginSession.isLoggedIn() ? Integer.parseInt(loginSession.getUserId()) : 0;
            CartItem existingItem = db.cartDao().checkItem(productId, userId);
            String pName = product.getProductName();
            String pImage = product.getImagePath();
            double price = Double.parseDouble(product.getSellingPrice());
            //int quantity = 1;
            int sizeId = selectedSizeId;

            if (existingItem != null) {
                existingItem.setQuantity(quantity);
                db.cartDao().update(existingItem);
                Log.d("DEBUG_CART","Updated insert");
            } else {
                CartItem newItem = new CartItem(productId,pName,
                        pImage,price,quantity,sizeId,userId);
                db.cartDao().insert(newItem);
                
                Log.d("DEBUG_CART","new inserted");
            }
            if (cartUpdateListener != null) {
                ((FragmentActivity) context).runOnUiThread(() -> {
                    cartUpdateListener.onCartUpdated();
                });
            }

        });

    }



    private void wishListSaved(int pId, ProductVH vh) {
        LoginSession loginSession = new LoginSession(context);
        if(loginSession.isLoggedIn()){
            // if user login then call api
            WishListClicked.clicked(context, loginSession.getUserId(), String.valueOf(pId),vh.imgWishlist);
        }else{
            // else save in locally
            Log.d("Non Login User","clicked");
            WishListClicked.clickNonLogin(context, String.valueOf(pId),vh.imgWishlist);
        }
    }

    // --------------------------------------------------
    // ITEM COUNT / TYPE
    // --------------------------------------------------

    @Override
    public int getItemCount() {
        int count = productList == null ? 0 : productList.size();
        return showEndMessage ? count + 1 : count;
    }

    @Override
    public int getItemViewType(int position) {

        if (position < productList.size()
                && productList.get(position) == null) {
            return TYPE_LOADER;
        }

        if (position == productList.size() && showEndMessage) {
            return TYPE_END;
        }

        return TYPE_PRODUCT;
    }

    // --------------------------------------------------
    // HELPERS
    // --------------------------------------------------

    public void addLoader() {
        productList.add(null);
        notifyItemInserted(productList.size() - 1);
    }

    public void removeLoader() {
        int pos = productList.size() - 1;
        if (pos >= 0 && productList.get(pos) == null) {
            productList.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    public void setShowEndMessage(boolean show) {
        showEndMessage = show;
        notifyDataSetChanged();
    }

    private void updatePriceAndStock(ProductVH vh, SizeModel size) {

        vh.tvSalePrice.setText("₹" + size.getSellingPrice());
        vh.tvPrice.setText("₹" + size.getMrp());
        vh.sizeSelected = true;
        vh.selectedSizeId = size.getId();

        int productId = vh.vhProductId;
        int userId = loginSession.isLoggedIn()
                ? Integer.parseInt(loginSession.getUserId())
                : 0;

        // ✅ Database work in background
        executor.execute(() -> {
            db.cartDao().deleteCartByProductId(userId, productId);
        });

        // ✅ UI updates on main thread ONLY
        vh.layoutCartSection.setVisibility(View.GONE);
        vh.frIvCart.setVisibility(View.VISIBLE);
        vh.quantity = 0;
        vh.tvQuantity.setText("0");

        if (size.getStock() > 0) {
            vh.tvStockStatus.setText("IN STOCK");
            vh.tvStockStatus.setBackgroundResource(R.drawable.bg_stock_in);
            vh.ivAddToCart.setEnabled(true);
            vh.itemView.setAlpha(1f);
        } else {
            vh.tvStockStatus.setText("OUT OF STOCK");
            vh.tvStockStatus.setBackgroundResource(R.drawable.bg_stock_out);
            vh.ivAddToCart.setEnabled(false);
            vh.itemView.setAlpha(0.6f);
        }
    }



    private void openProductDetails(Integer productId) {
        if (!(context instanceof FragmentActivity)) return;

        FragmentActivity activity = (FragmentActivity) context;
        ProductDetailsFragment fragment = new ProductDetailsFragment();

        Bundle b = new Bundle();
        b.putInt("product_id", productId);
        fragment.setArguments(b);

        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("product_details")
                .commit();
    }

    private int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem()
                .getDisplayMetrics().density);
    }

    // --------------------------------------------------
    // VIEW HOLDERS
    // --------------------------------------------------

    public static class ProductVH extends RecyclerView.ViewHolder {

        ImageView imgProduct, ivAddToCart,imgWishlist;
        TextView tvProductName, tvSalePrice, tvPrice,
                tvRatingCount, tvStockStatus;
        RatingBar ratingBar;
        RecyclerView rvSizes;
        Boolean sizeSelected = false;
        int selectedSizeId ;
        LinearLayout layoutCartSection;
        ImageView btnMinus,btnPlus;
        TextView tvQuantity;
        FrameLayout frIvCart;
        int quantity;
        int vhProductId;
        SizeAdapter sizeAdapter;

        public ProductVH(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            imgWishlist = itemView.findViewById(R.id.imgWishlist);
            ivAddToCart = itemView.findViewById(R.id.ivAddToCart);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvSalePrice = itemView.findViewById(R.id.tvSalePrice);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRatingCount = itemView.findViewById(R.id.tvRatingCount);
            tvStockStatus = itemView.findViewById(R.id.tvStockStatus);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            rvSizes = itemView.findViewById(R.id.rvSizes);
            layoutCartSection = itemView.findViewById(R.id.layoutCartSection);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            frIvCart = itemView.findViewById(R.id.frIvCart);
        }
    }

    public static class LoaderVH extends RecyclerView.ViewHolder {
        public LoaderVH(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class EndVH extends RecyclerView.ViewHolder {
        public EndVH(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface CartUpdateListener {
        void onCartUpdated();
    }
}
