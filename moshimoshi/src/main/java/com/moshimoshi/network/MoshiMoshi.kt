package com.moshimoshi.network

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
    private val client: OkHttpClient = OkHttpClient(),
    converterFactory: Converter.Factory = GsonConverterFactory.create()
) {
    private var retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(converterFactory)
        .baseUrl(baseUrl)
        .client(client)
        .build()

    fun <T> create(service: Class<T>?) {
        retrofit.create(service)
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
            when (e) {
                is HttpException -> {
                    val body = e.response()?.errorBody().toString()
                    throw NetworkError.HttpException(body)
                }
                is SocketTimeoutException -> throw NetworkError.Timeout("Timeout Error")
                is IOException -> throw NetworkError.Network("Thread Error")
                else -> throw NetworkError.Unknown("Unknown Error")
            }
        }
    }

    fun Int.toHttpError() =
        when (this) {
            204 -> HttpCodeError.ServerNoContent
            400 -> HttpCodeError.BadRequest
            401 -> HttpCodeError.Unauthorized
            403 -> HttpCodeError.Forbidden
            404 -> HttpCodeError.NotFound
            else -> HttpCodeError.InternalServerError
        }
}