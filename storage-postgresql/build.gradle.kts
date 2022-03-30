dependencies {
    implementation(project(":storage-plugin"))
    implementation("org.postgresql:postgresql:42.3.3")
}

tasks {
    shadowJar {
        val classifier = "postgresql"
        archiveFileName.set("${rootProject.name}-${rootProject.version}-$classifier.jar")
        archiveClassifier.set(classifier)
    }
}
