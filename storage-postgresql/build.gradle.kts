dependencies {
    implementation(project(":storage-plugin"))
    implementation("org.postgresql:postgresql:42.3.3")
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}
