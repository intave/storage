package de.jpx3.intavestorage

import org.bukkit.configuration.ConfigurationSection

/**
 * Service provider of TODO.
 */
class PostgreSqlStorageGatewayFactory : StorageGatewayFactory {
    override fun storageGatewayFor(
        storageType: String
    ): ((ConfigurationSection) -> ExpiringStorageGateway)? {
        return storageType.takeIf { it == "POSTGRESQL" }?.let {
            { PostgreSqlStorageGateway(postgreSqlConfiguration(it)) }
        }
    }
}
