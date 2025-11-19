package pl.teksusik.kick4k.authorization

enum class Scope(val value: String) {
    USER_READ("user.read"),
    CHANNEL_READ("channel.read"),
    CHAT_WRITE("chat.write"),
    CHANNEL_WRITE("channel.write"),
    LIVESTREAM_READ("livestream.read"),
    LIVESTREAM_WRITE("livestream.write")
}
