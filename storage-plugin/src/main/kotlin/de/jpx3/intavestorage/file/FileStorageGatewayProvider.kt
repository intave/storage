package de.jpx3.intavestorage.file

import de.jpx3.intavestorage.ExpiringStorageGateway
import de.jpx3.intavestorage.StorageGatewayProvider
import org.bukkit.configuration.ConfigurationSection

/**
 * Service provider for [FileStorageGateway].
 */
class FileStorageGatewayProvider : StorageGatewayProvider {
    override fun storageGatewayFor(storageType: String): ((ConfigurationSection) -> ExpiringStorageGateway)? {
        return storageType.takeIf { it == "FILE" }?.let {
            { FileStorageGateway(fileConfiguration(it)) }
        }
    }
}
