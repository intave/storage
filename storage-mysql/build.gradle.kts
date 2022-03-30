dependencies {
    implementation(project(":storage-plugin"))
    implementation("mysql:mysql-connector-java:8.0.28")
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}
