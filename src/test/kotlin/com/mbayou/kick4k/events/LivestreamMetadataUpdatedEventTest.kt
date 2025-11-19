package com.mbayou.kick4k.events

import com.mbayou.kick4k.events.type.LivestreamMetadataUpdatedEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class LivestreamMetadataUpdatedEventTest : KickEventDispatcherTest() {
    @Test
    fun shouldDeserializeLivestreamMetadataUpdatedEvent() {
        val event = dispatchAndCapture(
            LivestreamMetadataUpdatedEvent::class.java,
            "livestream.metadata.updated",
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

        val metadata = event.metadata
        assertNotNull(metadata)
        assertEquals("Stream Title", metadata!!.title)
        assertEquals("en", metadata.language)
        assertTrue(metadata.hasMatureContent!!)

        val category = metadata.category
        assertNotNull(category)
        assertEquals(123, category!!.id)
        assertEquals("Category name", category.name)
        assertEquals("http://example.com/image123", category.thumbnail)
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
              "metadata": {
                "title": "Stream Title",
                "language": "en",
                "has_mature_content": true,
                "category": {
                  "id": 123,
                  "name": "Category name",
                  "thumbnail": "http://example.com/image123"
                }
              }
            }
        """
    }
}
