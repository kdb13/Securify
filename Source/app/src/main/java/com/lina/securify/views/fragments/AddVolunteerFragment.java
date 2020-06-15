package com.lina.securify.views.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.lina.securify.R;
import com.lina.securify.databinding.FragmentAddVolunteerBinding;
import com.lina.securify.validation.PhoneValidator;
import com.lina.securify.validation.RequiredFieldValidator;
import com.lina.securify.views.viewmodels.AddVolunteerViewModel;

/**
 * Provides a form to add a new volunteer.
 */
public class AddVolunteerFragment extends Fragment {
    
    private static final int REQUEST_PICK_CONTACT = 1;
    
    private FragmentAddVolunteerBinding binding;
    
    private AddVolunteerViewModel viewModel;

    private PhoneValidator phoneValidator;
    private RequiredFieldValidator nameRequiredValidator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(AddVolunteerViewModel.class);
        
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddVolunteerBinding.inflate(inflater, container, false);
        
        binding.setViewModel(viewModel);
        
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.inputLayoutName.setStartIconOnClickListener(this::pickContact);

        nameRequiredValidator = new RequiredFieldValidator(binding.inputLayoutName, getString(R.string.required), true);
        phoneValidator = new PhoneValidator(binding.inputLayoutPhone, getString(R.string.error_invalid_phone), true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.add_volunteer, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_save) {

            if (nameRequiredValidator.validate() &
                phoneValidator.validate()) {

                viewModel.addVolunteer();
                NavHostFragment.findNavController(this).navigateUp();

            }
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if (requestCode == REQUEST_PICK_CONTACT
                && resultCode == Activity.RESULT_OK) {

            if (intent != null && intent.getData() != null) {
                viewModel.loadContactFromURI(intent.getData());
            }

        }

        super.onActivityResult(requestCode, resultCode, intent);
    }
    
    private void pickContact(View view) {
        
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(CommonDataKinds.Phone.CONTENT_TYPE);

        // Let the user pick a contact from Contacts app
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_PICK_CONTACT);
        }
        
    }
    
}
