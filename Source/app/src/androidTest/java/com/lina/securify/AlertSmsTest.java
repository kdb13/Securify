package com.lina.securify;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.lina.securify.data.models.Alert;
import com.lina.securify.utils.Utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AlertSmsTest {

    private static final String TAG = AlertSmsTest.class.getSimpleName();

    private final String OUTPUT =
            "//SECURIFY\\\\\n\nThis person needs your help: John Doe (12345)\n\nLocation: 0:0";

    private Context context;

    public AlertSmsTest() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void alertSmsTest_makeSms() {

        Alert alert = new Alert("John Doe", "12345", "", "0:0", "");

        Assert.assertEquals(OUTPUT, Utils.makeAlertMessage(context, alert));

    }

    @Test
    public void alertSmsTest_isAnAlert() {

        Assert.assertTrue(Utils.isAnAlert(Utils.makeAlertMessage(context, null)));

    }
}
