package com.mbayou.kick4k.events.type

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class ChatMessageSentEvent @JsonCreator constructor(
    @JsonProperty("message_id") val messageId: String,
    @JsonProperty("replies_to") val repliesTo: ReplyInfo?,
    @JsonProperty("broadcaster") val broadcaster: EventUser,
    @JsonProperty("sender") val sender: EventUser,
    @JsonProperty("content") val content: String,
    @JsonProperty("emotes") val emotes: List<Emote>?,
    @JsonProperty("created_at") val createdAt: Instant,
) : KickEvent() {
    companion object {
        @JvmStatic
        fun getEventType(): String = "chat.message.sent"

        @JvmStatic
        fun getEventVersion(): String = "1"
    }
}
