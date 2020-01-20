package com.lina.securify.services;

import android.view.KeyEvent;

/**
 * The base class for listening to KeyEvents and performing a task
 * based on some event logic.
 */
public abstract class BaseListener implements KeyEvent.Callback {

    protected int keyCode;

    protected Runnable task;

    /**
     * Must be used by the sub-classes to set the common parameters
     * @param keyCode The KeyEvent with keyCode upon which the listener will listen
     * @param task The task to perform on success
     */
    public BaseListener(int keyCode, Runnable task) {
        this.keyCode = keyCode;
        this.task = task;
    }

    /**
     * This method must be called by the client to dispatch the event to this class.
     *
     * @param event
     * The KeyEvent to be dispatched
     */
    public void dispatch(KeyEvent event) {
        event.dispatch(this, new KeyEvent.DispatcherState(), this);
    }

    public interface Callback {

        /**
         * It is called when the listening is completed and is a success
         */
        void onSuccess();

        /**
         * It is called when listening is started.
         */
        void onStartedListening();

        /**
         * It is called when listening is completed. To check for success
         * implement the onSuccess() method instead.
         */
        void onCompletedListening();

    }
}
