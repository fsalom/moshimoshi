package com.moshimoshi.network.entities

import okhttp3.MultipartBody
import okhttp3.Request
import org.json.JSONObject
import java.io.Serializable

enum class Method {
    GET, POST, PUT, PATCH
}

data class Endpoint(
    var url: String = "",
    var method: Method = Method.GET,
    private var json: JSONObject? = null,
    private var parameters: List<Parameter> = emptyList(),
    private var queryParams: List<Parameter> = emptyList(),
    private var formParams: List<Parameter> = emptyList()
): Serializable {
    private fun checkIfExists(list:  List<Parameter>, key: String): Boolean {
        list.forEach {
            if(it.key == key) {
                return true
            }
        }
        return false
    }

    fun add(formParams: List<Parameter>) {
        formParams.forEach { param ->
            if(!checkIfExists(list = this.formParams, key = param.key)) {
                this.formParams = this.formParams + param
            } else {
                this.formParams.filter { it.key == param.key }.forEach { it.value = param.value}
            }
        }
    }

    fun getRequest(): Request {
        var request = Request.Builder()
            .url(url)
            .build()
        var body = MultipartBody.Builder()
        if(formParams.isNotEmpty()) {
            body.setType(MultipartBody.FORM)
            formParams.forEach {
                body.addFormDataPart(it.key, it.value)
            }
        }

        return when(method) {
            Method.GET -> {
                request
            }
            Method.POST -> {
                 request.newBuilder()
                    .post(body.build())
                    .build()
            }
            Method.PUT -> {
                request.newBuilder()
                    .put(body.build())
                    .build()
            }
            Method.PATCH -> {
                request.newBuilder()
                    .patch(body.build())
                    .build()
            }
        }

    }
}