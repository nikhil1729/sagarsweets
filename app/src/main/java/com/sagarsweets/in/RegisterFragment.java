package com.sagarsweets.in;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sagarsweets.in.ApiControllers.OtpRetrofitClient;
import com.sagarsweets.in.ApiControllers.RegisterRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.OtpResponse;
import com.sagarsweets.in.ApiModel.RegisterUserRequest;
import com.sagarsweets.in.utils.ButtonLoaderUtil;
import com.sagarsweets.in.utils.DeviceInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    EditText etFirstName,etLastName,etMobile,etOtp,etPassword,etRePassword;
    Button btnGetOtp,btnRegister;

    ProgressBar progressRegister,progressOtp;

    TextView tvError,tvSuccess;

    private static final int OTP_RESEND_TIME = 120; // seconds
    private CountDownTimer resendTimer;

    private Handler messageHandler = new Handler(Looper.getMainLooper());
    private Runnable hideMessageRunnable;


    public RegisterFragment() {
        // Required empty public constructor
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // stop timer on destroy fragment
        if (resendTimer != null) {
            resendTimer.cancel();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        etFirstName =  view.findViewById(R.id.etFirstName);
        etLastName =  view.findViewById(R.id.etLastName);
        etMobile =  view.findViewById(R.id.etMobile);
        etOtp =  view.findViewById(R.id.etOtp);
        etPassword =  view.findViewById(R.id.etPassword);
        etRePassword =  view.findViewById(R.id.etRePassword);
        // buttons
        btnGetOtp =  view.findViewById(R.id.btnGetOtp);
        btnRegister =  view.findViewById(R.id.btnRegister);
        // progressbar
        progressRegister =  view.findViewById(R.id.progressRegister);
        progressOtp =  view.findViewById(R.id.progressOtp);
        // textview error
        tvError = view.findViewById(R.id.tvError);
        tvSuccess= view.findViewById(R.id.tvSuccess);
        btnGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOtpFunction();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRegister();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void getRegister() {
        if(TextUtils.isEmpty(etFirstName.getText().toString().trim())){
            etFirstName.setError("Required");
            return;
        }
        if (!isValidName(etFirstName.getText().toString().trim())) {
            showError("First name should not contain numbers or special characters");
            return;
        }
        if(TextUtils.isEmpty(etLastName.getText().toString().trim())){
            etLastName.setError("Required");
            return;
        }
        if (!isValidName(etLastName.getText().toString().trim())) {
            showError("Last name should not contain numbers or special characters");
            return;
        }
        if(TextUtils.isEmpty(etMobile.getText().toString().trim())){
            etMobile.setError("Required");
            return;
        }
        if (!isValidMobile(etMobile.getText().toString().trim())) {
            showError("Enter a valid 10 digit mobile number");
            return;
        }
        if(TextUtils.isEmpty(etOtp.getText().toString().trim())){
            etOtp.setError("Required");
            return;
        }
        if(TextUtils.isEmpty(etPassword.getText().toString().trim())){
            etPassword.setError("Required");
            return;
        }
        if(TextUtils.isEmpty(etRePassword.getText().toString().trim())){
            etRePassword.setError("Required");
            return;
        }
        if(!checkPassword()){
            return;
        }
        registerNow();
    }

    private void registerNow() {
        // Show loader
        ButtonLoaderUtil.showLoading(btnRegister, progressRegister);
        String ip = DeviceInfo.getLocalIpAddress();
        String device = DeviceInfo.getDeviceString(getContext());
        RegisterUserRequest registerUserRequest = new RegisterUserRequest(
                etFirstName.getText().toString().trim(),
                etLastName.getText().toString().trim(),
                etMobile.getText().toString().trim(),
                etOtp.getText().toString().trim(),
                etPassword.getText().toString().trim(),
                etRePassword.getText().toString().trim(),
                ip,
                device
        );
        ApiService apiService = RegisterRetrofitClient.getClient().create(ApiService.class);

        apiService.registerUser(registerUserRequest).enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                ButtonLoaderUtil.hideLoading(
                        btnRegister, progressRegister, "Register");
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isStatus()) {
                        showSuccess(response.body().getMessage());
                    } else {
                        showError(response.body().getMessage());
                    }
                }else {
                    showError("Server error. Try again");
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                ButtonLoaderUtil.hideLoading(
                        btnRegister, progressRegister, "Register");

                showError("Network error. Please check internet");
            }
        });

    }

    private boolean checkPassword() {
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etRePassword.getText().toString().trim();

        if (password.isEmpty()) {
            showError("Please enter password");
            return false;
        }

        if (confirmPassword.isEmpty()) {
            showError("Please enter confirm password");
            return false;
        }
        if (password.length() < 6) {
            showError("Password must be at least 6 characters");
            return false;
        }
        if (!isPasswordMatch(password, confirmPassword)) {
            etRePassword.setError("Mismatch");
            showError("Password and Confirm Password do not match");
            return false;
        }

        return true;
    }

    private boolean isPasswordMatch(String password, String confirmPassword) {
        return password != null
                && confirmPassword != null
                && password.equals(confirmPassword);
    }

    private boolean isValidMobile(String mobile) {
        mobile = mobile.replaceAll("^\\+91|^0", "");

        if (!mobile.matches("^[6-9]\\d{9}$")) {
            return false;
        }
        return true;
    }

    private boolean isValidName(String string) {
        return string != null && string.matches("^[A-Za-z ]+$");
    }

    private void getOtpFunction() {
        String mobile = etMobile.getText().toString().trim();

        if (TextUtils.isEmpty(mobile)) {
            etMobile.setError("Required");
            return;
        }
        if (!btnGetOtp.isEnabled()) return;
        sendOtp();
    }


    private void sendOtp() {

        String mobile = etMobile.getText().toString().trim();

        if (mobile.length() != 10) {
            showError("Enter a valid mobile number");
            return;
        }

        // Show loader
        ButtonLoaderUtil.showLoading(btnGetOtp, progressOtp);

        ApiService apiService = OtpRetrofitClient.getApiService();

        Call<OtpResponse> call = apiService.sendOtp(mobile);

        call.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {

                ButtonLoaderUtil.hideLoading(btnGetOtp, progressOtp, "Get OTP");

                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().isStatus()) {

                        Toast.makeText(getContext(),
                                response.body().getMessage(),
                                Toast.LENGTH_SHORT).show();
                        tvError.setVisibility(View.GONE);
                        startResendOtpCountdown();
                        showSuccess(response.body().getMessage());
                    } else {
                        showError(response.body().getMessage());

                    }

                } else {
                    showError("Server error. Try again");

                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {

                ButtonLoaderUtil.hideLoading(btnGetOtp, progressOtp, "Get OTP");
                showError("Network error. Please check internet");

            }
        });
    }

    private void showSuccess(String message) {
        // cancelling previous hide if any
        if (hideMessageRunnable != null) {
            messageHandler.removeCallbacks(hideMessageRunnable);
        }
        tvSuccess.setText(message);
        tvSuccess.setTextColor(Color.parseColor("#2E7D32"));
        tvSuccess.setBackgroundResource(R.drawable.bg_success);
        tvSuccess.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_success, 0, 0, 0);
        tvSuccess.setVisibility(View.VISIBLE);
        // Fade in
        tvSuccess.setAlpha(0f);
        tvSuccess.animate().alpha(1f).setDuration(200).start();

        // Auto hide after 3 seconds
        hideMessageRunnable = () -> {
            tvSuccess.animate()
                    .alpha(0f)
                    .setDuration(200)
                    .withEndAction(() -> tvSuccess.setVisibility(View.GONE))
                    .start();
        };

        messageHandler.postDelayed(hideMessageRunnable, 3000);
    }

    private void showError(String message) {
        if (hideMessageRunnable != null) {
            messageHandler.removeCallbacks(hideMessageRunnable);
        }
        tvError.setText(message);
        tvError.setTextColor(Color.parseColor("#D32F2F"));
        tvError.setBackgroundResource(R.drawable.bg_error);
        tvError.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_error, 0, 0, 0);
        tvError.setVisibility(View.VISIBLE);
        // Fade in
        tvError.setAlpha(0f);
        tvError.animate().alpha(1f).setDuration(200).start();

        // Auto hide after 3 seconds
        hideMessageRunnable = () -> {
            tvError.animate()
                    .alpha(0f)
                    .setDuration(200)
                    .withEndAction(() -> tvError.setVisibility(View.GONE))
                    .start();
        };
        messageHandler.postDelayed(hideMessageRunnable, 10000);
    }

    private void startResendOtpCountdown() {

        btnGetOtp.setEnabled(false);

        resendTimer = new CountDownTimer(OTP_RESEND_TIME * 1000L, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                btnGetOtp.setText("Resend in " + seconds + "s");
            }

            @Override
            public void onFinish() {
                btnGetOtp.setEnabled(true);
                btnGetOtp.setText("Resend OTP");
            }
        }.start();
    }


}