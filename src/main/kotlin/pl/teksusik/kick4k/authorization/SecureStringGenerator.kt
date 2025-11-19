package pl.teksusik.kick4k.authorization

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

internal object SecureStringGenerator {
    private val secureRandom = SecureRandom()

    fun nextValue(length: Int = 64): String {
        val bytes = ByteArray(length)
        secureRandom.nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }

    fun sha256(value: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(value.toByteArray())
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash)
    }
}
