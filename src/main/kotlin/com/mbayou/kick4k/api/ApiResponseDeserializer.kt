package com.mbayou.kick4k.api

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import java.io.IOException

class ApiResponseDeserializer<T>(private val valueType: JavaType? = null) :
    JsonDeserializer<ApiResponse<T>>(),
    ContextualDeserializer {

    override fun deserialize(parser: JsonParser, context: DeserializationContext): ApiResponse<T> {
        val codec = parser.codec
        val root = codec.readTree(parser) as JsonNode

        val dataNode = root.get("data")
        val data: T? = if (dataNode == null || dataNode.isNull || (dataNode.isObject && dataNode.isEmpty)) {
            null
        } else {
            val mapper = codec as ObjectMapper
            val type = valueType ?: mapper.typeFactory.constructType(Any::class.java)
            mapper.readerFor(type).readValue(dataNode.traverse(mapper))
        }

        val message = if (root.has("message")) root.get("message").asText(null) else null
        return ApiResponse(data, message)
    }

    override fun createContextual(context: DeserializationContext, property: BeanProperty?): JsonDeserializer<*> {
        val contextualType = property?.type ?: context.contextualType
        if (contextualType == null || !contextualType.hasGenericTypes()) {
            return this
        }

        val contentType = contextualType.containedType(0)
        return ApiResponseDeserializer<Any>(contentType)
    }
}
