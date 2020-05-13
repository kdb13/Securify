package com.lina.securify.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.lina.securify.R;
import com.lina.securify.adapters.VolunteersAdapter;
import com.lina.securify.data.models.Volunteer;
import com.lina.securify.databinding.FragmentVolunteersBinding;
import com.lina.securify.utils.Utils;
import com.lina.securify.contracts.Constants;
import com.lina.securify.viewmodels.VolunteersViewModel;

public class VolunteersFragment extends Fragment {

    private static final String TAG = "VolunteersFragment";

    private FragmentVolunteersBinding binding;
    private VolunteersViewModel viewModel;
    private VolunteersAdapter adapter;
    private SelectionTracker<String> selectionTracker;
    private ActionMode actionMode;
    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            // Inflate the menu
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.menu_volunteers_action_mode, menu);

            // Change the status bar color
            Utils.changeStatusBarColor(requireActivity(), android.R.color.black);

            // Hide the FAB
            binding.floatingActionButton.hide();

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {

                case R.id.delete:

                    viewModel.removeVolunteers(adapter.getSelectedPhones());
                    actionMode.finish();
                    return true;

                default:
                    return false;
            }

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            actionMode = null;

            // Revert the status bar color
            Utils.changeStatusBarColor(requireActivity(), R.color.colorPrimaryDark);

            // Clear the selection
            selectionTracker.clearSelection();

            // Show the FAB
            binding.floatingActionButton.show();
        }

    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(VolunteersViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVolunteersBinding.inflate(inflater, container, false);
        binding.setFragment(this);

        setAdapter();

        return binding.getRoot();
    }


    public void onFabClick(View view) {

        if (adapter.getSnapshots().size() ==
                getResources().getInteger(R.integer.volunteer_limit)) {

            Snackbar.make(binding.getRoot(),
                    R.string.volunteer_limit_message, Snackbar.LENGTH_SHORT)
                    .show();

            return;
        }

        NavHostFragment
                .findNavController(this)
                .navigate(VolunteersFragmentDirections.actionAddVolunteer());
    }

    /**
     * Build the VolunteersAdapter.
     */
    private void setAdapter() {

        FirestoreRecyclerOptions<Volunteer> options =
                new FirestoreRecyclerOptions.Builder<Volunteer>()
                        .setQuery(viewModel.getVolunteers(), Volunteer.class)
                        .setLifecycleOwner(this)
                        .build();

        adapter = new VolunteersAdapter(options);

        binding.volunteersList.setAdapter(adapter);

        selectionTracker = new SelectionTracker.Builder<>(
                Constants.VOLUNTEERS_SELECTION_ID,
                binding.volunteersList,
                new VolunteersAdapter.VolunteerKeyProvider(adapter),
                new VolunteersAdapter.VolunteerDetailsLookup(binding.volunteersList),
                StorageStrategy.createStringStorage())
                .build();

        selectionTracker.addObserver(new SelectionTracker.SelectionObserver() {
            @Override
            public void onSelectionChanged() {
                super.onSelectionChanged();

                if (selectionTracker.hasSelection()) {

                    if (actionMode == null) {

                        actionMode = ((AppCompatActivity) requireActivity())
                                .startSupportActionMode(actionModeCallback);

                    }

                    actionMode.setTitle(getString(R.string.cab_title, selectionTracker.getSelection().size()));

                } else {
                    if (actionMode != null)
                        actionMode.finish();
                }

            }
        });

        adapter.setSelectionTracker(selectionTracker);
    }

}
