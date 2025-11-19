package com.mbayou.kick4k.events.handler

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

class BuiltInKickWebhookHandler(
    private val verifier: KickSignatureVerifier,
    private val dispatcher: KickEventDispatcher,
) : HttpHandler {

    override fun handle(exchange: HttpExchange) {
        val requestMethod = exchange.requestMethod
        val headers = exchange.requestHeaders

        val messageId = headers.getFirst("Kick-Event-Message-Id")
        val subscriptionId = headers.getFirst("Kick-Event-Subscription-Id")
        val signature = headers.getFirst("Kick-Event-Signature")
        val messageTimestamp = headers.getFirst("Kick-Event-Message-Timestamp")
        val eventType = headers.getFirst("Kick-Event-Type")
        val eventVersion = headers.getFirst("Kick-Event-Version")

        val body = readRequestBody(exchange.requestBody)

        exchange.sendResponseHeaders(200, -1)
        exchange.requestBody.close()

        if (!"POST".equals(requestMethod, ignoreCase = true)) {
            return
        }

        if (messageId == null || subscriptionId == null || signature == null || messageTimestamp == null || eventType == null || eventVersion == null) {
            return
        }

        if (!verifier.isValidSignature(messageId, messageTimestamp, body, signature)) {
            return
        }

        dispatcher.dispatch(eventType, eventVersion, body)
    }

    private fun readRequestBody(input: InputStream): String {
        BufferedReader(InputStreamReader(input, StandardCharsets.UTF_8)).use { reader ->
            val builder = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                builder.append(line).append('\n')
            }
            return builder.toString().trim()
        }
    }
}
