package de.jpx3.intavestorage.file

import org.bukkit.configuration.ConfigurationSection

data class FileConfiguration(val expire: Long)

fun ConfigurationSection.fileConfiguration(): FileConfiguration {
    return FileConfiguration(getLong("expire"))
}
