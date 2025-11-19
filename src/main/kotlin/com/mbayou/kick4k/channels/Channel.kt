package com.mbayou.kick4k.channels

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.mbayou.kick4k.categories.Category

data class Channel @JsonCreator constructor(
    @JsonProperty("banner_picture") val bannerPicture: String?,
    @JsonProperty("broadcaster_user_id") val broadcasterUserId: Int?,
    @JsonProperty("category") val category: Category?,
    @JsonProperty("channel_description") val channelDescription: String?,
    @JsonProperty("slug") val slug: String?,
    @JsonProperty("stream") val stream: StreamInformation?,
    @JsonProperty("stream_title") val streamTitle: String?,
)
