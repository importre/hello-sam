class SamEvents(
    internal val events: HashSet<SamEvent> = LinkedHashSet()
) {

    operator fun String.invoke(
        type: SamEvent.Type = SamEvent.Type.API,
        init: SamEvent.() -> Unit
    ) {
        SamEvent(name = this, type = type)
            .apply(init)
            .apply(events::add)
    }
}
