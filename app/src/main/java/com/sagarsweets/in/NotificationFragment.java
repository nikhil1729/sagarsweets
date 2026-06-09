package com.sagarsweets.in;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.sagarsweets.in.Adapters.NotificationAdapter;
import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.NotificationItem;
import com.sagarsweets.in.ApiModel.NotificationRequest;
import com.sagarsweets.in.ApiModel.NotificationResponse;
import com.sagarsweets.in.Session.LoginSession;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationFragment extends Fragment {

    RecyclerView recyclerNotifications;
    ShimmerFrameLayout shimmerLayout;
    LinearLayout emptyView;
    TextView tvErrorNotification,txtUnreadCount;
    LoginSession loginSession;
    Integer page = 1;
    Integer limit = 10;
    NotificationAdapter notificationAdapter;
    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        initView(view);
        loadNotification();
        // Inflate the layout for this fragment
        return view;
    }

    private void loadNotification() {
        startSmimmer();
        NotificationRequest request =
                new NotificationRequest(loginSession.getUserId(), page, limit);
        ApiService apiService =
                LoginRetrofitClient.getClient()
                        .create(ApiService.class);
        apiService.getNotification(request).enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().isStatus()) {
                    stopShimmer();
                    List<NotificationItem> notifications =
                            response.body().getData();

                    if (notifications != null && !notifications.isEmpty()) {
                        notificationAdapter.updateList(notifications);
                        SharedPreferences prefs =
                                requireContext().getSharedPreferences(
                                        "notification_prefs",
                                        Context.MODE_PRIVATE);
                        txtUnreadCount.setVisibility(View.VISIBLE);
                        int count = prefs.getInt("unread_count", 0);
                        txtUnreadCount.setText(count+" unread notifications");
                        prefs.edit()
                                .putInt("unread_count", 0)
                                .apply();
                        ((HomeActivity) requireActivity()).updateNotificationBadge();
                    } else {
                        Toast.makeText(getContext(),
                                "No notifications found",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    showError("No notifications found");
                    Toast.makeText(
                            getContext(),
                            "No notifications found",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                showError(t.getMessage());
            }
        });
    }

    private void initView(View view) {
        recyclerNotifications = view.findViewById(R.id.recyclerNotifications);
        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        emptyView = view.findViewById(R.id.emptyView);
        tvErrorNotification = view.findViewById(R.id.tvErrorNotification);
        txtUnreadCount = view.findViewById(R.id.txtUnreadCount);
        loginSession = new LoginSession(getContext());

        notificationAdapter = new NotificationAdapter(getContext());

        recyclerNotifications.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        recyclerNotifications.setAdapter(notificationAdapter);
    }

    public void startSmimmer(){
        shimmerLayout.startShimmer();
        shimmerLayout.setVisibility(View.VISIBLE);
        recyclerNotifications.setVisibility(View.GONE);
    }
    public void stopShimmer(){
        shimmerLayout.stopShimmer();
        shimmerLayout.setVisibility(View.GONE);
        recyclerNotifications.setVisibility(View.VISIBLE);
    }
    public void showError(String message){
        shimmerLayout.stopShimmer();
        shimmerLayout.setVisibility(View.GONE);
        recyclerNotifications.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        tvErrorNotification.setText(message);
    }
}