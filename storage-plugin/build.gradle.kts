plugins {
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

bukkit {
    name = "IntaveStorage"
    main = "de.jpx3.intavestorage.BukkitEntrypoint"
    version = "${rootProject.version}"

    softDepend = listOf("Intave", "IntaveBootstrap")
}

tasks {
    shadowJar {
        val classifier = "file"
        archiveFileName.set("${rootProject.name}-${rootProject.version}-$classifier.jar")
        archiveClassifier.set(classifier)
    }
}
