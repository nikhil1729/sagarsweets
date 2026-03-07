package com.sagarsweets.in.utils;



// this class show the prograss bat while clicking any button passed here

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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
}
