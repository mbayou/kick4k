package com.mbayou.kick4k.events.handler;

import com.mbayou.kick4k.events.type.KickEvent;

public interface KickEventListener<T extends KickEvent> {
    void handle(T event);
}
