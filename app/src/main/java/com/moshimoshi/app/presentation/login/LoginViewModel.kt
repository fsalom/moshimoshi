package com.moshimoshi.app.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moshimoshi.app.data.datasources.character.remote.dto.CharacterDTO
import com.moshimoshi.app.di.Container
import com.moshimoshi.network.MoshiMoshi
import com.moshimoshi.network.authenticator.Authenticator
import com.moshimoshi.network.entities.Parameter
import com.moshimoshi.network.retrofit.Authenticated
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import retrofit2.Response
import retrofit2.http.GET

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
                Container.getInstance().authenticator.getNewToken(parameters, null)
                load()
            } catch (e:Exception) {
                _error.value = "La autenticación ha fallado"
            }
        }
    }
}
