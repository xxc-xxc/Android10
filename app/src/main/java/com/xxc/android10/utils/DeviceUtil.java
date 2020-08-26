package com.xxc.android10.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

/**
 * Create By xxc
 * Date: 2020/8/26 14:17
 * Desc:
 */
public class DeviceUtil {
    private static String imei;
    @SuppressLint({"CheckResult", "HardwareIds"})
    public static String getIMEI(Context context, FragmentActivity fragmentActivity) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
        // 检验权限
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android10及以上
                PermissionUtil.getRxPermissions(fragmentActivity)
                        .request(Manifest.permission.READ_PHONE_STATE)
                        .subscribe(granted -> {
                           if (granted) {
//                               imei = telephonyManager.getImei();
//                               if (TextUtils.isEmpty(imei)) {
//                                   imei = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//                               }
                               imei = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                           } else {
                               imei = null;
                               Toast.makeText(context, "您取消了授权", Toast.LENGTH_SHORT).show();
                           }
                        });
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // [Android8 - Android10)
                PermissionUtil.getRxPermissions(fragmentActivity)
                        .request(Manifest.permission.READ_PHONE_STATE)
                        .subscribe(granted -> {
                            if (granted) {
                                imei = telephonyManager != null ? telephonyManager.getImei() : null;
                            } else {
                                imei = null;
                                Toast.makeText(context, "您取消了授权", Toast.LENGTH_SHORT).show();
                            }
                        });

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                // [Android6 - Android8)
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    PermissionUtil.getRxPermissions(fragmentActivity)
                            .request(Manifest.permission.READ_PHONE_STATE)
                            .subscribe(granted -> {
                                if (granted) {
                                    imei = telephonyManager != null ? telephonyManager.getDeviceId() : null;
                                } else {
                                    imei = null;
                                    Toast.makeText(context, "您取消了授权", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    imei = telephonyManager != null ? telephonyManager.getDeviceId() : null;
                }
            } else {
                // Android6以下
                imei = telephonyManager != null ? telephonyManager.getDeviceId() : null;
            }

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android10及以上
//                imei = telephonyManager.getImei();
//                if (TextUtils.isEmpty(imei)) {
//                    imei = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//                }
                imei = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // [Android8 - Android10)
                imei = telephonyManager != null ? telephonyManager.getImei() : null;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                // [Android6 - Android8)
                imei = telephonyManager != null ? telephonyManager.getDeviceId() : null;
            } else {
                // Android6以下
                imei = telephonyManager != null ? telephonyManager.getDeviceId() : null;
            }

        }
        return imei;

    }

}
