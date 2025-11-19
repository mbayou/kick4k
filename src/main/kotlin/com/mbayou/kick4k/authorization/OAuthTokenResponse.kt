package com.mbayou.kick4k.authorization

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class OAuthTokenResponse @JsonCreator constructor(
    @JsonProperty("access_token") val accessToken: String,
    @JsonProperty("token_type") val tokenType: String,
    @JsonProperty("refresh_token") val refreshToken: String,
    @JsonProperty("expires_in") val expiresIn: Int,
    @JsonProperty("scope") val scope: String,
)
