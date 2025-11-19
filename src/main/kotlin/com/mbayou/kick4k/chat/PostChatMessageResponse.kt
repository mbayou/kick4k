package com.mbayou.kick4k.chat

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class PostChatMessageResponse @JsonCreator constructor(
    @JsonProperty("is_sent") val isSent: Boolean?,
    @JsonProperty("message_id") val messageId: String?,
)
