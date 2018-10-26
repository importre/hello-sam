class SamFunctions(
    internal val functions: HashSet<SamResource.SamFunction> = LinkedHashSet()
) {

    operator fun String.invoke(init: SamResource.SamFunction.() -> Unit) {
        SamResource.SamFunction(name = this)
            .apply(init)
            .apply(functions::add)
    }
}
