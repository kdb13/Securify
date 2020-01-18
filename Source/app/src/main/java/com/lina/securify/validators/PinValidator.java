package com.lina.securify.validators;

import com.google.android.material.textfield.TextInputLayout;
import com.lina.securify.utils.Utils;

public class PinValidator extends TextInputValidator {

    private String errorString;

    public PinValidator(TextInputLayout inputLayout, String errorString) {
        super(inputLayout);

        this.errorString = errorString;
    }

    @Override
    public boolean validate() {

        isValid = Utils
                .getTextInside(inputLayout)
                .length() == 4;

        setError();

        return isValid;
    }

    @Override
    protected void setError() {
        if (isValid)
            inputLayout.setError("");
        else
            inputLayout.setError(errorString);
    }
}
