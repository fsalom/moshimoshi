package com.moshimoshi.app.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moshimoshi.app.di.Container
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginViewModel(): ViewModel() {
    private var _isLogged: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isLogged: StateFlow<Boolean> = _isLogged

    fun load() {
        viewModelScope.launch {
            _isLogged.value = Container.getInstance().authenticator.isLogged()
        }
    }
    fun login(username: String, password: String) {
        viewModelScope.launch {
            Container.getInstance().authenticator.login(
                username = username,
                password = password
            )
            load()
        }
    }
}
