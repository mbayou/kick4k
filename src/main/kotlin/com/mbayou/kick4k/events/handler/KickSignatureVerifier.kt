package com.mbayou.kick4k.events.handler

import com.mbayou.kick4k.publicKey.PublicKeyClient
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.PublicKey
import java.security.Signature
import java.security.spec.InvalidKeySpecException
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

class KickSignatureVerifier(private val publicKeyClient: PublicKeyClient) {
    fun isValidSignature(messageId: String, messageTimestamp: String, body: String, signature: String): Boolean {
        return try {
            val pem = publicKeyClient.getPublicKey()
            val key = parsePublicKey(pem)
            val signedData = buildSignedData(messageId, messageTimestamp, body)
            val signatureBytes = decodeBase64(signature)
            verifySignature(key, signedData, signatureBytes)
        } catch (exception: Exception) {
            false
        }
    }

    @Throws(Exception::class)
    private fun verifySignature(publicKey: PublicKey, data: ByteArray, signatureBytes: ByteArray): Boolean {
        val signature = Signature.getInstance("SHA256withRSA")
        signature.initVerify(publicKey)
        signature.update(data)
        return signature.verify(signatureBytes)
    }

    companion object {
        @Throws(InvalidKeySpecException::class, NoSuchAlgorithmException::class)
        private fun parsePublicKey(pem: String): PublicKey {
            val stripped = pem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("\\s".toRegex(), "")
            val keyBytes = Base64.getDecoder().decode(stripped)
            val spec = X509EncodedKeySpec(keyBytes)
            val keyFactory = KeyFactory.getInstance("RSA")
            return keyFactory.generatePublic(spec)
        }

        private fun buildSignedData(messageId: String, timestamp: String, body: String): ByteArray {
            val data = "$messageId.$timestamp.$body"
            return data.toByteArray(StandardCharsets.UTF_8)
        }

        private fun decodeBase64(base64: String): ByteArray = Base64.getDecoder().decode(base64)
    }
}
