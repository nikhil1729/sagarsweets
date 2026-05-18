package com.sagarsweets.in.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.Address;
import com.sagarsweets.in.ApiModel.AddressSetDefaultRequest;
import com.sagarsweets.in.ApiModel.addressSetDefaultResponse;
import com.sagarsweets.in.R;
import com.sagarsweets.in.utils.ButtonLoaderUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressProfileAdapter extends RecyclerView.Adapter<AddressProfileAdapter.ViewHolder>{
    Context context;
    List<Address> list;
    ApiService apiService;
    String userId;
    public AddressProfileAdapter(Context context, List<Address> list, String userId) {
        this.context = context;
        this.list = list;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_address_profile, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //AddressProfileModel model = list.get(position);

        Address model = list.get(position);
        String name = model.getFullName();;

        String[] parts = name.split(" ");

        String initials = "";

        for (String part : parts) {
            if (!part.isEmpty()) {
                initials += part.substring(0, 1).toUpperCase();
            }
        }

        //System.out.println(initials); // TG
        holder.tvAvatar.setText(initials);
        /*holder.tvName.setText(
                model.getFullName() + " (" + model.getEmailId() + ")"
        );*/
        holder.tvName.setText(
                model.getFullName().toUpperCase());
        holder.tvAddressType.setText(model.getAddressType());

        holder.tvMobile.setText("Mobile: " + model.getMobileNumber());

        //holder.tvEmail.setText("Email: " + model.getEmailId());

        String fullAddress =
                model.getFullAddress() + ", "
                        + model.getCity() + ", "
                        + model.getDistricName() + ", "
                        + model.getState() + " - "
                        + model.getPincode();

        holder.tvAddress.setText(fullAddress);

        holder.tvLandmark.setText("Landmark: " + model.getLandMark());
        if(model.getIsDefault() == 0){
            holder.tvSetDefault.setVisibility(View.VISIBLE);
            holder.tvDefault.setVisibility(View.GONE);
        }else{
            holder.tvSetDefault.setVisibility(View.GONE);
            holder.tvDefault.setVisibility(View.VISIBLE);
        }
        holder.tvSetDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer addId = model.getAddressId();
                AddressSetDefaultRequest addressSetDefaultRequest = new AddressSetDefaultRequest(addId,userId);
                apiService = LoginRetrofitClient
                        .getClient()
                        .create(ApiService.class);
                Call<addressSetDefaultResponse> call =
                        apiService.setDefaultAddress(addressSetDefaultRequest);
                ButtonLoaderUtil.showLoadingText(holder.tvSetDefault,holder.progressDefault);
                call.enqueue(new Callback<addressSetDefaultResponse>() {
                    @Override
                    public void onResponse(Call<addressSetDefaultResponse> call, Response<addressSetDefaultResponse> response) {
                        // here if status true refresh recyclerview
                        ButtonLoaderUtil.hideLoadingText(holder.tvSetDefault,holder.progressDefault,"SET DEFAULT");
                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().getStatus()) {

                            // Reset all addresses
                            for (Address address : list) {
                                address.setIsDefault(0);
                            }

                            // Set clicked item as default
                            model.setIsDefault(1);

                            // Refresh RecyclerView
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<addressSetDefaultResponse> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName,tvMobile,tvSetDefault,tvAddress,tvLandmark,tvAddressType,tvDefault,tvAvatar;
        TextView tvMakeDefault;
        ProgressBar progressDefault;
        LinearLayout ll_swipe;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAddressType = itemView.findViewById(R.id.tvType);
            tvName = itemView.findViewById(R.id.tvName);
            tvMobile = itemView.findViewById(R.id.tvPhone);
            tvAvatar = itemView.findViewById(R.id.tvAvatar);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvLandmark = itemView.findViewById(R.id.tvLandmark);
            tvSetDefault = itemView.findViewById(R.id.tvSetDefault);
            tvDefault = itemView.findViewById(R.id.tvDefault);
            progressDefault = itemView.findViewById(R.id.progressDefault);
            //ll_swipe = itemView.findViewById(R.id.ll_swipe);
        }
    }
}
