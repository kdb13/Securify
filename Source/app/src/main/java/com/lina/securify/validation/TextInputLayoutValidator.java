package com.lina.securify.validation;

import android.text.TextWatcher;
import android.util.Log;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

/**
 * Provides input validation for a {@link TextInputLayout}.
 */
public abstract class TextInputLayoutValidator {

    private static final String TAG = TextInputLayoutValidator.class.getSimpleName();

    /**
     * The {@link TextInputLayout} to validate.
     */
    protected TextInputLayout textInputLayout;

    /**
     * The error to show in {@link #textInputLayout}.
     */
    protected String errorString;

    /**
     * The validation status.
     */
    // TODO: Remove the attribute. Instead, pass the status as argument of setError()
    protected boolean isValid;

    // TODO: Remove this constructor and refactor.
    public TextInputLayoutValidator(TextInputLayout textInputLayout) {
        this.textInputLayout = textInputLayout;
    }

    // TODO: Remove this constructor and refactor.
    public TextInputLayoutValidator(TextInputLayout textInputLayout, String errorString) {
        this.textInputLayout = textInputLayout;
        this.errorString = errorString;
    }

    public TextInputLayoutValidator(TextInputLayout textInputLayout, String errorString,
                                    boolean shouldAutoValidate) {
        this.textInputLayout = textInputLayout;
        this.errorString = errorString;

        if (shouldAutoValidate) {
            addNewTextWacher();
        }
    }

    /**
     * Performs the validation and toggles the error based on validation status.
     *
     * @return true if validation was successful, else false.
     */
    public boolean validate() {
        isValid = _validate();
        setError();
        return  isValid;
    }

    /**
     * This method must be implemented by validators to contain the
     * validation logic.
     *
     * @return true if validation was successful, else false.
     */
    // TODO: Make this method abstract and refactor other validators to implement it.
    protected boolean _validate() { return false; };

    /**
     * Show/hide the error string of {@link #textInputLayout} based on
     * validation status - {@link #isValid}.
     */
    protected void setError() {
        if (isValid) {
            textInputLayout.setError(null);
        } else {
            textInputLayout.setError(errorString);
        }
    }

    /**
     * Sets the {@link TextWatcher} of {@link android.widget.EditText} as
     * {@code this}.
     */
    // TODO: Remove this method and its references
    protected void addTextWatcher() {

        try {
            TextWatcher textWatcher = (TextWatcher) this;
            Objects.requireNonNull(textInputLayout.getEditText())
                    .addTextChangedListener(textWatcher);
        } catch (ClassCastException c) {
            Log.e(TAG, "TextWatcher not implemented!", c);
        }

    }

    /**
     * Adds a {@link TextWatcher} to the EditText. It will hide the error if user types
     * correct input after the validation failed.
     */
    // TODO: Rename this method to addTextWatcher
    protected void addNewTextWacher() {
        textInputLayout.getEditText().addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (_validate()) {
                    textInputLayout.setError(null);
                }
            }
        });
    }
}
