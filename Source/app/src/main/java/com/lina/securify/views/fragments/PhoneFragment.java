package com.lina.securify.views.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.lina.securify.R;
import com.lina.securify.data.repositories.AuthTaskListener;
import com.lina.securify.databinding.FragmentPhoneBinding;
import com.lina.securify.viewmodels.SignUpViewModel;
import com.lina.securify.views.validations.PhoneValidation;

import java.util.concurrent.TimeUnit;


/**
 * Verifies the phone no. of the user and adds it to the account.
 */
public class PhoneFragment extends Fragment {

    private static final String TAG = PhoneFragment.class.getSimpleName();

    private FragmentPhoneBinding binding;
    private PhoneValidation validation;
    private SignUpViewModel viewModel;

    private String verificationId;
    private boolean isCodeTimeout = false;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    // TODO: Handle auto verification
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Log.e(TAG, "onVerificationFailed: ", e);

                    viewModel.showCodeUi.set(false);
                }

                @Override
                public void onCodeSent(@NonNull String id,
                                       @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(id, forceResendingToken);

                    // Save the verification ID
                    verificationId = id;

                    // Toggle the UI to get the OTP
                    viewModel.isLoading.set(false);
                    viewModel.showCodeUi.set(true);
                }

                @Override
                public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                    super.onCodeAutoRetrievalTimeOut(s);

                    isCodeTimeout = true;

                    viewModel.isLoading.set(false);
                    binding.buttonResend.setEnabled(true);
                }
            };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(
                NavHostFragment.findNavController(this)
                        .getBackStackEntry(R.id.signUpFragment))
                .get(SignUpViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPhoneBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        binding.setFragment(this);

        validation = new PhoneValidation(binding);

        return binding.getRoot();
    }

    public void sendOtp(View view) {

        if (validation.validate()) {

            binding.buttonResend.setEnabled(false);

            viewModel.checkPhoneExists(result -> {

                if (result == AuthTaskListener.EXISTING_PHONE) {

                    viewModel.isLoading.set(false);
                    binding.inputPhone.setError(
                            requireContext().getString(R.string.error_existing_phone)
                    );

                } else
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91" + viewModel.getCredentials().getPhone(),
                            30,
                            TimeUnit.SECONDS,
                            requireActivity(),
                            callbacks);
            });

        }

    }

    public void verifyOtp(View view) {

        if (validation.validateSmsCode()) {

            viewModel.verifyOtp(verificationId, result -> {

                switch (result) {

                    case AuthTaskListener.INCORRECT_OTP:
                        binding.inputSmsCode.setError(
                                requireContext().getString(R.string.error_invalid_sms_code)
                        );
                        break;

                    case AuthTaskListener.SIGNED_UP:
                        goToMainActivity();
                        break;

                    default:

                }

                viewModel.isLoading.set(false);

            });

        }

    }

    public void resendOtp(View view) {

        if (!isCodeTimeout)
            new OtpWaitDialogFragment().show(getChildFragmentManager(), "OTP Wait Dialog");
        else
            sendOtp(null);

    }

    public void changePhone(View view) {
        viewModel.showCodeUi.set(false);
    }

    /**
     * Go to MainActivity.
     */
    private void goToMainActivity() {

        NavHostFragment.findNavController(this)
                .navigate(PhoneFragmentDirections.actionMainApp());

        requireActivity().finish();
    }

    static class OtpWaitDialogFragment extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

            builder.setMessage(R.string.resend_message);
            builder.setPositiveButton("Ok", null);

            return builder.create();

        }
    }
}
