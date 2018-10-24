import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

fun PluginDependenciesSpec.shadow(): PluginDependencySpec =
    id("com.github.johnrengelman.shadow").version(Version.shadow)
