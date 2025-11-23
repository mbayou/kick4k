# Kick4k Usage Guide

This guide expands on the scenarios that come up once you embed Kick4k inside a real application. Each section
builds on the previous one, walking through OAuth, refresh tokens, webhooks, and common API workflows.

> All snippets below assume a single `KickClient` instance that gets reused for the life of your application.

## 1. Completing the OAuth flow

Kick uses OAuth 2.0 with PKCE. Kick4k exposes every step of that exchange through `AuthorizationClient`:

```kotlin
val config = KickConfiguration.builder()
    .clientId(System.getenv("KICK_CLIENT_ID"))
    .clientSecret(System.getenv("KICK_CLIENT_SECRET"))
    .redirectUri("http://localhost:8080/callback")
    .tokenStore(FileRefreshTokenStore(Paths.get("kick-tokens.json")))
    .build()

val client = KickClient(config)

val codeVerifier = AuthorizationClient.generateCodeVerifier()
val codeChallenge = AuthorizationClient.generateCodeChallenge(codeVerifier)

val authUrl = client.authorization().getAuthorizationUrl(
    scopeList = listOf(Scope.USER_READ, Scope.CHANNEL_READ, Scope.CHAT_WRITE),
    codeChallenge = codeChallenge,
    redirectUri = "http://localhost:8080/callback"
)
println("Visit $authUrl and approve the app")

// Inside your redirect handler:
val code = parameters["code"] ?: error("Missing code")
val tokens = client.authorization().exchangeCodeForToken(
    code = code,
    codeVerifier = codeVerifier,
    redirectUri = "http://localhost:8080/callback"
)
client.authorization().setTokens(tokens)
```

Key points:

- Generate a new `codeVerifier` for every auth attempt, persist it while waiting for the callback, and validate the
  returned `state` yourself if you need CSRF protection.
- Call `setTokens` right away so that the `RefreshTokenStore` sees the latest refresh token, even before any API call.
- Once you have tokens, every other client (`users()`, `channels()`, and so on) automatically attaches the bearer token.

> Tip: If your app has multiple redirect URIs, persist the one used for each authorization attempt and pass it to both
> `getAuthorizationUrl` and `exchangeCodeForToken`. Leaving the argument out falls back to the default URI in
> `KickConfiguration`.

## 2. Refresh token system

`RefreshTokenStore` is how Kick4k remembers the latest refresh token between runs. Provide your own implementation or use
`FileRefreshTokenStore` if writing to disk is acceptable.

```kotlin
class InMemoryTokenStore : RefreshTokenStore {
    private var token: String? = null
    override fun getRefreshToken(): String? = token
    override fun notifyRefreshTokenRoll(newRefreshToken: String?) { token = newRefreshToken }
}

val config = KickConfiguration.builder()
    .tokenStore(InMemoryTokenStore())
    // other fields...
    .build()
```

When any API call needs an access token, Kick4k calls `AuthorizationClient.getAccessToken()`. If the cached token is
null or expires in the next ten seconds, Kick4k automatically:

1. Looks up the refresh token from the store.
2. Calls `refreshAccessToken()`.
3. Invokes `notifyRefreshTokenRoll` so the store can persist the new refresh token.

Because of this workflow, your app typically just starts with the latest refresh token (from disk, a database, or a
secret manager) and never needs to handle refresh logic itself.

## 3. Webhook declaration and event processing

Kick4k includes a lightweight HTTP server backed by the JDK’s `HttpServer`. The two key pieces are
`KickEventDispatcher` (routes JSON payloads to listeners) and `KickSignatureVerifier` (ensures events were sent by Kick).

```kotlin
// 1. Register listeners for the event classes you care about.
client.eventDispatcher().registerListener(ChatMessageSentEvent::class.java, KickEventListener { event ->
    println("${event.sender?.username}: ${event.content}")
})

client.eventDispatcher().registerListener(LivestreamStatusUpdatedEvent::class.java, KickEventListener { event ->
    if (event.isLive == true) println("Stream started!") else println("Stream ended")
})

// 2. Start the built-in webhook receiver.
client.startWebhookReceiver(path = "/webhooks/kick", port = 8080)
```

What happens under the hood:

- `KickEventDispatcher` maps the combination of event name and version (for example `chat.message.sent:1`) to the
  corresponding Kotlin data class. During dispatch it deserializes the payload and invokes every listener registered for
  that class.
- `BuiltInKickWebhookHandler` extracts `Kick-Event-*` headers, reads the request body, returns HTTP 200 immediately, and
  then verifies the RSA/SHA256 signature using the current Kick public key (fetched with `PublicKeyClient`). Only valid
  POST requests reach the dispatcher.
