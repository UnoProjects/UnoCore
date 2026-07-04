plugins {
    `java-library`
    id("com.gradleup.shadow")
}

dependencies {
    implementation(project(":api"))

    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
    compileOnly("dev.jorel:commandapi-paper-core:11.2.0")

    implementation("com.zaxxer:HikariCP:7.1.0")
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
        relocate("com.zaxxer.hikari", "me.unoprojects.unocore.libs.hikari")
        archiveClassifier.set("")
    }
}
