package com.lina.securify.views.validations;

import android.content.Context;

import com.lina.securify.R;
import com.lina.securify.databinding.FragmentSignUpBinding;
import com.lina.securify.validators.EmailValidator;
import com.lina.securify.validators.PasswordValidator;
import com.lina.securify.validators.RequiredFieldValidator;

public class SignUpValidation {

    private RequiredFieldValidator emailRequiredValidator;
    private RequiredFieldValidator firstNameRequiredValidator;
    private RequiredFieldValidator lastNameRequiredValidator;
    private RequiredFieldValidator passwordRequiredValidator;
    private RequiredFieldValidator confPasswordRequiredValidator;

    private EmailValidator emailValidator;
    private PasswordValidator passwordValidator;

    public SignUpValidation(FragmentSignUpBinding binding) {

        Context context = binding.inputEmail.getContext();

        emailRequiredValidator = new RequiredFieldValidator(
                binding.inputEmail,
                context.getString(R.string.error_email_required)
        );

        firstNameRequiredValidator = new RequiredFieldValidator(
                binding.inputFirstName,
                context.getString(R.string.error_first_name_required)
        );

        lastNameRequiredValidator = new RequiredFieldValidator(
                binding.inputLastName,
                context.getString(R.string.error_last_name_required)
        );

        passwordRequiredValidator = new RequiredFieldValidator(
                binding.inputPassword,
                context.getString(R.string.error_password_required)
        );

        confPasswordRequiredValidator = new RequiredFieldValidator(
                binding.inputConfirmPassword,
                context.getString(R.string.error_conf_password_required)
        );

        emailValidator = new EmailValidator(
                binding.inputEmail,
                context.getString(R.string.error_invalid_email)
        );

        passwordValidator = new PasswordValidator(
                binding.inputPassword,
                context.getString(R.string.error_weak_password)
        );
    }

    public boolean validate() {

        if (    (emailRequiredValidator.validate() && emailValidator.validate()) &
                firstNameRequiredValidator.validate() &
                lastNameRequiredValidator.validate() &
                (passwordRequiredValidator.validate() && passwordValidator.validate()) &
                confPasswordRequiredValidator.validate()
        ) {
            return true;
        }
        else
            return false;

    }

    public boolean doPasswordsMatch(String s1, String s2) {
        return s1.equals(s2);
    }
}
