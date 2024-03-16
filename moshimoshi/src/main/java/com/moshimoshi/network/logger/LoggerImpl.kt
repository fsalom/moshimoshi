package com.moshimoshi.network.logger

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import okhttp3.Request
import okhttp3.Response

class LoggerImpl(
    private val identifier: String,
    private val style: Style
): Logger {
    enum class Style {
        COMPLETE, SHORT
    }
    override fun log(message: String) {
        Log.i(identifier, message)
    }

    override fun log(request: Request, response: Response, duration: Double) {
        logRequest(request)
        logResponse(response, duration)
    }

    private fun logRequest(request: Request) {
        log(String.format("☎️ ➡️ %s con headers: [%s]",
            request.url(),
            request.headers().toString())
        )
    }

    private fun logResponse(response: Response, duration: Double) {
        when (style){
            Style.COMPLETE -> logComplete(response, duration)
            Style.SHORT -> logShort(response, duration)
        }
    }

    private fun logComplete(response: Response, duration: Double) {
        log(String.format("\uD83D\uDD3D %s [%d] %s en %.1fms",
            getIcon(response.code()),
            response.code(),
            response.request().url(),
            duration
        ))
        log(bodyToString(response))
        log(String.format(
            "\uD83D\uDD3C %s [%d] %s en %.1fms",
            getIcon(response.code()),
            response.code(),
            response.request().url(),
            duration
        ))
    }

    private fun logShort(response: Response, duration: Double) {
        log(String.format("▶️ %s [%d] %s en %.1fms",
            getIcon(response.code()),
            response.code(),
            response.request().url(),
            duration
        ))
    }

    private fun getIcon(code: Int): String {
        return if(code in 200..300) "✅" else "❌"
    }

    private fun bodyToString(response: Response): String {
        if (response.body() == null) {
            ""
        }
        return GsonBuilder().setPrettyPrinting().create().toJson(
            JsonParser().parse(
                response.peekBody(Long.MAX_VALUE).string()
            )
        )
    }
}
