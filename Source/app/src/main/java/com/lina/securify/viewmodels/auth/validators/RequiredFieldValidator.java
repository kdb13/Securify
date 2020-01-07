package com.lina.securify.viewmodels.auth.validators;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputLayout;
import com.lina.securify.utils.Utils;

/**
 * Used to validate a TextInputLayout for empty text fields.
 */
public class RequiredFieldValidator extends TextInputValidator implements TextWatcher {

    private String errorRequired;

    /**
     *
     * @param inputLayout The TextInputLayout to be validated
     * @param errorRequired The error string displayed if field is empty
     */
    public RequiredFieldValidator(TextInputLayout inputLayout, String errorRequired) {
        super(inputLayout);

        this.errorRequired = errorRequired;

        addTextWatcher();
    }

    @Override
    protected void setError() {
        if (isValid) {
            inputLayout.setError("");
        } else {
            inputLayout.setError(errorRequired);
        }
    }

    @Override
    public boolean validate() {

        isValid = !Utils.getTextInside(inputLayout).isEmpty();

        setError();

        return isValid;
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
