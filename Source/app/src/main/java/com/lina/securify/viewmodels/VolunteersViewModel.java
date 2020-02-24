package com.lina.securify.viewmodels;


import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.Query;
import com.lina.securify.data.FirestoreRepository;
import com.lina.securify.data.models.Volunteer;

import java.util.List;

public class VolunteersViewModel extends ViewModel {

    private static final String TAG = VolunteersViewModel.class.getSimpleName();

    private FirestoreRepository repository = FirestoreRepository.getInstance();

    public Query getVolunteers() {
        return repository.getVolunteers();
    }

    public void addVolunteer(Volunteer volunteer) {
        repository.addVolunteer(volunteer);
    }

    public void removeVolunteers(List<String> phones) {
        repository.removeVolunteers(phones);
    }
}
