package de.jpx3.intavestorage.mariadb

import de.jpx3.intavestorage.string
import org.bukkit.configuration.ConfigurationSection

/**
 * Configuration for the [MariaDbStorageGateway].
 *
 * @property uri The URI to connect to.
 * @property user The username to connect with.
 * @property password The password to connect with.
 * @property expirationThreshold After how many days the data expires.
 */
data class MariaDbConfiguration(
    val uri: String,
    val user: String,
    val password: String,
    val expirationThreshold: Long
)

/**
 * Creates a [MariaDbConfiguration] from a [ConfigurationSection].
 *
 * @param config Section from the Bukkit configuration corresponding to the
 * [MariaDbConfiguration].
 * @return The [MariaDbConfiguration].
 */
fun mariaDbConfiguration(config: ConfigurationSection): MariaDbConfiguration {
    return with(config) {
        MariaDbConfiguration(
            "jdbc:mariadb://${string("host")}/${string("database")}",
            string("user"),
            string("password"),
            getLong("expire")
        )
    }
}
