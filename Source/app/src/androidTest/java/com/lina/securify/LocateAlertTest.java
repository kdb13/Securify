package com.lina.securify;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LocateAlertTest {

    private static final String LOCATION = "geo:0,0?q=23.1330991,72.8011308";
    private Context context;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void Should_OpenGoogleMaps() {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(LOCATION));
        intent.setPackage("com.google.android.apps.maps");

        context.startActivity(intent);
    }
}
