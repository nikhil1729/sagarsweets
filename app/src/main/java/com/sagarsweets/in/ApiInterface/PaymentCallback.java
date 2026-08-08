package com.sagarsweets.in.ApiInterface;

public interface PaymentCallback {
    void onPaymentSuccess(
            String paymentId
    );

    void onPaymentError(
            int code,
            String response
    );
}
