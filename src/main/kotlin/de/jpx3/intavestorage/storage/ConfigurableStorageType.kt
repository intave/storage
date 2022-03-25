package de.jpx3.intavestorage.storage

import de.jpx3.intave.access.player.storage.EmptyStorageGateway
import de.jpx3.intavestorage.storage.sql.MariaDbStorage
import de.jpx3.intavestorage.storage.sql.MySqlStorage
import de.jpx3.intavestorage.storage.sql.PostgreSqlStorage
import org.bukkit.configuration.ConfigurationSection

val fallbackStorageGateway by lazy(::EmptyStorageGateway)

enum class ConfigurableStorageType(
    private val resolve: (ConfigurationSection) -> ExpiringStorageGateway
) {
    FILE(::FileStorage),
    POSTGRESQL(::PostgreSqlStorage),
    MYSQL(::MySqlStorage),
    MARIADB(::MariaDbStorage),
    MONGODB(::MongoDbStorage);

    fun storageGatewayFrom(configuration: ConfigurationSection): ExpiringStorageGateway {
        return resolve(configuration)
    }
}
