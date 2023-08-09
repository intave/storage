package de.jpx3.intavestorage.postgresql

import de.jpx3.intavestorage.string
import org.bukkit.configuration.ConfigurationSection

/**
 * Configuration for the [PostgreSqlStorageGateway].
 *
 * @property uri The URI to connect to.
 * @property user The username to connect with.
 * @property password The password to connect with.
 * @property expirationThreshold After how many days the data expires.
 */
data class PostgreSqlConfiguration(
    val uri: String,
    val user: String,
    val password: String,
    val expirationThreshold: Long,
    val schema: String
)

/**
 * Creates a [PostgreSqlConfiguration] from a [ConfigurationSection].
 *
 * @return The [PostgreSqlConfiguration].
 */
fun ConfigurationSection.postgreSqlConfiguration(): PostgreSqlConfiguration {
    return PostgreSqlConfiguration(
        "jdbc:postgresql://${string("host")}/${string("database")}",
        string("user"),
        string("password"),
        getLong("expire"),
        string("schema")
    )
}
