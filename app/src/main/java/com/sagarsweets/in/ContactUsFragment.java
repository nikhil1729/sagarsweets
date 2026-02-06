package com.sagarsweets.in;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiControllers.OtpRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.ContactUsFormRequest;
import com.sagarsweets.in.ApiModel.ContactUsFormResponse;
import com.sagarsweets.in.ApiModel.ContactUsResponse;
import com.sagarsweets.in.ApiModel.LoginResponse;
import com.sagarsweets.in.utils.ButtonLoaderUtil;
import com.sagarsweets.in.utils.DeviceInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ContactUsFragment extends Fragment {
    TextView tvAddress, tvError,tvSuccess;
    EditText etName,etEmail,etMessage;
    Button btnSendMessage;
    WebView mapWebView;
    NestedScrollView scrollView;
    String device;
    ProgressBar progressCanctus;
    private Runnable hideMessageRunnable;
    private Handler messageHandler = new Handler(Looper.getMainLooper());
    public ContactUsFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        initViews(view);
        String address = "Sagar Sweets\\nMG Road," +
                " Andheri East\\nMumbai, " +
                "Maharashtra - " +
                "400069\\nIndia";
        loadAddress();
        loadMap();
        saveContactUs();
        requireActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // Inflate the layout for this fragment
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        requireActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void setAutoScroll(View view) {
        view.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                scrollView.post(() -> {
                    scrollView.smoothScrollTo(0, v.getBottom());
                });
            }
        });
    }


    private void saveContactUs() {
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name =  etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String message = etMessage.getText().toString().trim();

                if (validateForm()) {
                    ButtonLoaderUtil.showLoading(btnSendMessage, progressCanctus);
                    // submit form
                    ContactUsFormRequest contactUsFormRequest =
                            new ContactUsFormRequest(name,email,message,device);
                    ApiService apiService = LoginRetrofitClient
                            .getClient()
                            .create(ApiService.class);
                    apiService.saveContactUs(contactUsFormRequest).enqueue(new Callback<ContactUsFormResponse>() {
                        @Override
                        public void onResponse(Call<ContactUsFormResponse> call, Response<ContactUsFormResponse> response) {
                            contactResponse(response);
                        }

                        @Override
                        public void onFailure(Call<ContactUsFormResponse> call, Throwable t) {
                            ButtonLoaderUtil.hideLoading(btnSendMessage, progressCanctus, "Send Message");
                            showErrorDialog(t.getMessage());
                        }
                    });
                }

            }
        });
    }

    private void contactResponse(Response<ContactUsFormResponse> response) {
        if (response.isSuccessful() && response.body() != null) {

            ContactUsFormResponse contactUsFormResponse = response.body();
            if ( contactUsFormResponse.isStatus() ) {

                ButtonLoaderUtil.hideLoading(btnSendMessage, progressCanctus, "Send Message");
                showSuccess(contactUsFormResponse.getMessage());
                etName.setText("");
                etEmail.setText("");
                etMessage.setText("");
            }else{
                ButtonLoaderUtil.hideLoading(btnSendMessage, progressCanctus, "Send Message");
                showErrorDialog(contactUsFormResponse.getMessage());
            }
        }else{
            ButtonLoaderUtil.hideLoading(btnSendMessage, progressCanctus, "Send Message");
            showErrorDialog("Invalid response from server.");

        }
    }

    private boolean validateForm() {

        if (etName.getText().toString().trim().isEmpty()) {
            showError(etName, "Please enter your name");
            return false;
        }

        if (etEmail.getText().toString().trim().isEmpty()) {
            showError(etEmail, "Please enter email");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            showError(etEmail, "Enter valid email");
            return false;
        }

        if (etMessage.getText().toString().trim().isEmpty()) {
            showError(etMessage, "Please enter message");
            return false;
        }

        return true;
    }
    private void showError(EditText editText, String error) {

        editText.setError(error);
        editText.requestFocus();

        scrollView.post(() -> {
            scrollView.smoothScrollTo(0, editText.getBottom());
        });

        InputMethodManager imm =
                (InputMethodManager) requireActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null) {
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }



    private void loadMap() {
        // 🔥 REQUIRED WebView setup
        WebSettings settings = mapWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        mapWebView.setWebViewClient(new WebViewClient());

        // Optional polish
        mapWebView.setLongClickable(false);
        mapWebView.setHapticFeedbackEnabled(false);

        String shopName = "Sagar Sweets";
        String lat = "26.4459844";
        String lng = "83.6145783";

        String html =
                "<html><body style='margin:0'>" +
                        "<iframe " +
                        "width='100%' height='100%' frameborder='0' " +
                        "src='https://www.google.com/maps?q=" +
                        shopName.replace(" ", "+") + "@" + lat + "," + lng +
                        "&z=20&output=embed'>" +
                        "</iframe></body></html>";

        mapWebView.loadDataWithBaseURL(
                "https://www.google.com",
                html,
                "text/html",
                "UTF-8",
                null
        );
        mapWebView.setOnTouchListener((v, event) -> true);

    }

    private void loadAddress() {
        ApiService apiService = OtpRetrofitClient.getApiService();
        Call<ContactUsResponse> call = apiService.getContactUs();
        call.enqueue(new Callback<ContactUsResponse>() {
            @Override
            public void onResponse(Call<ContactUsResponse> call, Response<ContactUsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ContactUsResponse.Result data = response.body().getResult();
                    Log.d("addressNikhil",data.getAddress());
                    String address = data.getAddress();
                    String phone = "Contact No- "+data.getPhoneNumber();
                    String email = "Email- "+ data.getEmail();
                    String printAddress = address+"\n"+phone+"\n"+email;
                    tvAddress.setText(printAddress);
                }
            }

            @Override
            public void onFailure(Call<ContactUsResponse> call, Throwable t) {
                t.printStackTrace();
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

        messageHandler.postDelayed(hideMessageRunnable, 10000);
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
    private void initViews(View view) {
        scrollView = view.findViewById(R.id.main);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvError = view.findViewById(R.id.tvError);
        tvSuccess =  view.findViewById(R.id.tvSuccess);
        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);
        etMessage = view.findViewById(R.id.etMessage);
        btnSendMessage = view.findViewById(R.id.btnSendMessage);
        mapWebView = view.findViewById(R.id.mapWebView);
        progressCanctus = view.findViewById(R.id.progressCanctus);

        device = DeviceInfo.getDeviceString(getContext());
        setAutoScroll(etName);
        setAutoScroll(etEmail);
        setAutoScroll(etMessage);
    }
}