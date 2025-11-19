package com.mbayou.kick4k.moderation

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PostModerationBansRequest @JsonCreator constructor(
    @JsonProperty("broadcaster_user_id") val broadcasterUserId: Int,
    @JsonProperty("duration") val duration: Int?,
    @JsonProperty("reason") val reason: String?,
    @JsonProperty("user_id") val userId: Int,
) {
    companion object {
        @JvmStatic
        fun builder(): Builder = Builder()
    }

    class Builder {
        private var broadcasterUserId: Int? = null
        private var duration: Int? = null
        private var reason: String? = null
        private var userId: Int? = null

        fun broadcasterUserId(broadcasterUserId: Int) = apply { this.broadcasterUserId = broadcasterUserId }
        fun duration(duration: Int?) = apply { this.duration = duration }
        fun reason(reason: String?) = apply { this.reason = reason }
        fun userId(userId: Int) = apply { this.userId = userId }

        fun build(): PostModerationBansRequest {
            val broadcasterId = requireNotNull(broadcasterUserId) { "BroadcasterUserId is required" }
            val userIdValue = requireNotNull(userId) { "UserId is required" }
            return PostModerationBansRequest(broadcasterId, duration, reason, userIdValue)
        }
    }
}
