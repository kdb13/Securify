package com.lina.securify.validators;

import android.text.TextWatcher;
import android.util.Log;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

/**
 * Validates the input from a TextInputLayout
 */
public abstract class TextInputValidator {

    private static final String TAG = TextInputValidator.class.getSimpleName();

    protected TextInputLayout inputLayout;
    protected boolean isValid;

    public TextInputValidator(TextInputLayout inputLayout) {
        this.inputLayout = inputLayout;
    }

    /**
     * This method will be implemented by the child validators to
     * provide the validation logic.
     *
     * @return <code>true</code> if validation was successfull, else <code>false</code>
     */
    public abstract boolean validate();

    /**
     * This method should toggle the error string based
     * on the validation status.
     */
    protected abstract void setError();

    /**
     * This method sets the TextWatcher interface of the EditText to the child
     * TextInputValidator class if it's implemented.
     */
    protected void addTextWatcher() {

        try {
            TextWatcher textWatcher = (TextWatcher) this;

            Objects.requireNonNull(inputLayout.getEditText())
                    .addTextChangedListener(textWatcher);

        } catch (ClassCastException c) {
            Log.e(TAG, "TextWatcher not implemented!", c);
        }

    }

    public void removeTextWatcher() {
        Objects.requireNonNull(inputLayout.getEditText())
                .removeTextChangedListener((TextWatcher) this);
    }

}
