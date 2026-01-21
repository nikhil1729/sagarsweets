package com.sagarsweets.in.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sagarsweets.in.ApiControllers.SuperController;
import com.sagarsweets.in.ApiModel.ImageModel;
import com.sagarsweets.in.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageVH> {

    private final List<ImageModel> imageList;
    private final String defaultImage;

    public ImageAdapter(List<ImageModel> imageList, String defaultImage) {
        this.imageList = imageList;
        this.defaultImage = defaultImage;
    }

    @NonNull
    @Override
    public ImageVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_image, parent, false);
        return new ImageVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageVH holder, int position) {

        String imageUrl;

        if (imageList != null && !imageList.isEmpty()) {
            imageUrl = defaultImage.replace(
                    defaultImage.substring(defaultImage.lastIndexOf("/") + 1),
                    "view?file="+imageList.get(position).getImage()

            );
            Log.d("image_last",imageUrl);
            imageUrl = SuperController.base_url_images+imageUrl+"&size=400x250";
            Log.d("image_last",imageUrl);
        } else {
            imageUrl = SuperController.base_url_images+defaultImage;
        }
        Log.d("in if image",imageUrl);
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_product_placeholder)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageList != null && !imageList.isEmpty() ? imageList.size() : 1;
    }

    static class ImageVH extends RecyclerView.ViewHolder {
        ImageView imageView;

        ImageVH(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgProduct);
        }
    }
}

