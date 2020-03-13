package com.lina.securify;

import androidx.test.core.app.ApplicationProvider;

import com.lina.securify.data.models.Alert;
import com.lina.securify.sendalert.AlertSmsBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class AlertSmsBuilderTest {

    private AlertSmsBuilder builder;

    @Before
    public void init() {
        builder = new AlertSmsBuilder(ApplicationProvider.getApplicationContext(), null);
    }

    @Test
    public void Should_BuildSms_WhenPassed_ValidAlert() {

        final String TEST_SMS = "//SECURIFY\\\\\n\n" +
                "This person needs your help: John Doe (1234567890)\n\n" +
                "Current location: https://www.google.com/maps/search/?api=1&query=45.6,67.8";

        final String TEST_SMS_NO_LOCATION = "//SECURIFY\\\\\n\n" +
                "This person needs your help: John Doe (1234567890)\n\n" +
                "Current location: No location";

        builder.setAlert(
                new Alert("John Doe", "1234567890", "No location"));

        assertEquals(TEST_SMS_NO_LOCATION, builder.build());

        builder.setAlert(
                new Alert("John Doe", "1234567890", "45.6,67.8"));

        assertEquals(TEST_SMS, builder.build());
    }

    @Test
    public void Should_ThrowException_WhenPassed_NullAlert() {

        assertThrows(NullPointerException.class, () -> {
            builder.setAlert(null);
            builder.build();
        });

    }
}
