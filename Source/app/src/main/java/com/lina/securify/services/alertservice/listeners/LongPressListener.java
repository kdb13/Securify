package com.lina.securify.services.alertservice.listeners;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;

/**
 * It allows the client to perform a task when there is a long press KeyEvent
 * of a particular duration.
 */

public class LongPressListener extends BaseListener implements BaseListener.Callback {

    private final String LOG_TAG = "LongPressListener";

    private int durationInMillis;
    private boolean isUpTriggered = false;
    private boolean isCounterExisting = false;

    public LongPressListener(int keyCode, int durationInMillis, Runnable task) {
        super(keyCode, task);

        this.durationInMillis = durationInMillis;
    }


    /* BASE LISTENER CALLBACKS */
    @Override
    public void onSuccess() {

        task.run();

    }

    @Override
    public void onStartedListening() {

        isUpTriggered = false;

        if (isCounterExisting)
            return;

        isCounterExisting = true;

        Log.d(LOG_TAG, "Starting counter...");

        new CountDownTimer(durationInMillis, durationInMillis) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Do nothing
            }

            @Override
            public void onFinish() {

                Log.d(LOG_TAG, "Counter finished!");

                isCounterExisting = false;

                checkForSuccess();

            }

        }.start();
    }

    @Override
    public void onCompletedListening() {

        isUpTriggered = true;

    }


    /* KEYEVENT CALLBACKS */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == super.keyCode) {

            onStartedListening();

        }

        return false;
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == super.keyCode) {

            onCompletedListening();

        }

        return false;
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int count, KeyEvent event) {
        return false;
    }


    private void checkForSuccess() {

        if (!isUpTriggered) {

            onSuccess();

        }

    }
}
