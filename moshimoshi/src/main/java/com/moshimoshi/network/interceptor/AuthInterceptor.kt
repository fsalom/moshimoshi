package com.moshimoshi.network.interceptor

import com.moshimoshi.network.authenticator.Authenticator
import com.moshimoshi.network.retrofit.authenticated
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val authenticator: Authenticator) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val builder = originRequest.newBuilder()

        return when {
            originRequest.authenticated() -> {
                var currentRequest = chain.request()
                var request = authenticator.authorize(request = currentRequest.newBuilder())
                chain.proceed(request.build())
            }
            else -> {
                chain.proceed(builder.build())
            }
        }
    }
}