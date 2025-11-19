package com.mbayou.kick4k.events

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EventSubscriptionRequest @JsonCreator constructor(
    @JsonProperty("broadcast_user_id") val broadcasterUserId: Int?,
    @JsonProperty("events") val events: List<Event>,
    @JsonProperty("method") val method: Method?,
) {
    companion object {
        @JvmStatic
        fun builder(): Builder = Builder()
    }

    data class Event(
        @JsonProperty("name") val name: String,
        @JsonProperty("version") val version: Int,
    )

    enum class Method {
        WEBHOOK;

        @JsonValue
        fun toLower(): String = name.lowercase()
    }

    class Builder {
        private var broadcasterUserId: Int? = null
        private val events: MutableList<Event> = mutableListOf()
        private var method: Method? = null

        fun broadcasterUserId(broadcasterUserId: Int?) = apply { this.broadcasterUserId = broadcasterUserId }
        fun addEvent(event: Event) = apply { this.events.add(event) }
        fun method(method: Method) = apply { this.method = method }

        fun build(): EventSubscriptionRequest {
            return EventSubscriptionRequest(broadcasterUserId, events.toList(), method)
        }
    }
}
