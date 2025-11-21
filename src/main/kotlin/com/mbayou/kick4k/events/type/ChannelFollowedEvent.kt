package com.mbayou.kick4k.events.type

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ChannelFollowedEvent @JsonCreator constructor(
    @JsonProperty("broadcaster") val broadcaster: EventUser,
    @JsonProperty("follower") val follower: EventUser,
) : KickEvent() {
    companion object {
        @JvmStatic
        fun getEventType(): String = "channel.followed"

        @JvmStatic
        fun getEventVersion(): String = "1"
    }
}
