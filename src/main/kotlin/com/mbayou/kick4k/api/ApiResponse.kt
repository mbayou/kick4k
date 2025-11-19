package com.mbayou.kick4k.api

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ApiResponse<T> @JsonCreator constructor(
    @JsonProperty("data") val data: T?,
    @JsonProperty("message") val message: String?,
) {
    fun isSuccess(): Boolean = data != null
}
