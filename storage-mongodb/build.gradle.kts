plugins {
    kotlin("jvm") version "1.6.10"
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.18-R0.1-SNAPSHOT")
    compileOnly(fileTree("../libs/") { include("*.jar") })
    implementation(project(":storage-plugin"))
    implementation("org.mongodb:mongodb-driver-sync:4.5.0")
}
