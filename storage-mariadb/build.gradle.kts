dependencies {
    implementation(project(":storage-plugin"))
    implementation("org.mariadb.jdbc:mariadb-java-client:3.0.3")
}

tasks {
    shadowJar {
        val classifier = "mariadb"
        archiveFileName.set("${rootProject.name}-${rootProject.version}-$classifier.jar")
        archiveClassifier.set(classifier)
    }
}
