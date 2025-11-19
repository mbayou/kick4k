package com.mbayou.kick4k.authorization;

public interface RefreshTokenStore {
    String getRefreshToken();
    void notifyRefreshTokenRoll(String newRefreshToken);
}
