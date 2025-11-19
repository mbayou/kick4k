package com.mbayou.kick4k.channels

import com.fasterxml.jackson.databind.ObjectMapper
import com.mbayou.kick4k.KickConfiguration
import com.mbayou.kick4k.api.ApiClient
import com.mbayou.kick4k.authorization.AuthorizationClient
import java.net.http.HttpClient

class ChannelsClient(
    httpClient: HttpClient,
    mapper: ObjectMapper,
    configuration: KickConfiguration,
    authorization: AuthorizationClient,
) : ApiClient(httpClient, mapper, configuration, authorization) {

    fun getCurrentChannel(): Channel = getChannels().first()

    fun getChannels(): List<Channel> {
        return get(configuration.channels)
            .send(responseType<List<Channel>>()) ?: emptyList()
    }

    fun getChannels(vararg broadcasterUserIds: Int): List<Channel> {
        return get(configuration.channels)
            .queryParams(mapOf("broadcaster_user_id" to broadcasterUserIds))
            .send(responseType<List<Channel>>()) ?: emptyList()
    }

    fun getChannelsByUserIds(broadcasterUserIds: List<Int>): List<Channel> {
        return get(configuration.channels)
            .queryParams(mapOf("broadcaster_user_id" to broadcasterUserIds))
            .send(responseType<List<Channel>>()) ?: emptyList()
    }

    fun getChannel(broadcasterUserId: Int): Channel = getChannels(broadcasterUserId).first()

    fun getChannelsBySlugs(slugs: List<String>): List<Channel> {
        return get(configuration.channels)
            .queryParams(mapOf("slug" to slugs))
            .send(responseType<List<Channel>>()) ?: emptyList()
    }

    fun getChannels(vararg slugs: String): List<Channel> {
        return get(configuration.channels)
            .queryParams(mapOf("slug" to slugs))
            .send(responseType<List<Channel>>()) ?: emptyList()
    }

    fun getChannel(slug: String): Channel = getChannels(slug).first()

    fun updateChannel(request: UpdateChannelRequest) {
        patch(configuration.channels)
            .body(request)
            .send(responseType<Unit>())
    }
}
