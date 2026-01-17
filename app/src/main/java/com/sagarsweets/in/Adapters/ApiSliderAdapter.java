package com.sagarsweets.in.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.sagarsweets.in.ApiModel.SliderModel;
import com.sagarsweets.in.R;

import java.util.List;

public class ApiSliderAdapter extends RecyclerView.Adapter<ApiSliderAdapter.SliderViewHolder> {

    private Context context;
    private List<SliderModel> sliderList;

    private static final String BASE_IMAGE_URL =
            "http://192.168.0.103";

    public ApiSliderAdapter(Context context, List<SliderModel> sliderList) {
        this.context = context;
        this.sliderList = sliderList;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_slider, parent, false);
        return new SliderViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {

        SliderModel model = sliderList.get(position);


        String imageUrl = BASE_IMAGE_URL  + model.getImage();
        Log.d("FINAL_IMAGE_URL", imageUrl);

        holder.shimmer.setVisibility(View.VISIBLE);
        holder.shimmer.startShimmer();
        holder.imageView.setVisibility(View.INVISIBLE);
        holder.errorLayout.setVisibility(View.GONE);

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.slider_placeholder)
                .error(R.drawable.slider_error)
                .into(new CustomTarget<Drawable>() {

                    @Override
                    public void onResourceReady(
                            @NonNull Drawable resource,
                            @Nullable Transition<? super Drawable> transition) {

                        holder.shimmer.stopShimmer();
                        holder.shimmer.setVisibility(View.GONE);

                        holder.imageView.setImageDrawable(resource);
                        holder.imageView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        Log.e("GLIDE_FAIL", "Load failed. errorDrawable = " + errorDrawable);
                        Log.e("GLIDE_FAIL",imageUrl);
                        holder.shimmer.stopShimmer();
                        holder.shimmer.setVisibility(View.GONE);
                        holder.errorLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // View recycled — optional cleanup
                    }
                });


    }



    @Override
    public int getItemCount() {
        Log.d("SLIDER_COUNT", String.valueOf(sliderList.size()));
        return sliderList.size();
    }

    static class SliderViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ShimmerFrameLayout shimmer;
        LinearLayout errorLayout;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgSlider);
            shimmer = itemView.findViewById(R.id.shimmerLayout);
            errorLayout = itemView.findViewById(R.id.errorLayout);
        }
    }
}
