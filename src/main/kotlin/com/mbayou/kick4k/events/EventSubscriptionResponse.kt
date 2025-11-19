package com.mbayou.kick4k.events

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class EventSubscriptionResponse @JsonCreator constructor(
    @JsonProperty("error") val error: String?,
    @JsonProperty("name") val name: String?,
    @JsonProperty("subscription_id") val subscriptionId: String?,
    @JsonProperty("version") val version: Int?,
)
