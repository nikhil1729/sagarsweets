package com.sagarsweets.in;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.LoginRequest;
import com.sagarsweets.in.ApiModel.LoginResponse;
import com.sagarsweets.in.ApiModel.User;
import com.sagarsweets.in.Session.LoginSession;
import com.sagarsweets.in.utils.DeviceInfo;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {
    private EditText edtMobile,edtPassword;
    private Button btnLogin;
    private TextView txtForgot,txtSignup,tvError;
    ProgressBar progressLogin;
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
        progressLogin = view.findViewById(R.id.progressLogin);
        removeError();




        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateLogin();
            }
        });
        //
        return view;
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
        // Disable button
        btnLogin.setEnabled(false);
        btnLogin.setText("");
        // Show progress
        progressLogin.setVisibility(View.VISIBLE);
        // TODO: Call Login API here
        login(mobile,password);
        // Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
    }

    private void login(String mobile, String password) {



        // creating login request class
        LoginRequest request = new LoginRequest(mobile, password, getDeviceString());

        ApiService apiService = LoginRetrofitClient
                .getClient()
                .create(ApiService.class);
        apiService.loginUser(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    LoginResponse loginResponse = response.body();
                    if ( loginResponse.isStatus() ) {
                        progressLogin.setVisibility(View.GONE);
                        btnLogin.setEnabled(true);
                        btnLogin.setText("Login");
                        // SAVE TOKEN
                        saveSession(loginResponse);

                    }else{
                        progressLogin.setVisibility(View.GONE);
                        btnLogin.setEnabled(true);
                        btnLogin.setText("Login");
                        showErrorDialog(loginResponse.getMessage());
                    }
                }else{
                    progressLogin.setVisibility(View.GONE);
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Login");
                    showErrorDialog("Invalid response from server.");

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                if (t instanceof IOException) {
                    showErrorDialog("No internet connection");
                } else {
                    showErrorDialog("Something went wrong");
                }
            }
        });
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
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
        tvError.setAlpha(0f);
        tvError.animate().alpha(1f).setDuration(300).start();

    }
}