import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class SamEvent(
    @get:JsonIgnore
    val name: String,

    @get:JsonProperty("Type")
    var type: Type = Type.API,

    @get:JsonProperty("Properties")
    var properties: Properties = Properties()
) {
    enum class Type {
        @JsonProperty("Api")
        API,
    }

    class Properties(
        @get:JsonProperty("Path")
        var path: String = "",

        @get:JsonProperty("Method")
        var method: Method = Method.GET
    ) {
        enum class Method {
            @JsonProperty("get")
            GET,

            @JsonProperty("put")
            PUT,
        }
    }

    fun properties(init: Properties.() -> Unit) {
        properties = Properties().also(init)
    }
}
