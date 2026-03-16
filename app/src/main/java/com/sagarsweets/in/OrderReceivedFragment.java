package com.sagarsweets.in;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.sagarsweets.in.Adapters.OrderItemAdapter;
import com.sagarsweets.in.ApiModel.CheckoutProcessData;
import com.sagarsweets.in.ApiModel.Item;
import com.sagarsweets.in.ApiModel.PodVerifyOtpResponse;

import java.util.List;


public class OrderReceivedFragment extends Fragment {
    MaterialCardView cardItems;
    TextView txtTitle,txtOrderId,txtDeliveryDate,txtTimeSlot;
    TextView txtSubtotal,txtDelivery,txtDiscount,txtTotal;
    TextView txtPaymentMethod;
    MaterialCardView cardAddress;
    TextView txtAddress;
    MaterialCardView cardPickupAddress;
    TextView txtStorePickupAddress;
    RecyclerView recyclerOrderedItems;
    LinearLayout layoutCoupon;
    TextView txtCouponCode,txtCouponSaving;
    PodVerifyOtpResponse order;
    public OrderReceivedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_received, container, false);
        if(getArguments()!=null){
            String json = getArguments().getString("order_data");
            Gson gson = new Gson();
            order = gson.fromJson(json, PodVerifyOtpResponse.class);
        }
        initViews(view);
        settextView();
        backSpaceClicked();
        // Inflate the layout for this fragment
        return view;
    }

    private void backSpaceClicked() {
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {

                        Fragment homeFragment = new HomeFragment();

                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, homeFragment)
                                .commit();
                    }
                });
    }

    private void settextView() {
        txtOrderId.setText(order.getTxn_id());
        txtDeliveryDate.setText(order.getDelivery_date());
        txtTimeSlot.setText(order.getDelivery_time_slot());
        txtPaymentMethod.setText(order.getPayment_gateway());
        String deliveryType = order.getDelivery_type();

        boolean isHomeDelivery = "HOME_DELIVERY".equals(deliveryType);

        cardPickupAddress.setVisibility(isHomeDelivery ? View.GONE : View.VISIBLE);
        cardAddress.setVisibility(isHomeDelivery ? View.VISIBLE : View.GONE);
        if (isHomeDelivery) {
            if (order.getDelivery_address() != null) {
                String address = "Name: " + order.getDelivery_address().getName() + "\n" +
                        "Phone No: " + order.getDelivery_address().getMobile() + "\n" +
                        "Address: " + order.getDelivery_address().getFull_address();
                txtAddress.setText(address);
            }
        } else {
            if (order.getPickupAddress() != null) {
                String pickupAddress = "Store Name: " + order.getPickupAddress().getStoreName() +
                        "\nContact No: " + order.getPickupAddress().getContactNo() +
                        "\nAddress: " + order.getPickupAddress().getAddress();
                txtStorePickupAddress.setText(pickupAddress);
            }
        }

        recyclerOrderedItems.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Item> itemList = order.getItem();
        OrderItemAdapter adapter = new OrderItemAdapter(itemList);
        recyclerOrderedItems.setAdapter(adapter);


        String coupon = order.getCoupon_code();
        if(coupon != null){
            txtCouponCode.setText("Coupon: "+coupon);
            txtCouponSaving.setText("You saved ₹"+order.getCoupon_discount()+" with this coupon");
        }else{
            txtCouponSaving.setVisibility(View.GONE);
            layoutCoupon.setVisibility(View.GONE);
        }
        double discount = Double.parseDouble(order.getCoupon_discount());
        double delivery = Double.parseDouble(order.getDelivery_charge());
        double product_total = order.getTotal_product_cost();
        double sub_total = product_total+delivery-discount;

        txtDiscount.setText("Discount : -₹"+discount);
        txtDelivery.setText("Delivery : ₹"+delivery);
        txtSubtotal.setText("Subtotal : ₹"+product_total);
        txtTotal.setText("Total : ₹"+sub_total);

    }

    private void initViews(View view) {
        txtStorePickupAddress = view.findViewById(R.id.txtStorePickupAddress);
        cardPickupAddress = view.findViewById(R.id.cardPickupAddress);
        txtAddress = view.findViewById(R.id.txtAddress);
        cardAddress = view.findViewById(R.id.cardAddress);
        txtPaymentMethod = view.findViewById(R.id.txtPaymentMethod);
        txtTotal = view.findViewById(R.id.txtTotal);
        txtDiscount = view.findViewById(R.id.txtDiscount);
        txtDelivery = view.findViewById(R.id.txtDelivery);
        txtSubtotal = view.findViewById(R.id.txtSubtotal);
        txtTimeSlot = view.findViewById(R.id.txtTimeSlot);
        txtDeliveryDate = view.findViewById(R.id.txtDeliveryDate);
        recyclerOrderedItems = view.findViewById(R.id.recyclerOrderedItems);
        cardItems = view.findViewById(R.id.cardItems);
        txtOrderId = view.findViewById(R.id.txtOrderId);
        txtTitle = view.findViewById(R.id.txtTitle);
        layoutCoupon = view.findViewById(R.id.layoutCoupon);
        txtCouponSaving = view.findViewById(R.id.txtCouponSaving);
        txtCouponCode = view.findViewById(R.id.txtCouponCode);
    }
}