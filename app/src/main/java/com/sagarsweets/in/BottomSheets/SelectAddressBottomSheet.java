package com.sagarsweets.in.BottomSheets;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sagarsweets.in.Adapters.AddressAdapter;
import com.sagarsweets.in.ApiModel.Address;
import com.sagarsweets.in.R;

import java.util.ArrayList;
import java.util.List;

public class SelectAddressBottomSheet extends BottomSheetDialogFragment {

    RecyclerView recyclerView;
    AddressAdapter adapter;
    private List<Address> addressList = new ArrayList<>();

    OnAddressSelected listener;

    public interface OnAddressSelected{
        void onSelected(Address address);
    }


    public SelectAddressBottomSheet(List<Address> list, OnAddressSelected listener){
        this.addressList = list;
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottomsheet_select_address, container, false);

        recyclerView = view.findViewById(R.id.addressRecycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Log.d("addressList","size-"+addressList.size());
        adapter = new AddressAdapter(addressList, address -> {

            listener.onSelected(address);
            dismiss();

        });

        recyclerView.setAdapter(adapter);

        return view;
    }
}
