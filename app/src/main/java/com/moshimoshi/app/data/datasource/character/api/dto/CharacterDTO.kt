package com.moshimoshi.app.data.datasource.character.api.dto

import com.google.gson.annotations.SerializedName

data class CharacterDTO(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("species") val species: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("image") val image: String? = null,
    @SerializedName("episode") val episodes: List<String>? = null
)


