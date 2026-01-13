package com.sagarsweets.in;

import android.content.Intent;
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
import androidx.fragment.app.Fragment;

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

        // Default fragment (Home)
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(), "Home",true);
            navigationView.setCheckedItem(R.id.nav_home);
        }

        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            openDrawerItem(item.getItemId());
            return true;

        });


    }

    private void openDrawerItem(int id) {
        if (id == R.id.draw_home) {
            loadFragment(new HomeFragment(), "Home", false);
        } else if (id == R.id.draw_about_us) {
            loadFragment(new AboutUsFragment(), "About Us", false);
        } else if (id == R.id.nav_contact) {
            loadFragment(new ContactUsFragment(), "Contact Us", false);
        }else if (id == R.id.draw_term_and_condition) {
            loadFragment(new TermAndConditionFragment(), "Our T&C", false);
        }else if (id == R.id.draw_login) {
            loadFragment(new LoginFragment(), "Login", false);
        }else if (id == R.id.draw_register) {
            loadFragment(new RegisterFragment(), "Register", false);
        }

    }

    private void loadFragment(Fragment fragment, String title, boolean b) {
        // Load fragment if true then add else replace fragment
        if(b){
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            android.R.anim.fade_in,
                            android.R.anim.fade_out
                    )
                    .add(R.id.container, fragment)
                    .addToBackStack(title) // optional
                    .commit();
        }else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(title) // optional
                    .commit();
        }



        // Close drawer
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        drawerLayout.closeDrawer(GravityCompat.START);
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