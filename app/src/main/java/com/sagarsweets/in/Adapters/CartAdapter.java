package com.sagarsweets.in.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sagarsweets.in.ApiControllers.SuperController;
import com.sagarsweets.in.HomeActivity;
import com.sagarsweets.in.R;
import com.sagarsweets.in.Session.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartList = new ArrayList<>();
    private CartItemListener listener;
    private long lastClickTime = 0;

    public CartAdapter(Context context, CartItemListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setList(List<CartItem> list) {
        this.cartList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        CartItem model = cartList.get(position);

        // ---------- IMAGE ----------
        Glide.with(context)
                .load(SuperController.base_url_images + model.getProductImage())
                .placeholder(R.drawable.category_placeholder)
                .error(R.drawable.category_error)
                .into(holder.imgProduct);
        holder.txtName.setText(model.getProductName());
        holder.txtPrice.setText("₹" + model.getPrice());
        holder.txtQuantity.setText(String.valueOf(model.getQuantity()));


        // hide size if its id is zero
        if(model.getSizeId() == 0){
            holder.txtSize.setVisibility(View.GONE);
        }else{
            holder.txtSize.setText(model.getSizeSelectedName());
        }
        // PLUS BUTTON
        holder.btnPlus.setOnClickListener(v -> {


            int qty = model.getQuantity() + 1;
            model.setQuantity(qty);
            holder.txtQuantity.setText(String.valueOf(qty));
            listener.onQuantityChanged(model, v);
            animateAddToCart(holder.imgProduct);
        });

        // MINUS BUTTON
        holder.btnMinus.setOnClickListener(v -> {

            lastClickTime = System.currentTimeMillis();
            if (model.getQuantity() > 1) {
                int qty = model.getQuantity() - 1;
                model.setQuantity(qty);
                holder.txtQuantity.setText(String.valueOf(qty));
                listener.onQuantityChanged(model,v);
            }
        });

        // REMOVE BUTTON
        holder.btnRemove.setOnClickListener(v -> {
            listener.onItemRemoved(model,v);

        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
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

        // ✅ SAFE cart position
        int[] cartLocation = activity.getCartIconLocation();

        float endX = cartLocation[0];
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

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtPrice, txtQuantity, txtSize;
        TextView btnPlus, btnMinus;
        ImageView btnRemove,imgProduct;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtSize = itemView.findViewById(R.id.txtSize);

            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }

    // Interface for Fragment communication
    public interface CartItemListener {
        void onQuantityChanged(CartItem model, View v);
        void onItemRemoved(CartItem model, View v);
    }
}
