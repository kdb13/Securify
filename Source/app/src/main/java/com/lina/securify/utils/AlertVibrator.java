package com.lina.securify.utils;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class AlertVibrator {

    private Vibrator vibrator;

    private final long DURATION = 600;
    private final long GAP = 500;

    public AlertVibrator(Context context) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void vibrate() {

        long[] pattern = new long[] {0, DURATION, GAP, DURATION};

        vibrator.vibrate(pattern, -1);
    }
}
