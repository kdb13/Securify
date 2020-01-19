package com.lina.securify.services;

import android.content.Context;
import android.media.AudioManager;

public class VolumeLongPressListener extends LongPressListener {

    private VolumeResetter volumeResetter;

    private boolean isSuccess = false;

    public VolumeLongPressListener(Context context, int keyCode, int durationInMillis, Runnable task) {
        super(keyCode, durationInMillis, task);

        volumeResetter = new VolumeResetter(context, AudioManager.STREAM_RING);
    }

    @Override
    public void onStartedListening() {
        super.onStartedListening();

        // Set the initial volume
        volumeResetter.setInitialVolume();
    }

    @Override
    public void onSucess() {
        super.onSucess();

        isSuccess = true;
    }

    @Override
    public void onCompletedListening() {
        super.onCompletedListening();

        if (isSuccess) {
            volumeResetter.reset();
            isSuccess = false;
        }
    }
}
