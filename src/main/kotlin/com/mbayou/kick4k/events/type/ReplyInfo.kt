package com.mbayou.kick4k.events.type

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ReplyInfo @JsonCreator constructor(
    @JsonProperty("message_id") val messageId: String?,
    @JsonProperty("content") val content: String?,
    @JsonProperty("sender") val sender: EventUser?,
)
