package com.sagarsweets.in.Adapters.PolicyRelated;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sagarsweets.in.R;

public class PolicyFragment extends Fragment {

    private static final String ARG_CONTENT = "content";

    public static PolicyFragment newInstance(String content) {
        PolicyFragment fragment = new PolicyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CONTENT, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_policy_common, container, false);

        TextView tvContent = view.findViewById(R.id.tvContent);

        if (getArguments() != null) {
            tvContent.setText(getArguments().getString(ARG_CONTENT));
        }

        return view;
    }
}
