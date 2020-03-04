package com.lina.securify.services.alertservice.listeners;

import android.content.Context;
import android.media.AudioManager;

import com.lina.securify.services.alertservice.VolumeReseter;

public class VolumeLongPressListener extends LongPressListener {

    private VolumeReseter volumeReseter;
    private boolean isSuccess = false;

    public VolumeLongPressListener(Context context, int keyCode, int durationInMillis, Runnable task) {
        super(keyCode, durationInMillis, task);

        volumeReseter = new VolumeReseter(context, AudioManager.STREAM_RING);
    }

    @Override
    public void onStartedListening() {
        super.onStartedListening();

        // Set the initial volume
        volumeReseter.setInitialVolume();
    }

    @Override
    public void onSuccess() {
        super.onSuccess();

        isSuccess = true;
    }

    @Override
    public void onCompletedListening() {
        super.onCompletedListening();

        if (isSuccess) {
            volumeReseter.reset();
            isSuccess = false;
        }
    }
}
