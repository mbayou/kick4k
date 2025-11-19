# Kick4k

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

Add the repository to your `build.gradle` or `build.gradle.kts`:

```gradle
repositories {
    mavenCentral()
    maven {
        name = "teksusik"
        url = uri("https://repo.teksusik.pl/snapshots")
    }
}

dependencies {
    implementation("com.mbayou:kick4k:1.1.0-SNAPSHOT")
}
```

## Quick Start

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

```java
// Register event listeners
client.eventDispatcher().registerListener(
    ChatMessageSentEvent.class,
    event -> {
        System.out.println(event.getSender().getUsername() + 
                          " said: " + event.getContent());
    }
);

client.eventDispatcher().registerListener(
    ChannelFollowedEvent.class,
    event -> {
        System.out.println("New follower: " + 
                          event.getFollower().getUsername());
    }
);

// Start webhook receiver
client.startWebhookReceiver("/webhooks", 8080);

// Subscribe to events
EventSubscriptionRequest subscription = EventSubscriptionRequest.builder()
    .broadcasterUserId(currentUser.getUserId())
    .addEvent(new EventSubscriptionRequest.Event("chat.message.sent", 1))
    .addEvent(new EventSubscriptionRequest.Event("channel.followed", 1))
    .method(EventSubscriptionRequest.Method.WEBHOOK)
    .build();

client.events().postEventsSubscription(subscription);
```

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

```java
// Ban a user
PostModerationBansRequest banRequest = PostModerationBansRequest.builder()
    .broadcasterUserId(channel.getBroadcasterUserId())
    .userId(123456)
    .duration(3600)
    .reason("Spam")
    .build();

client.moderation().postModerationBans(banRequest);

// Unban a user
client.moderation().deleteModerationBans(
    channel.getBroadcasterUserId(), 
    123456
);
```

### Livestream Management

```java
// Get current livestream
Livestream stream = client.livestreams()
    .getLivestream(channel.getBroadcasterUserId());

if (stream != null) {
    System.out.println("Currently streaming: " + stream.getStreamTitle());
    System.out.println("Viewers: " + stream.getViewerCount());
}

// Search livestreams
GetLivestreamsRequest searchRequest = GetLivestreamsRequest.builder()
    .category(12) // Gaming
    .language("en")
    .limit(10)
    .sort(GetLivestreamsRequest.Sort.VIEWER_COUNT)
    .build();

List<Livestream> streams = client.livestreams().getLivestreams(searchRequest);
```

### Categories

```java
// Search categories
List<Category> categories = client.categories().getCategories("gaming");

// Get specific category
Category category = client.categories().getCategory(12);
System.out.println("Category: " + category.getName());
```

## Publishing the library

Kick4k already ships with a Gradle `maven-publish` configuration that targets the TekSUSik Reposilite instance under the `com.mbayou`
group. Refer to [docs/PUBLISHING.md](docs/PUBLISHING.md) for a detailed checklist covering version bumps, credential setup, and the
commands to push snapshot or release artifacts.

## Configuration

### Custom Token Storage

Implement the `RefreshTokenStore` interface for custom token storage:

```java
public class DatabaseTokenStore implements RefreshTokenStore {
    @Override
    public String getRefreshToken() {
        // Retrieve from database
        return database.getRefreshToken();
    }

    @Override
    public void notifyRefreshTokenRoll(String newRefreshToken) {
        // Store in database
        database.saveRefreshToken(newRefreshToken);
    }
}
```

### Custom Configuration

```java
KickConfiguration config = KickConfiguration.builder()
    .clientId("your-client-id")
    .clientSecret("your-client-secret")
    .redirectUri("https://your-app.com/callback")
    .tokenStore(new DatabaseTokenStore())
    .baseUrl("https://api.kick.com/public/v1") // Custom API base URL
    .oAuthHost("https://id.kick.com") // Custom OAuth host
    .build();
```

## Error Handling

Kick4k throws specific exceptions for different error conditions:

```java
try {
    User user = client.users().getCurrentUser();
} catch (ApiException e) {
    System.err.println("API Error " + e.getErrorCode() + ": " + e.getPayload());
} catch (OAuthTokenException e) {
    System.err.println("Auth Error " + e.getErrorCode() + ": " + e.getPayload());
    // Handle token refresh or re-authentication
}
```

## Requirements

- Java 21 or higher
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