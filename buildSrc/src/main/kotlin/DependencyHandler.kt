import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.aws(
    module: String,
    version: String
): Any = "com.amazonaws:aws-lambda-java-$module:$version"

fun DependencyHandler.junit(): Any = "junit:junit:${Version.junit}"

fun DependencyHandler.kotlinx(module: String, version: String? = null): Any =
    "org.jetbrains.kotlinx:kotlinx-$module${version?.let { ":$version" } ?: ""}"
