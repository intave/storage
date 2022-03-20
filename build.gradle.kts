plugins {
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "de.jpx3"
version = "0.1.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.18-R0.1-SNAPSHOT")
    compileOnly(fileTree("libs/") { include("*.jar") })
    implementation("org.postgresql:postgresql:42.3.3")
    implementation("mysql:mysql-connector-java:8.0.28")
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}
