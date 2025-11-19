package pl.teksusik.kick4k.events

class EventsClient(private val dispatcher: EventDispatcher) {
    fun postEventsSubscription(events: List<String>) {
        events.forEach { dispatcher.dispatch("events.subscription", it) }
    }
}
