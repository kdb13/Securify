package com.lina.securify;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.lina.securify.utils.Utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AlertSmsTest {

    private static final String TAG = AlertSmsTest.class.getSimpleName();

    private final String OUTPUT = "//SECURIFY//\n\nI need help!\n\nMy location: 0:0";

    private Context context;

    public AlertSmsTest() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void alertSmsTest_parseAlertMessage() {

        Assert.assertEquals(OUTPUT, Utils.parseAlertMessage(context));

    }

    @Test
    public void alertSmsTest_isAnAlert() {

        Assert.assertTrue(Utils.isAnAlert(OUTPUT));

    }
}
