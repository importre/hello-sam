import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.invoke
import java.io.File

class SamPlugin : Plugin<Project> {

    private val template1File = "template.yaml"
    private val template2File = "package.yaml"

    override fun apply(target: Project): Unit = target.run {
        val sam = extensions.create(SamExtension.name, SamExtension::class.java)
        val temp = File(buildDir, "tmp").apply { takeUnless { exists() }?.mkdirs() }
        tasks {
            val clean = getByName("clean")
            val shadowJar = getByName("shadowJar")
                .apply {
                    dependsOn(clean)
                    mustRunAfter(clean)
                }

            val templateYamlTask = register("generateTemplateYaml") {
                group = "sam"
                doLast {
                    val out = File(temp, "template.yaml").outputStream()
                    YAMLMapper(YAMLFactory())
                        .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                        .writeValue(out, sam.template)
                }
                dependsOn(shadowJar)
                mustRunAfter(shadowJar)
            }

            val packageTask = register("packageSamApp", Exec::class.java) {
                group = "sam"
                workingDir = rootDir
                commandLine = listOf(
                    "sam", "package",
                    "--template-file", "${File(temp, template1File)}",
                    "--s3-bucket", sam.s3Bucket,
                    "--output-template-file", "${File(temp, template2File)}"
                )
                dependsOn(templateYamlTask)
                mustRunAfter(templateYamlTask)
            }

            register("deploySamApp", Exec::class.java) {
                group = "sam"
                workingDir = rootDir
                commandLine = listOf(
                    "sam", "deploy",
                    "--template-file", "${File(temp, template2File)}",
                    "--stack-name", sam.stackName,
                    "--capabilities", "CAPABILITY_IAM"
                )
                dependsOn(packageTask)
                mustRunAfter(packageTask)
            }
        }
    }
}
