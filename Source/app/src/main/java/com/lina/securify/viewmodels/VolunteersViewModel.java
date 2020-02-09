package com.lina.securify.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.lina.securify.adapters.VolunteersAdapter;
import com.lina.securify.data.FirestoreRepository;
import com.lina.securify.data.models.Volunteer;

import java.util.List;

public class VolunteersViewModel extends ViewModel {

    private static final String TAG = VolunteersViewModel.class.getSimpleName();

    private FirestoreRepository repository;
    private VolunteersAdapter adapter;

    private final MutableLiveData<Integer> selection = new MutableLiveData<>();

    public VolunteersViewModel() {
        repository = FirestoreRepository.getInstance();
        selection.setValue(0);
    }

    public VolunteersAdapter getAdapter() {
        return adapter;
    }

    public Query getVolunteers() {
        return repository.getVolunteers();
    }

    public LiveData<Integer> getSelection() {
        return selection;
    }

    public void addVolunteer(Volunteer volunteer) {
        repository.addVolunteer(volunteer);
    }

    public void removeVolunteers() {
        repository.removeVolunteers(adapter.getSelectedPhones());
    }

    public void setAdapter(
            FirestoreRecyclerOptions<Volunteer> options,
            RecyclerView recyclerView) {

        adapter = new VolunteersAdapter(options, recyclerView);
        adapter.getTracker().addObserver(new SelectionTracker.SelectionObserver() {

            @Override
            public void onSelectionChanged() {
                selection.setValue(adapter.getTracker().getSelection().size());
            }

        });

    }


}
