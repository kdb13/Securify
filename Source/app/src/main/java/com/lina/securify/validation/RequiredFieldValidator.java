package com.lina.securify.validation;

import com.google.android.material.textfield.TextInputLayout;
import com.lina.securify.utils.Utils;

/**
 * Provides input validation for empty text fields.
 */
public final class RequiredFieldValidator extends TextInputLayoutValidator {

    /**
     * Constructs a new {@link TextInputLayoutValidator} which provides input validation
     * for empty text fields, with no auto validation.
     * @param textInputLayout The {@link TextInputLayout} to validate.
     * @param errorString The error to show.
     */
    public RequiredFieldValidator(TextInputLayout textInputLayout, String errorString) {
        super(textInputLayout, errorString, true);
    }

    /**
     * Constructs a new {@link TextInputLayoutValidator} which provides input validation
     * for empty text fields.
     * @param textInputLayout The {@link TextInputLayout} to validate.
     * @param errorString The error to show.
     * @param shouldAutoValidate Whether to automatically validate input on text change.
     */
    public RequiredFieldValidator(TextInputLayout textInputLayout, String errorString,
                                  boolean shouldAutoValidate) {
        super(textInputLayout, errorString, shouldAutoValidate);
    }

    @Override
    protected boolean _validate() {
        return !Utils.getTextInside(textInputLayout).trim().isEmpty();
    }

}
