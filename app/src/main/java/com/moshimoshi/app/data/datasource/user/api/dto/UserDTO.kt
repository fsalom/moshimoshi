package com.moshimoshi.app.data.datasource.user.api.dto

import com.google.gson.annotations.SerializedName

data class UserDTO(
    @SerializedName("name") val name: String? = null
)