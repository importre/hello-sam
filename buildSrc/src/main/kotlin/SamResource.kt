import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

sealed class SamResource(
    open val name: String,
    open val type: String,
    open val properties: Properties
) {

    data class Properties(
        @get:JsonProperty("Handler")
        var handler: String = "",

        @get:JsonProperty("CodeUri")
        var codeUri: String = "",

        @get:JsonProperty("Events")
        var events: HashMap<String, SamEvent> = LinkedHashMap(),

        @get:JsonProperty("Runtime")
        var runtime: Runtime = Runtime.JAVA8
    ) {
        enum class Runtime {
            @JsonProperty("java8")
            JAVA8,
        }

        fun events(init: SamEvents.() -> Unit) {
            SamEvents()
                .also(init)
                .events
                .associateTo(events) {
                    it.name to it
                }
        }
    }

    abstract operator fun String.invoke(init: SamResource.() -> Unit)

    abstract fun properties(init: Properties.() -> Unit)

    data class SamFunction(
        @get:JsonIgnore
        override val name: String,

        @get:JsonProperty("Type")
        override val type: String = "AWS::Serverless::Function",

        @get:JsonProperty("Properties")
        override var properties: Properties = Properties()
    ) : SamResource(name, type, properties) {

        override fun String.invoke(init: SamResource.() -> Unit) {
        }

        override fun properties(init: Properties.() -> Unit) {
            properties = Properties().also(init)
        }
    }
}
