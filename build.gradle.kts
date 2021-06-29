plugins {
    kotlin("jvm") version "1.5.20"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

repositories {
    mavenCentral() // 코드 한줄을 줄여주셨습니다 각별님 감사합니다
    maven("https://papermc.io/repo/repository/maven-public/") // PaperMC
}

dependencies {
    compileOnly(kotlin("stdlib")) // Kotlin
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT") // Paper Latest
    implementation("io.github.monun:kommand:1.2.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "8" // 응 8로 내릴거야
    }
    processResources {
        filesMatching("**/*.yml") {
            expand(project.properties)
        }
        filteringCharset = "UTF-8"
    }
    shadowJar {
        archiveClassifier.set("dist")
        archiveVersion.set("")
    }
    create<Copy>("dist") {
        from (shadowJar)
        into(".\\.server\\plugins")
    }
}
