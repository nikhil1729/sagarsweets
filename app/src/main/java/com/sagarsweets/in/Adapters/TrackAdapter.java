package com.sagarsweets.in.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sagarsweets.in.ApiModel.TimelineItem;
import com.sagarsweets.in.R;

import java.util.List;

public class TrackAdapter
        extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    private final List<TimelineItem> list;

    public TrackAdapter(
            List<TimelineItem> list
    ) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(
                        parent.getContext()
                ).inflate(
                        R.layout.item_tracking,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        TimelineItem item =
                list.get(position);

        holder.txtStep.setText(
                item.getStep()
        );

        holder.txtTime.setText(
                item.getTime()
        );

        if (item.getTimeAgo() != null) {

            holder.txtTimeAgo
                    .setVisibility(
                            View.VISIBLE
                    );

            holder.txtTimeAgo.setText(
                    item.getTimeAgo()
            );

        } else {

            holder.txtTimeAgo
                    .setVisibility(
                            View.GONE
                    );
        }


        // Completed color

        if (item.isCompleted()) {

            holder.indicator
                    .setBackgroundResource(
                            R.drawable.bg_circle_green
                    );

        } else {

            holder.indicator
                    .setBackgroundResource(
                            R.drawable.bg_circle_gray
                    );
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    static class ViewHolder
            extends RecyclerView.ViewHolder {

        View indicator;

        TextView txtStep;

        TextView txtTime;

        TextView txtTimeAgo;

        ViewHolder(
                View itemView
        ) {

            super(itemView);

            indicator =
                    itemView.findViewById(
                            R.id.indicator
                    );

            txtStep =
                    itemView.findViewById(
                            R.id.txtStep
                    );

            txtTime =
                    itemView.findViewById(
                            R.id.txtTime
                    );

            txtTimeAgo =
                    itemView.findViewById(
                            R.id.txtTimeAgo
                    );
        }

    }

}
