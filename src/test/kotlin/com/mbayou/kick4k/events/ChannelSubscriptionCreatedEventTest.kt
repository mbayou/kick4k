package com.mbayou.kick4k.events

import com.mbayou.kick4k.events.type.ChannelSubscriptionCreatedEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Instant

class ChannelSubscriptionCreatedEventTest : KickEventDispatcherTest() {
    @Test
    fun shouldDeserializeChannelSubscriptionCreatedEvent() {
        val event = dispatchAndCapture(
            ChannelSubscriptionCreatedEvent::class.java,
            "channel.subscription.new",
            "1",
            PAYLOAD,
        )

        val broadcaster = event.broadcaster
        assertNotNull(broadcaster)
        assertFalse(broadcaster!!.isAnonymous!!)
        assertEquals(123456789, broadcaster.userId)
        assertEquals("broadcaster_name", broadcaster.username)
        assertTrue(broadcaster.isVerified!!)
        assertEquals("https://example.com/broadcaster_avatar.jpg", broadcaster.profilePicture)
        assertEquals("broadcaster_channel", broadcaster.channelSlug)
        assertNull(broadcaster.identity)

        val subscriber = event.subscriber
        assertNotNull(subscriber)
        assertFalse(subscriber!!.isAnonymous!!)
        assertEquals(987654321, subscriber.userId)
        assertEquals("subscriber_name", subscriber.username)
        assertFalse(subscriber.isVerified!!)
        assertEquals("https://example.com/sender_avatar.jpg", subscriber.profilePicture)
        assertEquals("subscriber_channel", subscriber.channelSlug)
        assertNull(subscriber.identity)

        assertEquals(1, event.duration)
        assertEquals(Instant.parse("2025-01-14T16:08:06Z"), event.createdAt)
        assertEquals(Instant.parse("2025-02-14T16:08:06Z"), event.expiresAt)
    }

    companion object {
        private const val PAYLOAD = """
            {
              "broadcaster": {
                "is_anonymous": false,
                "user_id": 123456789,
                "username": "broadcaster_name",
                "is_verified": true,
                "profile_picture": "https://example.com/broadcaster_avatar.jpg",
                "channel_slug": "broadcaster_channel",
                "identity": null
              },
              "subscriber": {
                "is_anonymous": false,
                "user_id": 987654321,
                "username": "subscriber_name",
                "is_verified": false,
                "profile_picture": "https://example.com/sender_avatar.jpg",
                "channel_slug": "subscriber_channel",
                "identity": null
              },
              "duration": 1,
              "created_at": "2025-01-14T16:08:06Z",
              "expires_at": "2025-02-14T16:08:06Z"
            }
        """
    }
}
