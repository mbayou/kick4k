package com.mbayou.kick4k.events

import com.mbayou.kick4k.events.type.ChannelSubscriptionGiftsEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Instant

class ChannelSubscriptionGiftsEventTest : KickEventDispatcherTest() {
    @Test
    fun shouldDeserializeChannelSubscriptionGiftsEvent() {
        val event = dispatchAndCapture(
            ChannelSubscriptionGiftsEvent::class.java,
            "channel.subscription.gifts",
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

        val gifter = event.gifter
        assertNotNull(gifter)
        assertFalse(gifter!!.isAnonymous!!)
        assertEquals(987654321, gifter.userId)
        assertEquals("gifter_name", gifter.username)
        assertFalse(gifter.isVerified!!)
        assertEquals("https://example.com/sender_avatar.jpg", gifter.profilePicture)
        assertEquals("gifter_channel", gifter.channelSlug)
        assertNull(gifter.identity)

        val giftees = event.giftees
        assertNotNull(giftees)
        assertEquals(1, giftees!!.size)

        val firstGiftee = giftees[0]
        assertFalse(firstGiftee.isAnonymous!!)
        assertEquals(561654654, firstGiftee.userId)
        assertEquals("giftee_name", firstGiftee.username)
        assertTrue(firstGiftee.isVerified!!)
        assertEquals("https://example.com/broadcaster_avatar.jpg", firstGiftee.profilePicture)
        assertEquals("giftee_channel", firstGiftee.channelSlug)
        assertNull(firstGiftee.identity)

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
              "gifter": {
                "is_anonymous": false,
                "user_id": 987654321,
                "username": "gifter_name",
                "is_verified": false,
                "profile_picture": "https://example.com/sender_avatar.jpg",
                "channel_slug": "gifter_channel",
                "identity": null
              },
              "giftees":
              [
                {
                  "is_anonymous": false,
                  "user_id": 561654654,
                  "username": "giftee_name",
                  "is_verified": true,
                  "profile_picture": "https://example.com/broadcaster_avatar.jpg",
                  "channel_slug": "giftee_channel",
                  "identity": null
                }
              ],
              "created_at": "2025-01-14T16:08:06Z",
              "expires_at": "2025-02-14T16:08:06Z"
            }
        """
    }
}
