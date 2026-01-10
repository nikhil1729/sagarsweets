package com.sagarsweets.in;

import static com.sagarsweets.in.R.id.registerActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {

    EditText etPassword, etRePassword;
    ScrollView scrollView;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(registerActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        etPassword = findViewById(R.id.etPassword);
        etRePassword = findViewById(R.id.etRePassword);
        scrollView = findViewById(R.id.registerActivity);

        getWindow().getDecorView().setOnApplyWindowInsetsListener((v, insets) -> {
            @SuppressLint({"NewApi", "LocalSuppress"}) int imeHeight = insets.getInsets(WindowInsets.Type.ime()).bottom;
            v.setPadding(0, 0, 0, imeHeight);
            return insets;
        });

        etRePassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                scrollView.post(() ->
                        scrollView.smoothScrollTo(0, v.getBottom()));
            }
        });
    }
}