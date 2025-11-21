package com.mbayou.kick4k.events

import com.fasterxml.jackson.databind.ObjectMapper
import com.mbayou.kick4k.KickConfiguration
import com.mbayou.kick4k.api.ApiClient
import java.net.http.HttpClient

class EventsClient(
    httpClient: HttpClient,
    mapper: ObjectMapper,
    configuration: KickConfiguration,
): ApiClient(httpClient, mapper, configuration) {

    fun getEventsSubscriptions(accessToken: String): List<EventSubscription> {
        return get(configuration.events)
            .withAccessToken(accessToken)
            .send(responseType<List<EventSubscription>>()) ?: emptyList()
    }

    fun postEventsSubscription(accessToken: String, request: EventSubscriptionRequest): List<EventSubscriptionResponse> {
        return post(configuration.events)
            .body(request)
            .withAccessToken(accessToken)
            .send(responseType<List<EventSubscriptionResponse>>()) ?: emptyList()
    }

    fun deleteEventsSubscriptions(accessToken: String, vararg ids: String) {
        delete(configuration.events)
            .queryParams(mapOf("id" to ids))
            .withAccessToken(accessToken)
            .send(responseType<Unit>())
    }

    fun deleteEventsSubscriptions(accessToken: String, ids: List<String>) {
        delete(configuration.events)
            .queryParams(mapOf("id" to ids))
            .withAccessToken(accessToken)
            .send(responseType<Unit>())
    }
}
