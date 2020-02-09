package com.lina.securify.views.validations;

import android.content.Context;

import com.lina.securify.R;
import com.lina.securify.databinding.DialogAddVolunteerBinding;
import com.lina.securify.validators.PhoneValidator;
import com.lina.securify.validators.RequiredFieldValidator;

public class AddVolunteerValidation {

    private RequiredFieldValidator phoneRequiredValidator;
    private RequiredFieldValidator nameRequiredValidator;
    private PhoneValidator phoneValidator;

    public AddVolunteerValidation(DialogAddVolunteerBinding binding) {

        Context context = binding.inputName.getContext();

        phoneRequiredValidator = new RequiredFieldValidator(
                binding.inputPhone,
                context.getString(R.string.error_phone_required)
        );

        binding.inputPhone.setErrorIconDrawable(null);

        phoneValidator = new PhoneValidator(
                binding.inputPhone,
                context.getString(R.string.error_invalid_phone)
        );

        nameRequiredValidator = new RequiredFieldValidator(
                binding.inputName,
                context.getString(R.string.error_name_required)
        );
    }

    public boolean validate() {

        return (phoneRequiredValidator.validate() && phoneValidator.validate()) &
                nameRequiredValidator.validate();

    }
}
