plugins {
    kotlin("jvm") version "1.5.21"
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/") // PaperMC
}

dependencies {
    compileOnly("io.github.monun:kommand-api:2.2.0")
    compileOnly("io.github.monun:tap-api:4.1.1")
    compileOnly(kotlin("stdlib")) // Kotlin
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT") // Paper Latest
}

tasks {
    jar {
        archiveClassifier.set("dist")
        archiveVersion.set("")
    }
    processResources {
        filesMatching("**/*.yml") {
            expand(project.properties)
        }
        filteringCharset = "UTF-8"
    }
    create<Copy>("dist") {
        from (jar)
        into(".\\.server\\plugins")
    }
}