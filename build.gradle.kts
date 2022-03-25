plugins {
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

group = "de.jpx3"
version = "0.2.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.18-R0.1-SNAPSHOT")
    compileOnly(fileTree("libs/") { include("*.jar") })
    implementation("org.postgresql:postgresql:42.3.3")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.0.3")
    implementation("mysql:mysql-connector-java:8.0.28")
    implementation("org.mongodb:mongodb-driver-sync:4.5.0")
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}

bukkit {
    name = "IntaveStorage"
    main = "de.jpx3.intavestorage.bukkit.BukkitPlugin"
    version = "${rootProject.version}"

    depend = listOf("Intave")
}
