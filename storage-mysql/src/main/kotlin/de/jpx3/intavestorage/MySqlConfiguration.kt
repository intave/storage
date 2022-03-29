package de.jpx3.intavestorage

import org.bukkit.configuration.ConfigurationSection

data class MySqlConfiguration(val url: String, val user: String, val password: String)

fun mySqlConfiguration(config: ConfigurationSection): MySqlConfiguration {
    return with(config) {
        MySqlConfiguration(
            "jdbc:mysql://${string("host")}/${string("database")}",
            string("user"),
            string("password")
        )
    }
}

fun ConfigurationSection.string(path: String): String {
    return this.getString(path) ?: error("")
}
