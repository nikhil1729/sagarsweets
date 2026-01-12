package com.sagarsweets.in;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.badge.ExperimentalBadgeUtils;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    TextView tvLocation;
    DrawerLayout drawerLayout;
    MaterialToolbar topAppBar;
    NavigationView navigationView;
    BadgeDrawable badge;

    @OptIn(markerClass = ExperimentalBadgeUtils.class)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        drawerLayout = findViewById(R.id.drawerLayout);
        topAppBar = findViewById(R.id.topAppBar);
        navigationView = findViewById(R.id.navigationView);
        topAppBar.inflateMenu(R.menu.top_app_bar_menu);

        // cart count is setting here
        myCartSet(badge);

        topAppBar.post(() -> {
            BadgeUtils.attachBadgeDrawable(
                    badge,
                    topAppBar,
                    R.id.action_cart
            );
        });

        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


    }

    private void myCartSet(BadgeDrawable badge) {

        // Create badge
        this.badge = BadgeDrawable.create(this);
        this.badge.setBackgroundColor(getResources().getColor(R.color.red));
        this.badge.setBadgeTextColor(getResources().getColor(android.R.color.white));
        // Example count
        int cartCount = 99;

        if (cartCount > 0) {
            this.badge.setNumber(cartCount);
            this.badge.setVisible(true);
        } else {
            this.badge.clearNumber();
            this.badge.setVisible(false);
        }

    }


}