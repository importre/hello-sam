plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    jcenter()
}

gradlePlugin {
    plugins {
        register("sam-plugin") {
            id = "sam"
            implementationClass = "SamPlugin"
        }
    }
}

dependencies {
    compile("com.fasterxml.jackson.core:jackson-databind:2.9.7")
    compile("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.9.7")
}
