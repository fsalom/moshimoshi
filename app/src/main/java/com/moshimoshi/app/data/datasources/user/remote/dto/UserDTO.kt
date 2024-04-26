package com.moshimoshi.app.data.datasources.user.remote.dto

import com.google.gson.annotations.SerializedName

data class UserDTO(
    @SerializedName("name") val name: String? = null
)