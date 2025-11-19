package com.mbayou.kick4k.moderation

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class DeleteModerationBansRequest @JsonCreator constructor(
    @JsonProperty("broadcaster_user_id") val broadcasterUserId: Int,
    @JsonProperty("user_id") val userId: Int,
) {
    companion object {
        @JvmStatic
        fun builder(): Builder = Builder()
    }

    class Builder {
        private var broadcasterUserId: Int? = null
        private var userId: Int? = null

        fun broadcasterUserId(broadcasterUserId: Int) = apply { this.broadcasterUserId = broadcasterUserId }
        fun userId(userId: Int) = apply { this.userId = userId }

        fun build(): DeleteModerationBansRequest {
            val broadcasterId = requireNotNull(broadcasterUserId) { "BroadcasterUserId is required" }
            val userIdValue = requireNotNull(userId) { "UserId is required" }
            return DeleteModerationBansRequest(broadcasterId, userIdValue)
        }
    }
}
