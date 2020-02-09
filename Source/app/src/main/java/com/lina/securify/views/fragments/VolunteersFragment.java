package com.lina.securify.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.lina.securify.R;
import com.lina.securify.adapters.VolunteersAdapter;
import com.lina.securify.data.models.Volunteer;
import com.lina.securify.databinding.FragmentVolunteersBinding;
import com.lina.securify.utils.Utils;
import com.lina.securify.viewmodels.VolunteersViewModel;

public class VolunteersFragment extends Fragment {

    private static final String TAG = "VolunteersFragment";

    private FragmentVolunteersBinding binding;
    private VolunteersViewModel viewModel;

    private int volunteersLimit;
    private ActionMode actionMode;
    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate the menu
            mode.getMenuInflater().inflate(R.menu.menu_volunteers_action_mode, menu);

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

            if (item.getItemId() == R.id.delete) {

                viewModel.removeVolunteers();
                actionMode.finish();

                return true;
            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // Change the status bar color
            Utils.changeStatusBarColor(requireActivity(), R.color.colorPrimaryDark);

            // Show the FAB
            binding.floatingActionButton.show();

            // Clear the selection
            viewModel.getAdapter().getTracker().clearSelection();

            mode = null;
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVolunteersBinding.inflate(inflater, container, false);
        binding.setFragment(this);

        viewModel = new ViewModelProvider(requireActivity()).get(VolunteersViewModel.class);

        setVolunteersList();

        volunteersLimit = getResources().getInteger(R.integer.volunteer_limit);

        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        viewModel.getAdapter().getTracker().onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        viewModel.getAdapter().getTracker().onRestoreInstanceState(savedInstanceState);
    }

    public void onFabClick(View view) {

        // Check if the limit is reached
        if (viewModel.getAdapter().getSnapshots().size() == volunteersLimit) {

            Snackbar
                    .make(
                            binding.volunteersList,
                            R.string.volunteer_limit_message,
                            Snackbar.LENGTH_SHORT)
                    .show();

            return;
        }

        goToAddVolunteerDialog();
    }

    private void goToAddVolunteerDialog() {
        NavHostFragment
                .findNavController(this)
                .navigate(VolunteersFragmentDirections.actionAddVolunteer(getId()));

    }

    private void setVolunteersList() {

        viewModel.setAdapter(
                new FirestoreRecyclerOptions.Builder<Volunteer>()
                .setQuery(viewModel.getVolunteers(), Volunteer.class)
                .setLifecycleOwner(this)
                .build(),
                binding.volunteersList
        );

        viewModel.getSelection().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer selection) {

                if (selection > 0) {

                    if (actionMode == null)
                        startActionMode();

                    actionMode.setTitle(selection + " selected");

                } else {

                    if (actionMode != null) {
                        actionMode.finish();
                        actionMode = null;
                    }

                }

            }
        });

    }

    private void startActionMode() {

        actionMode =  ((AppCompatActivity) requireActivity())
                .startSupportActionMode(actionModeCallback);

    }
}
