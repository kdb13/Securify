package com.lina.securify;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Listens to volume button long press events.
 *
 * <p>If a volume button is long pressed for a specific amount of time, then
 * it invokes the {@link Runnable} passed by the client object.</p>
 */
public final class VolumeLongPressHandler extends KeyEventCallbackAdapter {

    private static final String TAG = VolumeLongPressHandler.class.getSimpleName();

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
     * @param time The amount of time in milliseconds
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
                    Log.d(TAG, "onFinish: Long pressed for " + time + " seconds.");

                    // Invoke the Runnable
//                    runnable.run();

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
