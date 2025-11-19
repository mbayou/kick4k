package com.mbayou.kick4k.users

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class User @JsonCreator constructor(
    @JsonProperty("email") val email: String?,
    @JsonProperty("name") val name: String?,
    @JsonProperty("profile_picture") val profilePicture: String?,
    @JsonProperty("user_id") val userId: Int?,
)
