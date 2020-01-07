package com.lina.securify.views.auth;

import android.content.Context;

import com.lina.securify.R;
import com.lina.securify.databinding.FragmentEmailBinding;
import com.lina.securify.viewmodels.auth.validators.EmailValidator;
import com.lina.securify.viewmodels.auth.validators.RequiredFieldValidator;

public class EmailValidation {

    private RequiredFieldValidator emailRequiredValidator;
    private EmailValidator emailValidator;

    public EmailValidation(FragmentEmailBinding binding) {

        Context context = binding.inputEmail.getContext();

        emailRequiredValidator = new RequiredFieldValidator(
                binding.inputEmail,
                context.getString(R.string.error_email_required)
        );

        emailValidator = new EmailValidator(
                binding.inputEmail,
                context.getString(R.string.error_invalid_email)
        );
    }

    public boolean validate() {
        return emailRequiredValidator.validate() &&
                emailValidator.validate();
    }
}
