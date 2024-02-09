package com.moshimoshi.app.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moshimoshi.app.di.Container
import com.moshimoshi.network.entities.Parameter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginViewModel(): ViewModel() {
    private var _isLogged: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isLogged: StateFlow<Boolean> = _isLogged

    private var _error: MutableStateFlow<String> = MutableStateFlow("")
    var error: StateFlow<String> = _error

    fun load() {
        viewModelScope.launch {
            _isLogged.value = Container.getInstance().authenticator.isLogged()
        }
    }
    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                _error.value = ""
                val parameters = listOf<Parameter>(
                    Parameter(key = "username", value = username),
                    Parameter(key = "password", value = password)
                )
                Container.getInstance().authenticator.getNewToken(parameters)
                load()
            } catch (e:Exception) {
                _error.value = "La autenticación ha fallado"
            }
        }
    }
}
