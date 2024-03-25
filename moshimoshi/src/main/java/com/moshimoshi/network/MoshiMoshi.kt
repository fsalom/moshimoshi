package com.moshimoshi.network

import com.moshimoshi.network.authenticator.Authenticator
import com.moshimoshi.network.entities.NetworkError
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException

class MoshiMoshi(
    private val baseUrl: String,
    private val interceptors: Array<Interceptor>,
    val authenticator: Authenticator,
    converterFactory: Converter.Factory = GsonConverterFactory.create()
) {
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(converterFactory)
        .baseUrl(baseUrl)
        .client(
            OkHttpClient
                .Builder()
                .apply {
                    interceptors.forEach { addInterceptor(it) }
                }
                .build()
        )
        .build()

    fun <T> create(service: Class<T>?): T {
        return retrofit.create(service)
    }

    suspend fun <T> load(call: suspend () -> Response<T>): T {
        try {
            val response = call()
            if (response.isSuccessful) {
                return response.body() ?: throw  NetworkError.EmptyBody
            } else {
                val code = response.code()
                val body = response.errorBody()?.string() ?: ""
                throw NetworkError.Failure(code, body)
            }
        } catch (error: Exception) {
            throw handler(error)
        }
    }

    private fun handler(error: Exception): Exception {
        return when (error) {
            is HttpException -> {
                val code = error.response()?.code()
                val body = error.response()?.errorBody()?.string() ?: ""
                NetworkError.Failure(code ?: 0, body)
            }
            is SocketTimeoutException -> NetworkError.Timeout
            is IOException -> NetworkError.NoInternet
            is NetworkError -> error
            else -> NetworkError.Unknown
        }
    }
}