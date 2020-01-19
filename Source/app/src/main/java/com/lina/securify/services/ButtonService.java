package com.lina.securify.services;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

public class ButtonService extends AccessibilityService {

    private static final String TAG = "ButtonService";

    BaseListener listener;

    @Override
    protected void onServiceConnected() {

        Log.d(TAG, "Service started.");

        listener = new VolumeLongPressListener(
                getApplicationContext(),
                KeyEvent.KEYCODE_VOLUME_UP,
                2000,
                new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Success!");
                    }
                }
        );

    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {

        if (isVolumeKey(event)) {

            listener.dispatch(event);

            return false;
        }

        return super.onKeyEvent(event);
    }

    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }

    private boolean isVolumeKey(KeyEvent event) {

        return ((event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.ACTION_UP) &&
                event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP);

    }
}
