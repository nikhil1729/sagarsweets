package com.sagarsweets.in;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sagarsweets.in.Adapters.ReviewAdapter;
import com.sagarsweets.in.ApiModel.ReviewModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ReviewsBottomSheetFragment extends BottomSheetDialogFragment {

    private List<ReviewModel> reviewList;
    private boolean reviewStatus;

    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        if (view != null && view.getParent() instanceof View) {
            View parent = (View) view.getParent();

            BottomSheetBehavior<View> behavior =
                    BottomSheetBehavior.from(parent);

            behavior.setPeekHeight(
                    (int) (getResources().getDisplayMetrics().heightPixels * 0.5)
            );

            //behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            //behavior.setDraggable(true);     // swipe-down enabled
            behavior.setHideable(true);      // close on swipe
        }
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.bottomsheet_reviews, container, false);
        if (getArguments() != null) {
            reviewStatus = getArguments().getBoolean("review_status");
            reviewList = (List<ReviewModel>) getArguments()
                    .getSerializable("review_list");
        }

        if (!reviewStatus || reviewList == null || reviewList.isEmpty()) {
            TextView tvNoReviews = view.findViewById(R.id.tvNoReviews);
            RecyclerView rvReviews = view.findViewById(R.id.rvReviews);
            tvNoReviews.setVisibility(View.VISIBLE);
            rvReviews.setVisibility(View.GONE);
        } else {
            RecyclerView rvReviews = view.findViewById(R.id.rvReviews);
            TextView txtTitle = view.findViewById(R.id.txtTitle);

            txtTitle.setText("Customer Reviews");

            rvReviews.setLayoutManager(new LinearLayoutManager(getContext()));
            rvReviews.setAdapter(new ReviewAdapter(reviewList));
        }



        return view;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }


}

