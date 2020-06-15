package com.lina.securify.views.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.lina.securify.R;
import com.lina.securify.adapters.VolunteersAdapter;
import com.lina.securify.data.models.Volunteer;
import com.lina.securify.databinding.FragmentVolunteersBinding;
import com.lina.securify.utils.Utils;
import com.lina.securify.views.viewmodels.VolunteersViewModel;

/**
 * Displays the volunteers added by the user.
 */
public class VolunteersFragment extends Fragment {

    private FragmentVolunteersBinding binding;

    private VolunteersAdapter adapter;

    private VolunteersViewModel viewModel;

    private SelectionTracker<String> selectionTracker;

    private DialogFragment removeAllVolunteersDialog;

    private ActionMode actionMode;
    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            Utils.changeStatusBarColor(requireActivity(), android.R.color.black);

            mode.getMenuInflater().inflate(R.menu.volunteers_action_mode, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.action_delete) {

                viewModel.remove(adapter.getSelectedKeys());

                actionMode.finish();

                return true;
            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            selectionTracker.clearSelection();

            Utils.changeStatusBarColor(requireActivity(), R.color.colorPrimaryDark);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(VolunteersViewModel.class);

        setupRecyclerViewAdapter();

        setHasOptionsMenu(true);

        removeAllVolunteersDialog = new DialogFragment() {
            @NonNull
            @Override
            public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
                return new MaterialAlertDialogBuilder(requireContext())
                        .setMessage(R.string.msg_remove_volunteers)
                        .setPositiveButton(R.string.button_remove, (dialog, which) -> viewModel.removeAll())
                        .setNegativeButton(R.string.button_cancel, null)
                        .create();
            }
        };

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVolunteersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fabAddVolunteer.setOnClickListener(this::navigateToAddVolunteer);

        setupRecyclerView();

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.volunteers, menu);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        selectionTracker.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        selectionTracker.onRestoreInstanceState(savedInstanceState);
        handleSelectionChange();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_removeAll) {
            confirmRemoveAllVolunteers();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.startListening();
    }

    private void setupRecyclerView() {
        binding.recyclerViewVolunteers.setAdapter(adapter);
        binding.recyclerViewVolunteers.setHasFixedSize(true);

        selectionTracker = new SelectionTracker.Builder<>(
                "volunteers-selection",
                binding.recyclerViewVolunteers,
                new VolunteersAdapter.MyItemKeyProvider(adapter),
                new VolunteersAdapter.MyItemDetailsLookup(binding.recyclerViewVolunteers),
                StorageStrategy.createStringStorage())
                .withSelectionPredicate(SelectionPredicates.createSelectAnything())
                .build();

        adapter.setSelectionTracker(selectionTracker);

        selectionTracker.addObserver(new SelectionTracker.SelectionObserver<String>() {
            @Override
            public void onSelectionChanged() {
                handleSelectionChange();
            }
        });
    }

    private void handleSelectionChange() {

        if (selectionTracker.hasSelection()) {

            if (actionMode == null) {
                actionMode = requireActivity().startActionMode(actionModeCallback);
            }

            actionMode.setTitle(String.valueOf(selectionTracker.getSelection().size()));

        } else {
            if (actionMode != null) {
                actionMode.finish();
            }
        }

    }

    private void setupRecyclerViewAdapter() {

        FirestoreRecyclerOptions<Volunteer> options =
                new FirestoreRecyclerOptions.Builder<Volunteer>()
                        .setQuery(viewModel.queryVolunteers(), Volunteer.class)
                        .build();

        adapter = new VolunteersAdapter(options);

    }

    private void confirmRemoveAllVolunteers() {
        removeAllVolunteersDialog.show(getChildFragmentManager(), "removeAllVolunteers");
    }

    private void navigateToAddVolunteer(View view) {

        int volunteersLimit = getResources().getInteger(R.integer.volunteer_limit);

        if (adapter.getSnapshots().size() == volunteersLimit) {

            Snackbar
                    .make(view, R.string.volunteers_limit_message, Snackbar.LENGTH_SHORT)
                    .show();

            return;
        }

        NavHostFragment
                .findNavController(this)
                .navigate(VolunteersFragmentDirections.actionAddVolunteer());
    }

}
