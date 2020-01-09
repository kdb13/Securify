package com.lina.securify.utils.bindingadapters;

import android.widget.Button;

import androidx.databinding.BindingAdapter;

import com.google.android.material.textfield.TextInputLayout;

public class AuthBindingAdapters {

    @BindingAdapter("errorText")
    public static void setErrorText(TextInputLayout inputLayout, int errorID) {

        // Set empty error if errorID is -1
        String error = "";

        if (errorID != -1)
            error = inputLayout.getContext().getString(errorID);

        inputLayout.setError(error);
    }

    @BindingAdapter("buttonText")
    public static void setErrorText(Button button, int buttonTextId) {

        // Set empty error if errorID is -1
        String text = "";

        if (buttonTextId != -1)
            text = button.getContext().getString(buttonTextId);

        button.setText(text);
    }

}
