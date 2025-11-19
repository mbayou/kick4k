package pl.teksusik.kick4k.authorization

import java.time.Duration

data class OAuthTokenResponse(
    val accessToken: String,
    val refreshToken: String?,
    val expiresIn: Duration,
    val scope: List<Scope>
)
