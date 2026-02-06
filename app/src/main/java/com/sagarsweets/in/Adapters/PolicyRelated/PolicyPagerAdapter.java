package com.sagarsweets.in.Adapters.PolicyRelated;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.sagarsweets.in.ApiModel.PolicyItem;

import java.util.ArrayList;
import java.util.List;

public class PolicyPagerAdapter extends FragmentStateAdapter {

    private List<String> policyTypes;
    private List<PolicyItem> fullList;

    public PolicyPagerAdapter(@NonNull Fragment fragment,
                              List<String> policyTypes,
                              List<PolicyItem> fullList) {
        super(fragment);
        this.policyTypes = policyTypes;
        this.fullList = fullList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return PolicyPageFragment.newInstance(
                policyTypes.get(position),
                new ArrayList<PolicyItem>(fullList)
        );
    }

    @Override
    public int getItemCount() {
        return policyTypes.size();
    }
}

