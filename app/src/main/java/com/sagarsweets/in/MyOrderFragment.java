package com.sagarsweets.in;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sagarsweets.in.Adapters.MyOrderAdapter;
import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.MyOrderRequest;
import com.sagarsweets.in.ApiModel.MyOrderResponse;
import com.sagarsweets.in.ApiModel.OrderData;
import com.sagarsweets.in.Session.LoginSession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyOrderFragment extends Fragment {

    RecyclerView recyclerOrders;
    ApiService apiService;
    LoginSession loginSession;
    int limit = 10;
    int page = 1;
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

    private void loadMyOrders(int page) {
        MyOrderRequest myOrderRequest = new MyOrderRequest(loginSession.getUserId(),limit,page,"");
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
        apiService = LoginRetrofitClient
                .getClient()
                .create(ApiService.class);
        loginSession = new LoginSession(getContext());
    }
}