package com.mbayou.kick4k.chat

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonValue

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PostChatMessageRequest(
    val broadcasterUserId: Int?,
    val content: String,
    val replyToMessageId: String?,
    val type: Type,
) {
    companion object {
        @JvmStatic
        fun builder(): Builder = Builder()
    }

    class Builder {
        private var broadcasterUserId: Int? = null
        private var content: String? = null
        private var replyToMessageId: String? = null
        private var type: Type? = null

        fun broadcastUserId(broadcastUserId: Int?) = apply { this.broadcasterUserId = broadcastUserId }
        fun content(content: String) = apply { this.content = content }
        fun replyToMessageId(replyToMessageId: String?) = apply { this.replyToMessageId = replyToMessageId }
        fun type(type: Type) = apply { this.type = type }

        fun build(): PostChatMessageRequest {
            val contentValue = requireNotNull(content) { "Content is required" }
            val typeValue = requireNotNull(type) { "Type is required" }
            if (typeValue == Type.USER && broadcasterUserId == null) {
                throw IllegalStateException("BroadcastUserId is required when sending as user")
            }
            return PostChatMessageRequest(broadcasterUserId, contentValue, replyToMessageId, typeValue)
        }
    }

    enum class Type {
        USER,
        BOT;

        @JsonValue
        fun toLower(): String = name.lowercase()
    }
}
