package com.mbayou.kick4k.publicKey

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.mbayou.kick4k.KickConfiguration
import com.mbayou.kick4k.api.ApiClient
import java.net.http.HttpClient

class PublicKeyClient(
    httpClient: HttpClient,
    mapper: ObjectMapper,
    configuration: KickConfiguration,
): ApiClient(httpClient, mapper, configuration) {

    fun getPublicKey(accessToken: String? = null): String {
        val request = get(configuration.publicKey)
        if (accessToken == null) {
            request.withoutAccessToken()
        } else {
            request.withAccessToken(accessToken)
        }
        return request
            .send(responseType<ApiPublicKey>())
            ?.publicKey ?: throw IllegalStateException("No public key returned")
    }

    private data class ApiPublicKey @JsonCreator constructor(
        @JsonProperty("public_key") val publicKey: String,
    )
}
