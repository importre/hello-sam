import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class SamResource(
    @get:JsonIgnore
    val name: String,

    @get:JsonProperty("Type")
    val type: Type? = null,

    @get:JsonProperty("Properties")
    val properties: Properties = Properties()
) {

    enum class Type {
        @JsonProperty("AWS::Serverless::Function")
        FUNCTION,
    }

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

    fun properties(init: Properties.() -> Unit) {
        init(properties)
    }
}
