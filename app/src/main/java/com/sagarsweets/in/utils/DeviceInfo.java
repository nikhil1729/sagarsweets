package com.sagarsweets.in.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import com.google.gson.Gson;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class DeviceInfo {
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID
        );
    }

    public static String getDeviceModel() {
        return Build.MANUFACTURER + " " + Build.MODEL;
    }

    public static String getOsVersion() {
        return "Android " + Build.VERSION.RELEASE;
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "0.0.0.0";
    }
    public static String getDeviceString(Context context) {
        Map<String, String> device_data = new HashMap<>();
        device_data.put("ip", getLocalIpAddress());
        device_data.put("device_id",DeviceInfo.getDeviceId(context));
        device_data.put("device_model",DeviceInfo.getDeviceModel());
        device_data.put("os_version",DeviceInfo.getOsVersion());
        Gson gson = new Gson();
        return gson.toJson(device_data);
    }

    public static String getTimeAgo(String dateTime) {
        try {
            SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

            long time = sdf.parse(dateTime).getTime();
            long now = System.currentTimeMillis();

            long diff = now - time;

            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if (seconds < 60) {
                return "Just now";
            } else if (minutes < 60) {
                return minutes + " min ago";
            } else if (hours < 24) {
                return hours + " hour ago";
            } else if (days < 7) {
                return days + " day ago";
            } else if (days < 30) {
                return (days / 7) + " week ago";
            } else if (days < 365) {
                return (days / 30) + " month ago";
            } else {
                return (days / 365) + " year ago";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "No time";
        }
    }

    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }

        String[] parts = email.split("@");
        String username = parts[0];
        String domain = parts[1];

        if (username.length() <= 4) {
            return "xxxx@" + domain;
        }

        String visible = username.substring(0, 4);
        return visible + "xxxx@" + domain;
    }


}
