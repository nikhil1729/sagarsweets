package com.sagarsweets.in.utils;



// this class show the prograss bat while clicking any button passed here

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ButtonLoaderUtil {
    public static void showLoading(Button button, ProgressBar progressBar) {
        button.setEnabled(false);
        button.setText("");
        progressBar.setVisibility(View.VISIBLE);
    }

    public static void hideLoading(Button button, ProgressBar progressBar, String buttonText) {
        progressBar.setVisibility(View.GONE);
        button.setEnabled(true);
        button.setText(buttonText);
    }

    public static void showLoadingText(TextView button, ProgressBar progressBar) {
        button.setEnabled(false);
        button.setText("");
        progressBar.setVisibility(View.VISIBLE);
    }

    public static void hideLoadingText(TextView button, ProgressBar progressBar, String buttonText) {
        progressBar.setVisibility(View.GONE);
        button.setEnabled(true);
        button.setText(buttonText);
    }
    public static void makeToast(Context context,String message){
        CustomToast.warning(context,message);
    }

    public static void showSizeSelected(Context context, View view) {
        //Context context = requireContext();
        DeviceInfo.vibratMobile(context);
        // Scroll parent containers to make this view visible
        view.post(() -> {
            Rect rect = new Rect();
            view.getDrawingRect(rect);
            view.requestRectangleOnScreen(rect, true);
        });
        // Shake animation
        view.animate()
                .translationX(20)
                .setDuration(50)
                .withEndAction(() ->
                        view.animate()
                                .translationX(-20)
                                .setDuration(50)
                                .withEndAction(() ->
                                        view.animate()
                                                .translationX(0)
                                                .setDuration(50)
                                )
                );
    }
}
