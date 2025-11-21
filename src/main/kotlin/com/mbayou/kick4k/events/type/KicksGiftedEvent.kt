package com.mbayou.kick4k.events.type

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class KicksGiftedEvent @JsonCreator constructor(
    @JsonProperty("broadcaster") val broadcaster: EventUser,
    @JsonProperty("sender") val sender: EventUser,
    @JsonProperty("gift") val gift: KickGift,
    @JsonProperty("created_at") val createdAt: Instant,
) : KickEvent() {
    companion object {
        @JvmStatic
        fun getEventType(): String = "kicks.gifted"

        @JvmStatic
        fun getEventVersion(): String = "1"
    }

    data class KickGift @JsonCreator constructor(
        @JsonProperty("amount") val amount: Int,
        @JsonProperty("name") val name: String,
        @JsonProperty("type") val type: String,
        @JsonProperty("tier") val tier: String,
        @JsonProperty("message") val message: String,
    )
}
