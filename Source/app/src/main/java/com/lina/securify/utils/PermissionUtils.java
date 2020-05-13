package com.lina.securify.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.common.util.ArrayUtils;

import java.util.List;

@RequiresApi(Build.VERSION_CODES.M)
public class PermissionUtils {

    /**
     * @return A List of runtime permissions required by the app
     */
    private static List<String> getAppPermissions() {

        String[] appPermissions = new String[]{
                Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        List<String> permissions = ArrayUtils.toArrayList(appPermissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);

        return permissions;

    }

    /**
     * Checks if the required runtime permissions are granted by the user.
     * @param context Required to check permissions
     * @return true if granted, otherwise false
     */
    public static boolean areGranted(Context context) {

        for (String permission : getAppPermissions()) {
            if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED)
                return false;
        }

        return true;

    }

}
