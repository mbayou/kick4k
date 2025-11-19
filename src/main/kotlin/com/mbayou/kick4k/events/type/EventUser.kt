package com.mbayou.kick4k.events.type

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class EventUser @JsonCreator constructor(
    @JsonProperty("is_anonymous") val isAnonymous: Boolean?,
    @JsonProperty("user_id") val userId: Int?,
    @JsonProperty("username") val username: String?,
    @JsonProperty("is_verified") val isVerified: Boolean?,
    @JsonProperty("profile_picture") val profilePicture: String?,
    @JsonProperty("channel_slug") val channelSlug: String?,
    @JsonProperty("identity") val identity: Identity?,
)
