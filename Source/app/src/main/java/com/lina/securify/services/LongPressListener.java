package com.lina.securify.services;

import android.content.Context;
import android.media.AudioManager;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;

import com.lina.securify.BaseListener;

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

    /**
     * This method must be called by the client to dispatch the event to LongPressListener so that
     * it can be tracked for long press
     *
     * @param event
     * The KeyEvent to be dispatched
     */
    public void dispatch(KeyEvent event) {
        event.dispatch(this, new KeyEvent.DispatcherState(), this);
    }

    private void checkForSuccess() {

        if (!isUpTriggered) {

            onSucess();

        }

    }

    /* BASE LISTENER CALLBACKS */

    @Override
    public void onSucess() {

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

}
