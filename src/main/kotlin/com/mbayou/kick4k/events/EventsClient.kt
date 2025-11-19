package com.mbayou.kick4k.events

import com.fasterxml.jackson.databind.ObjectMapper
import com.mbayou.kick4k.KickConfiguration
import com.mbayou.kick4k.api.ApiClient
import com.mbayou.kick4k.authorization.AuthorizationClient
import java.net.http.HttpClient

class EventsClient(
    httpClient: HttpClient,
    mapper: ObjectMapper,
    configuration: KickConfiguration,
    authorization: AuthorizationClient,
) : ApiClient(httpClient, mapper, configuration, authorization) {

    fun getEventsSubscriptions(): List<EventSubscription> {
        return get(configuration.events)
            .send(responseType<List<EventSubscription>>()) ?: emptyList()
    }

    fun postEventsSubscription(request: EventSubscriptionRequest): List<EventSubscriptionResponse> {
        return post(configuration.events)
            .body(request)
            .send(responseType<List<EventSubscriptionResponse>>()) ?: emptyList()
    }

    fun deleteEventsSubscriptions(vararg ids: String) {
        delete(configuration.events)
            .queryParams(mapOf("id" to ids))
            .send(responseType<Unit>())
    }

    fun deleteEventsSubscriptions(ids: List<String>) {
        delete(configuration.events)
            .queryParams(mapOf("id" to ids))
            .send(responseType<Unit>())
    }
}
