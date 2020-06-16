package com.lina.securify;

import com.lina.securify.utils.Utils;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UtilsTest {

    @Test
    public void trimPhoneWithSpacesAndCC() {

        final String INPUT_PHONE = "+91 12345 67890";
        final String OUTPUT_PHONE = "1234567890";

        String result = Utils.trimPhone(INPUT_PHONE);
        assertEquals(OUTPUT_PHONE, result);

    }

    @Test
    public void trimPhoneWithSpaces() {

        final String INPUT_PHONE = " 12345 67890 ";
        final String OUTPUT_PHONE = "1234567890";

        String result = Utils.trimPhone(INPUT_PHONE);
        assertEquals(OUTPUT_PHONE, result);

    }

    @Test
    public void trimPhoneWithCC() {

        final String INPUT_PHONE = "+911234567890";
        final String OUTPUT_PHONE = "1234567890";

        String result = Utils.trimPhone(INPUT_PHONE);
        assertEquals(OUTPUT_PHONE, result);

    }

}
