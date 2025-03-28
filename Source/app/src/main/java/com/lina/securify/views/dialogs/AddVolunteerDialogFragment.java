package com.lina.securify.views.dialogs;

import androidx.fragment.app.DialogFragment;

public class AddVolunteerDialogFragment extends DialogFragment {

    /*private DialogAddVolunteerBinding binding;
    private VolunteersViewModel viewModel;
    private AddVolunteerValidation validation;

    private static final String TAG = AddVolunteerDialogFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(VolunteersViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DialogAddVolunteerBinding.inflate(
                inflater, container, false
        );
        binding.inputPhone.setEndIconOnClickListener(view -> goToContactPicker());
        binding.setVolunteer(new Volunteer());
        binding.setIsLoading(false);

        validation = new AddVolunteerValidation(binding);

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
        // Clear the menu parent activity
        menu.clear();

        // Inflate the new menu
        inflater.inflate(R.menu.add_volunteer, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.save) {

            if (validation.validate()) {
                addVolunteer();
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

    *//**
     * Navigate to the contact picker.
     *//*
    private void goToContactPicker() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);

        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            startActivityForResult(intent, RequestCodes.REQUEST_PICK_CONTACT);
        }

    }

    *//**
     * Update the UI with the picked contact.
     *//*
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

    *//**
     * Set the dialog's toolbar.
     *//*
    private void setToolbar() {

        setHasOptionsMenu(true);

        Toolbar toolbar =
                ((AppCompatActivity) requireActivity())
                        .findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_close);

    }

    private void addVolunteer() {

        binding.setIsLoading(true);

        viewModel.isExistingVolunteer(binding.getVolunteer().getPhone())
                .get()
                .addOnSuccessListener(documentSnapshots -> {

                    if (documentSnapshots.isEmpty()) {
                        viewModel.addVolunteer(binding.getVolunteer());

                        NavHostFragment.findNavController(this)
                                .navigateUp();
                    }
                    else {
                        binding.inputPhone
                                .setError(getString(R.string.existing_volunteer_message));

                        binding.setIsLoading(false);
                    }

                });

    }*/

}
