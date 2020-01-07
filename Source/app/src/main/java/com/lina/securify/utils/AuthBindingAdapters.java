package com.lina.securify.utils;

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

}
