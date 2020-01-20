package com.lina.securify.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lina.securify.intents.Intents;

public class AlertReceiver extends BroadcastReceiver {

    private static final String TAG = "AlertReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null) {

            switch (intent.getAction()) {

                case Intents.ACTION_ALERT_SENT:
                    Log.d(TAG, "Alert sent!");
                    break;

                case Intents.ACTION_ALERT_DELIVERED:
                    Log.d(TAG, "Alert delivered!");
                    break;

            }

        }

    }

}
