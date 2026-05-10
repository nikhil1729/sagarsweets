package com.sagarsweets.in;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sagarsweets.in.Adapters.MyOrderAdapter;
import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.MyOrderRequest;
import com.sagarsweets.in.ApiModel.MyOrderResponse;
import com.sagarsweets.in.ApiModel.OrderData;
import com.sagarsweets.in.Session.LoginSession;

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
    public MyOrderFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_order, container, false);
        initViews(view);
        filterCalling(); // filter clicked
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerOrders.setLayoutManager(layoutManager);

        adapter = new MyOrderAdapter(getContext(), list);
        recyclerOrders.setAdapter(adapter);
        loadMyOrders(page);
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
                        loadMyOrders(page);
                    }
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void filterCalling() {
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
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
                        loadMyOrders(1);
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

                            Toast.makeText(getContext(), order_status, Toast.LENGTH_SHORT).show();
                        }else{
                            order_status = "";
                            Toast.makeText(getContext(),"hello",Toast.LENGTH_LONG).show();
                        }

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


    private void loadMyOrders(int page) {
        MyOrderRequest myOrderRequest = new MyOrderRequest(loginSession.getUserId(),limit,page,search,order_status,first_date,second_date);
        apiService.getMyOrder(myOrderRequest).enqueue(new Callback<MyOrderResponse>() {
            @Override
            public void onResponse(Call<MyOrderResponse> call, Response<MyOrderResponse> response) {
                if(response.body() != null){
                    MyOrderResponse res = response.body();
                    if(res.isStatus()){
                        isLoading = false;
                        List<OrderData> newData = response.body().getData();
                        if (page == 1) {
                            list.clear();
                        }
                        adapter.addData(newData);
                        if (page >= response.body().getTotal_pages()) {
                            isLastPage = true;
                        }
                    }else{
                        // show error

                    }
                }
            }

            @Override
            public void onFailure(Call<MyOrderResponse> call, Throwable t) {
                isLoading = false;
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
    }
}