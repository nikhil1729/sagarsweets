package com.sagarsweets.in;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.PincodeData;
import com.sagarsweets.in.ApiModel.PincodeRequest;
import com.sagarsweets.in.ApiModel.PincodeResponse;
import com.sagarsweets.in.Session.LoginSession;
import com.sagarsweets.in.Session.PincodeSession;
import com.sagarsweets.in.utils.DeviceInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    TextView tvLocation;
    TextView drawName, drawEmail;
    DrawerLayout drawerLayout;
    MaterialToolbar topAppBar;
    NavigationView navigationView;
    BadgeDrawable badge;
    LoginSession loginSession;
    View headerView;
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
        tvLocation = findViewById(R.id.tvLocation);

        headerView = navigationView.getHeaderView(0);
        drawName = headerView.findViewById(R.id.drawName);
        drawEmail = headerView.findViewById(R.id.drawEmail);
        loginSession = new LoginSession(this);

        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("location_clicked","locaadkasdnsalk");
                openPincodeBottomSheet();
            }
        });

        showHideElementDrawer();
        updateSessionName(); // Update session name by session class
        // tvlocation change
        setLocationSession();
        //tvLocation.setText("sasasas");
        // cart count is setting here
        myCartSet(badge);


        topAppBar.post(() -> {
            BadgeUtils.attachBadgeDrawable(
                    badge,
                    topAppBar,
                    R.id.action_cart
            );
        });
        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Integer id = item.getItemId();
                if (id == R.id.action_cart) {
                    loadFragment(new CartFragment(), "Cart", false);
                    return true;
                }
                return false;
            }
        });
        // Default fragment (Home)
        if (savedInstanceState == null) {
            if(loginSession.isLoggedIn()){
                loadFragment(new HomeFragment(), "Home",true);
                navigationView.setCheckedItem(R.id.draw_home);
            }else{
                loadFragment(new LoginFragment(), "Login",true);
                navigationView.setCheckedItem(R.id.draw_login);
            }

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

    private void openPincodeBottomSheet() {
        Log.d("openBottom","first");
        BottomSheetDialog bottomSheetDialog =
                new BottomSheetDialog(HomeActivity.this);

        View view = getLayoutInflater()
                .inflate(R.layout.bottomsheet_pincode, null);

        EditText etPincode = view.findViewById(R.id.etPincode);
        TextView tvPincodeError = view.findViewById(R.id.tvPincodeError);
        Button btnConfirm = view.findViewById(R.id.btnConfirmPincode);
        Log.d("openBottom","second");
        etPincode.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvPincodeError.setVisibility(View.GONE);
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        btnConfirm.setOnClickListener(v -> {
            String pincode = etPincode.getText().toString().trim();

            if (pincode.isEmpty()) {
                tvPincodeError.setText("Pincode is required");
                tvPincodeError.setVisibility(View.VISIBLE);
                return;
            }
            if (pincode.length() != 6) {
                tvPincodeError.setText("Please enter a valid 6-digit pincode");
                tvPincodeError.setVisibility(View.VISIBLE);
                return;
            }
            String user_id ="";
            String device = DeviceInfo.getDeviceString(this);
            String lot = "";
            String lon = "";
            Log.d("latNikhil",lot+" "+lon);
            PincodeRequest pincodeRequest = new PincodeRequest(pincode,user_id,device,lon,lot);
            ApiService apiService  = LoginRetrofitClient
                    .getClient()
                    .create(ApiService.class);
            apiService.getPincodeStatus(pincodeRequest).enqueue(new Callback<PincodeResponse>() {
                @Override
                public void onResponse(Call<PincodeResponse> call, Response<PincodeResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        PincodeResponse pincodeResponse = response.body();
                        if (pincodeResponse.isStatus()
                                && pincodeResponse.getData() != null
                                && !pincodeResponse.getData().isEmpty()) {
                            // ✅ SUCCESS
                            PincodeData data = pincodeResponse.getData().get(0);
                            PincodeSession sessionManager = new PincodeSession(getApplicationContext());
                            if(sessionManager.hasPincode()){
                                sessionManager.clearPincode();
                            }
                            sessionManager.savePincode(
                                    data.getPincode(),
                                    data.getCity_name(),
                                    data.getDistric_name(),
                                    data.getState()
                            );
                            tvLocation.setText("Deliver to "+data.getCity_name()+","+data.getDistric_name());
                            //tvPincodeError.setVisibility(View.GONE);
                            // ✅ proceed with API / delivery check
                            Log.d("PINCODE", "User entered pincode: " + pincode);
                            recreate();
                            // TODO: Save pincode or call API
                            bottomSheetDialog.dismiss();
                        } else {
                            // ❌ Status false OR empty data
                            tvPincodeError.setText(pincodeResponse.getMessage());
                            tvPincodeError.setVisibility(View.VISIBLE);
                            //showError("Delivery not available for this pincode");
                        }
                    }else {
                        tvPincodeError.setText("Something went wrong");
                        tvPincodeError.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<PincodeResponse> call, Throwable t) {

                }
            });



        });
        Log.d("openBottom","last");
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.show();
    }

    private void setLocationSession() {
        PincodeSession pincodeSession = new PincodeSession(this);
        if(pincodeSession.hasPincode()){
            String location = pincodeSession.getCity() + ", " + pincodeSession.getPincode();
            tvLocation.setText("Deliver to "+location);

        }else{
            tvLocation.setText("please change location");
        }
    }

    private void showHideElementDrawer() {
        Menu menu = navigationView.getMenu();
        if(loginSession.isLoggedIn()){
            //show profile, my order
            menu.findItem(R.id.draw_login).setVisible(false);
            menu.findItem(R.id.draw_register).setVisible(false);

            menu.findItem(R.id.draw_myorders).setVisible(true);
            menu.findItem(R.id.draw_profile).setVisible(true);
            menu.findItem(R.id.draw_logout).setVisible(true);
        }else{
            menu.findItem(R.id.draw_login).setVisible(true);
            menu.findItem(R.id.draw_register).setVisible(true);

            menu.findItem(R.id.draw_myorders).setVisible(false);
            menu.findItem(R.id.draw_profile).setVisible(false);
            menu.findItem(R.id.draw_logout).setVisible(false);
        }
    }

    private void updateSessionName() {

        if(loginSession.isLoggedIn()){
           // true
            drawName.setText("Hi! "+loginSession.getUserName());
            drawEmail.setText("");

        }else{
            // not logged
            drawName.setText(R.string.hi_guest);
            //drawEmail.setText(R.string.email_afterlogged_in);
        }
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
        }if (id == R.id.draw_logout) {
            LoginSession loginSession = new LoginSession(this);
            loginSession.logout();
            Toast.makeText(this,"Successfully logout from this device",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
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

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.container); // replace with your container ID

        if (currentFragment instanceof HomeFragment) {
            Toast.makeText(this,"Thank You, Shop again",Toast.LENGTH_LONG).show();
            // Close the app
            finishAffinity(); // closes all activities and exits
        } else {
            // Let the fragment manager handle back normally
            super.onBackPressed();
        }
    }


}