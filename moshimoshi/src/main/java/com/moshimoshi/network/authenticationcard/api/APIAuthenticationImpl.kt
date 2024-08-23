package com.moshimoshi.network.authenticationcard.api

import android.content.Context
import android.content.Intent
import com.google.gson.GsonBuilder
import com.moshimoshi.network.authenticationcard.AuthenticationCard
import com.moshimoshi.network.authenticationcard.api.dto.TokensDTO
import com.moshimoshi.network.authenticationcard.api.dto.toDomain
import com.moshimoshi.network.entities.Endpoint
import com.moshimoshi.network.entities.NetworkError
import com.moshimoshi.network.entities.Parameter
import com.moshimoshi.network.entities.Token
import com.moshimoshi.network.entities.Tokens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Response

class APIAuthenticationImpl(private var loginEndpoint: Endpoint,
                            private var refreshEndpoint: Endpoint?,
                            private val packageName: String,
                            private val className: String,
                            private val context: Context): AuthenticationCard {

    override suspend fun getCurrentToken(parameters: List<Parameter>, endpoint: Endpoint?): Tokens {
        return withContext(Dispatchers.IO) {
            var completeLoginEndpoint = loginEndpoint
            if (endpoint != null) {
                completeLoginEndpoint = endpoint
            }
            completeLoginEndpoint.add(params = parameters)
            val request = completeLoginEndpoint.getRequest()
            val response = OkHttpClient().newCall(request).execute()
            getTokensOrFail(response)
        }
    }

    override suspend fun refreshAccessToken(refreshToken: String): Tokens {
        return withContext(Dispatchers.IO) {
            val completeRefreshEndpoint = refreshEndpoint
            val refreshParameter = Parameter(key = "refresh_token", value = refreshToken)
            if (completeRefreshEndpoint != null) {
                completeRefreshEndpoint.add(params = listOf<Parameter>(refreshParameter))
                val request = completeRefreshEndpoint.getRequest()
                val response = OkHttpClient().newCall(request).execute()
                getTokensOrFail(response)
            } else {
                throw NetworkError.NoRefresh
            }
        }
    }

    override suspend fun logout() {
        showLogin()
    }

    private fun getTokensOrFail(response: Response): Tokens {
        return if (response.isSuccessful) {
            val gson = GsonBuilder().create()
            val tokensDTO = gson.fromJson(response.body()?.string(), TokensDTO::class.java)
            tokensDTO.toDomain()
        } else {
            val code = response.code()
            val body = response.body()?.string()
            throw NetworkError.Failure(code, body ?: "")
        }
    }
    private fun showLogin() {
        val intent = Intent()
        intent.setClassName(packageName, className)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}