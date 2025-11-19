package com.mbayou.kick4k.channels

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class StreamInformation @JsonCreator constructor(
    @JsonProperty("is_live") val isLive: Boolean?,
    @JsonProperty("is_mature") val isMature: Boolean?,
    @JsonProperty("key") val key: String?,
    @JsonProperty("language") val language: String?,
    @JsonProperty("start_time") val startTime: String?,
    @JsonProperty("thumbnail") val thumbnail: String?,
    @JsonProperty("url") val url: String?,
    @JsonProperty("viewer_count") val viewerCount: Int?,
)
