package com.sagarsweets.in.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sagarsweets.in.ApiModel.ReviewModel;
import com.sagarsweets.in.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.VH> {

    private final List<ReviewModel> list;

    public ReviewAdapter(List<ReviewModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        ReviewModel m = list.get(pos);

        h.txtUser.setText(m.getEmail());
        h.txtDate.setText(m.getCreated_at());
        h.ratingBar.setRating(m.getRating());
        h.txtReview.setText(m.getReview());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView txtUser, txtDate, txtReview,tvNoReviews;
        RatingBar ratingBar;

        VH(View v) {
            super(v);
            tvNoReviews = v.findViewById(R.id.tvNoReviews);
            txtUser = v.findViewById(R.id.txtUser);
            txtDate = v.findViewById(R.id.txtDate);
            txtReview = v.findViewById(R.id.txtReview);
            ratingBar = v.findViewById(R.id.ratingBar);
        }
    }
}
