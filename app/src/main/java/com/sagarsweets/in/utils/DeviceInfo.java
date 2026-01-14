package com.sagarsweets.in.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import com.google.gson.Gson;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

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

}
