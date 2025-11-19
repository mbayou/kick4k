package com.mbayou.kick4k.events

import com.mbayou.kick4k.events.type.LivestreamStatusUpdatedEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Instant

class LivestreamStatusUpdatedEventTest : KickEventDispatcherTest() {
    @Test
    fun shouldDeserializeLivestreamStartedEvent() {
        val event = dispatchAndCapture(
            LivestreamStatusUpdatedEvent::class.java,
            "livestream.status.updated",
            "1",
            STREAM_STARTED_PAYLOAD,
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

        assertTrue(event.isLive!!)
        assertEquals("Stream Title", event.title)
        assertEquals(Instant.parse("2025-01-01T11:00:00+11:00"), event.startedAt)
        assertNull(event.endedAt)
    }

    @Test
    fun shouldDeserializeLivestreamEndedEvent() {
        val event = dispatchAndCapture(
            LivestreamStatusUpdatedEvent::class.java,
            "livestream.status.updated",
            "1",
            STREAM_ENDED_PAYLOAD,
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

        assertFalse(event.isLive!!)
        assertEquals("Stream Title", event.title)
        assertEquals(Instant.parse("2025-01-01T11:00:00+11:00"), event.startedAt)
        assertEquals(Instant.parse("2025-01-01T15:00:00+11:00"), event.endedAt)
    }

    companion object {
        private const val STREAM_STARTED_PAYLOAD = """
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
              "is_live": true,
              "title": "Stream Title",
              "started_at": "2025-01-01T11:00:00+11:00",
              "ended_at": null
            }
        """

        private const val STREAM_ENDED_PAYLOAD = """
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
              "is_live": false,
              "title": "Stream Title",
              "started_at": "2025-01-01T11:00:00+11:00",
              "ended_at": "2025-01-01T15:00:00+11:00"
            }
        """
    }
}
