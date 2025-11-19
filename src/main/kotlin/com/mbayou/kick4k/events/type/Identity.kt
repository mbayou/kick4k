package com.mbayou.kick4k.events.type

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Identity @JsonCreator constructor(
    @JsonProperty("username_color") val usernameColor: String?,
    @JsonProperty("badges") val badges: List<Badge>?,
)