- You can stop the server with `client.stopWebhookReceiver()` when shutting down.

If you need to plug Kick4k into an existing web framework, replicate the same verification and dispatcher calls inside
your own HTTP handler.

## 4. Creating subscriptions for a streamer

Kick only sends events you explicitly subscribe to. Use `EventsClient` to create, list, or delete subscriptions.

```kotlin
val broadcaster = client.users().getCurrentUser() // assumes the authorized user is the broadcaster

val request = EventSubscriptionRequest.builder()
    .broadcasterUserId(broadcaster.userId)
    .method(EventSubscriptionRequest.Method.WEBHOOK)
    .addEvent(EventSubscriptionRequest.Event("chat.message.sent", 1))
    .addEvent(EventSubscriptionRequest.Event("livestream.status.updated", 1))
    .addEvent(EventSubscriptionRequest.Event("channel.followed", 1))
    .build()

val result = client.events().postEventsSubscription(request)
println("Created ${result.size} subscriptions")
```

Additional helpers:

- `client.events().getEventsSubscriptions()` returns every subscription tied to the authorized broadcaster.
- `client.events().deleteEventsSubscriptions(ids)` accepts either a vararg or a `List<String>` of subscription IDs and
  unsubscribes in bulk.
- Always ensure the webhook endpoint you pass to Kick is reachable from the public internet; Kick4k does not expose a
  tunneling layer.

## 5. Retrieving user information

`UsersClient` mirrors the `/users` endpoints. Common calls include:

```kotlin
val me = client.users().getCurrentUser()
println("Display name: ${me.displayName}, userId=${me.userId}")

val byId = client.users().getUsers(listOf(1234, 5678))
val tokenMetadata = client.users().tokenIntrospect()
```

Use `tokenIntrospect()` to confirm which scopes a token currently has, or to check whether it belongs to the expected
user.

## 6. Retrieving stream and channel information

Combine `ChannelsClient` and `LivestreamsClient` to inspect both static channel metadata and real-time stream status.

```kotlin
val channel = client.channels().getCurrentChannel()
println("Slug=${channel.slug} | title=${channel.streamTitle}")

val livestream = client.livestreams().getLivestream(channel.broadcasterUserId)
if (livestream != null) {
    println("Live since ${livestream.startedAt} with ${livestream.viewerCount} viewers")
} else {
    println("Currently offline")
}

val topStreams = client.livestreams().getLivestreams(
    GetLivestreamsRequest.builder()
        .language("en")
        .limit(20)
        .sort(GetLivestreamsRequest.Sort.VIEWER_COUNT)
        .build(),
)
```

Use `LivestreamsClient.getLivestreamsStats()` if you need aggregate platform statistics (for example, total live channels).

## 7. Event subscription reference

Kick4k currently ships type-safe models for the following Kick event feeds:

| Event name | Version | Kotlin type | Notes |
| --- | --- | --- | --- |
| `chat.message.sent` | 1 | `ChatMessageSentEvent` | Every new chat message with emotes and reply metadata. |
| `channel.followed` | 1 | `ChannelFollowedEvent` | Fires whenever a new user follows the broadcaster. |
| `channel.subscription.new` | 1 | `ChannelSubscriptionCreatedEvent` | Brand-new paid subscriptions. |
| `channel.subscription.renewal` | 1 | `ChannelSubscriptionRenewalEvent` | Recurring subscription payments. |
| `channel.subscription.gifts` | 1 | `ChannelSubscriptionGiftsEvent` | Gift batch metadata including quantity and gifter identity. |
| `livestream.status.updated` | 1 | `LivestreamStatusUpdatedEvent` | Indicates go-live (`is_live = true`) and go-offline events plus timestamps. |
| `livestream.metadata.updated` | 1 | `LivestreamMetadataUpdatedEvent` | Title/category updates made mid-stream. |
| `moderation.banned` | 1 | `ModerationBannedEvent` | Permanent bans and temporary timeouts issued by moderators. |

To subscribe, add an `EventSubscriptionRequest.Event(eventName, version)` entry for each row you care about and register
an appropriate listener class. If Kick introduces new event kinds (for example raids or channel point redemptions) before
Kick4k includes a type, you can still receive them by:

1. Adding a new data class under `com.mbayou.kick4k.events.type` that extends `KickEvent` and returns the correct
   `getEventType()` / `getEventVersion()` pair.
2. Registering a listener for that class.
3. Subscribing to the event name with `EventSubscriptionRequest`.

This approach keeps the dispatcher working without forking the library, and your custom type continues to work after
official support lands.
