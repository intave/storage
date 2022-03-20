package de.jpx3.intavestorage.storage

import de.jpx3.intave.access.player.storage.EmptyStorageGateway
import de.jpx3.intavestorage.storage.database.MySqlStorage
import de.jpx3.intavestorage.storage.database.PostgreSqlStorage
import org.bukkit.configuration.ConfigurationSection

val fallbackStorageGateway by lazy(::EmptyStorageGateway)

enum class ConfigurableStorageType(
    private val resolve: (ConfigurationSection) -> ExpiringStorageGateway
) {
    FILE(::FileStorage),
    POSTGRESQL(::PostgreSqlStorage),
    MYSQL(::MySqlStorage);

    fun storageGatewayFrom(configuration: ConfigurationSection): ExpiringStorageGateway {
        return resolve(configuration)
    }
}
