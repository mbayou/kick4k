package pl.teksusik.kick4k

import pl.teksusik.kick4k.authorization.RefreshTokenStore

/**
 * Immutable configuration holder for the Kick4k client.
 */
data class KickConfiguration(
    val clientId: String,
    val clientSecret: String?,
    val redirectUri: String,
    val baseUrl: String = "https://api.kick.com/public/v1",
    val oAuthHost: String = "https://id.kick.com",
    val tokenStore: RefreshTokenStore? = null
) {
    companion object {
        fun builder(): Builder = Builder()
    }

    class Builder internal constructor() {
        private var clientId: String? = null
        private var clientSecret: String? = null
        private var redirectUri: String? = null
        private var baseUrl: String = "https://api.kick.com/public/v1"
        private var oAuthHost: String = "https://id.kick.com"
        private var tokenStore: RefreshTokenStore? = null

        fun clientId(value: String) = apply { this.clientId = value }
        fun clientSecret(value: String?) = apply { this.clientSecret = value }
        fun redirectUri(value: String) = apply { this.redirectUri = value }
        fun baseUrl(value: String) = apply { this.baseUrl = value }
        fun oAuthHost(value: String) = apply { this.oAuthHost = value }
        fun tokenStore(value: RefreshTokenStore?) = apply { this.tokenStore = value }

        fun build(): KickConfiguration {
            val requiredClientId = clientId
            val requiredRedirectUri = redirectUri
            require(!requiredClientId.isNullOrBlank()) { "clientId is required" }
            require(!requiredRedirectUri.isNullOrBlank()) { "redirectUri is required" }
            return KickConfiguration(
                clientId = requiredClientId,
                clientSecret = clientSecret,
                redirectUri = requiredRedirectUri,
                baseUrl = baseUrl,
                oAuthHost = oAuthHost,
                tokenStore = tokenStore
            )
        }
    }
}
