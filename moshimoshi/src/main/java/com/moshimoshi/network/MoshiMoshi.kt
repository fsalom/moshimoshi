package com.moshimoshi.network

import com.moshimoshi.network.storage.TokenStorage
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
    private val interceptor: Interceptor,
    converterFactory: Converter.Factory = GsonConverterFactory.create()
) {
    private lateinit var retrofit: Retrofit

    init {
        retrofit = Retrofit.Builder()
            .addConverterFactory(converterFactory)
            .baseUrl(baseUrl)
            .client(OkHttpClient.Builder().addInterceptor(interceptor).build())
            .build()
    }

    fun <T> create(service: Class<T>?): T {
        return retrofit.create(service)
    }

    suspend fun <T> load(call: suspend () -> Response<T>): T {
        try {
            val response = call()
            response.body()?.let { body ->
                if (response.isSuccessful) {
                    return body
                } else {
                    throw response.code().toHttpError()
                }
            } ?: throw NetworkError.EmptyBody
        } catch (e: Exception) {
            throw handler(e)
        }
    }

    suspend fun <T> loadAuthenticated(call: suspend () -> Response<T>): T {
        try {
            val response = call()
            response.body()?.let { body ->
                if (response.isSuccessful) {
                    return body
                } else {
                    throw response.code().toHttpError()
                }
            } ?: throw NetworkError.EmptyBody
        } catch (e: Exception) {
            throw handler(e)
        }
    }

    private fun Int.toHttpError() =
        when (this) {
            204 -> HttpCodeError.ServerNoContent
            400 -> HttpCodeError.BadRequest
            401 -> HttpCodeError.Unauthorized
            403 -> HttpCodeError.Forbidden
            404 -> HttpCodeError.NotFound
            else -> HttpCodeError.InternalServerError
        }

    private fun handler(error: Exception): Exception {
        return when (error) {
            is HttpException -> {
                val body = error.response()?.errorBody().toString()
                NetworkError.HttpException(body)
            }

            is SocketTimeoutException -> NetworkError.Timeout("Timeout Error")
            is IOException -> NetworkError.Network("Thread Error")
            else -> NetworkError.Unknown("Unknown Error")
        }
    }
}