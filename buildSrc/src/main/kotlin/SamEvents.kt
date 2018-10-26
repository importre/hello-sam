class SamEvents(
    internal val events: HashSet<SamEvent> = LinkedHashSet()
) {

    operator fun String.invoke(init: SamEvent.() -> Unit) {
        SamEvent(name = this)
            .apply(init)
            .apply(events::add)
    }
}
