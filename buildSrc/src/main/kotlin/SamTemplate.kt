import com.fasterxml.jackson.annotation.JsonProperty

data class SamTemplate(
    @get:JsonProperty("AWSTemplateFormatVersion")
    val awstemplateformatversion: String = "2010-09-09",

    @get:JsonProperty("Transform")
    val transform: String = "AWS::Serverless-2016-10-31",

    @get:JsonProperty("Resources")
    val resources: HashMap<String, SamResource> = LinkedHashMap()
)
