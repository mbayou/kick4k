package pl.teksusik.kick4k

import pl.teksusik.kick4k.authorization.AuthorizationClient
import pl.teksusik.kick4k.events.EventDispatcher
import pl.teksusik.kick4k.events.EventsClient
import pl.teksusik.kick4k.models.Category
import pl.teksusik.kick4k.models.Channel
import pl.teksusik.kick4k.models.Livestream
import pl.teksusik.kick4k.models.User

class KickClient(private val configuration: KickConfiguration) {
    private val eventDispatcher = EventDispatcher()
    private val authorizationClient = AuthorizationClient(configuration)
    private val usersClient = UsersClient()
    private val channelsClient = ChannelsClient()
    private val chatClient = ChatClient(eventDispatcher)
    private val eventsClient = EventsClient(eventDispatcher)

    fun configuration(): KickConfiguration = configuration
    fun authorization(): AuthorizationClient = authorizationClient
    fun users(): UsersClient = usersClient
    fun channels(): ChannelsClient = channelsClient
    fun chat(): ChatClient = chatClient
    fun eventDispatcher(): EventDispatcher = eventDispatcher
    fun events(): EventsClient = eventsClient
}

class UsersClient {
    fun getCurrentUser(): User = User(1L, "kickbot", "Kick4k Bot")
}

class ChannelsClient {
    fun getCurrentChannel(): Channel = Channel(slug = "kick4k", broadcasterUserId = 1L, streamTitle = "Kick4k Demo")

    fun updateChannel(streamTitle: String): Channel = getCurrentChannel().copy(streamTitle = streamTitle)
}

class ChatClient(private val eventDispatcher: EventDispatcher) {
    fun postChatMessage(content: String, broadcasterUserId: Long): String {
        eventDispatcher.dispatch("chat.message.sent", content)
        return "message-${broadcasterUserId}-${content.hashCode()}"
    }
}

class LivestreamsClient {
    fun getLivestream(broadcasterUserId: Long): Livestream? = Livestream(
        broadcasterUserId = broadcasterUserId,
        viewerCount = 0,
        streamTitle = "Hello Kick",
        category = Category(12, "Gaming")
    )
}
