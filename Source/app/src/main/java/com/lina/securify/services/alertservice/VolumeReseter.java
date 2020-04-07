package com.lina.securify.services.alertservice;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

public class VolumeReseter {

    private static final String TAG = VolumeReseter.class.getSimpleName();

    private AudioManager audioManager;

    private int streamType;
    private int initialVolume;

    public VolumeReseter(Context context, int streamType) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        this.streamType = streamType;
    }

    public void setInitialVolume() {
        Log.d(TAG, "Initial volume: " + getVolume());

        initialVolume = getVolume();
    }

    public void reset() {
        audioManager.setStreamVolume(streamType, initialVolume, 0);

        Log.d(TAG, "New volume: " + getVolume());
    }

    private int getVolume() {
        return audioManager.getStreamVolume(streamType);
    }

}
