package de.jpx3.intavestorage.mongodb

import de.jpx3.intavestorage.ExpiringStorageGateway
import de.jpx3.intavestorage.StorageGatewayProvider
import org.bukkit.configuration.ConfigurationSection

/**
 * Service provider of TODO.
 */
class MongoDbStorageGatewayProvider : StorageGatewayProvider {
    override fun storageGatewayFor(
        storageType: String
    ): ((ConfigurationSection) -> ExpiringStorageGateway)? {
        return storageType.takeIf { it == "MONGODB" }?.let {
            { MongoDbStorageGateway(mongoDbConfiguration(it)) }
        }
    }
}
