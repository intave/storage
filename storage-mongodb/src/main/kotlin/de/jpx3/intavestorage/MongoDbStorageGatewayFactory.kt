package de.jpx3.intavestorage

import org.bukkit.configuration.ConfigurationSection

/**
 * Service provider of TODO.
 */
class MongoDbStorageGatewayFactory : StorageGatewayFactory {
    override fun storageGatewayFor(
        storageType: String
    ): ((ConfigurationSection) -> ExpiringStorageGateway)? {
        return storageType.takeIf { it == "MONGODB" }?.let {
            { MongoDbStorageGateway(mongoDbConfiguration(it)) }
        }
    }
}
