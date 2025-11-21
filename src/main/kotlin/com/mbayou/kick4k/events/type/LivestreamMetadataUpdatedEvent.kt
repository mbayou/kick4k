package com.mbayou.kick4k.events.type

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class LivestreamMetadataUpdatedEvent @JsonCreator constructor(
    @JsonProperty("broadcaster") val broadcaster: EventUser,
    @JsonProperty("metadata") val metadata: LivestreamMetadata,
) : KickEvent() {
    companion object {
        @JvmStatic
        fun getEventType(): String = "livestream.metadata.updated"

        @JvmStatic
        fun getEventVersion(): String = "1"
    }
}
