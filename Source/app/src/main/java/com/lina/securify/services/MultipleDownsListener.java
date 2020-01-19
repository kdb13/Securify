package com.lina.securify.services;


import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;

/**
 * It allows the client to perform a task when there are multiple KeyEvents with
 * a DOWN action.
 */
public class MultipleDownsListener extends BaseListener {

    private static final String LOG_TAG = MultipleDownsListener.class.getSimpleName();
    private static final int clickDuration = 600;

    private final int noOfDowns;
    private int count = 0;

    public MultipleDownsListener(int keyCode, int noOfDowns, Runnable task) {
        super(keyCode, task);

        this.noOfDowns = noOfDowns;
    }

    private void checkForSuccess() {

        if (count == noOfDowns) {
            Log.d(LOG_TAG, "Listen successfull!");

            task.run();
        }

        reset();
    }

    private void reset() {
        count = 0;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == super.keyCode) {

            if (count == 0) {

                Log.d(LOG_TAG, "Starting counter...");

                new CountDownTimer(clickDuration, clickDuration) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        // Nothing
                    }

                    @Override
                    public void onFinish() {
                        Log.d(LOG_TAG, "Counter finished!");

                        checkForSuccess();
                    }

                }.start();
            }

            count++;

            Log.d(LOG_TAG, "Count: " + count);
        }

        return false;
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        return false;
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int count, KeyEvent event) {
        return false;
    }
}
