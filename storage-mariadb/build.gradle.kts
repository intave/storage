plugins {
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.18-R0.1-SNAPSHOT")
    compileOnly(fileTree("../libs/") { include("*.jar") })
    implementation("org.mariadb.jdbc:mariadb-java-client:3.0.3")
    implementation(project(":storage-plugin"))
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}
