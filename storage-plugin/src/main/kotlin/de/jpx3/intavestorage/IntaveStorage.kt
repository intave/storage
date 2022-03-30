package de.jpx3.intavestorage

import de.jpx3.intave.IntaveAccessor
import de.jpx3.intave.access.IntaveAccess
import de.jpx3.intave.access.player.storage.EmptyStorageGateway
import org.bukkit.configuration.ConfigurationSection
import java.util.Locale

class IntaveStorage(private val classLoader: ClassLoader) {
    /**
     * Enables the Intave Storage system with a not configurable storage gateway
     * corresponding to the storage type passed. Note that the only storage type
     * matching that is "NONE".
     *
     * @param storageType The storage type the [ExpiringStorageGateway] will
     * communicate with.
     */
    fun enable(storageType: String) {
        storageType.takeIf { it == "NONE" }?.let {
            intave().setStorageGateway(EmptyStorageGateway())
        } ?: run {
            val configSectionName = storageType.lowercase(Locale.getDefault())
            error("Configuration section '$configSectionName' not found!")
        }
    }

    /**
     * Enables the Intave Storage system with a configurable storage gateway
     * corresponding to the storage type passed.
     *
     * @param storageType The storage type the [ExpiringStorageGateway] will
     * communicate with.
     * @param config
     */
    fun enable(storageType: String, config: ConfigurationSection) {
        val storageGatewayProvider = storageGatewayProviderFor(storageType)
        val storageGateway = storageGatewayProvider(config)
        intave().setStorageGateway(storageGateway)
    }

    /**
     * Finds the storage gateway provider corresponding to the storage type
     * passed.
     *
     * @param storageType The storage type the [ExpiringStorageGateway] will
     * communicate with.
     * @return A function that will return an [ExpiringStorageGateway] if passed
     * a [ConfigurationSection]
     */
    private fun storageGatewayProviderFor(
        storageType: String
    ): (ConfigurationSection) -> ExpiringStorageGateway {
        val serviceLoader = storageGatewayProviderLoader(classLoader)
        return serviceLoader.firstNotNullOfOrNull {
            it.storageGatewayFor(storageType)
        } ?: error("Storage gateway provider for '$storageType' not found!")
    }

    /**
     * Allows for interaction with Intave, if Intave has booted properly.
     *
     * @return A reference to [IntaveAccess], if Intave has loaded.
     */
    private fun intave(): IntaveAccess {
        return IntaveAccessor.unsafeAccess()
    }
}
