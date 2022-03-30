plugins {
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "de.jpx3"
version = "0.3.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":storage-mariadb"))
    implementation(project(":storage-mongodb"))
    implementation(project(":storage-mysql"))
    implementation(project(":storage-plugin"))
    implementation(project(":storage-postgresql"))
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}
