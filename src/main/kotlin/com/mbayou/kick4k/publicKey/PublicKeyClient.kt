package com.mbayou.kick4k.publicKey

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.mbayou.kick4k.KickConfiguration
import com.mbayou.kick4k.api.ApiClient
import com.mbayou.kick4k.authorization.AuthorizationClient
import java.net.http.HttpClient

class PublicKeyClient(
    httpClient: HttpClient,
    mapper: ObjectMapper,
    configuration: KickConfiguration,
    authorization: AuthorizationClient,
) : ApiClient(httpClient, mapper, configuration, authorization) {

    fun getPublicKey(): String {
        return get(configuration.publicKey)
            .send(responseType<ApiPublicKey>())
            ?.publicKey ?: throw IllegalStateException("No public key returned")
    }

    private data class ApiPublicKey @JsonCreator constructor(
        @JsonProperty("public_key") val publicKey: String,
    )
}
