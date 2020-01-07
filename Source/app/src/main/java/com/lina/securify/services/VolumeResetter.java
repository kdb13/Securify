package com.lina.securify;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

public class VolumeResetter {

    private static final String LOG_TAG = VolumeResetter.class.getSimpleName();

    private AudioManager audioManager;

    private int streamType;
    private int initialVolume;

    public VolumeResetter(Context context, int streamType) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        this.streamType = streamType;
    }

    public int getVolume() {
        return audioManager.getStreamVolume(streamType);
    }

    public void setInitialVolume() {
        Log.d(LOG_TAG, "Initial volume: " + getVolume());

        initialVolume = getVolume();
    }

    public void reset() {
        audioManager.setStreamVolume(streamType, initialVolume, 0);

        Log.d(LOG_TAG, "New volume: " + getVolume());
    }

}
