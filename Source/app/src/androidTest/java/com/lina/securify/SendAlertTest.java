package com.lina.securify;

import android.content.pm.InstrumentationInfo;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.lina.securify.services.AlertSender;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class SendAlertTest {

    private static final String TEST_PHONE = "8000046911";

    private AlertSender alertSender;

    @Before
    public void init() {

        alertSender = new AlertSender(
                InstrumentationRegistry
                .getInstrumentation()
                .getTargetContext()
        );

    }

    @Test
    public void sendAlertToVolunteers_isSmsReceived() {

        alertSender.onPositive();

    }
}
