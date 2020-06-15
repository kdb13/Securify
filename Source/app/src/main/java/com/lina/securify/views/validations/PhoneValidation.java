package com.lina.securify.views.validations;

import android.content.Context;

import com.lina.securify.R;
import com.lina.securify.databinding.FragmentPhoneBinding;
import com.lina.securify.validation.PhoneValidator;
import com.lina.securify.validation.RequiredFieldValidator;

/**
 * Abstracts the form validation for PhoneFragment
 */
public class PhoneValidation {

    private RequiredFieldValidator smsCodeRequiredValidator;
    private RequiredFieldValidator phoneRequiredValidator;
    private PhoneValidator phoneValidator;

    public PhoneValidation(FragmentPhoneBinding binding) {
        Context context = binding.inputLayoutPhone.getContext();

        smsCodeRequiredValidator = new RequiredFieldValidator(
                binding.inputSmsCode,
                context.getString(R.string.error_sms_code_required)
        );

        phoneRequiredValidator = new RequiredFieldValidator(
                binding.inputLayoutPhone,
                context.getString(R.string.error_phone_required)
        );

        phoneValidator = new PhoneValidator(
                binding.inputLayoutPhone,
                context.getString(R.string.error_invalid_phone),
                false
        );
    }

    public boolean validate() {
        return phoneRequiredValidator.validate() && phoneValidator.validate();
    }

    public boolean validateSmsCode() {
        return smsCodeRequiredValidator.validate();
    }
}
