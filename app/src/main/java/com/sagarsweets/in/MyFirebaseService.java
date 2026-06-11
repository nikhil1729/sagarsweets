package com.sagarsweets.in;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sagarsweets.in.ApiControllers.ResetOtpRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.OtpResponse;
import com.sagarsweets.in.ApiModel.TokenRequest;
import com.sagarsweets.in.ApiModel.TokenResponse;
import com.sagarsweets.in.Session.LoginSession;
import com.sagarsweets.in.utils.CustomToast;
import com.sagarsweets.in.utils.DeviceInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        Log.d("FCM_TOKEN", token);
        Log.d("BEFORE_TOKEN","HOME ACT");
        // API for saving token
        ApiService apiService = ResetOtpRetrofitClient.getApiService();
        LoginSession loginSession = new LoginSession(MyFirebaseService.this);
        TokenRequest tokenRequest = new TokenRequest(loginSession.getUserId(),
                DeviceInfo.getDeviceString(MyFirebaseService.this),token);
        Call<TokenResponse> call = apiService.saveTokenOnServer(tokenRequest);
        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if(response.body() != null && response.body().getStatus()){
                    Log.d("FCM_TOKEN",response.body().getMessage());
                }else{
                    CustomToast.warning(MyFirebaseService.this,response.body().getMessage());

                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                CustomToast.error(MyFirebaseService.this,t.getMessage());
            }
        });
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        SharedPreferences prefs =
                getSharedPreferences("notification_prefs", MODE_PRIVATE);

        int count = prefs.getInt("unread_count", 0);
        count++;

        prefs.edit()
                .putInt("unread_count", count)
                .apply();

        Intent badgeIntent = new Intent("UPDATE_NOTIFICATION_BADGE");
        badgeIntent.putExtra("count", count);
        sendBroadcast(badgeIntent);

        if (remoteMessage.getNotification() != null) {

            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            Log.d("FCM", title + " " + body);

            showNotification(title, body);
        }
    }

    private void showNotification(String title, String message) {

        String channelId = "fcm_channel";

        Intent intent = new Intent(this, HomeActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT |
                        PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(
                            channelId,
                            "FCM Notifications",
                            NotificationManager.IMPORTANCE_HIGH
                    );

            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, builder.build());
    }
}
