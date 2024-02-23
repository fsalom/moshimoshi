package com.moshimoshi.network.authenticationcard.api

import android.content.Context
import android.content.Intent
import com.google.gson.GsonBuilder
import com.moshimoshi.network.authenticationcard.AuthenticationCard
import com.moshimoshi.network.authenticationcard.api.dto.TokensDTO
import com.moshimoshi.network.authenticationcard.api.dto.toDomain
import com.moshimoshi.network.entities.Endpoint
import com.moshimoshi.network.entities.Parameter
import com.moshimoshi.network.entities.Tokens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient


class APIAuthenticationImpl(private var loginEndpoint: Endpoint,
                            private var refreshEndpoint: Endpoint,
                            private val packageName: String,
                            private val className: String,
                            private val context: Context): AuthenticationCard {

    override suspend fun getCurrentToken(parameters: List<Parameter>): Tokens? {
        return withContext(Dispatchers.IO) {
            try {
                var completeLoginEndpoint = loginEndpoint
                completeLoginEndpoint.add(params = parameters)
                val request = completeLoginEndpoint.getRequest()
                val response = OkHttpClient().newCall(request).execute()
                val gson = GsonBuilder().create()
                val tokensDTO = gson.fromJson(response.body()?.string(), TokensDTO::class.java)
                tokensDTO.toDomain()
            } catch (e: Exception) {
                null
            }
        }
    }

    override suspend fun refreshAccessToken(refreshToken: String): Tokens? {
        return withContext(Dispatchers.IO) {
            try {
                var completeRefreshEndpoint = refreshEndpoint
                var refreshParameter = Parameter(key = "refresh_token", value = refreshToken)
                completeRefreshEndpoint.add(params = listOf<Parameter>(refreshParameter))
                val request = completeRefreshEndpoint.getRequest()
                val response = OkHttpClient().newCall(request).execute()
                val gson = GsonBuilder().create()
                val tokensDTO = gson.fromJson(response.body()?.string(), TokensDTO::class.java)
                tokensDTO.toDomain()
            } catch (e: Exception) {
                null
            }
        }
    }

    override suspend fun logout() {
        showLogin()
    }

    private fun showLogin() {
        val intent = Intent()
        intent.setClassName(packageName, className)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}