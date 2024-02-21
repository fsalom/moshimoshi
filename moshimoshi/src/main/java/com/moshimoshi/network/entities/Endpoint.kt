package com.moshimoshi.network.entities

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.Serializable

enum class Method {
    GET, POST, PUT, PATCH
}

enum class BodyType {
    JSON, MULTIPART, NONE
}

data class Endpoint(
    var url: String = "",
    var method: Method = Method.GET,
    var bodyType: BodyType = BodyType.NONE,
    private var headers: List<Parameter> = emptyList(),
    private var queryParams: List<Parameter> = emptyList(),
    private var parameters: List<Parameter> = emptyList(),
    private var formParams: List<Parameter> = emptyList(),
    private var jsonParams: List<Parameter> = emptyList(),
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

        var body: RequestBody = MultipartBody.Builder().build()

        if(formParams.isNotEmpty() || bodyType == BodyType.MULTIPART) {
            var bodyWithMultipart = MultipartBody.Builder()
            bodyWithMultipart.setType(MultipartBody.FORM)
            formParams.forEach {
                bodyWithMultipart.addFormDataPart(it.key, it.value)
            }
            parameters.forEach {
                bodyWithMultipart.addFormDataPart(it.key, it.value)
            }
            body = bodyWithMultipart.build()
        }

        if(jsonParams.isNotEmpty() || bodyType == BodyType.JSON) {
            var jsonObject = JSONObject()
            jsonParams.forEach {
                jsonObject.put(it.key, it.value)
            }
            parameters.forEach {
                jsonObject.put(it.key, it.value)
            }
            body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonObject.toString()
            )

        }

        if(headers.isNotEmpty()) {
            headers.forEach {
                request.addHeader(it.key, it.value)
            }
        }

        when(method) {
            Method.GET -> {
                request
            }
            Method.POST -> {
                request
                    .post(body)
            }
            Method.PUT -> {
                request
                    .put(body)
            }
            Method.PATCH -> {
                request
                    .patch(body)
            }
        }
        return request.build()
    }
}