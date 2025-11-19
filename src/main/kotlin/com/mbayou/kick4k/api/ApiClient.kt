package com.mbayou.kick4k.api

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.mbayou.kick4k.KickConfiguration
import com.mbayou.kick4k.authorization.AuthorizationClient
import java.io.IOException
import java.lang.reflect.Array
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.util.StringJoiner

abstract class ApiClient(
    protected val httpClient: HttpClient,
    protected val mapper: ObjectMapper,
    protected val configuration: KickConfiguration,
    protected val authorization: AuthorizationClient,
) {
    protected fun get(path: String): RequestBuilder = RequestBuilder("GET", path)
    protected fun post(path: String): RequestBuilder = RequestBuilder("POST", path)
    protected fun patch(path: String): RequestBuilder = RequestBuilder("PATCH", path)
    protected fun delete(path: String): RequestBuilder = RequestBuilder("DELETE", path)

    inner class RequestBuilder(private val method: String, private var path: String) {
        private var queryParams: MutableMap<String, Any?>? = null
        private var bodyObject: Any? = null

        fun queryParams(params: Map<String, Any?>?): RequestBuilder = apply {
            if (params != null) {
                if (queryParams == null) {
                    queryParams = params.toMutableMap()
                } else {
                    queryParams!!.putAll(params)
                }
            }
        }

        fun queryParams(params: Any?): RequestBuilder = apply {
            if (params == null) return@apply
            val map: MutableMap<String, Any?> =
                mapper.convertValue(params, object : TypeReference<MutableMap<String, Any?>>() {})
            map.entries.removeIf { it.value == null }
            if (queryParams == null) {
                queryParams = map
            } else {
                queryParams!!.putAll(map)
            }
        }

        fun pathParams(params: Map<String, Any?>): RequestBuilder = apply {
            params.forEach { (key, value) ->
                val placeholder = "\\{" + key + "\\}"
                val encodedValue = encode(value?.toString() ?: "")
                path = path.replace(placeholder.toRegex(), encodedValue)
            }
        }

        fun body(bodyObject: Any?): RequestBuilder = apply { this.bodyObject = bodyObject }

        fun <T> send(typeRef: TypeReference<ApiResponse<T>>): T? {
            val url = buildUrl(configuration.baseUrl + path, queryParams)
            val requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer ${authorization.getAccessToken()}")
                .header("Accept", "application/json")

            if ("GET".equals(method, ignoreCase = true)) {
                requestBuilder.GET()
            } else {
                val jsonBody = bodyObject?.let { mapper.writeValueAsString(it) } ?: ""
                requestBuilder.header("Content-Type", "application/json")
                    .method(method, HttpRequest.BodyPublishers.ofString(jsonBody))
            }

            val response = sendRequest(requestBuilder)
            val body = response.body()
            if (body.isNullOrEmpty()) {
                return null
            }

            val apiResponse = mapper.readValue(body, typeRef)
            if (!apiResponse.isSuccess() && response.statusCode() >= 400) {
                throw ApiException(response.statusCode(), apiResponse.message)
            }
            return apiResponse.data
        }

        private fun sendRequest(requestBuilder: HttpRequest.Builder): HttpResponse<String> {
            return try {
                httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString())
            } catch (exception: InterruptedException) {
                Thread.currentThread().interrupt()
                throw RuntimeException("Failed to send API request", exception)
            } catch (exception: IOException) {
                throw RuntimeException("Failed to send API request", exception)
            }
        }
    }

    companion object {
        private fun buildUrl(path: String, queryParams: Map<String, Any?>?): String {
            if (queryParams.isNullOrEmpty()) {
                return path
            }

            val joiner = StringJoiner("&", "$path?", "")
            queryParams.forEach { (key, value) ->
                when {
                    value == null -> Unit
                    value is Iterable<*> -> value.forEach { joiner.add("${encode(key)}=${encode(it.toString())}") }
                    value.javaClass.isArray -> {
                        val length = Array.getLength(value)
                        for (i in 0 until length) {
                            val element = Array.get(value, i)
                            joiner.add("${encode(key)}=${encode(element.toString())}")
                        }
                    }
                    else -> joiner.add("${encode(key)}=${encode(value.toString())}")
                }
            }
            return joiner.toString()
        }

        private fun encode(value: String): String = URLEncoder.encode(value, StandardCharsets.UTF_8)
    }

    protected inline fun <reified T> responseType(): TypeReference<ApiResponse<T>> =
        object : TypeReference<ApiResponse<T>>() {}
}
