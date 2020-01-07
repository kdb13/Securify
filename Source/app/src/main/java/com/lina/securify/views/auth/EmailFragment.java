package com.lina.securify.views.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.lina.securify.R;
import com.lina.securify.databinding.FragmentEmailBinding;
import com.lina.securify.repositories.AuthRepository.Result;
import com.lina.securify.viewmodels.auth.Constants;
import com.lina.securify.viewmodels.auth.EmailViewModel;

public class EmailFragment extends Fragment implements Observer<Result> {

    private FragmentEmailBinding binding;
    private EmailValidation validation;
    private EmailViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewModel = ViewModelProviders.of(this).get(EmailViewModel.class);

        binding = FragmentEmailBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        binding.setViewModel(viewModel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        validation = new EmailValidation(binding);
    }

    @Override
    public void onChanged(Result result) {
        viewModel.toggleLoading(false);

        switch (result) {

            case EXISTING_EMAIL:
                goToPasswordFragment();
                break;

            default:

        }
    }

    public void onContinueClick(View view) {

        if (validation.validate()) {

            viewModel.toggleLoading(true);
            viewModel.checkEmailExists().observe(this, this);
        }

    }

    private void goToPasswordFragment() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_verifyEmailFragment_to_passwordFragment, getEmailBundle());
    }

    private Bundle getEmailBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_EMAIL, viewModel.getModel().getEmail());

        return bundle;
    }
}
