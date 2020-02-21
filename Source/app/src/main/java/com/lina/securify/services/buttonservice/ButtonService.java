package com.lina.securify.services.buttonservice;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import com.lina.securify.R;
import com.lina.securify.services.buttonservice.listeners.BaseListener;
import com.lina.securify.services.buttonservice.listeners.VolumeLongPressListener;
import com.lina.securify.views.dialogs.DialogListener;
import com.lina.securify.views.dialogs.SendAlertDialog;

public class ButtonService extends AccessibilityService {

    private static final String TAG = "ButtonService";

    private BaseListener listener;

    @Override
    protected void onServiceConnected() {

        // Set the application theme
        getApplicationContext().setTheme(R.style.AppTheme);

        listener = new VolumeLongPressListener(
                getApplicationContext(),
                KeyEvent.KEYCODE_VOLUME_UP,
                3000,
                () -> new SendAlertDialog(getApplicationContext())
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

    public void onInterrupt() {

    }

    private boolean isVolumeKey(KeyEvent event) {

        return ((event.getAction() == KeyEvent.ACTION_DOWN ||
                event.getAction() == KeyEvent.ACTION_UP) &&
                event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP);

    }

}
