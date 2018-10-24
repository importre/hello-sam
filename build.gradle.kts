buildscript {

    repositories {
        jcenter()
        maven { setUrl("https://kotlin.bintray.com/kotlinx") }
    }

    dependencies {
        classpath(
            kotlinx(
                "gradle-serialization-plugin",
                Version.KotlinX.serialization
            )
        )
    }
}

apply("plugin" to "kotlinx-serialization")

plugins {
    kotlin("jvm") version Version.kotlin
    shadow()
}

repositories {
    jcenter()
    maven { setUrl("https://kotlin.bintray.com/kotlinx") }
}

group = "com.importre"
version = "0.0.1"

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation(
        kotlinx(
            "serialization-runtime",
            Version.KotlinX.serialization
        )
    )
    implementation(aws("core", Version.Aws.Lambda.core))
    implementation(aws("events", Version.Aws.Lambda.events))

    testImplementation(junit())
}
