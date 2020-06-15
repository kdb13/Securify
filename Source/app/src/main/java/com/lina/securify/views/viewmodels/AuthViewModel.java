package com.lina.securify.views.viewmodels;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModel;

import com.lina.securify.data.repositories.AuthRepository;

abstract class AuthViewModel extends ViewModel {

    protected AuthRepository repository = AuthRepository.getInstance();

    public final ObservableBoolean isLoading = new ObservableBoolean();

}
