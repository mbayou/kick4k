package com.mbayou.kick4k.events

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.mbayou.kick4k.events.handler.KickEventDispatcher
import com.mbayou.kick4k.events.handler.KickEventListener
import com.mbayou.kick4k.events.type.KickEvent
import org.junit.jupiter.api.BeforeEach

open class KickEventDispatcherTest {
    protected lateinit var eventDispatcher: KickEventDispatcher

    @BeforeEach
    fun setup() {
        val objectMapper = ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .registerModule(JavaTimeModule())
        eventDispatcher = KickEventDispatcher(objectMapper)
    }

    protected fun <E : KickEvent> dispatchAndCapture(
        eventClass: Class<E>,
        eventType: String,
        version: String,
        payload: String,
    ): E {
        val events = mutableListOf<E>()
        val listener = KickEventListener<E> { event -> events.add(event) }
        eventDispatcher.registerListener(eventClass, listener)
        eventDispatcher.dispatch(eventType, version, payload)
        return events.first()
    }
}
