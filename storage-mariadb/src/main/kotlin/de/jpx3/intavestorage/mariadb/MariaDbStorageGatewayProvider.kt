package de.jpx3.intavestorage.mariadb

import de.jpx3.intavestorage.ExpiringStorageGateway
import de.jpx3.intavestorage.StorageGatewayProvider
import org.bukkit.configuration.ConfigurationSection

/**
 * Service provider of [MariaDbStorageGateway].
 */
class MariaDbStorageGatewayProvider : StorageGatewayProvider {
    override fun storageGatewayFor(
        storageType: String
    ): ((ConfigurationSection) -> ExpiringStorageGateway)? {
        return storageType.takeIf { it == "MARIADB" }?.let {
            { MariaDbStorageGateway(mariaDbConfiguration(it)) }
        }
    }
}
