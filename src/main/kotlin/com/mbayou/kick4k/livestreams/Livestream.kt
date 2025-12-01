package com.mbayou.kick4k.livestreams

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.mbayou.kick4k.categories.Category

@JsonIgnoreProperties(ignoreUnknown = true)
data class Livestream @JsonCreator constructor(
    @JsonProperty("broadcaster_user_id") val broadcasterUserId: Int?,
    @JsonProperty("category") val category: Category?,
    @JsonProperty("custom_tags") val customTags: List<String>?,
    @JsonProperty("channel_id") val channelId: Int?,
    @JsonProperty("has_mature_content") val hasMatureContent: Boolean?,
    @JsonProperty("language") val language: String?,
    @JsonProperty("slug") val slug: String?,
    @JsonProperty("started_at") val startedAt: String?,
    @JsonProperty("stream_title") val streamTitle: String?,
    @JsonProperty("thumbnail") val thumbnail: String?,
    @JsonProperty("viewer_count") val viewerCount: Int?,
)
