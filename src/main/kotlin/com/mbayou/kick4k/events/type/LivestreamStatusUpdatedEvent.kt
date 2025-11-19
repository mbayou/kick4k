package com.mbayou.kick4k.events.type

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class LivestreamStatusUpdatedEvent @JsonCreator constructor(
    @JsonProperty("broadcaster") val broadcaster: EventUser?,
    @JsonProperty("is_live") val isLive: Boolean?,
    @JsonProperty("title") val title: String?,
    @JsonProperty("started_at") val startedAt: Instant?,
    @JsonProperty("ended_at") val endedAt: Instant?,
) : KickEvent() {
    companion object {
        @JvmStatic
        fun getEventType(): String = "livestream.status.updated"

        @JvmStatic
        fun getEventVersion(): String = "1"
    }
}
