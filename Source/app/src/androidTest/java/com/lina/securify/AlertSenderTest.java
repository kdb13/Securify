package com.lina.securify;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AlertSenderTest {

    @Test
    public void Should_SendSms_When_Called() {

        AlertSender sender = new AlertSender(
                InstrumentationRegistry.getInstrumentation().getTargetContext());

        sender.send();
    }

}
