package com.moshimoshi.network.entities

sealed class NetworkError: Exception() {
    data class Failure(val code: Int, val body: String) : NetworkError()
    object Unknown : NetworkError()
    object NoInternet : NetworkError()
    object EmptyBody : NetworkError()
    object Timeout: NetworkError()

    override fun toString(): String {
        return when (this) {
            is Failure -> "NetworkError: Failure(code=$code, body='$body')"
            is Unknown -> "NetworkError: Unknown"
            is NoInternet -> "NetworkError: NoInternet"
            is Timeout -> "NetworkError: Timeout"
            is EmptyBody -> "NetworkError: EmptyBody"
        }
    }
}