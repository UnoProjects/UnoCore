plugins {
    `java-library`
    id("com.gradleup.shadow")
}

dependencies {
    implementation(project(":api"))

    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    processResources {
        val props = mapOf("version" to version)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    jar {
        enabled = false
    }

    shadowJar {
        archiveBaseName.set("UnoCore")
        archiveClassifier.set("")
    }
}
