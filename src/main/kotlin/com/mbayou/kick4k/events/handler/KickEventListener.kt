package com.mbayou.kick4k.events.handler

import com.mbayou.kick4k.events.type.KickEvent

fun interface KickEventListener<T : KickEvent> {
    fun handle(event: T)
}
