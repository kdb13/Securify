package com.lina.securify.validation;

import com.google.android.material.textfield.TextInputLayout;
import com.lina.securify.utils.Utils;

import java.util.regex.Pattern;

/**
 * Provides input validation for a phone number which has 10 digits.
 */
public class PhoneValidator extends TextInputLayoutValidator {

    private static final String PHONE_REG_EX = "\\d{10}";

    public PhoneValidator(TextInputLayout textInputLayout, String errorString,
                          boolean shouldAutoValidate) {
        super(textInputLayout, errorString, shouldAutoValidate);
    }

    @Override
    protected boolean _validate() {
        return Pattern.matches(PHONE_REG_EX, Utils.getTextInside(textInputLayout).trim());
    }
}
