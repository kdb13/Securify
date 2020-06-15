package com.lina.securify.alerts.instantalert;

import android.accessibilityservice.AccessibilityService;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import androidx.preference.PreferenceManager;

import com.lina.securify.R;
import com.lina.securify.alerts.sendalert.AlertSender;
import com.lina.securify.utils.Utils;
import com.lina.securify.views.dialogs.ConfirmationDialogBuilder;

public class InstantAlertService extends AccessibilityService
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int LONG_PRESS_DURATION = 2000;

    private VolumeLongPressListener listener;

    private AlertSender alertSender;

    private Dialog sendAlertDialog;

    private boolean isListening;

    private SharedPreferences preferences;

    @Override
    protected void onServiceConnected() {

        // Setting the theme for Material Dialogs, when we don't have a Activity Context
        setTheme(R.style.AppTheme);

        preferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        preferences.registerOnSharedPreferenceChangeListener(this);

        listener = new VolumeLongPressListener(this::showDialog, LONG_PRESS_DURATION);

        alertSender = new AlertSender(getApplicationContext());

        sendAlertDialog = ConfirmationDialogBuilder.build(this,
                getString(R.string.send_alert_title),
                getString(R.string.send_alert_msg),
                getString(R.string.button_alert),
                () -> alertSender.send());

        Utils.setWindowToOverlay(sendAlertDialog);

    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {

        // If the KeyEvent is a Volume button press, dispatch the event to listener
        if (isListening && isVolumeKey(event)) {
            event.dispatch(listener, null, null);
            return false;
        }

        return super.onKeyEvent(event);

    }

    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    public void onInterrupt() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(getString(R.string.pref_instant_alert))) {

            isListening = sharedPreferences.getBoolean(key,
                    getResources().getBoolean(R.bool.pref_instant_alert_default));

        }

    }

    private void showDialog() {

        // Show a cofirmation dialog for sending the alert
        if (Utils.canDrawOverlays(this)) {
            sendAlertDialog.show();
        }

    }

    private boolean isVolumeKey(KeyEvent event) {
        return ((event.getAction() == KeyEvent.ACTION_DOWN ||
                event.getAction() == KeyEvent.ACTION_UP) &&
                event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP);
    }

}
