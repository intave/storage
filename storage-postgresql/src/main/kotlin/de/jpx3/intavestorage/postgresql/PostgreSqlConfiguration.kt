package de.jpx3.intavestorage.postgresql

import org.bukkit.configuration.ConfigurationSection

data class PostgreSqlConfiguration(val url: String, val user: String, val password: String)

fun postgreSqlConfiguration(config: ConfigurationSection): PostgreSqlConfiguration {
    return with(config) {
        PostgreSqlConfiguration(
            "jdbc:postgresql://${string("host")}/${string("database")}",
            string("user"),
            string("password")
        )
    }
}

fun ConfigurationSection.string(path: String): String {
    return this.getString(path) ?: error("")
}
