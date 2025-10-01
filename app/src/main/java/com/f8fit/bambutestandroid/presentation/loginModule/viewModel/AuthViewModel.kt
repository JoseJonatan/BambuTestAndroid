package com.f8fit.bambutestandroid.presentation.loginModule.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.f8fit.bambutestandroid.data.repository.AuthRepository
import com.f8fit.bambutestandroid.data.repository.AuthResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {


    private val _state = MutableStateFlow<AuthResultState?>(null)
    val state: StateFlow<AuthResultState?> = _state


    fun register(name : String, email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthResultState.Loading
            _state.value = repo.register(name, email, password)
        }
    }


    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthResultState.Loading
            _state.value = repo.login(email, password)
        }
    }


    fun logout() {
        repo.logout()
        _state.value = null
    }


    fun currentUserId() = repo.currentUserId()
}