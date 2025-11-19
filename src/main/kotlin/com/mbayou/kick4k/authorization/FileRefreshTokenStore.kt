package com.mbayou.kick4k.authorization

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

class FileRefreshTokenStore(private val path: Path) : RefreshTokenStore {
    override fun getRefreshToken(): String {
        return try {
            Files.readString(path)
        } catch (exception: IOException) {
            throw RuntimeException("Couldn't read refresh token", exception)
        }
    }

    override fun notifyRefreshTokenRoll(newRefreshToken: String) {
        try {
            Files.writeString(path, newRefreshToken)
        } catch (exception: IOException) {
            throw RuntimeException("Couldn't write refresh token", exception)
        }
    }
}
