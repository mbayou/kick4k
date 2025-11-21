package com.mbayou.kick4k.livestreams

import com.fasterxml.jackson.databind.ObjectMapper
import com.mbayou.kick4k.KickConfiguration
import com.mbayou.kick4k.api.ApiClient
import java.net.http.HttpClient

class LivestreamsClient(
    httpClient: HttpClient,
    mapper: ObjectMapper,
    configuration: KickConfiguration,
): ApiClient(httpClient, mapper, configuration) {

    fun getLivestream(accessToken: String, broadcasterUserId: Int): Livestream? {
        return getLivestreams(accessToken, GetLivestreamsRequest.builder().broadcasterUserId(broadcasterUserId).build()).firstOrNull()
    }

    fun getLivestreams(accessToken: String, request: GetLivestreamsRequest): List<Livestream> {
        return get(configuration.livestreams)
            .queryParams(request)
            .withAccessToken(accessToken)
            .send(responseType<List<Livestream>>()) ?: emptyList()
    }

    fun getLivestreamsStats(accessToken: String): LivestreamsStats? {
        return get(configuration.livestreamsStats)
            .withAccessToken(accessToken)
            .send(responseType<LivestreamsStats>())
    }
}
