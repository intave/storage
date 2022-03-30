package de.jpx3.intavestorage.mongodb

import de.jpx3.intavestorage.string
import org.bukkit.configuration.ConfigurationSection

/**
 * Configuration for the [MongoDbConfiguration]
 *
 * @property host The host to connect to.
 * @property database The database to connect to.
 * @property authorization Whether authorization is required.
 * @property user The username to connect with, if authorization is required.
 * @property password The password to connect with, if authorization is
 * required.
 * @property defaultDb The default database of a user, if authorization is
 * required.
 * @property expirationThreshold After how many days the data expires.
 */
data class MongoDbConfiguration(
    val host: String,
    val database: String,
    val authorization: Boolean,
    val user: String?,
    val password: String?,
    val defaultDb: String?,
    val expirationThreshold: Long
)

/**
 * Creates a [MongoDbConfiguration] from a [ConfigurationSection].
 *
 * @param config Section from the Bukkit configuration corresponding to the
 * [MongoDbConfiguration].
 * @return The [MongoDbConfiguration].
 */
fun mongoDbConfiguration(config: ConfigurationSection): MongoDbConfiguration {
    return with(config) {
        MongoDbConfiguration(
            string("host"),
            string("database"),
            getBoolean("authorization"),
            getString("user"),
            getString("password"),
            getString("defaultdb"),
            getLong("expire")
        )
    }
}
