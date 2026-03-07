package com.sagarsweets.in.BottomSheets;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.AddAddressRequest;
import com.sagarsweets.in.ApiModel.AddAddressResponse;
import com.sagarsweets.in.ApiModel.Address;
import com.sagarsweets.in.ApiModel.UserDefaultAddress;
import com.sagarsweets.in.R;
import com.sagarsweets.in.utils.AddressFormatter;
import com.sagarsweets.in.utils.ButtonLoaderUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddressBottomSheet extends BottomSheetDialogFragment {

    TextInputEditText etName, etMobile, etAddress,etLandMark,etPincode,etArea,etDistrict,etState;
    MaterialButton btnSave;
    ChipGroup chipGroup;
    Chip chipHome, chipOffice, chipOther;
    ProgressBar progressSave;

    String latitude, longitude;
    String device,name,userId;
    MaterialCardView errorBox;
    TextView txtErrorMessage;
    String city;
    String district;
    String state;
    String pincode;
    String area;
    private AddressListener addressListener;
    public interface AddressListener {
        void onAddressSaved(String address_id, String address);

    }
    public void setAddressListener(AddressListener listener){
        this.addressListener = listener;
    }
    public static AddAddressBottomSheet newInstance(String userId,String name, String lat, String lon,
                                                    String device,String city,String district,String state,String pincode,String area) {

        AddAddressBottomSheet fragment = new AddAddressBottomSheet();

        Bundle args = new Bundle();
        args.putString("lat", lat);
        args.putString("lon", lon);
        args.putString("device", device);
        args.putString("name", name);
        args.putString("userId", userId);
        args.putString("city", city);
        args.putString("distric",district);
        args.putString("state",state);
        args.putString("pincode",pincode);
        args.putString("area",area);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            latitude = getArguments().getString("lat");
            longitude = getArguments().getString("lon");
            device = getArguments().getString("device");
            name = getArguments().getString("name");
            userId = getArguments().getString("userId");
            city = getArguments().getString("city");
            district = getArguments().getString("distric");
            pincode = getArguments().getString("pincode");
            area = getArguments().getString("area");
            state = getArguments().getString("state");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_add_address, container, false);

        etName = view.findViewById(R.id.etName);
        etMobile = view.findViewById(R.id.etMobile);
        etAddress = view.findViewById(R.id.etAddress);
        etLandMark = view.findViewById(R.id.etLandmark);
        etArea = view.findViewById(R.id.etArea);
        etPincode = view.findViewById(R.id.etPincode);
        etDistrict = view.findViewById(R.id.etDistrict);
        etState = view.findViewById(R.id.etState);

        chipGroup = view.findViewById(R.id.chipGroupAddressType);
        chipHome = view.findViewById(R.id.chipHome);
        chipOffice = view.findViewById(R.id.chipOffice);
        chipOther = view.findViewById(R.id.chipOther);

        errorBox = view.findViewById(R.id.errorBox);
        txtErrorMessage = view.findViewById(R.id.txtErrorMessage);

        progressSave = view.findViewById(R.id.progressSave);
        btnSave = view.findViewById(R.id.btnSaveAddress);

        etName.setText(name);
        etPincode.setText(pincode);
        etDistrict.setText(district);
        etState.setText(state);
        etArea.setText(area);
        btnSave.setOnClickListener(v -> saveAddress());
        return view;
    }

    private void saveAddress() {

        String name = etName.getText() != null ? etName.getText().toString().trim() : "";
        String mobile = etMobile.getText() != null ? etMobile.getText().toString().trim() : "";
        String address = etAddress.getText() != null ? etAddress.getText().toString().trim() : "";
        String landMark = etLandMark.getText() != null ? etLandMark.getText().toString().trim() : "";
        String pincode  = etPincode.getText() != null ? etPincode.getText().toString().trim() : "";
        String area = etArea.getText() != null ? etArea.getText().toString().trim() : "";
        String district = etDistrict.getText() != null ? etDistrict.getText().toString().trim() : "";
        String state = etState.getText() != null ? etState.getText().toString().trim() : "";

        // -------- NAME --------
        if (name.isEmpty()) {
            etName.setError("Full name is required");
            etName.requestFocus();
            return;
        }

        if (name.length() < 3) {
            etName.setError("Name must be at least 3 characters");
            etName.requestFocus();
            return;
        }

        // -------- MOBILE --------
        if (mobile.isEmpty()) {
            etMobile.setError("Mobile number required");
            etMobile.requestFocus();
            return;
        }

        if (!mobile.matches("^[6-9]\\d{9}$")) {
            etMobile.setError("Enter valid 10 digit mobile number");
            etMobile.requestFocus();
            return;
        }

        // -------- ADDRESS --------
        if (address.isEmpty()) {
            etAddress.setError("Address required");
            etAddress.requestFocus();
            return;
        }

        if (address.length() < 10) {
            etAddress.setError("Address too short");
            etAddress.requestFocus();
            return;
        }

        // -------- PINCODE --------
        if (pincode.isEmpty()) {
            etPincode.setError("Pincode required");
            etPincode.requestFocus();
            return;
        }

        if (!pincode.matches("^\\d{6}$")) {
            etPincode.setError("Enter valid 6 digit pincode");
            etPincode.requestFocus();
            return;
        }

        // -------- AREA --------
        if (area.isEmpty()) {
            etArea.setError("Area required");
            etArea.requestFocus();
            return;
        }

        // -------- DISTRICT --------
        if (district.isEmpty()) {
            etDistrict.setError("District required");
            etDistrict.requestFocus();
            return;
        }

        // -------- STATE --------
        if (state.isEmpty()) {
            etState.setError("State required");
            etState.requestFocus();
            return;
        }

        // -------- ADDRESS TYPE --------
        String addressType = "Home";
        int selectedId = chipGroup.getCheckedChipId();

        if (selectedId == R.id.chipHome) {
            addressType = "Home";
        } else if (selectedId == R.id.chipOffice) {
            addressType = "Office";
        } else if (selectedId == R.id.chipOther) {
            addressType = "Other";
        }

        // -------- ALL VALID → CALL API --------
        ButtonLoaderUtil.showLoading(btnSave,progressSave);
        //btnSave.setEnabled(false);  // prevent double click

        AddAddressRequest addAddressRequest = new AddAddressRequest(device,latitude,longitude,
                userId,pincode,name,"na@na.in",address,mobile,area,district,state,landMark,addressType);
        ApiService apiService = LoginRetrofitClient.getClient().create(ApiService.class);
        apiService.addAddressUser(addAddressRequest).enqueue(new Callback<AddAddressResponse>() {
            @Override
            public void onResponse(Call<AddAddressResponse> call, Response<AddAddressResponse> response) {
                ButtonLoaderUtil.hideLoading(btnSave,progressSave,"Save Address");
                if(response.body() != null){

                    if(response.body().getStatus()){

                        if(addressListener != null){
                            UserDefaultAddress userDefaultAddress = response.body().getAddress();
                            addressListener.onAddressSaved(
                                    response.body().getAddressId(),
                                    AddressFormatter.formatDeliveryAddress(userDefaultAddress)
                            );
                        }

                        dismiss(); // close bottom sheet
                    }else{
                        errorBox.setVisibility(View.VISIBLE);
                        txtErrorMessage.setText(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<AddAddressResponse> call, Throwable t) {
                ButtonLoaderUtil.hideLoading(btnSave,progressSave,"Save Address");
                Log.d("Error",t.getMessage());
            }
        });
        // Example API call
        // sendToServer(name, mobile, address, landMark, pincode, area, district, state, addressType);

        Toast.makeText(getContext(), "All fields valid ✅", Toast.LENGTH_SHORT).show();

        //btnSave.setEnabled(true);
    }

}
