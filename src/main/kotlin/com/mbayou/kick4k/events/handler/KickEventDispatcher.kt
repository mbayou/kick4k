package com.mbayou.kick4k.events.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.mbayou.kick4k.events.type.KickEvent
import java.io.IOException
import java.lang.reflect.Method

class KickEventDispatcher(private val mapper: ObjectMapper) {
    private val eventTypeToClass: MutableMap<String, Class<out KickEvent>> = mutableMapOf()
    private val handlers: MutableMap<Class<out KickEvent>, MutableList<KickEventListener<*>>> = mutableMapOf()

    fun <E : KickEvent> registerListener(eventClass: Class<E>, listener: KickEventListener<E>) {
        val eventType = invokeStaticStringMethod(eventClass, "getEventType")
        val eventVersion = invokeStaticStringMethod(eventClass, "getEventVersion")
        val key = produceKey(eventType, eventVersion)

        eventTypeToClass.putIfAbsent(key, eventClass)
        handlers.computeIfAbsent(eventClass) { mutableListOf() }.add(listener)
    }

    fun dispatch(eventType: String, version: String, payload: String) {
        val clazz = eventTypeToClass[produceKey(eventType, version)] ?: return
        val event = deserialize(payload, clazz)
        val listeners = handlers[clazz] ?: return

        listeners.forEach { listener ->
            @Suppress("UNCHECKED_CAST")
            (listener as KickEventListener<KickEvent>).handle(event)
        }
    }

    private fun deserialize(payload: String, clazz: Class<out KickEvent>): KickEvent {
        return try {
            mapper.readValue(payload, clazz)
        } catch (exception: IOException) {
            throw RuntimeException("Failed to deserialize Kick event", exception)
        }
    }

    companion object {
        private fun produceKey(eventType: String, version: String): String = "$eventType:$version"

        private fun invokeStaticStringMethod(clazz: Class<*>, methodName: String): String {
            return try {
                val method: Method = clazz.getMethod(methodName)
                method.invoke(null) as String
            } catch (exception: Exception) {
                throw RuntimeException("Failed to invoke static method $methodName on ${clazz.name}", exception)
            }
        }
    }
}
