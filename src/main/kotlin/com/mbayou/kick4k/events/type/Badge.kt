package com.mbayou.kick4k.events.type

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Badge @JsonCreator constructor(
    @JsonProperty("text") val text: String?,
    @JsonProperty("type") val type: String?,
    @JsonProperty("count") val count: Int?,
)
