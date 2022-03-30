package de.jpx3.intavestorage.file

import org.bukkit.configuration.ConfigurationSection

data class FileConfiguration(val expire: Long)

fun fileConfiguration(config: ConfigurationSection): FileConfiguration {
    return with(config) {
        FileConfiguration(
            getLong("expire")
        )
    }
}
