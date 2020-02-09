package com.lina.securify;

import com.lina.securify.utils.Utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class PhoneTrimTest {

    @Test
    public void phoneTrim_isCorrect() {

        assertEquals("8000046911", Utils.trimPhone("+918000046911"));
        assertEquals("8000046911", Utils.trimPhone("+91 80000 46911"));
        assertEquals("8000046911", Utils.trimPhone("+91 800 00 469 11"));
        assertEquals("8000046911", Utils.trimPhone("91 800 00 469 11"));
        assertEquals("8000046911", Utils.trimPhone(" 800 00 469 11"));
        assertEquals("9100046911", Utils.trimPhone("91000 46911"));

    }

}
