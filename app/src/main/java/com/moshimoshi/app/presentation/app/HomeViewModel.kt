package com.moshimoshi.app.presentation.app

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moshimoshi.app.data.datasources.character.remote.endpoints.CharacterApi
import com.moshimoshi.app.data.datasources.user.remote.endpoints.UserApi
import com.moshimoshi.app.di.Container
import com.moshimoshi.app.domain.usecases.character.CharacterUseCases
import com.moshimoshi.app.domain.usecases.user.UserUseCases
import com.moshimoshi.app.presentation.login.LoginActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(val characterUseCases: CharacterUseCases,
                    val userUseCases: UserUseCases): ViewModel() {


    private var _message: MutableStateFlow<String> = MutableStateFlow("")
    var message: StateFlow<String> = _message
    private var _isLogged: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isLogged: StateFlow<Boolean> = _isLogged

    fun initialize() {
        viewModelScope.launch {
            if (!Container.getInstance().authenticator.isLogged()) {
                val intent = Intent()
                intent.setClassName(Container.context.packageName, LoginActivity::class.java.canonicalName)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                Container.context.startActivity(intent)
            }
        }
    }

    fun loadAuthenticated() {
        viewModelScope.launch {
            try {
                userUseCases.getMe()
                _message.value = "Llamada autenticada"
            } catch (e:Exception){
                Log.e("MOSHI", "loadAuthenticated: "+ e.message, e)
                _message.value = "Error: Llamada autenticada"
            }
        }
    }

    fun load() {
        viewModelScope.launch {
            try {
                var characters = characterUseCases.getCharacters()
                _message.value = "Llamada normal"
            } catch (e: Exception) {
                Log.e("MOSHI", "load: "+ e.message, e)
                _message.value = "Error: Llamada"
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            Container.getInstance().moshi.authenticator.logout()
            _message.value = "Logout"
        }
    }

    fun expireAccessToken() {
        viewModelScope.launch {
            val accessToken = Container.getInstance().moshi.authenticator.tokenStore.getAccessToken()
            accessToken?.timestampExpires = System.currentTimeMillis()
            if (accessToken != null) {
                Container.getInstance().moshi.authenticator.tokenStore.setAccessToken(accessToken)
                _message.value = "Access token caducado"
            }
        }
    }

    fun expireAccessAndRefreshToken() {
        viewModelScope.launch {
            val accessToken = Container.getInstance().moshi.authenticator.tokenStore.getAccessToken()
            accessToken?.timestampExpires = System.currentTimeMillis()
            val refreshToken = Container.getInstance().moshi.authenticator.tokenStore.getRefreshToken()
            refreshToken?.timestampExpires = System.currentTimeMillis()
            if (accessToken != null && refreshToken != null) {
                Container.getInstance().moshi.authenticator.tokenStore.setAccessToken(accessToken)
                Container.getInstance().moshi.authenticator.tokenStore.setRefreshToken(refreshToken)
                _message.value = "Access y refresh caducados"
            }
        }
    }

    fun reset() {
        _message.value = ""
    }
}
