package com.lina.securify.validators;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputLayout;
import com.lina.securify.utils.Utils;

import java.util.regex.Pattern;

public class PhoneValidator extends TextInputValidator implements TextWatcher {

    private static final String PHONE_REG_EX = "\\d{10}";
    private String errorString;

    public PhoneValidator(TextInputLayout inputPhone, String errorString) {
        super(inputPhone);

        this.errorString = errorString;

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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        validate();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
