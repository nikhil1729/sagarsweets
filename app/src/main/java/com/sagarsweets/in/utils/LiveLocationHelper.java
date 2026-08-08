package com.sagarsweets.in.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class LiveLocationHelper {

    private final Context context;
    private final FusedLocationProviderClient fusedClient;

    private LocationCallback callback;

    public interface LocationListener {
        void onLocationChanged(
                double lat,
                double lng,
                float accuracy
        );

        void onError(String error);
    }

    public LiveLocationHelper(Context context) {
        this.context = context;
        fusedClient =
                LocationServices.getFusedLocationProviderClient(context);
    }

    public void start(LocationListener listener) {

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {

            listener.onError(
                    "Location permission denied"
            );
            return;
        }

        LocationRequest request =
                new LocationRequest.Builder(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        2000 // update every 2 sec
                )
                        .setMinUpdateIntervalMillis(1000)
                        .setWaitForAccurateLocation(true)
                        .setMinUpdateDistanceMeters(3)
                        .build();

        callback = new LocationCallback() {

            @Override
            public void onLocationResult(
                    LocationResult result
            ) {

                if (result == null)
                    return;

                Location location =
                        result.getLastLocation();

                if (location != null) {

                    listener.onLocationChanged(
                            location.getLatitude(),
                            location.getLongitude(),
                            location.getAccuracy()
                    );
                }
            }
        };

        fusedClient.requestLocationUpdates(
                request,
                callback,
                Looper.getMainLooper()
        );
    }

    public void stop() {

        if (callback != null) {
            fusedClient.removeLocationUpdates(
                    callback
            );
        }
    }
}