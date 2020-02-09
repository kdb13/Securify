package com.lina.securify.views.validations;

import android.content.Context;

import com.lina.securify.R;
import com.lina.securify.databinding.FragmentPasswordBinding;
import com.lina.securify.validators.PasswordValidator;
import com.lina.securify.validators.RequiredFieldValidator;

/**
 * Abstracts the form validation for PasswordFragment.
 */
public class PasswordValidation {

    private RequiredFieldValidator passwordRequiredValidator;
    private PasswordValidator passwordValidator;

    public PasswordValidation(FragmentPasswordBinding binding) {
        Context context = binding.inputPassword.getContext();

        passwordRequiredValidator = new RequiredFieldValidator(
                binding.inputPassword,
                context.getString(R.string.error_password_required)
        );

        passwordValidator = new PasswordValidator(
                binding.inputPassword,
                context.getString(R.string.error_weak_password)
        );
    }

    public boolean validate() {
        return passwordRequiredValidator.validate() &&
                passwordValidator.validate();
    }
}
