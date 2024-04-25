package com.moshimoshi.app.presentation.app

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moshimoshi.app.data.datasource.character.api.CharacterApi
import com.moshimoshi.app.data.datasource.user.api.UserApi
import com.moshimoshi.app.di.Container
import com.moshimoshi.app.presentation.login.LoginActivity
import com.moshimoshi.network.entities.Token
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(): ViewModel() {
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
            var api = Container.getInstance().moshi.create(UserApi::class.java)
            try {
                var me = Container.getInstance().moshi.load {
                    api.getMe()
                }
            } catch (e:Exception){

            }
            _message.value = "Llamada autenticada"
        }
    }

    fun load() {
        viewModelScope.launch {
            var api = Container.getInstance().moshi.create(CharacterApi::class.java)
            var x = Container.getInstance().moshi.load {
                api.getCharacter2(id = 1)
            }
            _message.value = "Llamada normal"
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
            var accessToken = Container.getInstance().moshi.authenticator.tokenStore.getAccessToken()
            accessToken?.timestampExpires = System.currentTimeMillis()
            if (accessToken != null) {
                Container.getInstance().moshi.authenticator.tokenStore.setAccessToken(accessToken)
                _message.value = "Access token caducado"
            }
        }
    }

    fun expireAccessAndRefreshToken() {
        viewModelScope.launch {
            var accessToken = Container.getInstance().moshi.authenticator.tokenStore.getAccessToken()
            accessToken?.timestampExpires = System.currentTimeMillis()
            var refreshToken = Container.getInstance().moshi.authenticator.tokenStore.getRefreshToken()
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
