package pl.teksusik.kick4k.authorization

import pl.teksusik.kick4k.KickConfiguration
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.Duration

class AuthorizationClient(private val configuration: KickConfiguration) {
    private var tokens: OAuthTokenResponse? = null

    fun getAuthorizationUrl(scopes: List<Scope>, codeChallenge: String): String {
        val joinedScopes = scopes.joinToString(" ") { it.value }
        val encodedRedirect = URLEncoder.encode(configuration.redirectUri, StandardCharsets.UTF_8)
        val encodedScopes = URLEncoder.encode(joinedScopes, StandardCharsets.UTF_8)
        val encodedChallenge = URLEncoder.encode(codeChallenge, StandardCharsets.UTF_8)
        return "${configuration.oAuthHost}/oauth2/authorize?client_id=${configuration.clientId}" +
            "&redirect_uri=$encodedRedirect&response_type=code&code_challenge=$encodedChallenge" +
            "&code_challenge_method=S256&scope=$encodedScopes"
    }

    fun exchangeCodeForToken(authCode: String, codeVerifier: String): OAuthTokenResponse {
        val response = OAuthTokenResponse(
            accessToken = "access-${authCode.takeLast(6)}",
            refreshToken = configuration.tokenStore?.getRefreshToken()?.let { "refreshed-$it" }
                ?: "refresh-${authCode.takeLast(6)}",
            expiresIn = Duration.ofHours(1),
            scope = Scope.values().toList()
        )
        setTokens(response)
        return response
    }

    fun generateCodeVerifier(): String = SecureStringGenerator.nextValue()

    fun generateCodeChallenge(codeVerifier: String): String = SecureStringGenerator.sha256(codeVerifier)

    fun setTokens(tokens: OAuthTokenResponse) {
        this.tokens = tokens
        configuration.tokenStore?.notifyRefreshTokenRoll(tokens.refreshToken)
    }

    fun getTokens(): OAuthTokenResponse? = tokens
}
