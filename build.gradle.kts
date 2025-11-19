import org.gradle.api.GradleException
import org.gradle.api.provider.Provider
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository

plugins {
    kotlin("jvm") version "2.0.21"
    id("java-library")
    id("maven-publish")
}

val mockitoAgent = configurations.create("mockitoAgent")

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

kotlin {
    jvmToolchain(21)
}

group = "com.mbayou"
version = "1.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.19.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.19.2")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(kotlin("test"))
    testImplementation("org.mockito:mockito-core:5.19.0")

    mockitoAgent("org.mockito:mockito-core:5.19.0") { isTransitive = false }
}

tasks.test {
    jvmArgs = listOf("-javaagent:${mockitoAgent.asPath}")
    useJUnitPlatform()
}

val ossrhUsername: Provider<String?> = providers.gradleProperty("ossrhUsername")
    .orElse(providers.gradleProperty("mavenUsername"))
    .orElse(providers.environmentVariable("OSSRH_USERNAME"))
    .orElse(providers.environmentVariable("MAVEN_USERNAME"))
    .orElse(providers.provider { null })

val ossrhPassword: Provider<String?> = providers.gradleProperty("ossrhPassword")
    .orElse(providers.gradleProperty("mavenPassword"))
    .orElse(providers.environmentVariable("OSSRH_TOKEN"))
    .orElse(providers.environmentVariable("OSSRH_PASSWORD"))
    .orElse(providers.environmentVariable("MAVEN_PASSWORD"))
    .orElse(providers.provider { null })

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            pom {
                name.set("Kick4k")
                description.set("Kotlin library for interaction with the Kick.com streaming platform API")
            }
        }
    }

    repositories {
        maven {
            name = "OSSRH"
            url = uri(
                if (version.toString().endsWith("-SNAPSHOT")) {
                    "https://oss.sonatype.org/content/repositories/snapshots/"
                } else {
                    "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                }
            )
            credentials {
                username = ossrhUsername.orNull ?: ""
                password = ossrhPassword.orNull ?: ""
            }
        }
    }
}

tasks.withType<PublishToMavenRepository>().configureEach {
    if (repository.name == "OSSRH") {
        doFirst {
            if (ossrhUsername.orNull.isNullOrBlank() || ossrhPassword.orNull.isNullOrBlank()) {
                throw GradleException(
                    "OSSRH credentials are not configured. Set the 'ossrhUsername'/'ossrhPassword' Gradle " +
                        "properties or the OSSRH_USERNAME/OSSRH_TOKEN (or OSSRH_PASSWORD / MAVEN_USERNAME / MAVEN_PASSWORD) environment variables."
                )
            }
        }
    }
}
