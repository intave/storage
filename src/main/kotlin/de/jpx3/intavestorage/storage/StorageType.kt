package de.jpx3.intavestorage.storage

import de.jpx3.intave.access.player.storage.EmptyStorageGateway
import de.jpx3.intave.access.player.storage.StorageGateway
import de.jpx3.intavestorage.storage.database.PostgreSqlStorage
import org.bukkit.configuration.ConfigurationSection

enum class StorageType(
    private val resolve: (ConfigurationSection) -> StorageGateway
) {
    NONE({ EmptyStorageGateway() }),
    FILE({ config -> FileStorage(config) }),
    POSTGRESQL({ config -> PostgreSqlStorage(config) });

    companion object {
        fun default(): StorageType = NONE
    }

    fun storageGatewayFrom(configuration: ConfigurationSection): StorageGateway {
        return resolve(configuration)
    }
}
