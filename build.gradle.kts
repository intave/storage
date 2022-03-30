plugins {
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "de.jpx3"
version = "0.3.0"

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.github.johnrengelman.shadow")

    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    dependencies {
        compileOnly("org.spigotmc:spigot-api:1.18-R0.1-SNAPSHOT")
        compileOnly(fileTree("${rootProject.projectDir}/libs/") { include("*.jar") })
    }

    tasks {
        build {
            dependsOn(shadowJar)
        }

        shadowJar {
            mergeServiceFiles()
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
