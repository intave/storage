import org.jetbrains.kotlin.backend.common.push

plugins {
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
    id("com.github.harbby.gradle.serviceloader") version "1.1.8"
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.18-R0.1-SNAPSHOT")
    compileOnly(fileTree("../libs/") { include("*.jar") })
}

bukkit {
    name = "IntaveStorage"
    main = "de.jpx3.intavestorage.BukkitEntrypoint"
    version = "${rootProject.version}"

    depend = listOf("Intave")
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}

serviceLoader {
    serviceInterfaces.push("de.jpx3.intavestorage.StorageGatewayProvider")
}
