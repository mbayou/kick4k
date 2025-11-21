package com.mbayou.kick4k.events.type

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class BanMetadata @JsonCreator constructor(
    @JsonProperty("reason") val reason: String,
    @JsonProperty("created_at") val createdAt: Instant,
    @JsonProperty("expires_at") val expiresAt: Instant?,
)
