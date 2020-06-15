package com.lina.securify.utils;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.view.accessibility.AccessibilityManager;

import com.lina.securify.alerts.instantalert.InstantAlertService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccessibilityUtils {

    /**
     * Checks if the "Instant Alert" Accessibility service is running (enabled) or not.
     * @return true if enabled, else false
     */
    public static boolean isEnabled(Context context) {

        AccessibilityManager manager = (AccessibilityManager) context
                .getSystemService(Context.ACCESSIBILITY_SERVICE);

        List<AccessibilityServiceInfo> services = manager.getEnabledAccessibilityServiceList(
                AccessibilityServiceInfo.FEEDBACK_GENERIC
        );

        String serviceString = buildServiceString(context);

        for (AccessibilityServiceInfo info : services) {
            if (info.getId().equals(serviceString))
                return true;
        }

        return false;

    }

    /**
     * @return The service string needed while checking the list of enabled services.
     */
    private static String buildServiceString(Context context) {

        String app = context.getPackageName();
        StringBuilder serviceStr = new StringBuilder(InstantAlertService.class.getName());

        Matcher matcher = Pattern.compile("(" + app + ")" + ".+").matcher(serviceStr);

        if (matcher.find()) {
            serviceStr.insert(matcher.end(1), "/");
        }

        return serviceStr.toString();

    }

}
