package com.sagarsweets.in;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.PincodeData;
import com.sagarsweets.in.ApiModel.PincodeRequest;
import com.sagarsweets.in.ApiModel.PincodeResponse;
import com.sagarsweets.in.Session.LoginSession;
import com.sagarsweets.in.Session.PincodeSession;
import com.sagarsweets.in.utils.ButtonLoaderUtil;
import com.sagarsweets.in.utils.CustomToast;
import com.sagarsweets.in.utils.DeviceInfo;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_LOCATION = 101;
    private static final int LOCATION_SETTINGS_REQUEST = 102;

    Button btnGetStarted;
    TextView txtSkip;

    FusedLocationProviderClient fusedLocationClient;
    Address address;
    PincodeSession pincodeSession;
    LoginSession loginSession;
    ProgressBar progressGetStarted;
    public void init(){

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        btnGetStarted = findViewById(R.id.btnGetStarted);
        progressGetStarted = findViewById(R.id.progressGetStarted);
        txtSkip = findViewById(R.id.txtSkip);
        ButtonLoaderUtil.showLoading(btnGetStarted, progressGetStarted);
        //btnGetStarted.setVisibility(View.GONE);
        txtSkip.setVisibility(View.GONE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();
        pincodeSession = new PincodeSession(this);
        loginSession = new LoginSession(this);
        btnGetStarted.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        checkLocationStatusAndPermission();

    }

    // Check if location is ON and permissions are granted
    private void checkLocationStatusAndPermission() {
        if (!isLocationEnabled(this)) {
            openLocationSettings();
            //openPincodeBottomSheet();
        } else {
            checkLocationPermission();
        }
    }

    // Check runtime permission
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_LOCATION
            );
        } else {
            getFreshLocation();
        }
    }

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getFreshLocation();
            } else {
                Log.d("LOCATIONNIKHIL1", "Permission denied");
                openPincodeBottomSheet();

            }
        }
    }

    // Check if device location is enabled
    public boolean isLocationEnabled(Context context) {
        LocationManager locationManager =
                (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // Open device location settings
    private void openLocationSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, LOCATION_SETTINGS_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOCATION_SETTINGS_REQUEST) {
            if (isLocationEnabled(this)) {
                checkLocationPermission();
            } else {

                Log.d("LOCATIONNIKHIL", "User did not enable location");
                // Inside your Activity
                this.recreate();

            }
        }
    }

    // Fetch fresh location using FusedLocationProvider
    private void getFreshLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null && !locationResult.getLocations().isEmpty()) {
                    Location location = locationResult.getLastLocation();
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();

                    Log.d("LOCATIONNIKHIL_OK", "Lat: " + lat + ", Lng: " + lng);

                    // Get address details
                    getAddressFromLocation(lat, lng);

                    fusedLocationClient.removeLocationUpdates(this);
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
        );
    }

    // Convert latitude & longitude to area, city, pincode
    private void getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                address = addresses.get(0);

                String area = address.getSubLocality(); // neighborhood / area
                String city = address.getLocality();
                String state = address.getAdminArea();
                String country = address.getCountryName();
                String pincode = address.getPostalCode();

                Log.d("LOCATIONNIKHIL_ADDRESS", "Area: " + area + ", City: " + city +
                        ", State: " + state + ", Country: " + country +
                        ", Pincode: " + pincode);
                if(pincode == null){
                    Log.d("LOCATIONNIKHIL2","else");
                    openPincodeBottomSheet();
                }else{
                    if(pincodeSession.hasPincode()){
                        // then match pincode with session
                        String sesPincode = pincodeSession.getPincode();
                        
                        if(pincode.equals(sesPincode)){
                            // if equal then check login session and open home activity
                            if(loginSession.isLoggedIn()){
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                // if equal then button make visible
                                ButtonLoaderUtil.hideLoading(btnGetStarted, progressGetStarted,
                                        "Get Started");
                            }

                        }else{
                            // else then open bottom dailog
                            Log.d("LOCATIONNIKHIL3","else");
                            openPincodeBottomSheet();
                        }
                    }else{
                        Log.d("LOCATIONNIKHIL4","else");
                        openPincodeBottomSheet();
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void openPincodeBottomSheet() {

        BottomSheetDialog bottomSheetDialog =
                new BottomSheetDialog(MainActivity.this);

        View view = getLayoutInflater()
                .inflate(R.layout.bottomsheet_pincode, null);

        EditText etPincode = view.findViewById(R.id.etPincode);
        TextView tvPincodeError = view.findViewById(R.id.tvPincodeError);
        Button btnConfirm = view.findViewById(R.id.btnConfirmPincode);
        String pincodeByGeo = address.getPostalCode();
        etPincode.setText(pincodeByGeo);
        etPincode.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvPincodeError.setVisibility(View.GONE);
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        btnConfirm.setOnClickListener(v -> {
            String pincode = etPincode.getText().toString().trim();

            if (pincode.isEmpty()) {
                tvPincodeError.setText("Pincode is required");
                tvPincodeError.setVisibility(View.VISIBLE);
                return;
            }
            if (pincode.length() != 6) {
                tvPincodeError.setText("Please enter a valid 6-digit pincode");
                tvPincodeError.setVisibility(View.VISIBLE);
                return;
            }
            String user_id ="";
            String device = DeviceInfo.getDeviceString(this);
            String lot = String.valueOf(address.getLatitude());
            String lon = String.valueOf(address.getLongitude());
            Log.d("latNikhil",lot+" "+lon);
            PincodeRequest pincodeRequest = new PincodeRequest(pincode,user_id,device,lon,lot);
            ApiService apiService  = LoginRetrofitClient
                    .getClient()
                    .create(ApiService.class);
            apiService.getPincodeStatus(pincodeRequest).enqueue(new Callback<PincodeResponse>() {
                @Override
                public void onResponse(Call<PincodeResponse> call, Response<PincodeResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        PincodeResponse pincodeResponse = response.body();
                        if (pincodeResponse.isStatus()
                                && pincodeResponse.getData() != null
                                && !pincodeResponse.getData().isEmpty()) {
                            // ✅ SUCCESS
                            PincodeData data = pincodeResponse.getData().get(0);
                            PincodeSession sessionManager = new PincodeSession(getApplicationContext());
                            sessionManager.savePincode(
                                    data.getPincode(),
                                    data.getCity_name(),
                                    data.getDistric_name(),
                                    data.getState()
                            );
                            //tvPincodeError.setVisibility(View.GONE);
                            // ✅ proceed with API / delivery check
                            ButtonLoaderUtil.hideLoading(btnGetStarted, progressGetStarted,
                                    "Get Started");
                            Log.d("PINCODE", "User entered pincode: " + pincode);
                            // TODO: Save pincode or call API
                            bottomSheetDialog.dismiss();
                        } else {
                            // ❌ Status false OR empty data
                            tvPincodeError.setText(pincodeResponse.getMessage());
                            tvPincodeError.setVisibility(View.VISIBLE);
                            //showError("Delivery not available for this pincode");
                        }
                    }else {
                        tvPincodeError.setText("Something went wrong.");
                        tvPincodeError.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<PincodeResponse> call, Throwable t) {
                    CustomToast.error(MainActivity.this,t.getMessage());
                }
            });



        });

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();
    }



}
