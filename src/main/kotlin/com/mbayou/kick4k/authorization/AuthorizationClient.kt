package com.mbayou.kick4k.authorization

import com.fasterxml.jackson.databind.ObjectMapper
import com.mbayou.kick4k.KickConfiguration
import java.io.IOException
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.time.Instant
import java.util.Base64
import java.util.StringJoiner
import java.util.UUID

class AuthorizationClient(
    private val httpClient: HttpClient,
    private val mapper: ObjectMapper,
    private val configuration: KickConfiguration,
    private val refreshToken: RefreshTokenStore,
) {
    private var accessTokenValue: String? = null
    private var expiresAt: Instant = Instant.EPOCH

    fun getAuthorizationUrl(scopeList: List<Scope>, codeChallenge: String): String {
        val joiner = StringJoiner("&", "${configuration.oAuthHost}${configuration.authorizationEndpoint}?", "")
        joiner.add("client_id=${encodeUrl(configuration.clientId)}")
        joiner.add("response_type=code")
        joiner.add("redirect_uri=${encodeUrl(configuration.redirectUri)}")
        joiner.add("state=${encodeUrl(UUID.randomUUID().toString())}")
        val scopes = scopeList.joinToString(" ") { it.scope }
        joiner.add("scope=${encodeUrl(scopes)}")
        joiner.add("code_challenge=${encodeUrl(codeChallenge)}")
        joiner.add("code_challenge_method=S256")
        return joiner.toString()
    }

    fun exchangeCodeForToken(code: String, codeVerifier: String): OAuthTokenResponse {
        val body = mapOf(
            "code" to code,
            "client_id" to configuration.clientId,
            "client_secret" to configuration.clientSecret,
            "redirect_uri" to configuration.redirectUri,
            "grant_type" to "authorization_code",
            "code_verifier" to codeVerifier,
        )
        return postOAuthTokenRequest(body)
    }

    fun refreshAccessToken(): OAuthTokenResponse {
        val body = mapOf(
            "refresh_token" to refreshToken.getRefreshToken(),
            "client_id" to configuration.clientId,
            "client_secret" to configuration.clientSecret,
            "grant_type" to "refresh_token",
        )

        val newToken = postOAuthTokenRequest(body)
        refreshToken.notifyRefreshTokenRoll(newToken.refreshToken)
        return newToken
    }

    fun getAccessToken(): String {
        val current = accessTokenValue
        val now = Instant.now()
        if (current == null || now.isAfter(expiresAt.minusSeconds(10))) {
            val response = refreshAccessToken()
            setTokens(response)
            return response.accessToken
        }
        return current
    }

    fun setTokens(oAuthToken: OAuthTokenResponse) {
        accessTokenValue = oAuthToken.accessToken
        refreshToken.notifyRefreshTokenRoll(oAuthToken.refreshToken)
        expiresAt = Instant.now().plusSeconds(oAuthToken.expiresIn.toLong())
    }

    fun setAccessToken(accessToken: String) {
        this.accessTokenValue = accessToken
    }

    fun setExpiresAt(expiresAt: Instant) {
        this.expiresAt = expiresAt
    }

    private fun postOAuthTokenRequest(body: Map<String, String>): OAuthTokenResponse {
        val request = HttpRequest.newBuilder()
            .uri(URI.create(configuration.oAuthHost + configuration.tokenEndpoint))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(generateUrlencodedForm(body)))
            .build()

        val response = try {
            httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        } catch (exception: InterruptedException) {
            Thread.currentThread().interrupt()
            throw RuntimeException("Failed to create OAuth token", exception)
        } catch (exception: IOException) {
            throw RuntimeException("Failed to create OAuth token", exception)
        }

        if (response.statusCode() != 200) {
            throw OAuthTokenException(response.statusCode(), response.body())
        }

        return mapper.readValue(response.body(), OAuthTokenResponse::class.java)
    }

    companion object {
        fun generateCodeVerifier(): String {
            val code = ByteArray(32)
            SecureRandom().nextBytes(code)
            return base64UrlEncoder(code)
        }

        fun generateCodeChallenge(codeVerifier: String): String {
            return try {
                val digest = MessageDigest.getInstance("SHA-256")
                val hash = digest.digest(codeVerifier.toByteArray(StandardCharsets.US_ASCII))
                base64UrlEncoder(hash)
            } catch (exception: NoSuchAlgorithmException) {
                throw RuntimeException("Failed to generate code challenge", exception)
            }
        }
    }
}

private fun generateUrlencodedForm(data: Map<String, String>): String {
    val formData = StringJoiner("&")
    data.forEach { (key, value) -> formData.add("${encodeUrl(key)}=${encodeUrl(value)}") }
    return formData.toString()
}

private fun encodeUrl(value: String): String = URLEncoder.encode(value, StandardCharsets.UTF_8)

private fun base64UrlEncoder(input: ByteArray): String =
    Base64.getUrlEncoder().withoutPadding().encodeToString(input)
