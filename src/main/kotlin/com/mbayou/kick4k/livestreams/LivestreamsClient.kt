package com.mbayou.kick4k.livestreams

import com.fasterxml.jackson.databind.ObjectMapper
import com.mbayou.kick4k.KickConfiguration
import com.mbayou.kick4k.api.ApiClient
import com.mbayou.kick4k.authorization.AuthorizationClient
import java.net.http.HttpClient

class LivestreamsClient(
    httpClient: HttpClient,
    mapper: ObjectMapper,
    configuration: KickConfiguration,
    authorization: AuthorizationClient,
) : ApiClient(httpClient, mapper, configuration, authorization) {

    fun getLivestream(broadcasterUserId: Int): Livestream? {
        return getLivestreams(GetLivestreamsRequest.builder().broadcasterUserId(broadcasterUserId).build()).firstOrNull()
    }

    fun getLivestreams(request: GetLivestreamsRequest): List<Livestream> {
        return get(configuration.livestreams)
            .queryParams(request)
            .send(responseType<List<Livestream>>()) ?: emptyList()
    }

    fun getLivestreamsStats(): LivestreamsStats? {
        return get(configuration.livestreamsStats)
            .send(responseType<LivestreamsStats>())
    }
}
