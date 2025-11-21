package com.mbayou.kick4k.channels

import com.fasterxml.jackson.databind.ObjectMapper
import com.mbayou.kick4k.KickConfiguration
import com.mbayou.kick4k.api.ApiClient
import java.net.http.HttpClient

class ChannelsClient(
    httpClient: HttpClient,
    mapper: ObjectMapper,
    configuration: KickConfiguration,
): ApiClient(httpClient, mapper, configuration) {

    fun getCurrentChannel(accessToken: String): Channel = getChannels(accessToken).first()

    fun getChannels(accessToken: String): List<Channel> {
        return get(configuration.channels)
            .withAccessToken(accessToken)
            .send(responseType<List<Channel>>()) ?: emptyList()
    }

    fun getChannels(accessToken: String, vararg broadcasterUserIds: Int): List<Channel> {
        return get(configuration.channels)
            .queryParams(mapOf("broadcaster_user_id" to broadcasterUserIds))
            .withAccessToken(accessToken)
            .send(responseType<List<Channel>>()) ?: emptyList()
    }

    fun getChannelsByUserIds(accessToken: String, broadcasterUserIds: List<Int>): List<Channel> {
        return get(configuration.channels)
            .queryParams(mapOf("broadcaster_user_id" to broadcasterUserIds))
            .withAccessToken(accessToken)
            .send(responseType<List<Channel>>()) ?: emptyList()
    }

    fun getChannel(accessToken: String, broadcasterUserId: Int): Channel =
        getChannels(accessToken, broadcasterUserId).first()

    fun getChannelsBySlugs(accessToken: String, slugs: List<String>): List<Channel> {
        return get(configuration.channels)
            .queryParams(mapOf("slug" to slugs))
            .withAccessToken(accessToken)
            .send(responseType<List<Channel>>()) ?: emptyList()
    }

    fun getChannels(accessToken: String, vararg slugs: String): List<Channel> {
        return get(configuration.channels)
            .queryParams(mapOf("slug" to slugs))
            .withAccessToken(accessToken)
            .send(responseType<List<Channel>>()) ?: emptyList()
    }

    fun getChannel(accessToken: String, slug: String): Channel = getChannels(accessToken, slug).first()

    fun updateChannel(accessToken: String, request: UpdateChannelRequest) {
        patch(configuration.channels)
            .body(request)
            .withAccessToken(accessToken)
            .send(responseType<Unit>())
    }
}
