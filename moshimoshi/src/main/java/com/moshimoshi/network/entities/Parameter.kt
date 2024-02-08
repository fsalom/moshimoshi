package com.moshimoshi.network.entities

import java.io.Serializable

data class Parameter(
    var key: String = "",
    var value: String = "",
): Serializable