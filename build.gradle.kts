import org.gradle.api.GradleException
import org.gradle.api.provider.Provider
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository
import org.gradle.api.credentials.HttpHeaderCredentials
import org.gradle.authentication.http.HttpHeaderAuthentication

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

val ossrhAuthHeaderName: Provider<String> = providers.gradleProperty("ossrhAuthHeaderName")
    .orElse(providers.environmentVariable("OSSRH_AUTH_HEADER_NAME"))
    .orElse(providers.provider { "Authorization" })

val ossrhAuthHeaderValue: Provider<String?> = providers.gradleProperty("ossrhAuthHeaderValue")
    .orElse(providers.environmentVariable("OSSRH_AUTH_HEADER_VALUE"))
    .orElse(providers.environmentVariable("OSSRH_TOKEN"))
    .orElse(providers.provider { null })

fun maskSecret(value: String?): String = when {
    value.isNullOrBlank() -> "<empty>"
    value.length <= 6 -> "*".repeat(value.length)
    else -> value.take(6) + "... (len=${value.length})"
}

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
            name = "sonatype"
            val releasesRepo = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepo = "https://s01.oss.sonatype.org/content/repositories/snapshots/"

            url = uri("https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/")
            credentials {
                username = "GamYbS"
                password = "ZHsPgLXeQ6afIvpYnVm3NxxrVhthZH9B2"
            }
//            credentials(HttpHeaderCredentials::class) {
//                name = ""
//                value = ossrhAuthHeaderValue.orNull ?: ""
//            }
//            authentication {
//                create<HttpHeaderAuthentication>("header")
//            }
        }
    }
}

tasks.withType<PublishToMavenRepository>().configureEach {
    if (repository.name == "OSSRH") {
        doFirst {
            if (ossrhAuthHeaderValue.orNull.isNullOrBlank()) {
                throw GradleException(
                    "OSSRH credentials are not configured. Set 'ossrhAuthHeaderValue' (or the " +
                        "OSSRH_AUTH_HEADER_VALUE / OSSRH_TOKEN environment variable) to the exact " +
                        "\"Bearer <base64 tokenName:tokenSecret>\" string provided by the Central portal."
                )
            }

            val headerName = ossrhAuthHeaderName.get()
            val maskedHeaderValue = maskSecret(ossrhAuthHeaderValue.orNull)
            val repoUrl = repository.url
            val publicationName = (this as? PublishToMavenRepository)?.publication?.name ?: "unknown"
            val artifactList = (this as? PublishToMavenRepository)
                ?.publication
                ?.artifacts
                ?.joinToString { it.file.name }
                ?: "none"

            logger.lifecycle(
                "Preparing to publish publication '$publicationName' to OSSRH repository '${repository.name}' at $repoUrl"
            )
            logger.lifecycle("Using header '$headerName' with value: $maskedHeaderValue")
            logger.lifecycle("Artifacts: $artifactList")
        }
        doLast {
            val publicationName = (this as? PublishToMavenRepository)?.publication?.name ?: "unknown"
            val result = if (state.failure == null) "succeeded" else "failed: ${state.failure?.message}"
            logger.lifecycle(
                "Finished publishing publication '$publicationName' to '${repository.name}' - $result"
            )
        }
    }
}
