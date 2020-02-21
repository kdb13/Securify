package com.lina.securify;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.lina.securify.data.models.Alert;
import com.lina.securify.utils.AlertSmsBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * This test tests the methods of <code>AlertSmsBuilder</code> class.
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class AlertSmsBuilderTest {

    private static final String TEST_SMS =
            "//SECURIFY\\\\\n\n" +
            "This person needs your help: John Doe (+911234567890)\n\n" +
            "Current location: 0.0:0.0";

    private AlertSmsBuilder builder;

    @Before
    public void init() {
        builder = new AlertSmsBuilder(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void Should_BuildSms_When_PassedValidAlert() {

        Alert alert = new Alert(
                "John Doe",
                "+911234567890",
                "0.0:0.0");

        assertEquals(builder.buildSms(alert), TEST_SMS);
    }

    @Test
    public void Should_ReturnNull_When_PassedInvalidAlert() {

        Alert alert = new Alert();

        assertNull(builder.buildSms(alert));
    }

    @Test
    public void Should_ReturnAlert_WhenPassedValidSms() {

        Alert alert = builder.parseAlertFromSms(TEST_SMS);

        assertEquals(alert.getVictimName(), "John Doe");
        assertEquals(alert.getVictimPhone(), "+911234567890");
        assertEquals(alert.getLocation(), "0.0:0.0");

    }

    @Test
    public void Should_ReturnNull_WhenPassedInvalidSms() {

        assertNull(builder.parseAlertFromSms("Invalid Sms"));

    }
}
