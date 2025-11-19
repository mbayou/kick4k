package com.mbayou.kick4k.authorization

interface RefreshTokenStore {
    fun getRefreshToken(): String
    fun notifyRefreshTokenRoll(newRefreshToken: String)
}
