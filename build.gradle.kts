plugins {
    kotlin("jvm") version Version.kotlin
    shadow()
    id("sam")
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
    implementation(aws("core", Version.Aws.Lambda.core))
    implementation(aws("events", Version.Aws.Lambda.events))
    implementation(gson())

    testImplementation(junit())
}

sam {
    s3Bucket = "riiid-dev-hello"
    stackName = "hello-sam"
    functions {
        "HelloSamFunction" {
            properties {
                handler = "com.importre.example.Hello::handleRequest"
                codeUri = "$buildDir/libs/hello-sam-$version-all.jar"
                runtime = SamResource.Properties.Runtime.JAVA8
                events {
                    "HelloSam" {
                        type = SamEvent.Type.API
                        properties {
                            path = "/hello"
                            method = SamEvent.Properties.Method.GET
                        }
                    }
                }
            }
        }
    }
}
