package com.lina.securify.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.regex.Pattern;

public class Utils {

    public static final String[] appPermissions = new String[] {
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    public static String getTextInside(TextInputLayout inputLayout) {
        return Objects.requireNonNull(inputLayout.getEditText())
                .getText()
                .toString();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void changeStatusBarColor(Activity activity, int color) {

        activity.getWindow()
                .setStatusBarColor(ContextCompat.getColor(activity, color));

    }

    public static String trimPhone(String phone) {

        StringBuilder string = new StringBuilder(phone);

        int index;

        while ( (index = string.indexOf(" ")) != -1 )
            string.delete(index, index + 1);

        if (string.length() > 10) {

            if ( (index = string.indexOf("+91")) == 0 ) {

                string.delete(index, index + 3);

            } else if ( (index = string.indexOf("91")) == 0 ) {

                string.delete(index, index + 2);

            }
        }

        return string.toString();
    }

    public static boolean isM() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
    }

    public static boolean canDrawOverlays(Context context) {
        if (isM())
            return true;
        else
            return Settings.canDrawOverlays(context);
    }

    public static boolean arePermissionsGranted(Context context, String[] permissions) {

        for (String permission : permissions) {

            if (ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_DENIED)
                return false;

        }

        return true;
    }
}
