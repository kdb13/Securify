package com.lina.securify.services;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lina.securify.R;
import com.lina.securify.VolumeLongPressHandler;
import com.lina.securify.sendalert.SendAlertTasks;
import com.lina.securify.utils.Utils;

/**
 * We will use this {@link AccessibilityService} to listen to Volume long press events,
 * even when app is not open.
 */
public class AlertService extends AccessibilityService {

    private static final String TAG = AlertService.class.getSimpleName();

    private VolumeLongPressHandler handler;

    /**
     * The Volume key (up or down) which will be listened for long press events.
     */
    private static final int VOLUME_KEY = KeyEvent.KEYCODE_VOLUME_UP;

    /**
     * The long press duration of Volume key in milliseconds.
     */
    private static final int LONG_PRESS_DURATION = 2500;

    /**
     * Determines whether the service is listening to Volume button long press events or not.
     */
    public static boolean isListening;

    @Override
    protected void onServiceConnected() {

        setTheme(R.style.AppTheme);
        handler = new VolumeLongPressHandler(this::showDialog, LONG_PRESS_DURATION);

    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {

        // If the KeyEvent is a Volume button press, dispatch the event to handler
        if (isListening && isVolumeKey(event)) {
            event.dispatch(handler, null, null);
            return false;
        }

        return super.onKeyEvent(event);
    }

    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    public void onInterrupt() {

    }

    /**
     * Shows the dialog for confirmation of sending the alert.
     */
    private void showDialog() {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder
                .setTitle(R.string.alert_volunteers)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {

                    // Check if required permissions are granted
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if (!Utils.checkPermissions(this,
                                Manifest.permission.SEND_SMS,
                                Manifest.permission.ACCESS_FINE_LOCATION)) {

                            Utils.showToast(this, "Missing required permissions!");
                            return;

                        }
                    }

                    SendAlertTasks.begin(this);

                })
                .setNegativeButton(android.R.string.cancel, null)
                .setCancelable(false);

        AlertDialog dialog = builder.create();
        Utils.setWindowToOverlay(dialog);

        dialog.show();

    }

    /**
     * Checks if the {@link KeyEvent} is for a Volume key (UP or DOWN)
     *
     * @param event The {@link KeyEvent} to check
     * @return true on success, else false
     */
    private boolean isVolumeKey(KeyEvent event) {

        return ((event.getAction() == KeyEvent.ACTION_DOWN ||
                event.getAction() == KeyEvent.ACTION_UP) &&
                event.getKeyCode() == VOLUME_KEY);

    }

}
