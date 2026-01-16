package com.sagarsweets.in;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiControllers.ResetOtpRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.LoginOtpRequest;
import com.sagarsweets.in.ApiModel.LoginRequest;
import com.sagarsweets.in.ApiModel.LoginResponse;
import com.sagarsweets.in.ApiModel.OtpResponse;
import com.sagarsweets.in.ApiModel.User;
import com.sagarsweets.in.Session.LoginSession;
import com.sagarsweets.in.utils.ButtonLoaderUtil;
import com.sagarsweets.in.utils.DeviceInfo;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {
    private EditText edtMobile,edtPassword,edtOtp;
    private Button btnLogin;
    private TextView txtForgot,txtSignup,tvError, txtLoginWithOtp, tvSuccess;
    ProgressBar progressLogin;
    private Runnable hideMessageRunnable;
    private Handler messageHandler = new Handler(Looper.getMainLooper());
    private TextInputLayout tilOtp, tilPassword;
    private CountDownTimer resendTimer;
    private static final int OTP_RESEND_TIME = 120; // seconds
    boolean isOtpLogin = false;
    boolean otpSent = false;
    String deviceInfo,ip;
    public LoginFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        edtMobile = view.findViewById(R.id.edtMobile);
        edtPassword = view.findViewById(R.id.edtPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        tvError = view.findViewById(R.id.tvError);
        tvSuccess = view.findViewById(R.id.tvSuccess);
        progressLogin = view.findViewById(R.id.progressLogin);
        txtForgot = view.findViewById(R.id.txtForgot);
        txtSignup = view.findViewById(R.id.txtSignup);

        tilOtp = view.findViewById(R.id.tilOtp);
        tilPassword = view.findViewById(R.id.tilPassword);
        txtLoginWithOtp = view.findViewById(R.id.txtLoginWithOtp);
        edtOtp = view.findViewById(R.id.edtOtp);
        ip = DeviceInfo.getLocalIpAddress();
        deviceInfo = DeviceInfo.getDeviceString(getContext());
        removeError();

        txtLoginWithOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOtpLogin = true;

                tilPassword.setVisibility(View.GONE);
                tilOtp.setVisibility(View.GONE);

                btnLogin.setText("Get OTP");
                txtLoginWithOtp.setVisibility(View.GONE);
                txtForgot.setVisibility(View.GONE);

            }
        });

        // signup textview clicked
        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment registerFragment = new RegisterFragment();
                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, registerFragment)
                        .addToBackStack("register_fragment")
                        .commit();
            }
        });
        // forgot password clicked
        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, forgotPasswordFragment)
                        .addToBackStack("forgot_password")
                        .commit();
            }
        });
        // login button clicked
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = edtMobile.getText().toString().trim();

                if (mobile.length() != 10) {
                    showErrorDialog("Enter valid mobile number");
                    return;
                }
                if (isOtpLogin) {
                    // login with otp
                    if (!otpSent) {
                        // if otp not sent then send otp
                        // CALL API → SEND OTP
                        sendOtpApi(mobile);


                    }else{
                        // if otp sent then call login with otp function
                        String otp = edtOtp.getText().toString().trim();

                        if (otp.length() != 6) {
                            showErrorDialog("Enter valid OTP");
                            return;
                        }

                        // CALL API → VERIFY OTP
                        verifyOtpApi(mobile, otp);
                    }
                }else{
                    // login with password
                    validateLogin();
                }

            }
        });
        //
        return view;
    }



    private void sendOtpApi(String mobile) {
        ButtonLoaderUtil.showLoading(btnLogin, progressLogin);
        ApiService apiService = ResetOtpRetrofitClient.getApiService();
        Call<OtpResponse> call = apiService.sendOtpForReset(mobile,ip,deviceInfo);
        call.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                ButtonLoaderUtil.hideLoading(btnLogin, progressLogin, "Get OTP");
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isStatus()) {
                        otpSent = true;
                        tilOtp.setVisibility(View.VISIBLE);
                        btnLogin.setText("Verify OTP");
                        //startResendOtpCountdown();
                        showSuccess(response.body().getMessage());
                    }else{
                        showErrorDialog(response.body().getMessage());
                    }
                }else{
                    Log.d("Error-otp", String.valueOf(response.errorBody()));
                    showErrorDialog("Server error. Try again");
                }

            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {

            }
        });
    }

    private void startResendOtpCountdown() {

        btnLogin.setEnabled(false);

        resendTimer = new CountDownTimer(OTP_RESEND_TIME * 1000L, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                btnLogin.setText("Resend in " + seconds + "s");
            }

            @Override
            public void onFinish() {
                btnLogin.setEnabled(true);
                btnLogin.setText("Resend OTP");
            }
        }.start();
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

    private void removeError() {
        edtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvError.setVisibility(View.GONE);
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvError.setVisibility(View.GONE);
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void validateLogin() {
        String mobile = edtMobile.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if (!isValidMobile(mobile)) {
            showErrorDialog("Enter a valid 10 digit mobile number");
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            edtMobile.setError("Required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Required");
            return;
        }


        // Hide previous error
        tvError.setVisibility(View.GONE);
        // Show loader
        ButtonLoaderUtil.showLoading(btnLogin, progressLogin);


        // TODO: Call Login API here
        login(mobile,password);
        // Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
    }



    private boolean isValidMobile(String mobile) {
        mobile = mobile.replaceAll("^\\+91|^0", "");

        if (!mobile.matches("^[6-9]\\d{9}$")) {
            return false;
        }
        return true;
    }

    // api calling for login otp
    private void verifyOtpApi(String mobile, String otp) {
        Log.d("loginotp","here");
        LoginOtpRequest loginOtpRequest = new LoginOtpRequest(
                mobile,otp,DeviceInfo.getDeviceString(getContext())
        );
        Log.d("loginotp","here1");
        ApiService apiService = LoginRetrofitClient
                .getClient()
                .create(ApiService.class);
        Log.d("loginotp","here2");
        apiService.loginUserByOtp(loginOtpRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.d("loginotp","responce");
                loginResponce(response);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("loginotp","failed");
                ButtonLoaderUtil.hideLoading(btnLogin, progressLogin, "Login");
                if (t instanceof IOException) {
                    showErrorDialog("No internet connection");
                } else {
                    showErrorDialog("Something went wrong");
                }
            }
        });
    }

    // api calling for mobile and password login
    private void login(String mobile, String password) {
        // creating login request class
        LoginRequest request = new LoginRequest(mobile, password, getDeviceString());

        ApiService apiService = LoginRetrofitClient
                .getClient()
                .create(ApiService.class);
        apiService.loginUser(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                loginResponce(response);

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                ButtonLoaderUtil.hideLoading(btnLogin, progressLogin, "Login");
                if (t instanceof IOException) {
                    showErrorDialog("No internet connection");
                } else {
                    showErrorDialog("Something went wrong");
                }
            }
        });
    }

    private void loginResponce(Response<LoginResponse> response) {
        if (response.isSuccessful() && response.body() != null) {

            LoginResponse loginResponse = response.body();
            if ( loginResponse.isStatus() ) {

                ButtonLoaderUtil.hideLoading(btnLogin, progressLogin, "Login");
                // SAVE TOKEN
                saveSession(loginResponse);

            }else{
                ButtonLoaderUtil.hideLoading(btnLogin, progressLogin, "Login");
                showErrorDialog(loginResponse.getMessage());
            }
        }else{
            ButtonLoaderUtil.hideLoading(btnLogin, progressLogin, "Login");
            showErrorDialog("Invalid response from server.");

        }
    }

    private String getDeviceString() {
        return DeviceInfo.getDeviceString(getContext());
    }

    private void saveSession(LoginResponse loginResponse) {
        LoginSession loginSession = new LoginSession(getContext() );
        User user;
        user = loginResponse.getUser();
        String userId = String.valueOf(user.getId());
        String userName = user.getFirst_name()+" "+user.getLast_name();
        loginSession.createLoginSession(userId, userName);

        startActivity(new Intent(getContext(), HomeActivity.class));

    }

    private void showErrorDialog(String message) {
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
}