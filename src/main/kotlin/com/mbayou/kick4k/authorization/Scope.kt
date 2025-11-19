package com.mbayou.kick4k.authorization

enum class Scope(val scope: String) {
    USER_READ("user:read"),
    CHANNEL_READ("channel:read"),
    CHANNEL_WRITE("channel:write"),
    CHAT_WRITE("chat:write"),
    STREAMKEY_READ("streamkey:read"),
    EVENTS_SUBSCRIBE("events:subscribe"),
    MODERATION_BAN("moderation:ban"),
}
