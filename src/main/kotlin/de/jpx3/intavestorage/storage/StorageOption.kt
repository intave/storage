package de.jpx3.intavestorage.storage

import de.jpx3.intave.access.player.storage.EmptyStorageGateway
import de.jpx3.intave.access.player.storage.StorageGateway
import org.bukkit.configuration.ConfigurationSection

enum class StorageOption(
    private val resolve: (ConfigurationSection) -> StorageGateway
) {
    NONE({ EmptyStorageGateway() }),
    FILE({ config -> FileStorage(config) })

    ;

    fun storageGatewayFrom(configuration: ConfigurationSection): StorageGateway {
        return resolve(configuration)
    }
}
