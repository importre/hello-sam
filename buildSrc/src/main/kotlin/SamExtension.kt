open class SamExtension(
    var s3Bucket: String = "",
    var stackName: String = "",
    val template: SamTemplate = SamTemplate()
) {
    companion object {
        const val name = "sam"
    }

    operator fun String.invoke(
        type: SamResource.Type = SamResource.Type.FUNCTION,
        init: SamResource.() -> Unit
    ) {
        SamResource(name = this, type = type)
            .apply(init)
            .apply { template.resources += name to this }
    }
}
