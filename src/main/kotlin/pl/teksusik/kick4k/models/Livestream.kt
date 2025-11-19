package pl.teksusik.kick4k.models

data class Livestream(
    val broadcasterUserId: Long,
    val viewerCount: Int,
    val streamTitle: String,
    val category: Category
)
