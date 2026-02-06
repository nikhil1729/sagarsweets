package com.sagarsweets.in.Adapters.PolicyRelated;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sagarsweets.in.ApiModel.PolicyItem;
import com.sagarsweets.in.R;

import java.util.ArrayList;
import java.util.List;

public class PolicyAdapter extends RecyclerView.Adapter<PolicyAdapter.ViewHolder> {

    private Context context;
    private List<PolicyItem> list;
    private List<PolicyItem> original;

    public PolicyAdapter(Context context, List<PolicyItem> list) {
        this.context = context;
        this.list = new ArrayList<>(list);
        this.original = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_policy, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PolicyItem item = list.get(position);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvContent.setText(
                    Html.fromHtml(item.getContent(), Html.FROM_HTML_MODE_LEGACY)
            );

        } else {
            holder.tvContent.setText(Html.fromHtml(item.getContent()));
        }
        holder.tvLastEditedTime.setText("Last Edited:"+item.getUpdatedAt());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterByType(String type) {
        list.clear();

        if (type == null || type.trim().isEmpty()) {
            // Optional: show all if no type
            list.addAll(original);
            notifyDataSetChanged();
            return;
        }

        for (PolicyItem item : original) {

            if (item == null) continue;

            String title = item.getType();

            if (title != null && title.equalsIgnoreCase(type)) {
                list.add(item);
            }
        }

        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent,tvLastEditedTime;
        ViewHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvLastEditedTime = itemView.findViewById(R.id.tvLastEditedTime);
        }
    }
}

