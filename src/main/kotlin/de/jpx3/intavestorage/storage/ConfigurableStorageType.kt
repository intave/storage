package de.jpx3.intavestorage.storage

import de.jpx3.intave.access.player.storage.EmptyStorageGateway
import de.jpx3.intave.access.player.storage.StorageGateway
import de.jpx3.intavestorage.storage.database.MySqlStorage
import de.jpx3.intavestorage.storage.database.PostgreSqlStorage
import org.bukkit.configuration.ConfigurationSection

enum class ConfigurableStorageType(
    private val resolve: (ConfigurationSection) -> ExpiringStorageGateway
) {
    FILE({ config -> FileStorage(config) }),
    POSTGRESQL({ config -> PostgreSqlStorage(config) }),
    MYSQL({ config -> MySqlStorage(config) });

    companion object {
        fun fallback(): StorageGateway {
            return EmptyStorageGateway()
        }
    }

    fun storageGatewayFrom(configuration: ConfigurationSection): ExpiringStorageGateway {
        return resolve(configuration)
    }
}
