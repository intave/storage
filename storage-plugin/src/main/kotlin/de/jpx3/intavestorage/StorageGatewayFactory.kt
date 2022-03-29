package de.jpx3.intavestorage

import org.bukkit.configuration.ConfigurationSection

/**
 * Interface for service providers of [ExpiringStorageGateway]s.
 */
fun interface StorageGatewayFactory {
    /**
     * Provides an [ExpiringStorageGateway] for a specific type.
     *
     * @param storageType The storage type the [ExpiringStorageGateway] must
     * implement.
     * @return An [ExpiringStorageGateway] if the service provider is able to
     * provide an implementation for the storage type, otherwise null.
     */
    fun storageGatewayFor(
        storageType: String
    ): ((ConfigurationSection) -> ExpiringStorageGateway)?
}
