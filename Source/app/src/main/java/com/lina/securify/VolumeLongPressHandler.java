package com.lina.securify;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;

/**
 * It will handle the ACTION_UP and ACTION_DOWN KeyEvents of the Volume buttons. If the Volume
 * buttons are pressed for a specified time, then the Runnable will be invoked.
 */
public final class VolumeLongPressHandler extends KeyEventCallbackAdapter {

    // Debug TAG
    private static final String TAG = VolumeLongPressHandler.class.getSimpleName();

    /**
     * Denotes whether the Volume key is still pressed or not
     */
    private boolean isKeyPressed;

    /**
     * Used to measure the duration of long press.
     */
    private CountDownTimer timer;

    /**
     * @param runnable The Runnable to invoke when Volume button is long pressed
     * @param time The time in milliseconds, determining the period of long press
     */
    public VolumeLongPressHandler(Runnable runnable, long time) {

        isKeyPressed = false;

        // Set the timer
        timer = new CountDownTimer(time, time) {

            @Override
            public void onTick(long millisUntilFinished) { }

            @Override
            public void onFinish() {

                // Check if the Volume key is still pressed
                if (isKeyPressed) {
                    Log.d(TAG, "onFinish: Long pressed!");

                    // Invoke the Runnable
                    new Thread(runnable).start();

                    isKeyPressed = false;
                }
            }

        };
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyDown: ");

        isKeyPressed = true;

        // Start the timer
        timer.start();

        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyUp: ");

        isKeyPressed = false;

        // Stop the timer
        timer.cancel();

        return false;
    }

}
