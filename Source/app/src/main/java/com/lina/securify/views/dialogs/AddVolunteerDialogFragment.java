package com.lina.securify.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.lina.securify.R;
import com.lina.securify.data.models.Volunteer;
import com.lina.securify.databinding.DialogAddVolunteerBinding;
import com.lina.securify.utils.Utils;
import com.lina.securify.utils.constants.RequestCodes;
import com.lina.securify.viewmodels.VolunteersViewModel;
import com.lina.securify.views.validations.AddVolunteerValidation;

public class AddVolunteerDialogFragment extends DialogFragment {

    private DialogAddVolunteerBinding binding;
    private VolunteersViewModel viewModel;
    private AddVolunteerValidation validation;

    private static final String TAG = AddVolunteerDialogFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogAddVolunteerBinding.inflate(
                inflater, container, false
        );

        binding.inputPhone.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToContactPicker();
            }
        });

        binding.setVolunteer(new Volunteer());

        validation = new AddVolunteerValidation(binding);
        viewModel = ViewModelProviders.of(requireActivity()).get(VolunteersViewModel.class);

        setToolbar();

        return binding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_add_volunteer, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add) {
            if (validation.validate()) {

                viewModel.addVolunteer(binding.getVolunteer());

                NavHostFragment
                        .findNavController(this)
                        .navigateUp();
            }

            return true;
        } else
            return false;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == RequestCodes.REQUEST_PICK_CONTACT &&
                resultCode == Activity.RESULT_OK) {

            if (data != null)
                setPickedVolunteer(data.getData(), binding.getVolunteer());

        }

    }

    private void goToContactPicker() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);

        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            startActivityForResult(intent, RequestCodes.REQUEST_PICK_CONTACT);
        }

    }

    private void setPickedVolunteer(Uri uri, Volunteer volunteer) {

        String[] projection = {
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        };

        Cursor cursor = requireContext()
                .getContentResolver()
                .query(uri, projection, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {

            int index = cursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            );

            volunteer.setName(cursor.getString(index));

            index = cursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.NUMBER
            );

            volunteer.setPhone(
                    Utils.trimPhone(cursor.getString(index))
            );

            cursor.close();
        }

    }

    private void setToolbar() {

        setHasOptionsMenu(true);

        Toolbar toolbar =
                ((AppCompatActivity) requireActivity())
                        .findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_close);
    }

}
