package pl.teksusik.kick4k.authorization

interface RefreshTokenStore {
    fun getRefreshToken(): String?
    fun notifyRefreshTokenRoll(newRefreshToken: String?)
}
