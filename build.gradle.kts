import org.gradle.api.GradleException
import org.gradle.api.provider.Provider
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository
import java.util.Base64

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

val ossrhTokenRaw: Provider<String?> = providers.gradleProperty("ossrhBearerToken")
    .orElse(providers.gradleProperty("ossrhToken"))
    .orElse(providers.environmentVariable("OSSRH_BEARER_TOKEN"))
    .orElse(providers.environmentVariable("OSSRH_TOKEN"))
    .orElse(providers.provider { null })

val ossrhDecodedToken: Provider<Pair<String, String>?> = ossrhTokenRaw.map { token ->
    token?.trim()
        ?.let { candidate ->
            runCatching { String(Base64.getDecoder().decode(candidate)) }
                .getOrNull()
                ?.split(":", limit = 2)
                ?.takeIf { it.size == 2 }
                ?.let { it[0] to it[1] }
        }
}

val ossrhDerivedUsername: Provider<String?> = ossrhDecodedToken.map { it?.first }
val ossrhDerivedPassword: Provider<String?> = ossrhDecodedToken.map { it?.second }

val ossrhUsername: Provider<String?> = providers.gradleProperty("ossrhTokenUsername")
    .orElse(providers.gradleProperty("ossrhUsername"))
    .orElse(providers.gradleProperty("mavenUsername"))
    .orElse(providers.environmentVariable("OSSRH_TOKEN_USERNAME"))
    .orElse(providers.environmentVariable("OSSRH_USERNAME"))
    .orElse(providers.environmentVariable("MAVEN_USERNAME"))
    .orElse(ossrhDerivedUsername)
    .orElse(providers.provider { null })

val ossrhPassword: Provider<String?> = providers.gradleProperty("ossrhTokenSecret")
    .orElse(providers.gradleProperty("ossrhPassword"))
    .orElse(providers.gradleProperty("mavenPassword"))
    .orElse(providers.environmentVariable("OSSRH_TOKEN_PASSWORD"))
    .orElse(providers.environmentVariable("OSSRH_PASSWORD"))
    .orElse(providers.environmentVariable("MAVEN_PASSWORD"))
    .orElse(ossrhDerivedPassword)
    .orElse(ossrhTokenRaw)
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
                    "OSSRH credentials are not configured. Provide the 'ossrhUsername'/'ossrhPassword' Gradle " +
                        "properties, export OSSRH_USERNAME together with OSSRH_PASSWORD/OSSRH_TOKEN_PASSWORD, " +
                        "or set OSSRH_TOKEN to the base64-encoded 'tokenName:tokenSecret' pair from the Central portal."
                )
            }
        }
    }
}
