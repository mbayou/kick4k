package com.mbayou.kick4k.events.type

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class ChannelSubscriptionGiftsEvent @JsonCreator constructor(
    @JsonProperty("broadcaster") val broadcaster: EventUser?,
    @JsonProperty("subscriber") val gifter: EventUser?,
    @JsonProperty("giftees") val giftees: List<EventUser>?,
    @JsonProperty("created_at") val createdAt: Instant?,
    @JsonProperty("expires_at") val expiresAt: Instant?,
) : KickEvent() {
    companion object {
        @JvmStatic
        fun getEventType(): String = "channel.subscription.gifts"

        @JvmStatic
        fun getEventVersion(): String = "1"
    }
}
