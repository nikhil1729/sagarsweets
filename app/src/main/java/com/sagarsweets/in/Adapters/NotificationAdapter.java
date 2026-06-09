package com.sagarsweets.in.Adapters;


import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sagarsweets.in.ApiModel.NotificationItem;
import com.sagarsweets.in.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private List<NotificationItem> notificationList;

    public NotificationAdapter(Context context) {
        this.context = context;
        this.notificationList = new ArrayList<>();
    }

    public void updateList(List<NotificationItem> list) {
        notificationList.clear();
        notificationList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.notification_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        NotificationItem item = notificationList.get(position);

        holder.txtTitle.setText(item.getTitle());
        //holder.txtMessage.setText(item.getMessage());
        holder.txtTime.setText(item.getCreatedAt());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.txtMessage.setText(
                    Html.fromHtml(
                            item.getMessage(),
                            Html.FROM_HTML_MODE_LEGACY
                    )
            );
        } else {
            holder.txtMessage.setText(
                    Html.fromHtml(item.getMessage())
            );
        }


        // Notification icon based on type
        switch (item.getType()) {

            case "CONFIRMED":
                holder.imgNotification.setImageResource(R.drawable.ic_confirmed);
                break;

            case "CANCLED_BY_ADMIN":
                holder.imgNotification.setImageResource(R.drawable.ic_cancelled);
                break;

            default:
                holder.imgNotification.setImageResource(R.drawable.ic_notifications);
                break;
        }

        // Unread indicator
        if ("0".equals(item.getIsRead())) {
            holder.viewUnread.setVisibility(View.VISIBLE);
        } else {
            holder.viewUnread.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtMessage;
        TextView txtTime;
        ImageView imgNotification;
        View viewUnread;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtTime = itemView.findViewById(R.id.txtTime);
            imgNotification = itemView.findViewById(R.id.imgNotification);
            viewUnread = itemView.findViewById(R.id.viewUnread);
        }
    }
}
