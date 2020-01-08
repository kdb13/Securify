package com.lina.securify.validators;

import com.google.android.material.textfield.TextInputLayout;
import com.lina.securify.utils.Utils;

import java.util.regex.Pattern;

public class PhoneValidator extends TextInputValidator{

    private static final String PHONE_REG_EX = "\\d{10}";
    private String errorString;

    public PhoneValidator(TextInputLayout inputPhone, String errorString) {
        super(inputPhone);

        this.errorString = errorString;

        addTextWatcher();
    }

    @Override
    public boolean validate() {

        isValid = Pattern.matches(PHONE_REG_EX, Utils.getTextInside(inputLayout));

        setError();

        return isValid;
    }

    @Override
    protected void setError() {

        if (isValid) {
            inputLayout.setError("");
        } else {
            inputLayout.setError(errorString);
        }

    }
}
