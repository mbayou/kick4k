# Kick4k [![](https://jitpack.io/v/mbayou/kick4k.svg)](https://jitpack.io/#mbayou/kick4k)

A native Kotlin library for interacting with the Kick.com streaming platform API. Kick4k provides easy-to-use clients for major Kick endpoints, OAuth 2.0 authentication with PKCE support, and a lightweight event dispatcher for real-time style workflows.

## Features

- **Complete API Coverage**: Support for all official Kick.com API endpoints
    - User management and authentication
    - Channel operations and metadata
    - Chat messaging
    - Livestream management
    - Moderation tools
    - Categories and discovery
    - Event subscriptions
- **OAuth 2.0 with PKCE**: Secure authentication flow implementation
- **Webhook Support**: Built-in webhook receiver with signature verification, for demonstration purposes
- **Event System**: Type-safe event handling for real-time notifications
- **Flexible Configuration**: Customizable endpoints and settings
- **Token Management**: Automatic token refresh and storage

## Installation

### Gradle (Kotlin DSL)

Add the dependency to your `build.gradle.kts` (or Groovy equivalent):

```kotlin
dependencies {
    implementation("com.github.mbayou:kick4k:1.1.0")
}
```

## Distribution via JitPack

Kick4k is distributed through [JitPack](https://jitpack.io/#mbayou/kick4k). Add the repository and depend on a tag or
branch build as shown below:

```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.mbayou:kick4k:1.1.0")
    // or use a specific git tag / commit
}
```

Every push to GitHub can be built on JitPack; tagging a release (for example `v1.1.0`) gives you a stable coordinate
`com.github.mbayou:kick4k:1.1.0`. See [docs/PUBLISHING.md](docs/PUBLISHING.md) for the release checklist.

## Quick Start

Looking for a deeper walkthrough of OAuth, webhooks, and the major APIs? Check the [Usage Guide](docs/USAGE.md).

### 1. Basic Setup

```kotlin
import com.mbayou.kick4k.KickClient
import com.mbayou.kick4k.KickConfiguration
import com.mbayou.kick4k.authorization.RefreshTokenStore

class InMemoryTokenStore : RefreshTokenStore {
    private var token: String? = null
    override fun getRefreshToken(): String? = token
    override fun notifyRefreshTokenRoll(newRefreshToken: String?) { token = newRefreshToken }
}

val config = KickConfiguration.builder()
    .clientId("your-client-id")
    .clientSecret("your-client-secret")
    .redirectUri("http://localhost:8080/callback")
    .tokenStore(InMemoryTokenStore())
    .build()

val client = KickClient(config)
```

### 2. OAuth Authentication

```kotlin
import com.mbayou.kick4k.authorization.Scope

// Generate PKCE codes
val codeVerifier = client.authorization().generateCodeVerifier()
val codeChallenge = client.authorization().generateCodeChallenge(codeVerifier)

// Get authorization URL
val authUrl = client.authorization().getAuthorizationUrl(
    listOf(Scope.USER_READ, Scope.CHANNEL_READ, Scope.CHAT_WRITE),
    codeChallenge
)

println("Visit: $authUrl")

// After user authorization, exchange code for tokens
val tokens = client.authorization().exchangeCodeForToken("code-from-callback", codeVerifier)
client.authorization().setTokens(tokens)
```

### 3. Basic API Usage

```kotlin
// Get current user
val currentUser = client.users().getCurrentUser()
println("Hello, ${currentUser.displayName}")

// Get current channel
val channel = client.channels().getCurrentChannel()
println("Channel: ${channel.slug}")

// Send a chat message
client.chat().postChatMessage("Hello from Kick4k!", channel.broadcasterUserId)

// Update channel information
val updated = client.channels().updateChannel("New Stream Title")
println("Stream title updated to ${updated.streamTitle}")
```

## Advanced Usage

### Event Webhooks

Kick4k provides built-in webhook support for handling real-time events:

```kotlin
import com.mbayou.kick4k.events.EventSubscriptionRequest
import com.mbayou.kick4k.events.handler.KickEventListener
import com.mbayou.kick4k.events.type.ChannelFollowedEvent
import com.mbayou.kick4k.events.type.ChatMessageSentEvent

// Register event listeners
client.eventDispatcher().registerListener(
    ChatMessageSentEvent::class.java,
    KickEventListener { event ->
        println("${event.sender.username} said: ${event.content}")
    },
)

client.eventDispatcher().registerListener(
    ChannelFollowedEvent::class.java,
    KickEventListener { event ->
        println("New follower: ${event.follower.username}")
    },
)

// Start webhook receiver
client.startWebhookReceiver("/webhooks", 8080)

// Subscribe to events
val subscription = EventSubscriptionRequest.builder()
    .broadcasterUserId(currentUser.userId)
    .addEvent(EventSubscriptionRequest.Event("chat.message.sent", 1))
    .addEvent(EventSubscriptionRequest.Event("channel.followed", 1))
    .method(EventSubscriptionRequest.Method.WEBHOOK)
    .build()

client.events().postEventsSubscription(subscription)
```

## Acknowledgements

Huge thanks to **teksusik** for creating the original Kick4J project that inspired this Kotlin rewrite.

### Supported Events

- `chat.message.sent` - New chat messages
- `channel.followed` - New followers
- `channel.subscription.new` - New subscriptions
- `channel.subscription.renewal` - Subscription renewals
- `channel.subscription.gifts` - Gift subscriptions
- `livestream.status.updated` - Stream start/stop
- `livestream.metadata.updated` - Stream title/category changes
- `moderation.banned` - User bans/timeouts

### Moderation

```kotlin
// Ban a user
val banRequest = PostModerationBansRequest.builder()
    .broadcasterUserId(channel.broadcasterUserId)
    .userId(123456)
    .duration(3600)
    .reason("Spam")
    .build()

client.moderation().postModerationBans(banRequest)

// Unban a user
client.moderation().deleteModerationBans(
    channel.broadcasterUserId,
    123456,
)
```

### Livestream Management

```kotlin
val stream = client.livestreams()
    .getLivestream(channel.broadcasterUserId)

stream?.let {
    println("Currently streaming: ${it.streamTitle}")
    println("Viewers: ${it.viewerCount}")
}

val searchRequest = GetLivestreamsRequest.builder()
    .category(12) // Gaming
    .language("en")
    .limit(10)
    .sort(GetLivestreamsRequest.Sort.VIEWER_COUNT)
    .build()

val streams = client.livestreams().getLivestreams(searchRequest)
```

### Categories

```kotlin
// Search categories
val categories = client.categories().getCategories("gaming")

// Get specific category
val category = client.categories().getCategory(12)
println("Category: ${category.name}")
```

## Publishing the library

Releases are delivered through JitPack. Tag a commit (for example `v1.2.0`), ensure `./gradlew clean build` passes, and
JitPack will build and serve the artifact as `com.github.mbayou:kick4k:1.2.0`. Refer to [docs/PUBLISHING.md](docs/PUBLISHING.md)
for the full checklist.

## Configuration

### Custom Token Storage

Implement the `RefreshTokenStore` interface for custom token storage:

```kotlin
class DatabaseTokenStore(
    private val database: TokenDao,
) : RefreshTokenStore {
    override fun getRefreshToken(): String? {
        // Retrieve from database
        return database.getRefreshToken()
    }

    override fun notifyRefreshTokenRoll(newRefreshToken: String?) {
        // Store in database
        database.saveRefreshToken(newRefreshToken)
    }
}
```

### Custom Configuration

```kotlin
val config = KickConfiguration.builder()
    .clientId("your-client-id")
    .clientSecret("your-client-secret")
    .redirectUri("https://your-app.com/callback")
    .tokenStore(DatabaseTokenStore())
    .baseUrl("https://api.kick.com/public/v1") // Custom API base URL
    .oAuthHost("https://id.kick.com") // Custom OAuth host
    .build()
```

## Error Handling

Kick4k throws specific exceptions for different error conditions:

```kotlin
try {
    val user = client.users().getCurrentUser()
    println("Fetched ${user.displayName}")
} catch (apiException: ApiException) {
    System.err.println("API error ${apiException.statusCode}: ${apiException.message}")
} catch (tokenException: OAuthTokenException) {
    System.err.println("Auth error ${tokenException.errorCode}: ${tokenException.payload}")
    // Handle token refresh or re-authentication
}
```

## Requirements

- JDK 21 or higher (Kick4k targets Kotlin/JVM 21)
- Valid Kick.com application credentials

## Dependencies

- Jackson (JSON processing)
- Java HTTP Client (built-in)

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

For questions, issues, or feature requests, please open an issue on the GitHub repository.

## Disclaimer

This library is not officially affiliated with Kick.com. Use at your own risk and ensure compliance with Kick.com's Terms of Service and API usage guidelines.
