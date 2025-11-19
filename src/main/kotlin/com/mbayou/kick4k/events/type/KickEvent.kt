package com.mbayou.kick4k.events.type

abstract class KickEvent {
    companion object {
        @JvmStatic
        fun getEventType(): String = throw UnsupportedOperationException()

        @JvmStatic
        fun getEventVersion(): String = throw UnsupportedOperationException()
    }
}
