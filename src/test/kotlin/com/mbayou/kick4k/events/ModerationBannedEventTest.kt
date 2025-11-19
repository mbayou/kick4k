package com.mbayou.kick4k.events

import com.mbayou.kick4k.events.type.ModerationBannedEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.time.Instant

class ModerationBannedEventTest : KickEventDispatcherTest() {
    @Test
    fun shouldDeserializeModerationBannedEvent() {
        val event = dispatchAndCapture(
            ModerationBannedEvent::class.java,
            "moderation.banned",
            "1",
            PAYLOAD,
        )

        val broadcaster = event.broadcaster
        assertNotNull(broadcaster)
        assertFalse(broadcaster!!.isAnonymous!!)
        assertEquals(123456789, broadcaster.userId)
        assertEquals("broadcaster_name", broadcaster.username)

        val moderator = event.moderator
        assertNotNull(moderator)
        assertEquals(987654321, moderator!!.userId)
        assertEquals("moderator_name", moderator.username)

        val bannedUser = event.bannedUser
        assertNotNull(bannedUser)
        assertEquals(135790135, bannedUser!!.userId)
        assertEquals("banned_user_name", bannedUser.username)

        val metadata = event.metadata
        assertNotNull(metadata)
        assertEquals("banned reason", metadata!!.reason)
        assertEquals(Instant.parse("2025-01-14T16:08:05Z"), metadata.createdAt)
        assertEquals(Instant.parse("2025-01-14T16:10:06Z"), metadata.expiresAt)
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
              "moderator": {
                "is_anonymous": false,
                "user_id": 987654321,
                "username": "moderator_name",
                "is_verified": false,
                "profile_picture": "https://example.com/moderator_avatar.jpg",
                "channel_slug": "moderator_channel",
                "identity": null
              },
              "banned_user": {
                "is_anonymous": false,
                "user_id": 135790135,
                "username": "banned_user_name",
                "is_verified": false,
                "profile_picture": "https://example.com/banned_user_avatar.jpg",
                "channel_slug": "banned_user_channel",
                "identity": null
              },
              "metadata": {
                "reason": "banned reason",
                "created_at": "2025-01-14T16:08:05Z",
                "expires_at": "2025-01-14T16:10:06Z"
              }
            }
        """
    }
}
