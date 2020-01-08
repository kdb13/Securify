package com.lina.securify.views.auth.validations;

import android.content.Context;

import com.lina.securify.R;
import com.lina.securify.databinding.FragmentPhoneBinding;
import com.lina.securify.validators.PhoneValidator;
import com.lina.securify.validators.RequiredFieldValidator;

/**
 * Abstracts the form validation for PhoneFragment
 */
public class PhoneValidation {

    private RequiredFieldValidator phoneRequiredValidator;
    private PhoneValidator phoneValidator;

    public PhoneValidation(FragmentPhoneBinding binding) {
        Context context = binding.inputPhone.getContext();

        phoneRequiredValidator = new RequiredFieldValidator(
                binding.inputPhone,
                context.getString(R.string.error_phone_required)
        );

        phoneValidator = new PhoneValidator(
                binding.inputPhone,
                context.getString(R.string.error_invalid_phone)
        );
    }

    public boolean validate() {
        return phoneRequiredValidator.validate() && phoneValidator.validate();
    }
}
