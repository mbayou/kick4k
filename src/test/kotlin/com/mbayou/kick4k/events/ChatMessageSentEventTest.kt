package com.mbayou.kick4k.events

import com.mbayou.kick4k.events.type.ChatMessageSentEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Instant

class ChatMessageSentEventTest : KickEventDispatcherTest() {
    @Test
    fun shouldDeserializeChatMessageSentEvent() {
        val event = dispatchAndCapture(
            ChatMessageSentEvent::class.java,
            "chat.message.sent",
            "1",
            PAYLOAD,
        )

        assertEquals("unique_message_id_123", event.messageId)

        val reply = event.repliesTo
        assertNotNull(reply)
        assertEquals("unique_message_id_456", reply!!.messageId)
        assertEquals("This is the parent message!", reply.content)

        val replySender = reply.sender
        assertNotNull(replySender)
        assertFalse(replySender!!.isAnonymous!!)
        assertEquals(12345, replySender.userId)
        assertEquals("parent_sender_name", replySender.username)
        assertFalse(replySender.isVerified!!)
        assertEquals("https://example.com/parent_sender_avatar.jpg", replySender.profilePicture)
        assertEquals("parent_sender_channel", replySender.channelSlug)
        assertNull(replySender.identity)

        val broadcaster = event.broadcaster
        assertNotNull(broadcaster)
        assertFalse(broadcaster!!.isAnonymous!!)
        assertEquals(123456789, broadcaster.userId)
        assertEquals("broadcaster_name", broadcaster.username)
        assertTrue(broadcaster.isVerified!!)
        assertEquals("https://example.com/broadcaster_avatar.jpg", broadcaster.profilePicture)
        assertEquals("broadcaster_channel", broadcaster.channelSlug)
        assertNull(broadcaster.identity)

        val sender = event.sender
        assertNotNull(sender)
        assertFalse(sender!!.isAnonymous!!)
        assertEquals(987654321, sender.userId)
        assertEquals("sender_name", sender.username)
        assertFalse(sender.isVerified!!)
        assertEquals("https://example.com/sender_avatar.jpg", sender.profilePicture)
        assertEquals("sender_channel", sender.channelSlug)
        assertNotNull(sender.identity)

        val identity = sender.identity
        assertEquals("#FF5733", identity!!.usernameColor)
        assertEquals(3, identity.badges!!.size)
        assertEquals("Moderator", identity.badges!![0].text)
        assertEquals("moderator", identity.badges!![0].type)
        assertNull(identity.badges!![0].count)
        assertEquals("Sub Gifter", identity.badges!![1].text)
        assertEquals("sub_gifter", identity.badges!![1].type)
        assertEquals(5, identity.badges!![1].count)
        assertEquals("Subscriber", identity.badges!![2].text)
        assertEquals("subscriber", identity.badges!![2].type)
        assertEquals(3, identity.badges!![2].count)

        assertEquals("This is a test message with emotes!", event.content)

        val emotes = event.emotes
        assertNotNull(emotes)
        assertEquals(2, emotes!!.size)
        val firstEmote = emotes[0]
        assertEquals("12345", firstEmote.emoteId)
        assertEquals("0", firstEmote.positions!![0].start)
        assertEquals("7", firstEmote.positions!![0].end)
        val secondEmote = emotes[1]
        assertEquals("67890", secondEmote.emoteId)
        assertEquals("20", secondEmote.positions!![0].start)
        assertEquals("25", secondEmote.positions!![0].end)

        assertEquals(Instant.parse("2025-01-14T16:08:06Z"), event.createdAt)
    }

    companion object {
        private const val PAYLOAD = """
            {
              "message_id": "unique_message_id_123",
              "replies_to": {
                "message_id": "unique_message_id_456",
                "content": "This is the parent message!",
                "sender": {
                  "is_anonymous": false,
                  "user_id": 12345,
                  "username": "parent_sender_name",
                  "is_verified": false,
                  "profile_picture": "https://example.com/parent_sender_avatar.jpg",
                  "channel_slug": "parent_sender_channel",
                  "identity": null
                }
              },
              "broadcaster": {
                "is_anonymous": false,
                "user_id": 123456789,
                "username": "broadcaster_name",
                "is_verified": true,
                "profile_picture": "https://example.com/broadcaster_avatar.jpg",
                "channel_slug": "broadcaster_channel",
                "identity": null
              },
              "sender": {
                "is_anonymous": false,
                "user_id": 987654321,
                "username": "sender_name",
                "is_verified": false,
                "profile_picture": "https://example.com/sender_avatar.jpg",
                "channel_slug": "sender_channel",
                "identity": {
                  "username_color": "#FF5733",
                  "badges": [
                    {
                      "text": "Moderator",
                      "type": "moderator"
                    },
                    {
                      "text": "Sub Gifter",
                      "type": "sub_gifter",
                      "count": 5
                    },
                    {
                      "text": "Subscriber",
                      "type": "subscriber",
                      "count": 3
                    }
                  ]
                }
              },
              "content": "This is a test message with emotes!",
              "emotes": [
                {
                  "emote_id": "12345",
                  "positions": [
                    { "s": 0, "e": 7 }
                  ]
                },
                {
                  "emote_id": "67890",
                  "positions": [
                    { "s": 20, "e": 25 }
                  ]
                }
              ],
              "created_at": "2025-01-14T16:08:06Z"
            }
        """
    }
}
