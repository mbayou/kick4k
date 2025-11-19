package com.mbayou.kick4k.events.type

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class ChannelSubscriptionRenewalEvent @JsonCreator constructor(
    @JsonProperty("broadcaster") val broadcaster: EventUser?,
    @JsonProperty("subscriber") val subscriber: EventUser?,
    @JsonProperty("duration") val duration: Int?,
    @JsonProperty("created_at") val createdAt: Instant?,
    @JsonProperty("expires_at") val expiresAt: Instant?,
) : KickEvent() {
    companion object {
        @JvmStatic
        fun getEventType(): String = "channel.subscription.renewal"

        @JvmStatic
        fun getEventVersion(): String = "1"
    }
}
