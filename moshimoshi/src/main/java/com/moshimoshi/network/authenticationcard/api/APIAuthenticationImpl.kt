package com.moshimoshi.network.authenticationcard.api

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.google.gson.GsonBuilder
import com.moshimoshi.network.authenticationcard.AuthenticationCard
import com.moshimoshi.network.entities.Tokens
import okhttp3.OkHttpClient
import okhttp3.Request


class APIAuthenticationImpl(private val loginCall: Request,
                            private val refreshCall: Request): AuthenticationCard {

    override suspend fun getCurrentToken(): Tokens {
        val response = OkHttpClient().newCall(loginCall).execute()
        val gson = GsonBuilder().create()
        return gson.fromJson(response.body()?.string(), Tokens::class.java)
    }

    override suspend fun refreshAccessToken(refreshToken: String): Tokens {
        val response = OkHttpClient().newCall(refreshCall).execute()
        val gson = GsonBuilder().create()
        return gson.fromJson(response.body()?.string(), Tokens::class.java)
    }

    override suspend fun logout() {

    }

    fun showLogin() {
    }
}