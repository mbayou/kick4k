package com.mbayou.kick4k.events.type

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ModerationBannedEvent @JsonCreator constructor(
    @JsonProperty("broadcaster") val broadcaster: EventUser?,
    @JsonProperty("moderator") val moderator: EventUser?,
    @JsonProperty("banned_user") val bannedUser: EventUser?,
    @JsonProperty("metadata") val metadata: BanMetadata?,
) : KickEvent() {
    companion object {
        @JvmStatic
        fun getEventType(): String = "moderation.banned"

        @JvmStatic
        fun getEventVersion(): String = "1"
    }
}
