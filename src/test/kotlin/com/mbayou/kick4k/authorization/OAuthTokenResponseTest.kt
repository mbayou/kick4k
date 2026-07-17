package com.mbayou.kick4k.authorization

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class OAuthTokenResponseTest {
    private val objectMapper = ObjectMapper()

    @Test
    fun shouldDeserializeRefreshExpiryAndIgnoreUnknownFields() {
        val response = objectMapper.readValue(TOKEN_RESPONSE_WITH_REFRESH_EXPIRY, OAuthTokenResponse::class.java)

        assertEquals("access-token", response.accessToken)
        assertEquals("Bearer", response.tokenType)
        assertEquals("refresh-token", response.refreshToken)
        assertEquals(3600, response.expiresIn)
        assertEquals("user:read channel:read", response.scope)
        assertEquals(15_552_000, response.refreshExpiresIn)
    }

    @Test
    fun shouldRemainCompatibleWithResponsesWithoutRefreshExpiry() {
        val response = objectMapper.readValue(LEGACY_TOKEN_RESPONSE, OAuthTokenResponse::class.java)

        assertNull(response.refreshExpiresIn)
    }

    companion object {
        private const val TOKEN_RESPONSE_WITH_REFRESH_EXPIRY = """
            {
              "access_token": "access-token",
              "token_type": "Bearer",
              "refresh_token": "refresh-token",
              "expires_in": 3600,
              "scope": "user:read channel:read",
              "refresh_expires_in": 15552000,
              "future_token_field": "ignored"
            }
        """

        private const val LEGACY_TOKEN_RESPONSE = """
            {
              "access_token": "access-token",
              "token_type": "Bearer",
              "refresh_token": "refresh-token",
              "expires_in": 3600,
              "scope": "user:read channel:read"
            }
        """
    }
}
