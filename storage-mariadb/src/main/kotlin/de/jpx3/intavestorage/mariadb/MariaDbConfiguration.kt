package de.jpx3.intavestorage.mariadb

import org.bukkit.configuration.ConfigurationSection

data class MariaDbConfiguration(val url: String, val user: String, val password: String)

fun mariaDbConfiguration(config: ConfigurationSection): MariaDbConfiguration {
    return with(config) {
        MariaDbConfiguration(
            "jdbc:mariadb://${string("host")}/${string("database")}",
            string("user"),
            string("password")
        )
    }
}

fun ConfigurationSection.string(path: String): String {
    return this.getString(path) ?: error("")
}
