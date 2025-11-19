package com.mbayou.kick4k.events.type

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Emote @JsonCreator constructor(
    @JsonProperty("emote_id") val emoteId: String?,
    @JsonProperty("positions") val positions: List<Position>?,
) {
    data class Position @JsonCreator constructor(
        @JsonProperty("s") val start: String?,
        @JsonProperty("e") val end: String?,
    )
}
