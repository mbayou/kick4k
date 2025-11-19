package com.mbayou.kick4k.api

class ApiException(val statusCode: Int, message: String?) : RuntimeException(message)
