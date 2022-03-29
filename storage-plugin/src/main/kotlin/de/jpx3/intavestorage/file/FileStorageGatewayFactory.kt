package de.jpx3.intavestorage.file

import de.jpx3.intavestorage.ExpiringStorageGateway
import de.jpx3.intavestorage.StorageGatewayFactory
import org.bukkit.configuration.ConfigurationSection

class FileStorageGatewayFactory : StorageGatewayFactory {
    override fun storageGatewayFor(storageType: String): ((ConfigurationSection) -> ExpiringStorageGateway)? {
        return storageType.takeIf { it == "FILE" }?.let {
            { FileStorageGateway() }
        }
    }
}
