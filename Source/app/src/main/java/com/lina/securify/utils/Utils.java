package com.lina.securify.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Objects;

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

    /**
     * Trims a phone number and returns the essential 10 digits, without the country code (India)
     * or whitespaces. It won't check if the string contains digits or not. It simply removes
     * whitespaces and the country code (+91 or 91).
     *
     * @param phone The phone number to trim
     * @return The resulting phone number after trimming
     */
    public static String trimPhone(@NonNull String phone) {

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
     * @return true if the app is allowed to <b>Display over other apps</b>, else false
     */
    public static boolean canDrawOverlays(Context context) {
        if (!isSDK(Build.VERSION_CODES.M))
            return true;
        else
            return Settings.canDrawOverlays(context);
    }

    /**
     * Sets the window type for the specified dialog to System Overlay.
     */
    public static void setWindowToOverlay(Dialog dialog) {

        Window window = Objects.requireNonNull(dialog.getWindow());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        } else {
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
    }

    /**
     * In M or above, it checks if the specified permissions are granted by the user or not.
     * @param context Used for checking permissions
     * @param permissions A list of permissions to check
     * @return true if all permissions are granted, else false
     */
    @RequiresApi(Build.VERSION_CODES.M)
    public static boolean arePermissionsGranted(Context context, List<String> permissions) {

        for (String permission : permissions) {
            if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }

        return true;

    }

    /**
     * Checks if the specified SDK version code matches the current build of device
     * @param versionCode The version code to check
     * @return true if matches, else false
     */
    public static boolean isSDK(int versionCode) {
        return Build.VERSION.SDK_INT >= versionCode;
    }

    /**
     * Checks if <b>Display over other apps</b> is granted and the AccessibilityService is running.
     */
    public static boolean areSpecialPermissionsGranted(Context context) {
        return canDrawOverlays(context) && AccessibilityUtils.isEnabled(context);
    }

}
