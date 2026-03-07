package com.sagarsweets.in.utils;

import com.sagarsweets.in.ApiModel.Address;
import com.sagarsweets.in.ApiModel.PickStoreAddress;
import com.sagarsweets.in.ApiModel.UserDefaultAddress;

public class AddressFormatter {


    // Delivery Address Formatter
    public static String formatDeliveryAddress(UserDefaultAddress userDefaultAddress) {

        if (userDefaultAddress == null || userDefaultAddress.getAddress() == null) {
            return "Address not available";
        }

        StringBuilder builder = new StringBuilder();
        String name = userDefaultAddress.getAddress().getFullName();
        String fullAddress = userDefaultAddress.getAddress().getFullAddress();
        String landMark = userDefaultAddress.getAddress().getLandMark();
        String city = userDefaultAddress.getAddress().getCity();
        String district = userDefaultAddress.getAddress().getDistricName();
        String state = userDefaultAddress.getAddress().getState();
        String pincode = String.valueOf(userDefaultAddress.getAddress().getPincode());
        String mobile = userDefaultAddress.getAddress().getMobileNumber();
        String email = userDefaultAddress.getAddress().getEmailId();

        if (name != null) builder.append(name).append("\n");
        if (fullAddress != null) builder.append(fullAddress).append("\n");
        if (landMark != null) builder.append("Landmark: ").append(landMark).append("\n");

        if (city != null) builder.append(city);
        if (district != null) builder.append(", ").append(district);
        builder.append("\n");

        if (state != null) builder.append(state);
        if (pincode != null) builder.append(" - ").append(pincode);
        builder.append("\n");

        if (mobile != null) builder.append("Mobile: ").append(mobile).append("\n");
        //if (email != null) builder.append("Email: ").append(email);

        return builder.toString();
    }


    // Pickup Address Formatter
    public static String formatPickupAddress(PickStoreAddress pickStoreAddress) {

        if (pickStoreAddress == null || pickStoreAddress.getAddress() == null) {
            return "Store address not available";
        }

        StringBuilder builder = new StringBuilder();

        String storeName = pickStoreAddress.getStoreName();
        String contact = pickStoreAddress.getContactNo();
        String street1 = pickStoreAddress.getAddress().getStreet1();
        String street2 = pickStoreAddress.getAddress().getStreet2();
        String area = pickStoreAddress.getAddress().getPostOfficeList();
        String city = pickStoreAddress.getAddress().getCity();
        String state = pickStoreAddress.getAddress().getState();
        String country = pickStoreAddress.getAddress().getCountry();

        if (storeName != null) builder.append(storeName).append("\n");

        if (street1 != null) builder.append(street1);
        if (street2 != null) builder.append(", ").append(street2);
        builder.append("\n");

        if (area != null) builder.append(area).append(", ");
        if (city != null) builder.append(city).append("\n");

        if (state != null) builder.append(state);
        if (country != null) builder.append(", ").append(country);
        builder.append("\n");

        if (contact != null) builder.append("Contact: ").append(contact);

        return builder.toString();
    }

    public static String formatDeliveryAddressSingle(Address userDefaultAddress) {

        if (userDefaultAddress == null ) {
            return "Address not available";
        }

        StringBuilder builder = new StringBuilder();
        String name = userDefaultAddress.getFullName();
        String fullAddress = userDefaultAddress.getFullAddress();
        String landMark = userDefaultAddress.getLandMark();
        String city = userDefaultAddress.getCity();
        String district = userDefaultAddress.getDistricName();
        String state = userDefaultAddress.getState();
        String pincode = String.valueOf(userDefaultAddress.getPincode());
        String mobile = userDefaultAddress.getMobileNumber();
        String email = userDefaultAddress.getEmailId();

        if (name != null) builder.append(name).append("\n");
        if (fullAddress != null) builder.append(fullAddress).append("\n");
        if (landMark != null) builder.append("Landmark: ").append(landMark).append("\n");

        if (city != null) builder.append(city);
        if (district != null) builder.append(", ").append(district);
        builder.append("\n");

        if (state != null) builder.append(state);
        if (pincode != null) builder.append(" - ").append(pincode);
        builder.append("\n");

        if (mobile != null) builder.append("Mobile: ").append(mobile).append("\n");
        //if (email != null) builder.append("Email: ").append(email);

        return builder.toString();
    }
}