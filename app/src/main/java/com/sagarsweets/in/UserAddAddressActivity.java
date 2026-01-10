package com.sagarsweets.in;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.WindowInsets;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserAddAddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_add_address);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().getDecorView().setOnApplyWindowInsetsListener((v, insets) -> {
            @SuppressLint({"NewApi", "LocalSuppress"}) int imeHeight = insets.getInsets(WindowInsets.Type.ime()).bottom;
            v.setPadding(0, 0, 0, imeHeight);
            return insets;
        });
    }
}