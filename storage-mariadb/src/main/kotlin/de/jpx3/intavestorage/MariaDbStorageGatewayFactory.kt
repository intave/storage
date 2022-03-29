package de.jpx3.intavestorage

import org.bukkit.configuration.ConfigurationSection

/**
 * Service provider of [MariaDbStorageGateway].
 */
class MariaDbStorageGatewayFactory : StorageGatewayFactory {
    override fun storageGatewayFor(
        storageType: String
    ): ((ConfigurationSection) -> ExpiringStorageGateway)? {
        return storageType.takeIf { it == "MARIADB" }?.let {
            { MariaDbStorageGateway(mariaDbConfiguration(it)) }
        }
    }
}
