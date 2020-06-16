package com.lina.securify.views.viewmodels;

import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.Query;
import com.lina.securify.data.repositories.VolunteersRepository;

import java.util.Iterator;

public class VolunteersViewModel extends ViewModel {

    private VolunteersRepository volunteersRepository;

    public VolunteersViewModel() {
        volunteersRepository = VolunteersRepository.getInstance();
    }

    public Query queryVolunteers() {
        return volunteersRepository.queryVolunteers();
    }

    public void remove(Iterator<String> keys) {
        volunteersRepository.remove(keys);
    }

    public void removeAll() {
        volunteersRepository.removeAll();
    }

}
