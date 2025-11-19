package com.mbayou.kick4k.events

import com.mbayou.kick4k.events.type.ChannelFollowedEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ChannelFollowedEventTest : KickEventDispatcherTest() {
    @Test
    fun shouldDeserializeChannelFollowedEvent() {
        val event = dispatchAndCapture(
            ChannelFollowedEvent::class.java,
            "channel.followed",
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

        val follower = event.follower
        assertNotNull(follower)
        assertFalse(follower!!.isAnonymous!!)
        assertEquals(987654321, follower.userId)
        assertEquals("follower_name", follower.username)
        assertFalse(follower.isVerified!!)
        assertEquals("https://example.com/sender_avatar.jpg", follower.profilePicture)
        assertEquals("follower_channel", follower.channelSlug)
        assertNull(follower.identity)
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
              "follower": {
                "is_anonymous": false,
                "user_id": 987654321,
                "username": "follower_name",
                "is_verified": false,
                "profile_picture": "https://example.com/sender_avatar.jpg",
                "channel_slug": "follower_channel",
                "identity": null
              }
            }
        """
    }
}
