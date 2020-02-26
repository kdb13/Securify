package com.lina.securify.adapters;

import android.view.View;

import androidx.databinding.BindingAdapter;

public class DataBindingAdapters {

    @BindingAdapter("visibility")
    public static void setVisibility(View view, boolean isTrue) {

        if (isTrue)
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.GONE);

    }
}
