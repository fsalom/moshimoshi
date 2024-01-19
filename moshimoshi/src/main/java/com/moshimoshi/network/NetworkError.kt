package com.moshimoshi.network

sealed class NetworkError : Exception() {
    object EmptyBody : NetworkError()
    class HttpException(val description: String) : NetworkError()
    class Network(val description: String) : NetworkError()
    class Timeout(val description: String) : NetworkError()
    class Unknown(val description: String) : NetworkError()
}

sealed class HttpCodeError : Exception() {
    object ServerNoContent : HttpCodeError() //204
    object BadRequest : HttpCodeError() //400
    object Unauthorized : HttpCodeError() //401
    object Forbidden : HttpCodeError()  //403
    object NotFound : HttpCodeError() //404
    object InternalServerError : HttpCodeError() //500
}