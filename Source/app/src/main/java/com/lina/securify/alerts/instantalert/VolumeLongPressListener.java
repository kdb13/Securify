package com.lina.securify.alerts.instantalert;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;

import com.lina.securify.utils.KeyEventCallbackAdapter;

/**
 * Listens to volume button's long press events. The {@link KeyEvent#ACTION_UP} and
 * {@link KeyEvent#ACTION_DOWN} events must be dispatched
 * by the client, which determines which particular Volume button to listen for (UP or DOWN).
 *
 * <p>If a volume button is long pressed for the specified duration, then
 * it invokes the {@link Runnable} passed by the client.</p>
 */
public final class VolumeLongPressListener extends KeyEventCallbackAdapter {

    private static final String TAG = VolumeLongPressListener.class.getSimpleName();

    /**
     * Whether the key is pressed ({@link KeyEvent#ACTION_DOWN}) or not.
     */
    private boolean isKeyPressed;

    /**
     * Used to track the long press.
     */
    private CountDownTimer timer;

    /**
     * @param runnable The Runnable to invoke when Volume button is long pressed for
     *                 {@code time} milliseconds.
     * @param time The long press duration in milliseconds
     */
    public VolumeLongPressListener(Runnable runnable, long time) {

        isKeyPressed = false;

        // Set the timer
        timer = new CountDownTimer(time, time) {

            @Override
            public void onTick(long millisUntilFinished) { }

            @Override
            public void onFinish() {

                // Check if the Volume key is still pressed
                if (isKeyPressed) {

                    runnable.run();

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
