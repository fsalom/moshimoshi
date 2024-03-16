package com.moshimoshi.network.interceptor.auth

import com.moshimoshi.network.authenticator.Authenticator
import com.moshimoshi.network.retrofit.authenticated
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val authenticator: Authenticator) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val requestWithHeaders = authenticator.addHeaders(originRequest.newBuilder())

        return when {
            originRequest.authenticated() -> {
                var requestAuthenticated = authenticator.authorize(request = requestWithHeaders)
                chain.proceed(requestAuthenticated.build())
            }
            else -> {
                chain.proceed(requestWithHeaders.build())
            }
        }
    }
}