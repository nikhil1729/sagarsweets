package com.sagarsweets.in;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.sagarsweets.in.Adapters.MyOrderAdapter;
import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.MyOrderRequest;
import com.sagarsweets.in.ApiModel.MyOrderResponse;
import com.sagarsweets.in.ApiModel.OrderData;
import com.sagarsweets.in.Session.LoginSession;
import com.sagarsweets.in.utils.CustomToast;
import com.sagarsweets.in.utils.DeviceInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyOrderFragment extends Fragment {

    RecyclerView recyclerOrders;
    ImageView btnFilter;
    ApiService apiService;
    LoginSession loginSession;
    int limit = 10;
    int page = 1;
    String search;
    String order_status;
    String first_date,second_date;
    boolean isLoading = false;
    boolean isLastPage = false;
    List<OrderData> list = new ArrayList<>();
    MyOrderAdapter adapter;
    BottomSheetDialog bottomSheetDialog;
    ShimmerFrameLayout shimmerLayout;
    TextView txtEmpty;
    ChipGroup chipGroupFilters;
    Chip chipStatus, chipDate;
    EditText edtSearch;
    public MyOrderFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_order, container, false);
        initViews(view);
        filterCalling(); // filter clicked
        chipGroupFunction();
        searchByTxnId();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerOrders.setLayoutManager(layoutManager);

        adapter = new MyOrderAdapter(getContext(), list,
                loginSession.getUserId(), DeviceInfo.getDeviceString(getContext()));
        recyclerOrders.setAdapter(adapter);
        loadMyOrders(page, order_status, first_date, second_date, search);
        // Pagination Scroll Listener
        recyclerOrders.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                super.onScrolled(rv, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                        page++;
                        loadMyOrders(page, order_status, first_date, second_date, search);
                    }
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void searchByTxnId() {
        edtSearch.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
                    || actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE
                    || (event != null
                    && event.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER
                    && event.getAction() == android.view.KeyEvent.ACTION_DOWN)) {
                page = 1;
                search = edtSearch.getText().toString().trim();
                loadMyOrders(page,order_status,first_date,second_date,search);
                return true;
            }

            return false;
        });
    }

    private void chipGroupFunction() {
        chipStatus.setOnCloseIconClickListener(v -> {

            order_status = "";

            chipStatus.setVisibility(View.GONE);

            updateFilterUI();

            page = 1;
            loadMyOrders(page, order_status, first_date, second_date, search);
        });

        chipDate.setOnCloseIconClickListener(v -> {

            first_date = "";
            second_date = "";

            chipDate.setVisibility(View.GONE);

            updateFilterUI();

            page = 1;
            loadMyOrders(page, order_status, first_date, second_date, search);
        });
    }

    private void updateFilterUI() {

        boolean hasFilter = false;

        // Status Filter
        if (order_status != null && !order_status.isEmpty()) {

            chipStatus.setVisibility(View.VISIBLE);
            chipStatus.setText(order_status);

            hasFilter = true;

        } else {
            chipStatus.setVisibility(View.GONE);
        }

        // Date Filter
        if (first_date != null && !first_date.isEmpty()
                && second_date != null && !second_date.isEmpty()) {

            chipDate.setVisibility(View.VISIBLE);
            chipDate.setText(first_date + " - " + second_date);

            hasFilter = true;

        } else {
            chipDate.setVisibility(View.GONE);
        }

        chipGroupFilters.setVisibility(hasFilter ? View.VISIBLE : View.GONE);
    }

    private void filterCalling() {
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog(getContext());
                View sheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_filter_myorder, null);

                RadioGroup radioGroup = sheetView.findViewById(R.id.radioStatus);
                MaterialButton btnReset = sheetView.findViewById(R.id.btnReset);
                MaterialButton btnApply = sheetView.findViewById(R.id.btnApply);
                TextInputEditText etFromDate = sheetView.findViewById(R.id.etFromDate);
                TextInputEditText etToDate = sheetView.findViewById(R.id.etToDate);
                setDatePicker(etFromDate,etToDate);// setting date picker

                btnReset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();

                        order_status = "";
                        first_date = "";
                        second_date = "";
                        isLastPage = false;
                        isLoading = false;
                        page = 1;
                        list.clear();
                        adapter.notifyDataSetChanged();
                        updateFilterUI();
                        loadMyOrders(page,order_status,first_date,second_date, search);
                    }
                });
                btnApply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Get selected RadioButton ID
                        int selectedId = radioGroup.getCheckedRadioButtonId();

                        if (selectedId != -1) {
                            RadioButton radioButton = sheetView.findViewById(selectedId);

                            // Get selected text
                            order_status = radioButton.getText().toString();

                        }else{
                            order_status = "";
                        }
                        isLastPage = false;
                        isLoading = false;
                        first_date   = etFromDate.getText().toString();
                        second_date  = etToDate.getText().toString();
                        page = 1;
                        updateFilterUI();
                        list.clear();
                        adapter.notifyDataSetChanged();
                        loadMyOrders(page,order_status,first_date,second_date, search);
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show();
            }
        });
    }

    private void setDatePicker(TextInputEditText etFromDate, TextInputEditText etToDate) {

        final Calendar fromCalendar = Calendar.getInstance();

        etFromDate.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            DatePickerDialog fromDatePicker = new DatePickerDialog(
                    getContext(),
                    (view, year, month, dayOfMonth) -> {

                        // Save selected FROM date
                        fromCalendar.set(year, month, dayOfMonth);

                        String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                        etFromDate.setText(date);
                        first_date = date;
                        // Clear TO date if FROM changes
                        etToDate.setText("");

                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            // Disable future dates
            fromDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());

            // Allow only last 90 days
            Calendar minDate = Calendar.getInstance();
            minDate.add(Calendar.DAY_OF_YEAR, -90);

            fromDatePicker.getDatePicker().setMinDate(minDate.getTimeInMillis());

            fromDatePicker.show();
        });

        etToDate.setOnClickListener(v -> {

            // Check if FROM date selected
            if (etFromDate.getText() == null || etFromDate.getText().toString().isEmpty()) {
                etFromDate.setError("Select From Date first");
                return;
            }

            Calendar calendar = Calendar.getInstance();

            DatePickerDialog toDatePicker = new DatePickerDialog(
                    getContext(),
                    (view, year, month, dayOfMonth) -> {

                        String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                        etToDate.setText(date);
                        second_date = date;
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            // TO date cannot be before FROM date
            toDatePicker.getDatePicker().setMinDate(fromCalendar.getTimeInMillis());

            // Disable future dates
            toDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());

            toDatePicker.show();
        });
    }


    private void loadMyOrders(int page, String order_status, String first_date, String second_date, String search) {
        isLoading = true;
        if (page == 1) {
            shimmerLayout.setVisibility(View.VISIBLE);
            shimmerLayout.startShimmer();

            recyclerOrders.setVisibility(View.GONE);
        }
        MyOrderRequest myOrderRequest = new MyOrderRequest(loginSession.getUserId(),
                limit,page, this.search, order_status, first_date, second_date);
        apiService.getMyOrder(myOrderRequest).enqueue(new Callback<MyOrderResponse>() {
            @Override
            public void onResponse(Call<MyOrderResponse> call, Response<MyOrderResponse> response) {
                if(response.body() != null){
                    shimmerLayout.stopShimmer();
                    shimmerLayout.setVisibility(View.GONE);

                    recyclerOrders.setVisibility(View.VISIBLE);
                    MyOrderResponse res = response.body();
                    if(res.isStatus() && res.getData() != null){
                        isLoading = false;
                        List<OrderData> newData = response.body().getData();
                        if (page == 1) {

                            list.clear();
                            list.addAll(newData);

                            adapter.notifyDataSetChanged();

                        } else {

                            adapter.addData(newData);
                        }
                        if (page >= response.body().getTotal_pages()) {
                            isLastPage = true;
                        }
                    }else{
                        // show error
                        txtEmpty.setVisibility(View.VISIBLE);
                        recyclerOrders.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyOrderResponse> call, Throwable t)
            {
                isLoading = false;

                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);

                if (list.isEmpty()) {

                    txtEmpty.setVisibility(View.VISIBLE);
                    recyclerOrders.setVisibility(View.GONE);

                } else {

                    recyclerOrders.setVisibility(View.VISIBLE);
                }
                CustomToast.error(getContext(),t.getMessage());

            }
        });
    }

    private void initViews(View view) {
        recyclerOrders = view.findViewById(R.id.recyclerOrders);
        btnFilter = view.findViewById(R.id.btnFilter);
        apiService = LoginRetrofitClient
                .getClient()
                .create(ApiService.class);
        loginSession = new LoginSession(getContext());
        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        txtEmpty = view.findViewById(R.id.txtEmpty);
        chipGroupFilters = view.findViewById(R.id.chipGroupFilters);
        chipStatus = view.findViewById(R.id.chipStatus);
        chipDate = view.findViewById(R.id.chipDate);
        edtSearch = view.findViewById(R.id.edtSearch);
    }
}