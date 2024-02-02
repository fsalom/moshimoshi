package com.moshimoshi.network.interceptor

import com.moshimoshi.network.authenticator.Authenticator
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val authenticator: Authenticator) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = authenticator.authorize(request = chain.request().newBuilder())
        return chain.proceed(request.build())
    }
}