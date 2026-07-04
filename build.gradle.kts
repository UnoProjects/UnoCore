plugins {
    id("com.gradleup.shadow") version "9.4.2" apply false
}

allprojects {
    group = "me.unoprojects.unocore"
    version = "1.0.0"

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.codemc.io/repository/maven-releases/")
        maven("https://repo.codemc.io/repository/maven-snapshots/")
        maven("https://maven.enginehub.org/repo/")
    }
}
