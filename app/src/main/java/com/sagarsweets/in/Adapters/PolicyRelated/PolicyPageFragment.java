package com.sagarsweets.in.Adapters.PolicyRelated;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sagarsweets.in.ApiModel.PolicyItem;
import com.sagarsweets.in.R;

import java.util.ArrayList;

public class PolicyPageFragment extends Fragment {

    private static final String ARG_TYPE = "type";
    private static final String ARG_LIST = "list";

    public static PolicyPageFragment newInstance(String type,
                                                 ArrayList<PolicyItem> list) {
        PolicyPageFragment fragment = new PolicyPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TYPE, type);
        //bundle.putParcelableArrayList(ARG_LIST, list);
        bundle.putSerializable(ARG_LIST, list);

        fragment.setArguments(bundle);
        return fragment;
    }

    private String policyType;
    private ArrayList<PolicyItem> policyList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            policyType = getArguments().getString(ARG_TYPE);
            policyList = (ArrayList<PolicyItem>) getArguments().getSerializable(ARG_LIST);

        }

        if (policyList == null) policyList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_policy_page, container, false);

        RecyclerView rv = view.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        PolicyAdapter adapter = new PolicyAdapter(getContext(), policyList);
        rv.setAdapter(adapter);

        adapter.filterByType(policyType);

        return view;
    }
}


