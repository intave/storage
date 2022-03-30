package de.jpx3.intavestorage.mysql

import de.jpx3.intavestorage.ExpiringStorageGateway
import de.jpx3.intavestorage.StorageGatewayProvider
import org.bukkit.configuration.ConfigurationSection

/**
 * Service provider of [MySqlStorageGateway].
 */
class MySqlStorageGatewayProvider : StorageGatewayProvider {
    override fun storageGatewayFor(
        storageType: String
    ): ((ConfigurationSection) -> ExpiringStorageGateway)? {
        return storageType.takeIf { it == "MYSQL" }?.let {
            { MySqlStorageGateway(mySqlConfiguration(it)) }
        }
    }
}
