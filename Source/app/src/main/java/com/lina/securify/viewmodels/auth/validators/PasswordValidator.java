package com.lina.securify.viewmodels.auth.validators;

import com.google.android.material.textfield.TextInputLayout;
import com.lina.securify.utils.Utils;

public class PasswordValidator extends TextInputValidator {

    private String errorString;

    public PasswordValidator(TextInputLayout inputPassword, String errorString) {
        super(inputPassword);

        this.errorString = errorString;
    }

    @Override
    public boolean validate() {

        if (Utils.getTextInside(inputLayout).length() < 6)
            isValid = false;
        else
            isValid = true;

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
