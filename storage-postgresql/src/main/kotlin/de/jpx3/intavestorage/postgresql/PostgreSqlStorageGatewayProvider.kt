package de.jpx3.intavestorage.postgresql

import de.jpx3.intavestorage.ExpiringStorageGateway
import de.jpx3.intavestorage.StorageGatewayProvider
import org.bukkit.configuration.ConfigurationSection

/**
 * Service provider of [PostgreSqlStorageGateway].
 */
class PostgreSqlStorageGatewayProvider : StorageGatewayProvider {
    override fun storageGatewayFor(
        storageType: String
    ): ((ConfigurationSection) -> ExpiringStorageGateway)? {
        return storageType.takeIf { it == "POSTGRESQL" }?.let {
            { PostgreSqlStorageGateway(postgreSqlConfiguration(it)) }
        }
    }
}
