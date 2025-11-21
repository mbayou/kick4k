package com.mbayou.kick4k

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.sun.net.httpserver.HttpServer
import com.mbayou.kick4k.api.ApiResponse
import com.mbayou.kick4k.api.ApiResponseDeserializer
import com.mbayou.kick4k.authorization.AuthorizationClient
import com.mbayou.kick4k.categories.CategoriesClient
import com.mbayou.kick4k.channels.ChannelsClient
import com.mbayou.kick4k.chat.ChatClient
import com.mbayou.kick4k.events.EventsClient
import com.mbayou.kick4k.events.handler.BuiltInKickWebhookHandler
import com.mbayou.kick4k.events.handler.KickEventDispatcher
import com.mbayou.kick4k.events.handler.KickSignatureVerifier
import com.mbayou.kick4k.livestreams.LivestreamsClient
import com.mbayou.kick4k.moderation.ModerationClient
import com.mbayou.kick4k.publicKey.PublicKeyClient
import com.mbayou.kick4k.users.UsersClient
import java.io.IOException
import java.net.InetSocketAddress
import java.net.http.HttpClient

class KickClient(configuration: KickConfiguration) {
    private val authorizationClient: AuthorizationClient
    private val categoriesClient: CategoriesClient
    private val usersClient: UsersClient
    private val channelsClient: ChannelsClient
    private val chatClient: ChatClient
    private val moderationClient: ModerationClient
    private val livestreamsClient: LivestreamsClient
    private val publicKeyClient: PublicKeyClient
    private val eventsClient: EventsClient
    private val signatureVerifier: KickSignatureVerifier
    private val eventDispatcher: KickEventDispatcher

    private var httpServer: HttpServer? = null

    init {
        val httpClient = HttpClient.newHttpClient()

        val serializerModule = SimpleModule()
            .addDeserializer(ApiResponse::class.java, ApiResponseDeserializer<Any>())

        val objectMapper = ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .registerModule(JavaTimeModule())
            .registerModule(serializerModule)

        this.authorizationClient = AuthorizationClient(httpClient, objectMapper, configuration)
        this.categoriesClient = CategoriesClient(httpClient, objectMapper, configuration)
        this.usersClient = UsersClient(httpClient, objectMapper, configuration)
        this.channelsClient = ChannelsClient(httpClient, objectMapper, configuration)
        this.chatClient = ChatClient(httpClient, objectMapper, configuration)
        this.moderationClient = ModerationClient(httpClient, objectMapper, configuration)
        this.livestreamsClient = LivestreamsClient(httpClient, objectMapper, configuration)
        this.publicKeyClient = PublicKeyClient(httpClient, objectMapper, configuration)
        this.eventsClient = EventsClient(httpClient, objectMapper, configuration)

        this.signatureVerifier = KickSignatureVerifier(publicKeyClient)
        this.eventDispatcher = KickEventDispatcher(objectMapper)
    }

    fun startWebhookReceiver(path: String, port: Int) {
        try {
            httpServer = HttpServer.create(InetSocketAddress(port), 0).apply {
                createContext(path, BuiltInKickWebhookHandler(signatureVerifier, eventDispatcher))
                executor = null
                start()
            }
        } catch (exception: IOException) {
            throw RuntimeException("Failed to start web server", exception)
        }
    }

    fun stopWebhookReceiver() {
        val server = httpServer ?: throw IllegalStateException("Web server is not running")
        server.stop(100)
        httpServer = null
    }

    fun authorization(): AuthorizationClient = authorizationClient
    fun categories(): CategoriesClient = categoriesClient
    fun users(): UsersClient = usersClient
    fun channels(): ChannelsClient = channelsClient
    fun chat(): ChatClient = chatClient
    fun moderation(): ModerationClient = moderationClient
    fun livestreams(): LivestreamsClient = livestreamsClient
    fun publicKey(): PublicKeyClient = publicKeyClient
    fun events(): EventsClient = eventsClient
    fun signatureVerifier(): KickSignatureVerifier = signatureVerifier
    fun eventDispatcher(): KickEventDispatcher = eventDispatcher
}
