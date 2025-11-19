package com.mbayou.kick4k.events

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class EventSubscription @JsonCreator constructor(
    @JsonProperty("app_id") val aapId: String?,
    @JsonProperty("broadcaster_user_id") val broadcasterUserId: Int?,
    @JsonProperty("created_at") val createdAt: String?,
    @JsonProperty("event") val event: String?,
    @JsonProperty("id") val id: String?,
    @JsonProperty("method") val method: String?,
    @JsonProperty("updated_at") val updatedAt: String?,
    @JsonProperty("version") val version: Int?,
)
