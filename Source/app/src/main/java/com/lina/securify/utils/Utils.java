package com.lina.securify.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.lina.securify.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Utils {

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

        while ((index = string.indexOf(" ")) != -1)
            string.delete(index, index + 1);

        if (string.length() > 10) {

            if ((index = string.indexOf("+91")) == 0) {

                string.delete(index, index + 3);

            } else if ((index = string.indexOf("91")) == 0) {

                string.delete(index, index + 2);

            }
        }

        return string.toString();
    }

    /**
     * @return true if the device is running Android M (6.0) or above, else false
     */
    public static boolean isM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * @return true if the app is allowed to <b>Display over other apps</b>, else false
     */
    public static boolean canDrawOverlays(Context context) {
        if (!isM())
            return true;
        else
            return Settings.canDrawOverlays(context);
    }


    /**
     * Sets the window type for the specified dialog to System Overlay.
     */
    public static void setWindowToOverlay(Dialog dialog) {

        if (Build.VERSION.SDK_INT >= 26)
            Objects.requireNonNull(dialog.getWindow())
                    .setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        else
            Objects.requireNonNull(dialog.getWindow())
                    .setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

    }

    public static String formatLocation(double latitude, double longitude) {
        return latitude + "," + longitude;
    }

    @RequiresApi(Build.VERSION_CODES.M)
    public static boolean checkPermissions(Context context, String ... permissions) {

        for (String permission : permissions) {
            if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED)
                return false;
        }

        return true;

    }

    /**
     * Wraps up the special access checks.
     * @return true if granted, else false
     */
    public static boolean isSpecialAccessGranted(Context context) {
        return Utils.canDrawOverlays(context) &&
                AccessibilityUtils.isEnabled(context);
    }
}
