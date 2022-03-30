dependencies {
    implementation(project(":storage-plugin"))
    implementation("org.mongodb:mongodb-driver-sync:4.5.0")
}

tasks {
    shadowJar {
        val classifier = "mongodb"
        archiveFileName.set("${rootProject.name}-${rootProject.version}-$classifier.jar")
        archiveClassifier.set(classifier)
    }
}
