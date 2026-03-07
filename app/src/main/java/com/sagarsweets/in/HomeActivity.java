package com.sagarsweets.in;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.badge.ExperimentalBadgeUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.sagarsweets.in.Adapters.PopularProductAdapter;
import com.sagarsweets.in.Adapters.SearchSuggestionAdapter;
import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.PincodeData;
import com.sagarsweets.in.ApiModel.PincodeRequest;
import com.sagarsweets.in.ApiModel.PincodeResponse;
import com.sagarsweets.in.RoomDatabase.AppDatabase;
import com.sagarsweets.in.Session.LoginSession;
import com.sagarsweets.in.Session.PincodeSession;
import com.sagarsweets.in.utils.ButtonLoaderUtil;
import com.sagarsweets.in.utils.DeviceInfo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements PopularProductAdapter.CartUpdateListener{

    TextView tvLocation;
    TextView drawName, drawEmail;
    DrawerLayout drawerLayout;
    MaterialToolbar topAppBar;
    NavigationView navigationView;
    BadgeDrawable badge;
    LoginSession loginSession;
    View headerView;
    private LiveData<Integer> cartCountLiveData;

    MaterialAutoCompleteTextView searchAuto;
    List<String> suggestions;
    SearchSuggestionAdapter adapter;

    private Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    @OptIn(markerClass = ExperimentalBadgeUtils.class)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // IMPORTANT
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        );
        drawerLayout = findViewById(R.id.drawerLayout);
        topAppBar = findViewById(R.id.topAppBar);
        navigationView = findViewById(R.id.navigationView);
        tvLocation = findViewById(R.id.tvLocation);

        headerView = navigationView.getHeaderView(0);
        drawName = headerView.findViewById(R.id.drawName);
        drawEmail = headerView.findViewById(R.id.drawEmail);
        loginSession = new LoginSession(this);

        tvLocation.setOnClickListener(v -> openPincodeBottomSheet());

        // Autocomplete
        initAutoComplete();

        showHideElementDrawer();
        updateSessionName();
        setLocationSession();


        topAppBar.setNavigationOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START));

        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_cart) {
                loadFragment(new CartFragment(), "Cart", false);
                return true;
            }
            return false;
        });

        topAppBar.post(this::setupCartBadge);

        navigationView.setNavigationItemSelectedListener(item -> {
            openDrawerItem(item.getItemId());
            return true;
        });

        // Default fragment
        if (savedInstanceState == null) {
            if (loginSession.isLoggedIn()) {
                loadFragment(new HomeFragment(), "Home", true);
                navigationView.setCheckedItem(R.id.draw_home);
            } else {
                loadFragment(new LoginFragment(), "Login", true);
                navigationView.setCheckedItem(R.id.draw_login);
            }
        }
    }

    @OptIn(markerClass = ExperimentalBadgeUtils.class)
    private void setupCartBadge() {

        badge = BadgeDrawable.create(this);
        badge.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
        badge.setBadgeTextColor(ContextCompat.getColor(this, android.R.color.white));
        badge.setMaxCharacterCount(3); // shows 99+

        BadgeUtils.attachBadgeDrawable(
                badge,
                topAppBar,
                R.id.action_cart
        );

        int userId = loginSession.isLoggedIn()
                ? Integer.parseInt(loginSession.getUserId())
                : 0;

        cartCountLiveData = AppDatabase.getInstance(this)
                .cartDao()
                .getCartCount(userId);

        observeCartCount();
    }
    private void observeCartCount() {

        if (cartCountLiveData == null) {
            Log.e("BADGE", "LiveData is NULL");
            return;
        }

        cartCountLiveData.observe(this, count -> {

            if (badge == null) return;

            if (count == null || count <= 0) {
                badge.setVisible(false);
            } else {
                badge.setNumber(count);
                badge.setVisible(true);

                topAppBar.animate()
                        .scaleX(1.1f)
                        .scaleY(1.1f)
                        .setDuration(150)
                        .withEndAction(() ->
                                topAppBar.animate()
                                        .scaleX(1f)
                                        .scaleY(1f)
                                        .setDuration(150)
                                        .start())
                        .start();
            }
        });
    }

    /** -------------------- AUTOCOMPLETE -------------------- **/
