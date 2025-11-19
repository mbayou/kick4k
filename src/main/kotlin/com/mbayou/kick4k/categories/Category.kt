package com.mbayou.kick4k.categories

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Category @JsonCreator constructor(
    @JsonProperty("id") val id: Int?,
    @JsonProperty("name") val name: String?,
    @JsonProperty("thumbnail") val thumbnail: String?,
)
