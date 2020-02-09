package com.lina.securify.adapters;

import android.widget.Button;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

public class DataBindingAdapters {

    @BindingAdapter("errorText")
    public static void setErrorText(TextInputLayout inputLayout, int errorId) {

        // Set empty error if errorID is -1
        String error = "";

        if (errorId != -1)
            error = inputLayout.getContext().getString(errorId);

        inputLayout.setError(error);
    }

    @BindingAdapter("buttonText")
    public static void setButtonText(Button button, int buttonTextId) {

        // Set empty error if errorID is -1
        String text = "";

        if (buttonTextId != -1)
            text = button.getContext().getString(buttonTextId);

        button.setText(text);
    }

    @BindingAdapter("text")
    public static void setTextViewText(TextView textView, int textId) {

        textView.setText(textId);

    }

}
