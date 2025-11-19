package com.mbayou.kick4k.authorization

class OAuthTokenException(val errorCode: Int, val payload: String) :
    RuntimeException("Authorization failed: $errorCode. $payload")
