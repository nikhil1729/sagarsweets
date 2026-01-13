package com.sagarsweets.in;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sagarsweets.in.Adapters.TermsPagerAdapter;

public class TermAndConditionFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    String[] tabTitles = {
            "Privacy Policy",
            "Shipping Policy",
            "Refund & Return",
            "Process Flow"
    };

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
        TermsPagerAdapter adapter = new TermsPagerAdapter(getActivity());
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabTitles[position])
        ).attach();
        return view;
    }
}