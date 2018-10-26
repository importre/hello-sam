open class SamExtension(
    var s3Bucket: String = "",
    var stackName: String = "",
    val template: SamTemplate = SamTemplate()
) {
    companion object {
        const val name = "sam"
    }

    fun functions(init: SamFunctions.() -> Unit) {
        SamFunctions()
            .also(init)
            .functions
            .associateTo(template.resources) {
                it.name to it
            }
    }
}
