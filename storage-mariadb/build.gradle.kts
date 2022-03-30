dependencies {
    implementation(project(":storage-plugin"))
    implementation("org.mariadb.jdbc:mariadb-java-client:3.0.3")
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}
