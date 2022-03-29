package de.jpx3.intavestorage

import org.bukkit.configuration.ConfigurationSection

data class MongoDbConfiguration(
    val host: String,
    val database: String,
    val authorization: Boolean,
    val user: String?,
    val password: String?,
    val defaultDb: String?
)

fun mongoDbConfiguration(config: ConfigurationSection): MongoDbConfiguration {
    return with(config) {
        MongoDbConfiguration(
            string("host"),
            string("database"),
            getBoolean("authorization"),
            getString("user"),
            getString("password"),
            getString("defaultdb"),
        )
    }
}

fun ConfigurationSection.string(path: String): String {
    return this.getString(path) ?: error("")
}