// ================= AUTOCOMPLETE INIT =================
    private void initAutoComplete() {

        searchAuto = findViewById(R.id.searchAuto);

        suggestions = new ArrayList<>();

        adapter = new SearchSuggestionAdapter(this,suggestions);

        searchAuto.setAdapter(adapter);
        searchAuto.setThreshold(1);

        // ⚠️ REQUIRED FOR ANDROID 13
        searchAuto.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && !suggestions.isEmpty()) {
                searchAuto.post(searchAuto::showDropDown);
            }
        });
        searchAuto.setOnClickListener(v -> {
            if (!suggestions.isEmpty()) {
                searchAuto.showDropDown();
            }
        });

        searchAuto.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                searchRunnable = () -> fetchSuggestionsFromAPI(s.toString());
                searchHandler.postDelayed(searchRunnable, 400);
            }
        });

        searchAuto.setOnItemClickListener((parent, view, position, id) -> {
            String value = adapter.getItem(position);
            searchAuto.setText(value);
            searchAuto.setSelection(value.length());
            searchAuto.dismissDropDown();
        });

        searchAuto.setOnEditorActionListener((v, actionId, event) -> {

            // Handle keyboard search / enter
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                            && event.getAction() == KeyEvent.ACTION_DOWN)) {

                String query = searchAuto.getText().toString().trim();

                if (!query.isEmpty()) {
                    openSearchFragment(query);
                    adapter.clear();
                    searchAuto.dismissDropDown();
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchAuto.getWindowToken(), 0);
                }
                return true; // consume event
            }
            return false;
        });

    }

    private void openSearchFragment(String query) {

        Fragment fragment = SearchFragment.newInstance(query);
        loadFragment(fragment,"search fragment",false);

    }


    // ================= API CALL =================
    private void fetchSuggestionsFromAPI(String query) {

        if (query.length() < 2) {
            suggestions.clear();
            adapter.notifyDataSetChanged();
            searchAuto.dismissDropDown();
            return;
        }

        ApiService apiService =
                LoginRetrofitClient.getClient().create(ApiService.class);

        apiService.getSuggestions(query).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {

                if (!response.isSuccessful() || response.body() == null) return;
                    List<String> data = response.body();
                    if (data == null || data.isEmpty()) {
                        adapter.clear();
                        searchAuto.dismissDropDown();
                        return;
                    }
                    adapter.clear();
                    adapter.addAll(data);
                    adapter.notifyDataSetChanged();
                    // ✅ SAFE LOG
                    Log.d("responseNikhil", "Count = " + adapter.getCount());
                    searchAuto.post(() -> {
                        if (searchAuto.hasFocus() && adapter.getCount() > 0) {
                            searchAuto.showDropDown();
                        }
                    });
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.e("SearchAPI", t.getMessage());
            }
        });
    }


    /** -------------------- PINCODE BOTTOM SHEET -------------------- **/
    private void openPincodeBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottomsheet_pincode, null);

        EditText etPincode = view.findViewById(R.id.etPincode);
        ProgressBar progressPincode = view.findViewById(R.id.progressPincode);
        TextView tvPincodeError = view.findViewById(R.id.tvPincodeError);
        Button btnConfirm = view.findViewById(R.id.btnConfirmPincode);

        etPincode.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvPincodeError.setVisibility(View.GONE);
            }
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

            String device = DeviceInfo.getDeviceString(this);
            PincodeRequest request = new PincodeRequest(pincode,"",device,"","");
            ButtonLoaderUtil.showLoading(btnConfirm,progressPincode);

            ApiService apiService = LoginRetrofitClient.getClient().create(ApiService.class);
            apiService.getPincodeStatus(request).enqueue(new Callback<PincodeResponse>() {
                @Override
                public void onResponse(Call<PincodeResponse> call, Response<PincodeResponse> response) {
                    ButtonLoaderUtil.hideLoading(btnConfirm,progressPincode,"Check Pincode");
                    if (response.isSuccessful() && response.body() != null) {

                        PincodeResponse res = response.body();
                        if (res.isStatus() && res.getData() != null && !res.getData().isEmpty()) {
                            PincodeData data = res.getData().get(0);
                            PincodeSession session = new PincodeSession(getApplicationContext());
                            session.clearPincode();
                            session.savePincode(data.getPincode(), data.getCity_name(), data.getDistric_name(), data.getState());
                            tvLocation.setText("Deliver to "+data.getCity_name()+","+data.getDistric_name());
                            bottomSheetDialog.dismiss();
                        } else {
                            tvPincodeError.setText(res.getMessage());
                            tvPincodeError.setVisibility(View.VISIBLE);
                        }
                    } else {
                        tvPincodeError.setText("Something went wrong");
                        tvPincodeError.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<PincodeResponse> call, Throwable t) {
                    ButtonLoaderUtil.hideLoading(btnConfirm,progressPincode,"Check Pincode");
                    tvPincodeError.setText("Network Error");
                    tvPincodeError.setVisibility(View.VISIBLE);
                }
            });
        });

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.show();
    }

    /** -------------------- DRAWER & FRAGMENTS -------------------- **/
    private void openDrawerItem(int id) {
        if (id == R.id.draw_home) loadFragment(new HomeFragment(), "Home", false);
        else if (id == R.id.draw_about_us) loadFragment(new AboutUsFragment(), "About Us", false);
        else if (id == R.id.nav_contact) loadFragment(new ContactUsFragment(), "Contact Us", false);
        else if (id == R.id.draw_term_and_condition) loadFragment(new TermAndConditionFragment(), "Our T&C", false);
        else if (id == R.id.draw_login) loadFragment(new LoginFragment(), "Login", false);
        else if (id == R.id.draw_register) loadFragment(new RegisterFragment(), "Register", false);
        else if (id == R.id.draw_wishlist) loadFragment(new WishListFragment(),"my_wish_list",false);
        else if (id == R.id.draw_logout) {
            loginSession.logout();
            Toast.makeText(this,"Successfully logout",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }
    }

    private void loadFragment(Fragment fragment, String title, boolean add) {
        if(add){
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .add(R.id.container, fragment)
                    .addToBackStack(title)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(title)
                    .commit();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void showHideElementDrawer() {
        Menu menu = navigationView.getMenu();
        if(loginSession.isLoggedIn()){
            menu.findItem(R.id.draw_login).setVisible(false);
            menu.findItem(R.id.draw_register).setVisible(false);
            menu.findItem(R.id.draw_myorders).setVisible(true);
            menu.findItem(R.id.draw_profile).setVisible(true);
            menu.findItem(R.id.draw_logout).setVisible(true);
        } else {
            menu.findItem(R.id.draw_login).setVisible(true);
            menu.findItem(R.id.draw_register).setVisible(true);
            menu.findItem(R.id.draw_myorders).setVisible(false);
            menu.findItem(R.id.draw_profile).setVisible(false);
            menu.findItem(R.id.draw_logout).setVisible(false);
        }
    }

    private void updateSessionName() {
        if(loginSession.isLoggedIn()){
            drawName.setText("Hi! "+loginSession.getUserName());
            drawEmail.setText("");
        } else {
            drawName.setText(R.string.hi_guest);
            drawEmail.setText("");
        }
    }

    private void setLocationSession() {
        PincodeSession session = new PincodeSession(this);
        if(session.hasPincode()){
            tvLocation.setText("Deliver to "+session.getCity()+", "+session.getPincode());
        } else tvLocation.setText("Please change location");
    }



    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        Log.d("currentfragment", String.valueOf(fm.getBackStackEntryCount()));

        if(currentFragment == null|| fm.getBackStackEntryCount() == 1){
            Toast.makeText(this,"Thank You, Shop again",Toast.LENGTH_LONG).show();
            finishAffinity();
        }
        if(currentFragment instanceof HomeFragment){
            Toast.makeText(this,"Thank You, Shop again",Toast.LENGTH_LONG).show();
            finishAffinity();
        } else super.onBackPressed();
    }

    @Override
    public void onCartUpdated() {
        // do nothing — LiveData will auto update
        //updateCartBadge();
    }
    public int[] getCartIconLocation() {

        int[] location = new int[2];

        View cartView = topAppBar.findViewById(R.id.action_cart);

        if (cartView != null) {
            cartView.getLocationOnScreen(location);
        }

        return location;
    }



}
