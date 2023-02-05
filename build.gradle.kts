plugins {
    kotlin("jvm") version "1.8.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "de.jpx3"
version = "0.4.1"

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.github.johnrengelman.shadow")

    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    dependencies {
        compileOnly("org.spigotmc:spigot-api:1.19.3-R0.1-SNAPSHOT")
        compileOnly(fileTree("${rootProject.projectDir}/libs/") { include("*.jar") })
    }

    tasks {
        build {
            dependsOn(shadowJar)
        }

        shadowJar {
            mergeServiceFiles()
            destinationDirectory.set(rootProject.buildDir.resolve("libs"))
        }
    }
}

dependencies {
    implementation(project(":storage-mariadb"))
    implementation(project(":storage-mongodb"))
    implementation(project(":storage-mysql"))
    implementation(project(":storage-plugin"))
    implementation(project(":storage-postgresql"))
}

tasks {
    shadowJar {
        val classifier = "all"
        archiveFileName.set("${rootProject.name}-${rootProject.version}-$classifier.jar")
        archiveClassifier.set(classifier)
    }
}
