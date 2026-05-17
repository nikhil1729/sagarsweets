package com.sagarsweets.in;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sagarsweets.in.Adapters.AddressAdapter;
import com.sagarsweets.in.Adapters.AddressProfileAdapter;
import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.Address;
import com.sagarsweets.in.ApiModel.AddressProfileModel;
import com.sagarsweets.in.ApiModel.ProfileRequest;
import com.sagarsweets.in.ApiModel.ProfileResponse;
import com.sagarsweets.in.ApiModel.ProfileUpdateRequest;
import com.sagarsweets.in.ApiModel.ProfileUpdateResponse;
import com.sagarsweets.in.ApiModel.WishListByLoggedInUserResponse;
import com.sagarsweets.in.BottomSheets.AddAddressBottomSheet;
import com.sagarsweets.in.Session.LoginSession;
import com.sagarsweets.in.utils.ButtonLoaderUtil;
import com.sagarsweets.in.utils.DeviceInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyProfileFragment extends Fragment {

    TextInputEditText etName,etDob,etEmail,etPhone,etCurrentPassword,etNewPassword,etConfirmPassword;
    TextView tvAge,tvProfileAge,tvSuccess,tvError;
    MaterialButton btnAddAddress;
    //Button btnLogout;
    MaterialButton btnSave,btnLogout;
    ProgressBar progressProfile,progressLogout;
    RecyclerView rvAddresses;
    SwitchCompat switchNotifications;
    private ShimmerFrameLayout shimmerLayout;
    private ScrollView contentLayout;
    LoginSession loginSession;
    ApiService apiService;
    String longitude,latitude;
    private static final int LOCATION_PERMISSION_CODE = 101;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean isLocationLoaded = false;
    private String city,district,state,pincode,area;
    private String device;
    private List<Address> addressList;
    AddressProfileAdapter adapter;
    public MyProfileFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        initView(view);
        getProfileFromServer();
        createDatePickerForDOB();
        addAddressForm();
        saveProfile();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        getLocation();
        getLogout();
        // Inflate the layout for this fragment
        return view;
    }

    private void getLogout() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonLoaderUtil.showLoading(btnLogout, progressLogout);
                loginSession.logout();
                Toast.makeText(getContext(),
                        "Successfully logout",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish();
            }
        });
    }

    private void getLocation() {

        if (ContextCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_CODE);

            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {

                    if (location != null) {

                        latitude = String.valueOf(location.getLatitude());
                        longitude = String.valueOf(location.getLongitude());
                        //String area = loca
                        Log.d("LOCATION", "Lat: " + latitude);
                        Log.d("LOCATION", "Lng: " + longitude);
                        Executors.newSingleThreadExecutor().execute(() -> {
                            getAddressFromLatLng(latitude, longitude);
                        });
                        isLocationLoaded = true;
                        checkAndCallApi();

                    } else {
                        Log.d("LOCATION", "Location is null");
                    }
                });
    }
    private void checkAndCallApi() {
        Log.d("API_DEBUG", "LocationLoaded: " + isLocationLoaded);
        if (isLocationLoaded) {
            Log.d("API_DEBUG", "Calling API with location + cart");
            Log.d("API_DEBUG", "Device: " + device);
            Log.d("API_DEBUG", "UserId: " + loginSession.getUserId());
            Log.d("API_DEBUG", "Lat: " + latitude);
            Log.d("API_DEBUG", "Lng: " + longitude);
            //gettingDetailsFromApi();
        }
    }
    private void getAddressFromLatLng(String latitude, String longitude) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {

            List<android.location.Address> addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);

            if (addresses != null && !addresses.isEmpty()) {

                android.location.Address address = addresses.get(0);

                city = address.getLocality();
                district = address.getSubAdminArea();
                state = address.getAdminArea();
                pincode = address.getPostalCode();
                area = address.getSubLocality();

                Log.d("ADDRESS", "City: " + city);
                Log.d("ADDRESS", "District: " + district);
                Log.d("ADDRESS", "State: " + state);
                Log.d("ADDRESS", "Pincode: " + pincode);
                Log.d("ADDRESS", "Area: " + area);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addAddressForm() {
        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAddressBottomSheet bottomSheet =
                        AddAddressBottomSheet.newInstance(
                                loginSession.getUserId(),
                                loginSession.getUserName(),
                                latitude,
                                longitude,
                                device,
                                city,district,state,pincode,area);
                bottomSheet.setAddressListener((address_id, address) -> {

                    //Log.d("ADDRESS_RESULT", address_id+" - "+address.getFullName());
                    if(address_id != null){
                        tvSuccess.setText("Address has been saved");
                        tvSuccess.setVisibility(View.VISIBLE);
                        tvError.setVisibility(View.GONE);
                        getProfileFromServer();
                    }else{
                        tvError.setText("Unable to save address details");
                        tvError.setVisibility(View.VISIBLE);
                        tvSuccess.setVisibility(View.GONE);
                    }

                    //bottomSheet.dismiss();
                });
                bottomSheet.show(getParentFragmentManager(), "AddAddressBottomSheet");
            }
        });
    }

    private void saveProfile() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dob = etDob.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String userId = loginSession.getUserId();
                String currentPassword = etCurrentPassword.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();
                String newsLater = switchNotifications.isChecked() ? "1" : "0";
                ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest(userId,
                        dob,
                        email,currentPassword,newPassword,
                        confirmPassword,newsLater,
                        DeviceInfo.getDeviceString(getContext()));
                Call<ProfileUpdateResponse> call =
                        apiService.getProfileUpdate(profileUpdateRequest);
                tvSuccess.setVisibility(View.GONE);
                tvError.setVisibility(View.GONE);
                ButtonLoaderUtil.showLoading(btnSave,progressProfile);
                call.enqueue(new Callback<ProfileUpdateResponse>() {
                    @Override
                    public void onResponse(Call<ProfileUpdateResponse> call, Response<ProfileUpdateResponse> response) {
                        ButtonLoaderUtil.hideLoading(btnSave,progressProfile,"Save Changes");
                        if (response.body() != null && response.body().getStatus()) {
                            tvSuccess.setText(response.body().getMessage());
                            tvSuccess.setVisibility(View.VISIBLE);
                            etCurrentPassword.setText("");
                            etNewPassword.setText("");
                            etConfirmPassword.setText("");
                            getProfileFromServer();
                        }else{
                            tvError.setText(response.body().getMessage());
                            tvError.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onFailure(Call<ProfileUpdateResponse> call, Throwable t) {
                        ButtonLoaderUtil.hideLoading(btnSave,progressProfile,"Save Changes");
                        tvError.setText(t.getMessage());
                        tvError.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    private void getProfileFromServer() {
        shimmerLayout.setVisibility(View.VISIBLE);
        contentLayout.setVisibility(View.GONE);


        ProfileRequest profileRequest = new ProfileRequest(
                loginSession.getUserId(),
                DeviceInfo.getDeviceString(getContext())
        );
        Call<ProfileResponse> call =
                apiService.getProfile(profileRequest);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.body() != null && response.body().getStatus()) {
                    shimmerLayout.setVisibility(View.GONE);
                    contentLayout.setVisibility(View.VISIBLE);
                    ProfileResponse profile = response.body();
                    etName.setText(profile.getFullName());
                    etDob.setText(profile.getDob());
                    etEmail.setText(profile.getEmail());
                    etPhone.setText(profile.getMobile());
                    tvAge.setText(profile.getAge());
                    if (profile.getNotification() == 1) {
                        switchNotifications.setChecked(true);
                    } else {
                        switchNotifications.setChecked(false);
                    }
                    tvProfileAge.setText("Member Since: " + profile.getProfileAge());
                    addressList = profile.getAddress();
                    setAddressOfUser(profile.getAddress());
                }else{
                    tvError.setText(response.body().getMessage());
                    tvError.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setAddressOfUser(List<Address> address) {

        rvAddresses.setLayoutManager(
                new LinearLayoutManager(
                        getContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                )
        );

        List<Address> addressList = address;



        adapter = new AddressProfileAdapter(getContext(), addressList);

        rvAddresses.setAdapter(adapter);
    }

    private void createDatePickerForDOB() {


        etDob.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    (view, selectedYear, selectedMonth, selectedDay) -> {

                        // Set DOB

                        String dob = selectedYear + "-"
                                + (selectedMonth + 1) + "-"
                                + selectedDay;

                        etDob.setText(dob);

                        // Calculate Age
                        Calendar dobCalendar = Calendar.getInstance();
                        dobCalendar.set(selectedYear, selectedMonth, selectedDay);

                        Calendar today = Calendar.getInstance();

                        int years = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR);
                        int months = today.get(Calendar.MONTH) - dobCalendar.get(Calendar.MONTH);
                        int days = today.get(Calendar.DAY_OF_MONTH) - dobCalendar.get(Calendar.DAY_OF_MONTH);

                        // Adjust negative days
                        if (days < 0) {
                            months--;

                            Calendar temp = (Calendar) today.clone();
                            temp.add(Calendar.MONTH, -1);

                            days += temp.getActualMaximum(Calendar.DAY_OF_MONTH);
                        }

                        // Adjust negative months
                        if (months < 0) {
                            years--;
                            months += 12;
                        }

                        // Show age
                        tvAge.setText("Age: "+years + " Years, "
                                + months + " Months, "
                                + days + " Days");
                    },
                    year, month, day
            );

            // Minimum age 14 years
            Calendar maxDate = Calendar.getInstance();
            maxDate.add(Calendar.YEAR, -14);

            datePickerDialog.getDatePicker()
                    .setMaxDate(maxDate.getTimeInMillis());

            datePickerDialog.show();
        });
    }

    private void initView(View view) {
        apiService = LoginRetrofitClient
                .getClient()
                .create(ApiService.class);
        loginSession = new LoginSession(getContext());
        device = DeviceInfo.getDeviceString(getContext());
        // tvSuccess,tvError
        tvSuccess = view.findViewById(R.id.tvSuccess);
        tvError = view.findViewById(R.id.tvError);
        etName = view.findViewById(R.id.etName);
        etDob = view.findViewById(R.id.etDob);
        etEmail = view.findViewById(R.id.etEmail);
        etPhone = view.findViewById(R.id.etPhone);
        etCurrentPassword = view.findViewById(R.id.etCurrentPassword);
        etNewPassword = view.findViewById(R.id.etNewPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        tvAge = view.findViewById(R.id.tvAge);
        tvProfileAge = view.findViewById(R.id.tvProfileAge);
        btnAddAddress = view.findViewById(R.id.btnAddAddress);
        btnSave = view.findViewById(R.id.btnSave);
        btnLogout = view.findViewById(R.id.btnLogout);
        progressProfile = view.findViewById(R.id.progressProfile);
        progressLogout = view.findViewById(R.id.progressLogout);
        switchNotifications = view.findViewById(R.id.switchNotifications);
        rvAddresses = view.findViewById(R.id.rvAddresses);
        // int notificationValue = switchNotifications.isChecked() ? 1 : 0;
        shimmerLayout = view.findViewById(R.id.shimmerView);
        contentLayout = view.findViewById(R.id.mainContent);
    }
}