dependencies {
    implementation(project(":storage-plugin"))
    implementation("mysql:mysql-connector-java:8.0.28")
}

tasks {
    shadowJar {
        val classifier = "mysql"
        archiveFileName.set("${rootProject.name}-${rootProject.version}-$classifier.jar")
        archiveClassifier.set(classifier)
    }
}
