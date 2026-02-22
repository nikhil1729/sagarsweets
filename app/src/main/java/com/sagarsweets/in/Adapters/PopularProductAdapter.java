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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
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
import com.sagarsweets.in.utils.CartSaveOnServer;
import com.sagarsweets.in.utils.DeviceInfo;
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
        vh.sizeSelectedName = "NA";

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
        Glide.with(vh.itemView)
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
        List<SizeModel> sizes = product.getSizeList();
        // ---------- STOCK ----------
        /*algorithm for stock start*/
        // first check size is available or not
            // if available then
                // stock will get from here
            // if size is not available
                // stock directly must be available

        if(!sizes.isEmpty() && sizes.size() > 0){
            // here size is available
            int totalStock = 0;
            for (SizeModel size : sizes) {
                Integer stock = size.getStock();
                totalStock += (stock == null ? 0 : stock);
                Log.d("ProductstockLL",product.getProductName()+"-"+String.valueOf(stock));
                totalStock += stock;
            }
            if(totalStock > 0){
                // product is in stock
                vh.tvStockStatus.setText("IN STOCK");
                vh.tvStockStatus.setBackgroundResource(R.drawable.bg_stock_in);
                vh.ivAddToCart.setEnabled(true);
                vh.itemView.setAlpha(1f);
            }else{
                // product is out of stock
                vh.tvStockStatus.setText("OUT OF STOCK");
                vh.tvStockStatus.setBackgroundResource(R.drawable.bg_stock_out);
                vh.ivAddToCart.setEnabled(false);
                vh.itemView.setAlpha(0.6f);
            }
            Log.d("PRODUCTSTOCKDEBUG","if-"+product.getProductName()+"-size-"+sizes.size()+",Totalstock-"+totalStock);
        }else{
            // here size is not available
            if (product.getStock() == null || product.getStock() == 0) {
                // stock not  available
                vh.tvStockStatus.setText("OUT OF STOCK");
                vh.tvStockStatus.setBackgroundResource(R.drawable.bg_stock_out);
                vh.ivAddToCart.setEnabled(false);
                vh.itemView.setAlpha(0.6f);
                Log.d("PRODUCTSTOCKDEBUG","ifstock -"+product.getProductName()+"-stock-"+product.getStock());
            }else{
                // stock available
                vh.tvStockStatus.setText("IN STOCK");
                vh.tvStockStatus.setBackgroundResource(R.drawable.bg_stock_in);
                vh.ivAddToCart.setEnabled(true);
                vh.itemView.setAlpha(1f);
                Log.d("PRODUCTSTOCKDEBUG","elsestock -"+product.getProductName()+"-stock-"+product.getStock());
            }

        }
        /*algorithm end*/

        // ---------- SIZE LIST ----------

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

                    addedToCart(product,vh.selectedSizeId,vh.quantity,vh.sizeSelectedName,vh,v);

                } else {

                    vh.quantity = 0;

                    vh.layoutCartSection.setVisibility(View.GONE);
                    vh.frIvCart.setVisibility(View.VISIBLE);
                    //addedToCart(product,vh.selectedSizeId,vh.quantity);
                    removeFromCart(product,v);
                }
            }
        });
        vh.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int availableStock = getAvailableStock(product, vh.selectedSizeId);

                if (vh.quantity >= availableStock) {
                    Toast.makeText(context, "Maximum stock reached", Toast.LENGTH_SHORT).show();
                    return;
                }
                vh.quantity++;
                vh.tvQuantity.setText(String.valueOf(vh.quantity));

                addedToCart(product, vh.selectedSizeId,vh.quantity, vh.sizeSelectedName, vh, v);
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
                    cartControlVisible(vh,product,vh.selectedSizeId,v);
                }else{
                    // size is not available
                    Log.d("DEBUG_CART","cart clicked-size is not available");
                    cartControlVisible(vh,product,0,v);
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
                        vh.tvPrice.setText("₹"+(int)existingItem.getMrp());
                        vh.tvSalePrice.setText("₹"+(int)existingItem.getPrice());
                        vh.sizeAdapter.setSelectedSizeById(sizeId);
                    }

                });

            }
        });
    }


    private void removeFromCart(ProductModel product, View v) {
        executor.execute(() -> {
            int productId = product.getId();
            int userId = loginSession.isLoggedIn() ? Integer.parseInt(loginSession.getUserId()) : 0;

            CartItem existingItem = db.cartDao().checkItem(productId, userId);
            CartSaveOnServer.cartRemoveFromServer(existingItem,v,
                    loginSession,DeviceInfo.getDeviceString(context));
            db.cartDao().deleteItem(productId,userId);
        });
    }

    private void cartControlVisible(ProductVH vh, ProductModel product,
                                    int selectedSizeId, View v) {
        int availableStock = getAvailableStock(product, selectedSizeId);

        if (availableStock <= 0) {
            Toast.makeText(context, "Out of stock", Toast.LENGTH_SHORT).show();
            return;
        }
        vh.layoutCartSection.setVisibility(View.VISIBLE);
        vh.frIvCart.setVisibility(View.GONE);
        vh.quantity = 1;
        vh.tvQuantity.setText("1");
        addedToCart(product, selectedSizeId, vh.quantity, vh.sizeSelectedName, vh, v);
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


    private void addedToCart(ProductModel product, int selectedSizeId,
                             int quantity, String sizeSelectedName, ProductVH vh, View v) {
        executor.execute(() -> {
            int productId = product.getId();
            int userId = loginSession.isLoggedIn() ? Integer.parseInt(loginSession.getUserId()) : 0;
            CartItem existingItem = db.cartDao().checkItem(productId, userId);
            String pName = product.getProductName();
            String pImage = product.getImagePath();
            // PRICE/MRP SETTING
            double price;
            double mrp;

            if (selectedSizeId != 0) {
                price = vh.sellPrice;
                mrp   = vh.mrpPrice;
            } else {
                price = Double.parseDouble(product.getSellingPrice());
                mrp   = Double.parseDouble(product.getMrp());
            }
            // END
            //int quantity = 1;
            int sizeId = selectedSizeId;
            Log.d("TESTCART","Product id-"+String.valueOf(productId));
            if (existingItem != null) {
                existingItem.setMrp(mrp);
                existingItem.setPrice(price);
                existingItem.setQuantity(quantity);
                existingItem.setUpdatedAt(System.currentTimeMillis());
                existingItem.setSynced(false);
                db.cartDao().update(existingItem);
                CartSaveOnServer.saveCartOnServer(existingItem,v,
                        loginSession, DeviceInfo.getDeviceString(context));
                Log.d("DEBUG_CART","Updated insert");
            } else {
                CartItem newItem = new CartItem(productId,pName,
                        pImage,price,mrp,quantity,sizeId,userId,
                        sizeSelectedName,System.currentTimeMillis(),false);
                db.cartDao().insert(newItem);
                CartSaveOnServer.saveCartOnServer(newItem,v,
                        loginSession, DeviceInfo.getDeviceString(context));
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



    public void setShowEndMessage(boolean show) {
        showEndMessage = show;
        notifyDataSetChanged();
    }

    private void updatePriceAndStock(ProductVH vh, SizeModel size) {

        // 1️⃣ Save selected size info
        vh.sizeSelected = true;
        vh.selectedSizeId = size.getId();
        vh.sizeSelectedName = size.getTitle();

        // 2️⃣ Store price in ViewHolder (important for cart)
        vh.sellPrice = Double.parseDouble(size.getSellingPrice());
        vh.mrpPrice  = Double.parseDouble(size.getMrp());

        // 3️⃣ Update UI price immediately
        vh.tvSalePrice.setText("₹" + size.getSellingPrice());
        vh.tvPrice.setText("₹" + size.getMrp());

        // 4️⃣ Reset cart UI (do NOT delete from DB)
        vh.layoutCartSection.setVisibility(View.GONE);
        vh.frIvCart.setVisibility(View.VISIBLE);
        vh.quantity = 0;
        vh.tvQuantity.setText("0");


        /*vh.sellPrice = Double.parseDouble(size.getSellingPrice());*/
        /*vh.mrpPrice  = Double.parseDouble(size.getMrp());*/
        /*vh.tvSalePrice.setText("₹" + size.getSellingPrice());*/
        /*vh.tvPrice.setText("₹" + size.getMrp());*/
        /*vh.sizeSelected = true;*/
        /*vh.selectedSizeId = size.getId();*/
        /*vh.sizeSelectedName = size.getTitle();*/


        int productId = vh.vhProductId;
        int userId = loginSession.isLoggedIn()
                ? Integer.parseInt(loginSession.getUserId())
                : 0;

        // ✅ Database work in background if any size will select
        executor.execute(() -> {

            CartItem existingItem = db.cartDao().checkItem(productId, userId);
            if(existingItem != null){
                CartSaveOnServer.cartRemoveFromServer(existingItem,null,
                        loginSession,DeviceInfo.getDeviceString(context));
            }

            db.cartDao().deleteCartByProductId(userId, productId);
        });

        // ✅ UI updates on main thread ONLY
        vh.layoutCartSection.setVisibility(View.GONE);
        vh.frIvCart.setVisibility(View.VISIBLE);
        vh.quantity = 0;
        vh.tvQuantity.setText("0");


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

    private int getAvailableStock(ProductModel product, int selectedSizeId) {
        List<SizeModel> sizes = product.getSizeList();

        // ✅ If size-based product
        if (sizes != null && !sizes.isEmpty()) {
            for (SizeModel size : sizes) {
                if (size.getId() == selectedSizeId) {
                    Integer stock = size.getStock();
                    return stock == null ? 0 : size.getStock();
                }
            }
            return 0; // size not found
        }

        // ✅ Non-size product
        return product.getStock() == null ? 0 : product.getStock();
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
        String sizeSelectedName;
        LinearLayout layoutCartSection;
        ImageView btnMinus,btnPlus;
        TextView tvQuantity;
        FrameLayout frIvCart;
        int quantity;
        int vhProductId;
        SizeAdapter sizeAdapter;
        double sellPrice,mrpPrice;
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
