package com.mbayou.kick4k

import com.mbayou.kick4k.authorization.RefreshTokenStore

data class KickConfiguration(
    val tokenStore: RefreshTokenStore,
    val clientId: String,
    val clientSecret: String,
    val redirectUri: String,
    val oAuthHost: String,
    val authorizationEndpoint: String,
    val tokenEndpoint: String,
    val baseUrl: String,
    val categories: String,
    val categoriesId: String,
    val tokenIntrospect: String,
    val users: String,
    val channels: String,
    val chat: String,
    val moderation: String,
    val livestreams: String,
    val livestreamsStats: String,
    val publicKey: String,
    val events: String,
) {
    companion object {
        @JvmStatic
        fun builder(): Builder = Builder()
    }

    class Builder {
        private var tokenStore: RefreshTokenStore? = null
        private var clientId: String? = null
        private var clientSecret: String? = null
        private var redirectUri: String? = null
        private var oAuthHost: String = "https://id.kick.com"
        private var authorizationEndpoint: String = "/oauth/authorize"
        private var tokenEndpoint: String = "/oauth/token"
        private var baseUrl: String = "https://api.kick.com/public/v1"
        private var categories: String = "/categories"
        private var categoriesId: String = "/categories/{id}"
        private var tokenIntrospect: String = "/token/introspect"
        private var users: String = "/users"
        private var channels: String = "/channels"
        private var chat: String = "/chat"
        private var moderation: String = "/moderation/bans"
        private var livestreams: String = "/livestreams"
        private var livestreamsStats: String = "/livestreams/stats"
        private var publicKey: String = "/public-key"
        private var events: String = "/events/subscriptions"

        fun tokenStore(tokenStore: RefreshTokenStore) = apply { this.tokenStore = tokenStore }
        fun clientId(clientId: String) = apply { this.clientId = clientId }
        fun clientSecret(clientSecret: String) = apply { this.clientSecret = clientSecret }
        fun redirectUri(redirectUri: String) = apply { this.redirectUri = redirectUri }
        fun oAuthHost(oAuthHost: String) = apply { this.oAuthHost = oAuthHost }
        fun authorizationEndpoint(authorizationEndpoint: String) = apply { this.authorizationEndpoint = authorizationEndpoint }
        fun tokenEndpoint(tokenEndpoint: String) = apply { this.tokenEndpoint = tokenEndpoint }
        fun baseUrl(baseUrl: String) = apply { this.baseUrl = baseUrl }
        fun categories(categories: String) = apply { this.categories = categories }
        fun categoriesId(categoriesId: String) = apply { this.categoriesId = categoriesId }
        fun tokenIntrospect(tokenIntrospect: String) = apply { this.tokenIntrospect = tokenIntrospect }
        fun users(users: String) = apply { this.users = users }
        fun channels(channels: String) = apply { this.channels = channels }
        fun chat(chat: String) = apply { this.chat = chat }
        fun moderation(moderation: String) = apply { this.moderation = moderation }
        fun livestreams(livestreams: String) = apply { this.livestreams = livestreams }
        fun livestreamsStats(livestreamsStats: String) = apply { this.livestreamsStats = livestreamsStats }
        fun publicKey(publicKey: String) = apply { this.publicKey = publicKey }
        fun events(events: String) = apply { this.events = events }

        fun build(): KickConfiguration {
            val tokenStore = requireNotNull(this.tokenStore) { "TokenStore is required" }
            val clientId = requireNotNull(this.clientId) { "ClientId is required" }
            val clientSecret = requireNotNull(this.clientSecret) { "ClientSecret is required" }
            val redirectUri = requireNotNull(this.redirectUri) { "RedirectUri is required" }

            return KickConfiguration(
                tokenStore = tokenStore,
                clientId = clientId,
                clientSecret = clientSecret,
                redirectUri = redirectUri,
                oAuthHost = oAuthHost,
                authorizationEndpoint = authorizationEndpoint,
                tokenEndpoint = tokenEndpoint,
                baseUrl = baseUrl,
                categories = categories,
                categoriesId = categoriesId,
                tokenIntrospect = tokenIntrospect,
                users = users,
                channels = channels,
                chat = chat,
                moderation = moderation,
                livestreams = livestreams,
                livestreamsStats = livestreamsStats,
                publicKey = publicKey,
                events = events,
            )
        }
    }
}
