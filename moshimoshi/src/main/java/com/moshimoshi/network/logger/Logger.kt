package com.moshimoshi.network.logger

import okhttp3.Request
import okhttp3.Response

interface Logger {
    fun log(message: String)
    fun log(request: Request, response: Response, duration: Double)
}