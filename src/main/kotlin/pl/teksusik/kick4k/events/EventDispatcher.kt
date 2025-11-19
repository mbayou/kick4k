package pl.teksusik.kick4k.events

typealias EventListener = (Any) -> Unit

class EventDispatcher {
    private val listeners: MutableMap<String, MutableList<EventListener>> = mutableMapOf()

    fun registerListener(eventType: String, listener: EventListener) {
        listeners.getOrPut(eventType) { mutableListOf() }.add(listener)
    }

    fun dispatch(eventType: String, payload: Any) {
        listeners[eventType]?.forEach { it.invoke(payload) }
    }
}
