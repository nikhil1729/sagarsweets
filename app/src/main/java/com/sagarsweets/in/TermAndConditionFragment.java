package com.sagarsweets.in;

import static java.security.Policy.getPolicy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sagarsweets.in.Adapters.PolicyRelated.PolicyPagerAdapter;
import com.sagarsweets.in.ApiControllers.OtpRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.PolicyItem;
import com.sagarsweets.in.ApiModel.PolicyResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermAndConditionFragment extends Fragment {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private List<PolicyItem> policyList = new ArrayList<>();

    public TermAndConditionFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view =  inflater.inflate(R.layout.fragment_term_and_condition, container, false);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        fetchPolicies();
        return view;
    }

    private void fetchPolicies() {
        ApiService apiService = OtpRetrofitClient.getApiService();
        Call<PolicyResponse> call = apiService.getPolicy();

        call.enqueue(new Callback<PolicyResponse>() {
            @Override
            public void onResponse(Call<PolicyResponse> call, Response<PolicyResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    policyList = response.body().getResult();
                    setupTabs(policyList);
                }
            }

            @Override
            public void onFailure(Call<PolicyResponse> call, Throwable t) {
                // handle error
            }
        });

    }
    private void setupTabs(List<PolicyItem> list) {

        List<String> tabTitles = new ArrayList<>();

        for (PolicyItem item : list) {
            if (!tabTitles.contains(item.getType())) {
                tabTitles.add(item.getType());
            }
        }

        PolicyPagerAdapter adapter =
                new PolicyPagerAdapter(this, tabTitles, list);

        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabTitles.get(position))
        ).attach();
    }
}

