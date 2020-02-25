package com.lina.securify.viewmodels;

import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.ViewModel;

import com.lina.securify.data.FirestoreRepository;
import com.lina.securify.data.repositories.AuthRepository;

abstract class AuthViewModel extends ViewModel {

    protected FirestoreRepository repository = FirestoreRepository.getInstance();

    public final ObservableBoolean isLoading = new ObservableBoolean();

}
