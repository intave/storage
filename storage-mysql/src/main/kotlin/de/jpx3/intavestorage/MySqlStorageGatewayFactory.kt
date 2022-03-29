package de.jpx3.intavestorage

import org.bukkit.configuration.ConfigurationSection

/**
 * Service provider of [MySqlStorageGateway].
 */
class MySqlStorageGatewayFactory : StorageGatewayFactory {
    override fun storageGatewayFor(
        storageType: String
    ): ((ConfigurationSection) -> ExpiringStorageGateway)? {
        return storageType.takeIf { it == "MYSQL" }?.let {
            { MySqlStorageGateway(mySqlConfiguration(it)) }
        }
    }
}
