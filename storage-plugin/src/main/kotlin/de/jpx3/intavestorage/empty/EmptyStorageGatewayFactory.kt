package de.jpx3.intavestorage.empty

import de.jpx3.intavestorage.ExpiringStorageGateway
import de.jpx3.intavestorage.StorageGatewayFactory
import org.bukkit.configuration.ConfigurationSection

class EmptyStorageGatewayFactory : StorageGatewayFactory {
    override fun storageGatewayFor(storageType: String): ((ConfigurationSection) -> ExpiringStorageGateway)? {
        return storageType.takeIf { it == "NONE" }?.let {
            { EmptyStorageGateway() }
        }
    }
}
