package de.jpx3.intavestorage.mysql

import de.jpx3.intavestorage.string
import org.bukkit.configuration.ConfigurationSection

/**
 * Configuration for the [MySqlStorageGateway].
 *
 * @property uri The URI to connect to.
 * @property user The username to connect with.
 * @property password The password to connect with.
 * @property expirationThreshold After how many days the data expires.
 */
data class MySqlConfiguration(
    val uri: String,
    val user: String,
    val password: String,
    val expirationThreshold: Long
)

/**
 * Creates a [MySqlStorageGateway] from a [ConfigurationSection].
 *
 * @return The [MySqlStorageGateway].
 */
fun ConfigurationSection.mySqlConfiguration(): MySqlConfiguration {
    return MySqlConfiguration(
        "jdbc:mysql://${string("host")}/${string("database")}",
        string("user"),
        string("password"),
        getLong("expire")
    )
}
