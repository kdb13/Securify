package com.lina.securify.services;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.lina.securify.BaseListener;

/*
    TODO: Control the volume changes
 */

public class ButtonService extends AccessibilityService {

    private static final String LOG_TAG = "ButtonService";

    MultipleDownsListener multipleDownsListener;

    BaseListener listener;

    @Override
    protected void onServiceConnected() {

        listener = new VolumeLongPressListener(
                getApplicationContext(),
                KeyEvent.KEYCODE_VOLUME_UP,
                2000,
                new Runnable() {
                    @Override
                    public void run() {
                        Log.d(LOG_TAG, "Sucess!");
                    }
                }
        );

    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {

        if (isVolumeUp(event) || isVolumeDown(event)) {

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

    private boolean isVolumeUp(KeyEvent event) {

        return (event.getAction() == KeyEvent.ACTION_UP &&
                event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP);

    }

    private boolean isVolumeDown(KeyEvent event) {

        return (event.getAction() == KeyEvent.ACTION_DOWN &&
                event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP);

    }

}
