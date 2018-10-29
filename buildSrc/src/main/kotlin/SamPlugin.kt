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
            val shadowJar = getByName("shadowJar")
                .apply {
                    dependsOn(clean)
                    mustRunAfter(clean)
                }

            val templateYamlTask = register("generateTemplateYaml") {
                group = groupName
                doLast {
                    val out = template1File.outputStream()
                    YAMLMapper(YAMLFactory())
                        .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                        .writeValue(out, sam.template)
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
                dependsOn(packageTask)
                mustRunAfter(packageTask)
            }

            register("runLocalSam", Exec::class.java) {
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
