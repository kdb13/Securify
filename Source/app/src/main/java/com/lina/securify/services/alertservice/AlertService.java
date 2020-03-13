package com.lina.securify.services.alertservice;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import com.lina.securify.R;
import com.lina.securify.services.alertservice.listeners.BaseListener;
import com.lina.securify.services.alertservice.listeners.VolumeLongPressListener;
import com.lina.securify.utils.constants.Constants;
import com.lina.securify.utils.constants.IntentActions;
import com.lina.securify.views.dialogs.SendAlertDialog;

public class AlertService extends AccessibilityService {

    private static final String TAG = AlertService.class.getSimpleName();

    private BaseListener listener;
    private boolean isListening = true;

    private BroadcastReceiver stateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null &&
                    intent.getAction().equals(IntentActions.ACTION_ALERT_SERVICE_STATE_CHANGED)) {
                isListening = intent.getBooleanExtra(
                        Constants.EXTRA_ALERT_SERVICE_STATE, false
                );

                Log.d(TAG, "State: " + isListening);
            }
        }
    };

    @Override
    protected void onServiceConnected() {

        // Set the app theme
        setTheme(R.style.AppTheme);

        // Register the broadcast receiver for listening state
        registerReceiver(stateReceiver, new IntentFilter(
                IntentActions.ACTION_ALERT_SERVICE_STATE_CHANGED
        ));

        listener = new VolumeLongPressListener(
                this,
                KeyEvent.KEYCODE_VOLUME_UP,
                2000,
                () -> new SendAlertDialog(this));


    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {

        if (isVolumeKey(event) && isListening) {

            listener.dispatch(event);

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

        unregisterReceiver(stateReceiver);
    }

    private boolean isVolumeKey(KeyEvent event) {

        return ((event.getAction() == KeyEvent.ACTION_DOWN ||
                event.getAction() == KeyEvent.ACTION_UP) &&
                event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP);

    }

}
