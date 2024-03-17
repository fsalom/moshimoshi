package com.moshimoshi.app.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.moshimoshi.app.presentation.login.LoginActivity
import com.moshimoshi.network.MoshiMoshi
import com.moshimoshi.network.authenticationcard.api.APIAuthenticationImpl
import com.moshimoshi.network.authenticator.AuthenticatorImpl
import com.moshimoshi.network.entities.Endpoint
import com.moshimoshi.network.entities.Method
import com.moshimoshi.network.entities.Parameter
import com.moshimoshi.network.interceptor.auth.AuthInterceptor
import com.moshimoshi.network.storage.datastore.TokenDataStoreImpl

class Container() {
    companion object {
        @Volatile
        private var instance: Container? = null

        lateinit var context: Context
        lateinit var dataStore: DataStore<Preferences>

        fun config(context: Context, dataStore: DataStore<Preferences>) {
            this.context = context
            this.dataStore = dataStore
        }

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: Container().also { instance = it }
            }
    }

    private val tokenStore = TokenDataStoreImpl(
        uniqueIdentifier = "APP",
        dataStore = dataStore)

    private val loginEndpoint = Endpoint(
        url = "https://dashboard-staging.rudo.es/auth/token",
        method = Method.POST,
        formParams = listOf(
            Parameter(key = "client_id", value = "1gzyJeSOyjUOmbSHREbsothngkBMato1VypQz35D"),
            Parameter(key = "client_secret", value = "ynM8CpvlDHivO1jma1Q3Jv1RIJraBbJ9EtK5XI3dw4RpkxDgi9cZnmJlQs0XzuVCGWCNwQd8qJKAHFrLdHlRRDIzx8B08HJ0Htu6XFzP4kTRTWYIPHuCpldjouJhKvoA"),
            Parameter(key = "grant_type", value = "password")))

    private val refreshEndpoint = Endpoint(
        url = "https://dashboard-staging.rudo.es/auth/token",
        method = Method.POST,
        formParams = listOf(
            Parameter(key = "client_id", value = "1gzyJeSOyjUOmbSHREbsothngkBMato1VypQz35D"),
            Parameter(key = "client_secret", value = "ynM8CpvlDHivO1jma1Q3Jv1RIJraBbJ9EtK5XI3dw4RpkxDgi9cZnmJlQs0XzuVCGWCNwQd8qJKAHFrLdHlRRDIzx8B08HJ0Htu6XFzP4kTRTWYIPHuCpldjouJhKvoA"),
            Parameter(key = "grant_type", value = "refresh_token")))

    private val authenticationCard = APIAuthenticationImpl(
        loginEndpoint = loginEndpoint,
        refreshEndpoint = refreshEndpoint,
        packageName = context.packageName,
        className = LoginActivity::class.java.canonicalName,
        context = context)

    val authenticator = AuthenticatorImpl(
        tokenStore = tokenStore,
        headers = emptyList(),
        card = authenticationCard)

    private val authInterceptor = AuthInterceptor(
        authenticator = authenticator)

    val moshi = MoshiMoshi(
        baseUrl = "https://rickandmortyapi.com/api/",
        interceptors = arrayOf(authInterceptor),
        authenticator = authenticator)
}