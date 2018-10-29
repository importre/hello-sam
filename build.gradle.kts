plugins {
    kotlin("jvm") version Version.kotlin
    shadow()
    id("sam")
}

repositories {
    jcenter()
}

group = "com.importre"
version = "0.0.1"

dependencies {
    implementation(kotlin("stdlib"))
    implementation(gson())
    implementation(arrow("core"))
    implementation(aws("core", Version.Aws.Lambda.core))
    implementation(aws("events", Version.Aws.Lambda.events))

    testImplementation(junit())
}

sam {
    s3Bucket = "riiid-dev-hello"
    stackName = "hello-sam"
    "HelloSamFunction" {
        properties {
            handler = "com.importre.example.Hello::handleRequest"
            events {
                "HelloSam" {
                    properties {
                        path = "/hello"
                        method = SamEvent.Properties.Method.GET
                    }
                }
            }
        }
    }
}
