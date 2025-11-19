package com.mbayou.kick4k.users

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class TokenIntrospect @JsonCreator constructor(
    @JsonProperty("active") val active: Boolean?,
    @JsonProperty("client_id") val clientId: String?,
    @JsonProperty("exp") val exp: Int?,
    @JsonProperty("scope") val scope: String?,
    @JsonProperty("token_type") val tokenType: String?,
)
