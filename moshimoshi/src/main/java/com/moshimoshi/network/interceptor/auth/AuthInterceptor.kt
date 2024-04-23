package com.moshimoshi.network.interceptor.auth

import com.moshimoshi.network.authenticator.Authenticator
import com.moshimoshi.network.retrofit.authenticated
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val authenticator: Authenticator) : Interceptor {
    private val authorizationMutex = Mutex()
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val requestWithHeaders = authenticator.addHeaders(originRequest.newBuilder())

        return when {
            originRequest.authenticated() -> {
                runBlocking {
                    authorizationMutex.withLock {
                        authenticator.authorize(requestWithHeaders)
                    }
                }.let { requestAuthenticated ->
                    var response = chain.proceed(requestAuthenticated.build())
                    if (response.code() == 401){
                        runBlocking {
                            authenticator.logout()
                        }
                        return response;
                    }
                    return response;
                }
            }
            else -> {
                chain.proceed(requestWithHeaders.build())
            }
        }
    }
}