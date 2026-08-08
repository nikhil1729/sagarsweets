package com.sagarsweets.in;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.CheckoutProcessData;
import com.sagarsweets.in.ApiModel.PayonDeleveryOtpRequest;
import com.sagarsweets.in.ApiModel.PayonDeleveryOtpResponse;
import com.sagarsweets.in.ApiModel.PodVerifyOtpRequest;
import com.sagarsweets.in.ApiModel.PodVerifyOtpResponse;
import com.sagarsweets.in.Session.LoginSession;
import com.sagarsweets.in.utils.ButtonLoaderUtil;
import com.sagarsweets.in.utils.DeviceInfo;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PayonDeliveryFragment extends Fragment {

    CheckoutProcessData checkoutData;
    EditText otp1,otp2,otp3,otp4,otp5,otp6;
    TextView txtTimer,txtResendOtp,tvError;
    LoginSession loginSession;
    ApiService apiService;
    ProgressBar progressResendOtp,progressVerifyOtp;
    Integer countTime;
    Button btnVerifyOtp;

    String otp;
    String podId;
    public PayonDeliveryFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payon_delivery, container, false);
        if(getArguments()!=null){
            checkoutData = (CheckoutProcessData)
                    getArguments().getSerializable("checkoutData");
        }

        if(checkoutData!=null){
            Log.d("CHECKOUT_DATA", checkoutData.getUserId());
            Log.d("CHECKOUT_DATA", new Gson().toJson(checkoutData));
            initViews(view);
            podId = checkoutData.getPodId();
            reSendOtp();
            verifyOtp();
        }
        // Handle back press
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {

                        CartFragment cartFragment = new CartFragment();

                        requireActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, cartFragment)
                                .commit();
                    }
                });
        // Inflate the layout for this fragment
        return view;
    }

    private void verifyOtp() {
        btnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp = getOTP();
                if(otp.length() < 6){
                    ButtonLoaderUtil.makeToast(getContext(),"Otp length");
                    return;
                }
                PodVerifyOtpRequest podVerifyOtpRequest = new PodVerifyOtpRequest(loginSession.getUserId(),
                        podId,otp, checkoutData.getTimeSlot(), checkoutData.getDate(), checkoutData.getAddressId(),
                        DeviceInfo.getDeviceString(getContext()), checkoutData.getDeliveryType(),
                        checkoutData.getLongitude(), checkoutData.getLatitude(), checkoutData.getCoupon()
                        );
                ButtonLoaderUtil.showLoadingText(btnVerifyOtp,progressVerifyOtp);
                tvError.setVisibility(View.GONE);
                apiService.getVerifyPodOtp(podVerifyOtpRequest).enqueue(new Callback<PodVerifyOtpResponse>() {
                    @Override
                    public void onResponse(Call<PodVerifyOtpResponse> call, Response<PodVerifyOtpResponse> response) {
                        ButtonLoaderUtil.hideLoading(btnVerifyOtp,progressVerifyOtp,"Verify OTP");
                        if(response.body() != null){
                            if(response.body().getStatus()){
                                //ButtonLoaderUtil.makeToast(getContext(),response.body().getMessage());
                                //PodVerifyOtpResponse res = response.body();
                                OrderReceivedFragment orderReceivedFragment = new OrderReceivedFragment();
                                Gson gson = new Gson();
                                String json = gson.toJson(response.body());
                                Bundle bundle = new Bundle();
                                bundle.putString("order_data", json);
                                //bundle.putSerializable("resultData", (Serializable) res);
                                orderReceivedFragment.setArguments(bundle);

                                requireActivity()
                                        .getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.container, orderReceivedFragment)
                                        .addToBackStack("order_received_fragment")
                                        .commit();
                            }else{
                                tvError.setVisibility(View.VISIBLE);
                                tvError.setText(response.body().getMessage());
                                ButtonLoaderUtil.makeToast(getContext(),response.body().getMessage());
                            }
                        }else {
                            ButtonLoaderUtil.makeToast(getContext(),"Internal server error");
                        }
                    }

                    @Override
                    public void onFailure(Call<PodVerifyOtpResponse> call, Throwable t) {
                        ButtonLoaderUtil.hideLoading(btnVerifyOtp,progressVerifyOtp,"Verify OTP");
                    }
                });
            }
        });
    }

    private void reSendOtp() {
        txtResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonLoaderUtil.showLoadingText(txtResendOtp,progressResendOtp);
                PayonDeleveryOtpRequest payonDeleveryOtpRequest =
                        new PayonDeleveryOtpRequest(checkoutData.getDevice(),loginSession.getUserId(),
                                checkoutData.getAddressId(),checkoutData.getLongitude(),checkoutData.getLatitude());
                apiService.getPayonDeliveryOtp(payonDeleveryOtpRequest).enqueue(new Callback<PayonDeleveryOtpResponse>() {
                    @Override
                    public void onResponse(Call<PayonDeleveryOtpResponse> call, Response<PayonDeleveryOtpResponse> response) {
                        ButtonLoaderUtil.hideLoadingText(txtResendOtp,progressResendOtp,"Resend OTP");
                        if(response.body() != null){
                            if(response.body().getStatus()){
                                ButtonLoaderUtil.makeToast(getContext(),response.body().getMessage());
                                podId = response.body().getPodId();
                                countTime += 30000;
                                startOTPTimer();
                            }else{
                                ButtonLoaderUtil.makeToast(getContext(),response.body().getMessage());
                            }
                        }else{
                            ButtonLoaderUtil.makeToast(getContext(),"Server error");
                        }
                    }

                    @Override
                    public void onFailure(Call<PayonDeleveryOtpResponse> call, Throwable t) {
                        ButtonLoaderUtil.makeToast(getContext(),"Internal Server error");
                    }
                });
            }
        });
    }

    private void initViews(View view) {
        apiService = LoginRetrofitClient.getClient().create(ApiService.class);
        otp1 = view.findViewById(R.id.otp1);
        otp2 = view.findViewById(R.id.otp2);
        otp3 = view.findViewById(R.id.otp3);
        otp4 = view.findViewById(R.id.otp4);
        otp5 = view.findViewById(R.id.otp5);
        otp6 = view.findViewById(R.id.otp6);
        txtTimer = view.findViewById(R.id.txtTimer);
        txtResendOtp = view.findViewById(R.id.txtResendOtp);
        progressResendOtp = view.findViewById(R.id.progressResendOtp);
        btnVerifyOtp = view.findViewById(R.id.btnVerifyOtp);
        progressVerifyOtp = view.findViewById(R.id.progressVerifyOtp);
        tvError = view.findViewById(R.id.tvError);

        countTime = 30000;
        loginSession = new LoginSession(getContext());

        setupOTPInputs();
        startOTPTimer();

    }

    private void startOTPTimer(){

        new android.os.CountDownTimer(countTime,1000){

            public void onTick(long millisUntilFinished){
                txtResendOtp.setVisibility(View.GONE);
                txtTimer.setVisibility(View.VISIBLE);
                txtTimer.setText("Resend OTP in " + millisUntilFinished/1000 + " sec");
            }

            public void onFinish(){
                txtResendOtp.setVisibility(View.VISIBLE);
                txtTimer.setVisibility(View.GONE);
                //txtTimer.setText("Resend OTP");
            }

        }.start();
    }
    private String getOTP(){

        return otp1.getText().toString() +
                otp2.getText().toString() +
                otp3.getText().toString() +
                otp4.getText().toString() +
                otp5.getText().toString() +
                otp6.getText().toString();
    }
    private void setupOTPInputs() {
        otp1.addTextChangedListener(new GenericTextWatcher(otp1, otp2));
        otp2.addTextChangedListener(new GenericTextWatcher(otp2, otp3));
        otp3.addTextChangedListener(new GenericTextWatcher(otp3, otp4));
        otp4.addTextChangedListener(new GenericTextWatcher(otp4, otp5));
        otp5.addTextChangedListener(new GenericTextWatcher(otp5, otp6));

        otp2.setOnKeyListener(new OTPKeyListener(otp1));
        otp3.setOnKeyListener(new OTPKeyListener(otp2));
        otp4.setOnKeyListener(new OTPKeyListener(otp3));
        otp5.setOnKeyListener(new OTPKeyListener(otp4));
        otp6.setOnKeyListener(new OTPKeyListener(otp5));
    }

    private class OTPTextWatcher implements android.text.TextWatcher {

        private View view;

        OTPTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(android.text.Editable editable) {

            String text = editable.toString();

            if (text.length() == 6) {
                fillOTP(text);
                return;
            }

            if (text.length() == 1) {
                moveNext(view);
            }
        }
    }
    private void fillOTP(String otp){

        if(otp.length()==6){

            otp1.setText(String.valueOf(otp.charAt(0)));
            otp2.setText(String.valueOf(otp.charAt(1)));
            otp3.setText(String.valueOf(otp.charAt(2)));
            otp4.setText(String.valueOf(otp.charAt(3)));
            otp5.setText(String.valueOf(otp.charAt(4)));
            otp6.setText(String.valueOf(otp.charAt(5)));

            otp6.requestFocus();
        }
    }
    private void moveNext(View view){

        if(view == otp1) otp2.requestFocus();
        else if(view == otp2) otp3.requestFocus();
        else if(view == otp3) otp4.requestFocus();
        else if(view == otp4) otp5.requestFocus();
        else if(view == otp5) otp6.requestFocus();
    }
    private class OTPKeyListener implements View.OnKeyListener {

        private EditText previousView;

        OTPKeyListener(EditText previousView) {
            this.previousView = previousView;
        }

        @Override
        public boolean onKey(View v, int keyCode, android.view.KeyEvent event) {

            if (keyCode == android.view.KeyEvent.KEYCODE_DEL
                    && event.getAction() == android.view.KeyEvent.ACTION_DOWN) {

                EditText current = (EditText) v;

                if (current.getText().toString().isEmpty()) {
                    previousView.requestFocus();
                    previousView.setSelection(previousView.getText().length());
                }
            }

            return false;
        }
    }
    private class GenericTextWatcher implements android.text.TextWatcher {

        private View currentView;
        private View nextView;

        public GenericTextWatcher(View currentView, View nextView) {
            this.currentView = currentView;
            this.nextView = nextView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(android.text.Editable editable) {

            if (editable.length() == 1) {
                if (nextView != null) {
                    nextView.requestFocus();
                }
            }
        }
    }
}