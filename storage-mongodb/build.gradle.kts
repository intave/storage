dependencies {
    implementation(project(":storage-plugin"))
    implementation("org.mongodb:mongodb-driver-sync:4.5.0")
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}
