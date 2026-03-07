package com.sagarsweets.in;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.sagarsweets.in.Adapters.PickupPreviewAdapter;
import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.CheckoutRequest;
import com.sagarsweets.in.ApiModel.CheckoutResponse;
import com.sagarsweets.in.ApiModel.CouponDetails;
import com.sagarsweets.in.ApiModel.GetUserAddressResponse;
import com.sagarsweets.in.ApiModel.PickStoreAddress;
import com.sagarsweets.in.ApiModel.UserAddressRequest;
import com.sagarsweets.in.ApiModel.UserDefaultAddress;
import com.sagarsweets.in.BottomSheets.AddAddressBottomSheet;
import com.sagarsweets.in.BottomSheets.SelectAddressBottomSheet;
import com.sagarsweets.in.RoomDatabase.AppDatabase;
import com.sagarsweets.in.Session.CartItem;
import com.sagarsweets.in.Session.LoginSession;
import com.sagarsweets.in.Session.PincodeSession;
import com.sagarsweets.in.utils.AddressFormatter;
import com.sagarsweets.in.utils.ButtonLoaderUtil;
import com.sagarsweets.in.utils.DeviceInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CheckoutFragment extends Fragment {
    TextView btnAddAddress,btnChangeAddress,txtCheckoutAddress,txtPickupAddress;
    ProgressBar progressChangeAddress;
    TextView txtDeliveryDate;
    RadioButton radioCOD;
    RadioButton radioOnline;

    LinearLayout layoutCOD;
    LinearLayout layoutOnline;
    RadioGroup radioOrderType;
    RadioButton radioDelivery, radioPickup;
    CardView cardDeliveryAddress, cardPickupInfo;
    RecyclerView rvOrderItems;
    List<CartItem> currentCartList;
    PincodeSession pincodeSession;
    LoginSession loginSession;
    AppDatabase db;
    String device;
    String longitude,latitude;
    ApiService apiService;
    String currentTime;


    String timeZone,deliveryCharge;
    UserDefaultAddress userDefaultAddress;
    PickStoreAddress pickStoreAddress;
    CouponDetails couponDetails;
    private static final int LOCATION_PERMISSION_CODE = 101;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean isCartLoaded = false;
    private boolean isLocationLoaded = false;
    TextView txtServerTime,txtTimeZone;
    private android.os.Handler handler = new android.os.Handler();
    private Runnable clockRunnable;
    Chip chipMorning,chipAfternoon,chipEvening;
    Chip chipToday, chipTomorrow, chipDayAfter, chipCustom;
    ChipGroup chipGroupDate;

    String selectedDate,selectedAddress;
    String serverDateTime;// server time
    TextView txtMorningCountdown, txtAfternoonCountdown, txtEveningCountdown;

    String receivedCoupon;
    TextView txtAppliedCouponCode,txtCouponDescription;
    CardView cardCouponApplied;

    TextView txtSubtotal,txtDiscount,txtDelivery,txtGrandTotal;

    String city;
    String district;
    String state;
    String pincode;
    String area;

    public CheckoutFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            receivedCoupon = getArguments().getString("coupon_code");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        initViews(view);
        initSessions();
        setupDateChips();   // ✅ ADD THIS
        getSelectedDate();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        getLocation();
        getCartItem();

        radioCodOnlineSetting();
        deliveryPickup();
        addressRelatedFunctions();

        // Inflate the layout for this fragment
        return view;
    }

    private void addressRelatedFunctions() {
        btnChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonLoaderUtil.showLoadingText(btnChangeAddress,progressChangeAddress);
                UserAddressRequest userAddressRequest = new UserAddressRequest(loginSession.getUserId());
                apiService.getUserAddress(userAddressRequest).enqueue(new Callback<GetUserAddressResponse>() {
                    @Override
                    public void onResponse(Call<GetUserAddressResponse> call, Response<GetUserAddressResponse> response) {
                        ButtonLoaderUtil.hideLoadingText(btnChangeAddress,progressChangeAddress,"Change");
                        if(response.body() != null){
                            if(response.body().isStatus()){
                                List<com.sagarsweets.in.ApiModel.Address> addressResponseList = response.body().getAddress();
                                Log.d("AddressList","size-"+addressResponseList.size());
                                SelectAddressBottomSheet sheet =
                                        new SelectAddressBottomSheet(addressResponseList, address -> {

                                            selectedAddress = String.valueOf(address.getAddressId());
                                            txtCheckoutAddress.setText(
                                                    AddressFormatter.formatDeliveryAddressSingle(address)
                                            );

                                        });

                                sheet.show(getParentFragmentManager(),"addressSheet");
                                //Log.d("USERADDRESS",addresses.getFirst().getFullAddress());
                            }else{
                                // show error
                                Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GetUserAddressResponse> call, Throwable t) {
                        ButtonLoaderUtil.hideLoadingText(btnChangeAddress,progressChangeAddress,"Change");
                    }
                });
            }
        });

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

                    // Update variables and address
                    //set here address_id in future
                    selectedAddress = address_id;
                    txtCheckoutAddress.setText(address);
                    Log.d("ADDRESS_RESULT", address_id+" - "+address);

                });
                bottomSheet.show(getParentFragmentManager(), "AddAddressBottomSheet");
            }
        });
    }

    private void updateSlotCountdown(String serverDateTime) {

        if (!isToday(selectedDate)) {
            txtMorningCountdown.setText("");
            txtAfternoonCountdown.setText("");
            txtEveningCountdown.setText("");
            return;
        }

        try {

            SimpleDateFormat sdf = new SimpleDateFormat(
                    "d MMMM yyyy, EEEE | hh:mm:ss a",
                    Locale.ENGLISH
            );

            sdf.setTimeZone(TimeZone.getTimeZone(timeZone));

            Date now = sdf.parse(serverDateTime);
            Calendar currentCal = Calendar.getInstance();
            currentCal.setTime(now);

            updateSingleSlotCountdown(currentCal, 12, txtMorningCountdown);
            updateSingleSlotCountdown(currentCal, 16, txtAfternoonCountdown);
            updateSingleSlotCountdown(currentCal, 20, txtEveningCountdown);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void updateSingleSlotCountdown(Calendar currentCal,
                                           int slotEndHour,
                                           TextView textView) {

        Calendar endCal = (Calendar) currentCal.clone();
        endCal.set(Calendar.HOUR_OF_DAY, slotEndHour);
        endCal.set(Calendar.MINUTE, 0);
        endCal.set(Calendar.SECOND, 0);

        long diff = endCal.getTimeInMillis() - currentCal.getTimeInMillis();

        if (diff <= 0) {
            textView.setText("Closed");
            // ✅ Set Green Color When Closed
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.slot_closed_green));
            return;
        }

        long seconds = diff / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long sec = seconds % 60;

        if (hours > 0) {
            textView.setText("Closes in " + hours + "h " + minutes + "m");
        } else {
            textView.setText("Closes in " + minutes + "m " + sec + "s");
        }
        // 🔴 Active countdown color (red)
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.slot_active_red));
    }
    private void getSelectedDate() {
        chipGroupDate.setOnCheckedStateChangeListener((group, checkedIds) -> {

            if (!checkedIds.isEmpty()) {
                int id = checkedIds.get(0);
                com.google.android.material.chip.Chip chip = group.findViewById(id);

                Object tag = chip.getTag();

                if (tag != null) {
                    selectedDate = tag.toString();
                } else {
                    return; // avoid crash
                }

                // Important: update time slots based on selected date
                Log.d("displayDate","selectedDate-"+selectedDate);
                if (isToday(selectedDate)) {
                    updateTimeSlots(serverDateTime);
                } else {
                    enableAllSlots();
                    chipMorning.setChecked(false);
                    chipAfternoon.setChecked(false);
                    chipEvening.setChecked(false);
                }
            }
        });
    }
    private void enableAllSlots() {

        chipMorning.setEnabled(true);
        chipMorning.setAlpha(1f);

        chipAfternoon.setEnabled(true);
        chipAfternoon.setAlpha(1f);

        chipEvening.setEnabled(true);
        chipEvening.setAlpha(1f);


    }
    private boolean isToday(String date) {
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd",
                        java.util.Locale.getDefault());
        String today = sdf.format(new java.util.Date());
        Log.d("TODAY",today+"-"+date);
        return today.equals(date);
    }

    private void setupDateChips() {

        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd",
                        java.util.Locale.getDefault());

        java.util.Calendar cal = java.util.Calendar.getInstance();

        // Today
        String today = sdf.format(cal.getTime());
        Log.d("displayDate",getDisplayDate(cal));
        chipToday.setText("Today-" + getDisplayDate(cal));
        chipToday.setTag(today);

        // Tomorrow
        cal.add(java.util.Calendar.DATE, 1);
        String tomorrow = sdf.format(cal.getTime());
        chipTomorrow.setText("Tomorrow-" + getDisplayDate(cal));
        chipTomorrow.setTag(tomorrow);

        // Day After
        cal.add(java.util.Calendar.DATE, 1);
        String dayAfter = sdf.format(cal.getTime());
        chipDayAfter.setText("Day After-" + getDisplayDate(cal));
        chipDayAfter.setTag(dayAfter);

        chipToday.setChecked(true);
        selectedDate = today;
    }
    private String getDisplayDate(java.util.Calendar cal) {
        java.text.SimpleDateFormat displayFormat =
                new java.text.SimpleDateFormat("d MMM",
                        java.util.Locale.getDefault());
        return displayFormat.format(cal.getTime());
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

    private void getAddressFromLatLng(String latitude, String longitude) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {

            List<android.location.Address> addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);

            if (addresses != null && !addresses.isEmpty()) {

                Address address = addresses.get(0);

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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_CODE) {

            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getLocation();

            } else {

                Log.d("LOCATION", "Permission Denied");
            }
        }
    }
    private void gettingDetailsFromApi() {
        if(loginSession.isLoggedIn()){
            CheckoutRequest checkoutRequest = new CheckoutRequest(device,currentCartList,
                    loginSession.getUserId(),longitude,latitude,receivedCoupon);
            apiService.checkoutIndex(checkoutRequest).enqueue(new Callback<CheckoutResponse>() {
                @Override
                public void onResponse(Call<CheckoutResponse> call, Response<CheckoutResponse> response) {
                    if (response.isSuccessful()) {

                        CheckoutResponse apiResponse = response.body();
                        if(apiResponse.isStatus()){
                            currentTime = apiResponse.getResult().getCurrentTime();
                            timeZone = apiResponse.getResult().getServerTimeZone();
                            deliveryCharge = apiResponse.getResult().getDeliveryCharge();
                            userDefaultAddress = apiResponse.getResult().getUserDefaultAddress();
                            pickStoreAddress = apiResponse.getResult().getPickStoreAddress();
                            couponDetails = apiResponse.getResult().getCouponDetails();

                            startLiveClock();
                            txtTimeZone.setText(timeZone);
                            setUserDefaultAddress();
                            setPickupAddress();
                            setcouponDetails();

                            calculateAmount();// always in last
                        }else{
                            Log.d("checkout",response.message());
                        }

                    }else{
                        Log.d("checkout",response.message());
                    }
                }

                @Override
                public void onFailure(Call<CheckoutResponse> call, Throwable t) {
                    Log.d("checkout",t.getMessage());
                    Log.e("checkout_error", "Error", t);
                }
            });
        }

    }

    private void calculateAmount() {
        Double subTotal = 0.0;

        for (CartItem item : currentCartList) {
            subTotal += item.getPrice() * item.getQuantity();
        }

        //double delivery = subTotal > 500 ? 0 : 40;   // Free delivery above 500
        double delivery = Double.parseDouble(deliveryCharge);
        double discount = 0.0;
        if(couponDetails.isStatus()){
            discount = Double.parseDouble(String.valueOf(couponDetails.getDiscount()));
        }


        double grandTotal = subTotal + delivery - discount;

        txtSubtotal.setText("₹" + subTotal);
        txtDelivery.setText("₹" + delivery);
        txtDiscount.setText("- ₹" + discount);
        txtGrandTotal.setText("₹" + grandTotal);
    }

    private void setcouponDetails() {
        if(couponDetails.isStatus()){
            txtAppliedCouponCode.setText(couponDetails.getMessage());
            txtCouponDescription.setText("You saved ₹"+couponDetails.getDiscount()+" on this order");
        }else{
            cardCouponApplied.setVisibility(View.GONE);
        }
    }

    private void updateTimeSlots(String serverDateTime) {
        // 🔥 IMPORTANT FIX
        if (!isToday(selectedDate)) {
            enableAllSlots();
            return;
        }
        try {
            Log.d("displayDate","serverTime"+serverDateTime);
            int currentHour = getCurrentHourFromServer(serverDateTime);

            if (currentHour == -1) return;

            disableChipIfPassed(currentHour, chipMorning, 12);
            disableChipIfPassed(currentHour, chipAfternoon, 16);
            disableChipIfPassed(currentHour, chipEvening, 20);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private int getCurrentHourFromServer(String serverDateTime) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(
                    "d MMMM yyyy, EEEE | hh:mm:ss a",
                    Locale.ENGLISH   // IMPORTANT
            );

            sdf.setTimeZone(TimeZone.getTimeZone(timeZone));

            Date date = sdf.parse(serverDateTime);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            return cal.get(Calendar.HOUR_OF_DAY); // 0–23 format

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }
    private void disableChipIfPassed(int currentHour,
                                     com.google.android.material.chip.Chip chip,
                                     int slotEndHour) {
        Log.d("displayDate",currentHour+"-"+slotEndHour);
        if (currentHour >= slotEndHour) {

            chip.setEnabled(false);
            chip.setAlpha(0.4f);   // faded look
            chip.setChecked(false);

        } else {

            chip.setEnabled(true);
            chip.setAlpha(1f);
        }
    }
    private void startLiveClock() {

        clockRunnable = new Runnable() {
            @Override
            public void run() {

                java.text.SimpleDateFormat sdf =
                        new java.text.SimpleDateFormat("d MMMM yyyy, EEEE | hh:mm:ss a",
                                java.util.Locale.getDefault());

                sdf.setTimeZone(java.util.TimeZone.getTimeZone(timeZone));

                String current = sdf.format(new java.util.Date());

                txtServerTime.setText(current);
                updateTimeSlots(current);
                updateSlotCountdown(current);
                handler.postDelayed(this, 1000);
            }
        };

        handler.post(clockRunnable);
    }

    private void setPickupAddress() {
        txtPickupAddress.setText(
                AddressFormatter.formatPickupAddress(pickStoreAddress)
        );
    }

    private void setUserDefaultAddress() {
        if(userDefaultAddress.getStatus() != null){
            if(userDefaultAddress.getStatus() == false){
                txtCheckoutAddress.setText(userDefaultAddress.getMessage());
            }else{
                selectedAddress = String.valueOf(userDefaultAddress.getAddress().getAddressId());
                txtCheckoutAddress.setText(
                        AddressFormatter.formatDeliveryAddress(userDefaultAddress)
                );
            }
        }else{
            txtCheckoutAddress.setText(
                    AddressFormatter.formatDeliveryAddress(userDefaultAddress)
            );
        }


    }

    private void getCartItem() {
        if(loginSession.isLoggedIn()){
            String userId = loginSession.getUserId();
            rvOrderItems.setLayoutManager(new LinearLayoutManager(getContext()));
            rvOrderItems.setNestedScrollingEnabled(false);
            Executors.newSingleThreadExecutor().execute(() -> {

                currentCartList = db.cartDao()
                        .getCartItemsList(Integer.parseInt(userId));
                requireActivity().runOnUiThread(() -> {
                    PickupPreviewAdapter adapter =
                            new PickupPreviewAdapter(currentCartList);
                    rvOrderItems.setAdapter(adapter);
                    // here i am getting details from api after cartlist loaded

                    isCartLoaded = true;
                    checkAndCallApi();

                });
            });


        }
    }

    private void checkAndCallApi() {
        Log.d("API_DEBUG", "CartLoaded: " + isCartLoaded);
        Log.d("API_DEBUG", "LocationLoaded: " + isLocationLoaded);
        if (isCartLoaded && isLocationLoaded) {
            Log.d("API_DEBUG", "Calling API with location + cart");
            Log.d("API_DEBUG", "Device: " + device);
            Log.d("API_DEBUG", "UserId: " + loginSession.getUserId());
            Log.d("API_DEBUG", "Lat: " + latitude);
            Log.d("API_DEBUG", "Lng: " + longitude);
            Log.d("API_DEBUG", "Cart Size: " + currentCartList.size());
            gettingDetailsFromApi();
        }
    }
    private void initSessions() {
        pincodeSession = new PincodeSession(getContext());
        loginSession = new LoginSession(getContext());
    }



    private void deliveryPickup() {
        radioOrderType.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.radioDelivery) {

                cardDeliveryAddress.setVisibility(View.VISIBLE);
                cardPickupInfo.setVisibility(View.GONE);

                //delivery = 40.0; // delivery charge
                //calculateTotal(currentCartList);

            } else if (checkedId == R.id.radioPickup) {

                cardDeliveryAddress.setVisibility(View.GONE);
                cardPickupInfo.setVisibility(View.VISIBLE);

                //delivery = 0.0; // no delivery charge
                //calculateTotal(currentCartList);
            }
        });
    }

    private void radioCodOnlineSetting() {
        layoutCOD.setOnClickListener(v -> {
            radioCOD.setChecked(true);
            radioOnline.setChecked(false);
        });

        layoutOnline.setOnClickListener(v -> {
            radioOnline.setChecked(true);
            radioCOD.setChecked(false);
        });

        radioCOD.setOnClickListener(v -> {
            radioOnline.setChecked(false);
        });

        radioOnline.setOnClickListener(v -> {
            radioCOD.setChecked(false);
        });
    }



    private void initViews(View view) {
        //txtDeliveryDate = view.findViewById(R.id.txtDeliveryDate);
        radioCOD = view.findViewById(R.id.radioCOD);
        radioOnline = view.findViewById(R.id.radioOnline);

        layoutCOD = view.findViewById(R.id.layoutCOD);
        layoutOnline = view.findViewById(R.id.layoutOnline);

        radioOrderType = view.findViewById(R.id.radioOrderType);
        radioDelivery = view.findViewById(R.id.radioDelivery);
        radioPickup = view.findViewById(R.id.radioPickup);
        cardDeliveryAddress = view.findViewById(R.id.cardDeliveryAddress);
        cardPickupInfo = view.findViewById(R.id.cardPickupInfo);

        rvOrderItems = view.findViewById(R.id.rvOrderItems);
        txtTimeZone = view.findViewById(R.id.txtTimeZone);
        txtServerTime = view.findViewById(R.id.txtServerTime);
        txtCheckoutAddress = view.findViewById(R.id.txtCheckoutAddress);
        txtPickupAddress = view.findViewById(R.id.txtPickupAddress);
        chipMorning = view.findViewById(R.id.chipMorning);
        chipAfternoon = view.findViewById(R.id.chipAfternoon);
        chipEvening = view.findViewById(R.id.chipEvening);
        db = AppDatabase.getInstance(getContext());
        device = DeviceInfo.getDeviceString(getContext());
        apiService = LoginRetrofitClient.getClient().create(ApiService.class);

        chipToday = view.findViewById(R.id.chipToday);
        chipTomorrow = view.findViewById(R.id.chipTomorrow);
        chipDayAfter = view.findViewById(R.id.chipDayAfter);
        chipCustom = view.findViewById(R.id.chipCustom);
        chipGroupDate = view.findViewById(R.id.chipGroupDate);

        txtMorningCountdown = view.findViewById(R.id.txtMorningCountdown);
        txtAfternoonCountdown = view.findViewById(R.id.txtAfternoonCountdown);
        txtEveningCountdown = view.findViewById(R.id.txtEveningCountdown);

        txtAppliedCouponCode = view.findViewById(R.id.txtAppliedCouponCode);
        txtCouponDescription = view.findViewById(R.id.txtCouponDescription);
        cardCouponApplied = view.findViewById(R.id.cardCouponApplied);

        txtGrandTotal = view.findViewById(R.id.txtGrandTotal);
        txtDelivery = view.findViewById(R.id.txtDelivery);
        txtDiscount = view.findViewById(R.id.txtDiscount);
        txtSubtotal = view.findViewById(R.id.txtSubtotal);

        btnAddAddress = view.findViewById(R.id.btnAddAddress);
        btnChangeAddress = view.findViewById(R.id.btnChangeAddress);
        progressChangeAddress = view.findViewById(R.id.progressChangeAddress);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (handler != null && clockRunnable != null) {
            handler.removeCallbacks(clockRunnable);
        }
    }
}