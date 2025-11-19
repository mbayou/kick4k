package pl.teksusik.kick4k

import pl.teksusik.kick4k.authorization.Scope
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class KickClientTest {
    @Test
    fun `should build configuration and exchange tokens`() {
        val config = KickConfiguration.builder()
            .clientId("client")
            .clientSecret("secret")
            .redirectUri("http://localhost")
            .build()

        val client = KickClient(config)
        val verifier = client.authorization().generateCodeVerifier()
        val challenge = client.authorization().generateCodeChallenge(verifier)

        val url = client.authorization().getAuthorizationUrl(listOf(Scope.USER_READ), challenge)
        assert(url.contains("client_id=client"))

        val tokens = client.authorization().exchangeCodeForToken("code", verifier)
        assertNotNull(tokens.accessToken)
        assertEquals(listOf(Scope.USER_READ, Scope.CHANNEL_READ, Scope.CHAT_WRITE, Scope.CHANNEL_WRITE, Scope.LIVESTREAM_READ, Scope.LIVESTREAM_WRITE).size, tokens.scope.size)
    }
}
