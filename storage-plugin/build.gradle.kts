plugins {
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

bukkit {
    name = "IntaveStorage"
    main = "de.jpx3.intavestorage.BukkitEntrypoint"
    version = "${rootProject.version}"

    depend = listOf("Intave")
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        mergeServiceFiles()
    }
}
