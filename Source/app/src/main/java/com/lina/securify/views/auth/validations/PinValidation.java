package com.lina.securify.views.auth.validations;

import android.content.Context;

import com.lina.securify.R;
import com.lina.securify.databinding.FragmentPinBinding;
import com.lina.securify.validators.PinValidator;
import com.lina.securify.validators.RequiredFieldValidator;

public class PinValidation {

    private RequiredFieldValidator pinRequiredValidator;
    private RequiredFieldValidator confPinRequiredValidator;
    private PinValidator pinValidator;

    public PinValidation(FragmentPinBinding binding) {
        Context context = binding.inputPin.getContext();

        pinRequiredValidator = new RequiredFieldValidator(
                binding.inputPin,
                context.getString(R.string.error_pin_required)
        );

        confPinRequiredValidator = new RequiredFieldValidator(
                binding.inputReenterPin,
                context.getString(R.string.error_conf_pin_required)
        );

        pinValidator = new PinValidator(
                binding.inputPin,
                context.getString(R.string.error_invalid_pin)
        );
    }

    public boolean validateNewPin() {

        return ( pinRequiredValidator.validate() && pinValidator.validate() ) &
                confPinRequiredValidator.validate();

    }

    public boolean validatePin() {
        return pinRequiredValidator.validate() && pinValidator.validate();
    }
}
