import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.invoke
import java.io.File

class SamPlugin : Plugin<Project> {

    private val groupName = "sam"

    private val Project.generatedDir
        get() = File(buildDir, "generated").apply(File::mkdirs)
    private val Project.template1File
        get() = File(generatedDir, "template.yaml")
    private val Project.template2File
        get() = File(generatedDir, "package.yaml")

    override fun apply(target: Project): Unit = target.run {
        val sam = extensions.create(SamExtension.name, SamExtension::class.java)

        tasks {
            val clean = getByName("clean")
            val test = getByName("test")
                .apply {
                    dependsOn(clean)
                    mustRunAfter(clean)
                }
            val shadowJar = getByName("shadowJar")
                .apply {
                    dependsOn(clean)
                    mustRunAfter(clean)
                }

            val templateYamlTask = register("generateTemplateYaml") {
                group = groupName
                doLast {
                    val output = File(
                        buildDir, arrayOf(
                            "libs",
                            "${project.name}-$version-all.jar"
                        ).joinToString(File.separator)
                    )
                    sam.template.resources
                        .values
                        .filter { it.type == SamResource.Type.FUNCTION }
                        .filter { it.properties.codeUri.isEmpty() && output.exists() }
                        .forEach { it.properties.codeUri = output.absolutePath }
                    YAMLMapper(YAMLFactory())
                        .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                        .writeValue(template1File.outputStream(), sam.template)
                }
                dependsOn(shadowJar)
                mustRunAfter(shadowJar)
            }

            val packageTask = register("packageSamApp", Exec::class.java) {
                group = groupName
                workingDir = rootDir
                commandLine = listOf(
                    "sam", "package",
                    "--template-file", template1File,
                    "--s3-bucket", sam.s3Bucket,
                    "--output-template-file", template2File
                )
                dependsOn(templateYamlTask)
                mustRunAfter(templateYamlTask)
            }

            register("deploySamApp", Exec::class.java) {
                group = groupName
                workingDir = rootDir
                commandLine = listOf(
                    "sam", "deploy",
                    "--template-file", template2File,
                    "--stack-name", sam.stackName,
                    "--capabilities", "CAPABILITY_IAM"
                )
                dependsOn(test, packageTask)
                mustRunAfter(test, packageTask)
            }

            register("runLocalSamApp", Exec::class.java) {
                group = groupName
                workingDir = rootDir
                commandLine = listOf(
                    "sam", "local",
                    "start-api", "--template", "$template1File"
                )
                dependsOn(templateYamlTask)
                mustRunAfter(templateYamlTask)
            }
        }
    }
}
