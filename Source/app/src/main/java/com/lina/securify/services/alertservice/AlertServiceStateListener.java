package com.lina.securify.services.alertservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.lina.securify.utils.constants.Constants;
import com.lina.securify.utils.constants.IntentActions;

public class AlertServiceStateListener implements LifecycleObserver {

    private Context context;
    private StateListener listener;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() != null &&
                    intent.getAction().equals(IntentActions.ACTION_ACCESSIBILITY_SERVICE_CHANGED))
                listener.onChanged(intent.getBooleanExtra(
                        Constants.EXTRA_ACCESSIBILITY_SERVICE_STATE, false
                ));

        }
    };

    public AlertServiceStateListener(Context context, StateListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void startReceiving() {
        context.registerReceiver(receiver, new IntentFilter(
                IntentActions.ACTION_ALERT_SERVICE_STATE_CHANGED));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void disableReceiving() {
        context.unregisterReceiver(receiver);
    }

    public interface StateListener {
        void onChanged(boolean isEnabled);
    }
}
