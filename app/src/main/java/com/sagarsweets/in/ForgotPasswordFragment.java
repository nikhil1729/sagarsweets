package com.sagarsweets.in;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sagarsweets.in.ApiControllers.ForgotPasswordRetrofitClient;
import com.sagarsweets.in.ApiControllers.ResetOtpRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.ForgetPasswordRequest;
import com.sagarsweets.in.ApiModel.OtpResponse;
import com.sagarsweets.in.utils.ButtonLoaderUtil;
import com.sagarsweets.in.utils.DeviceInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordFragment extends Fragment {
    EditText etMobile,etOtp,etPassword,etRePassword;
    Button btnGetOtp,btnResetPassword;
    private Runnable hideMessageRunnable;
    private Handler messageHandler = new Handler(Looper.getMainLooper());

    ProgressBar progressOtp, progressForgot;
    private TextView tvError, tvSuccess;
    private CountDownTimer resendTimer;
    private static final int OTP_RESEND_TIME = 120; // seconds
    private String ip, deviceInfo;

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
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        etMobile = view.findViewById(R.id.etMobile);
        etOtp    = view.findViewById(R.id.etOtp);
        etPassword= view.findViewById(R.id.etPassword);
        etRePassword = view.findViewById(R.id.etRePassword);
        // buttons
        btnGetOtp = view.findViewById(R.id.btnGetOtp);
        btnResetPassword = view.findViewById(R.id.btnResetPassword);
        // error text view
        tvError = view.findViewById(R.id.tvError);
        tvSuccess = view.findViewById(R.id.tvSuccess);
        // progress
        progressOtp = view.findViewById(R.id.progressOtp);
        progressForgot = view.findViewById(R.id.progressForgot);
        ip = DeviceInfo.getLocalIpAddress();
        deviceInfo = DeviceInfo.getDeviceString(getContext());
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = etMobile.getText().toString().trim();
                String otp = etOtp.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String rePassword = etRePassword.getText().toString().trim();

                if (!isValidMobile(mobile)) {
                    showError("Enter a valid 10 digit mobile number");
                    return;
                }

                if(TextUtils.isEmpty(mobile)){
                    etMobile.setError("Required");
                    return;
                }
                if(TextUtils.isEmpty(otp)){
                    etOtp.setError("Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    etPassword.setError("Required");
                    return;
                }
                if(TextUtils.isEmpty(rePassword)){
                    etRePassword.setError("Required");
                    return;
                }
                if(!isPasswaordMatch(password,rePassword)){
                    etRePassword.setError("Password not matched");
                    return;
                }

                ForgetPasswordRequest forgetPasswordRequest = new ForgetPasswordRequest(
                        mobile,
                        otp,
                        password,
                        rePassword,
                        ip,
                        deviceInfo);
                // Show loader
                ButtonLoaderUtil.showLoading(btnResetPassword, progressForgot);

                ApiService apiService = ForgotPasswordRetrofitClient.getClient().create(ApiService.class);
                apiService.forgetPassword(forgetPasswordRequest).enqueue(new Callback<OtpResponse>() {
                    @Override
                    public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                        ButtonLoaderUtil.hideLoading(btnResetPassword,progressForgot,"Reset Password");
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().isStatus()) {
                                showSuccess(response.body().getMessage());
                            } else {
                                showError(response.body().getMessage());
                            }
                        }else {
                            ButtonLoaderUtil.hideLoading(btnResetPassword,progressForgot,"Reset Password");
                            showError("Server error. Try again");
                        }
                    }

                    @Override
                    public void onFailure(Call<OtpResponse> call, Throwable t) {
                        ButtonLoaderUtil.hideLoading(btnResetPassword,progressForgot,"Reset Password");
                        showError("Network error. Please check internet."+t.getMessage());
                    }
                });
            }
        });
        btnGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = etMobile.getText().toString().trim();
                if (!isValidMobile(mobile)) {
                    showError("Enter a valid 10 digit mobile number");
                    return;
                }
                if(TextUtils.isEmpty(mobile)){
                    etMobile.setError("Required");
                    return;
                }
                if (!btnGetOtp.isEnabled()) return;
                sendOtp();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private boolean isValidMobile(String mobile) {
        mobile = mobile.replaceAll("^\\+91|^0", "");
        if (!mobile.matches("^[6-9]\\d{9}$")) {
            return false;
        }
        return true;
    }

    private boolean isPasswaordMatch(String password, String rePassword) {
            return password != null
                    && rePassword != null
                    && password.equals(rePassword);
    }

    private void sendOtp() {
        String mobile = etMobile.getText().toString().trim();

        if (mobile.length() != 10) {
            showError("Enter a valid mobile number");
            return;
        }
        // Show loader
        ButtonLoaderUtil.showLoading(btnGetOtp, progressOtp);
        ApiService apiService = ResetOtpRetrofitClient.getApiService();
        Call<OtpResponse> call = apiService.sendOtpForReset(mobile,ip,deviceInfo);
        call.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                ButtonLoaderUtil.hideLoading(btnGetOtp, progressOtp, "Get OTP");
                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().isStatus()) {


                        tvError.setVisibility(View.GONE);
                        startResendOtpCountdown();
                        showSuccess(response.body().getMessage());
                    } else {
                        showError(response.body().getMessage());

                    }

                } else {
                    ButtonLoaderUtil.hideLoading(btnGetOtp, progressOtp, "Get OTP");
                    showError("Server error. Try again");

                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                ButtonLoaderUtil.hideLoading(btnGetOtp, progressOtp, "Get OTP");
                showError("Network error. Please check internet."+ t.getMessage());
            }
        });
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