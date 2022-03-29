package de.jpx3.intavestorage

import de.jpx3.intave.IntaveAccessor
import org.bukkit.configuration.ConfigurationSection
import java.util.ServiceLoader

object IntaveStorage {
    fun enable(storageType: String, config: ConfigurationSection, classloader: ClassLoader) {
        val storageGateway = findStorageGatewayFor(storageType, classloader)
        IntaveAccessor.unsafeAccess().setStorageGateway(storageGateway(config))
    }

    private fun findStorageGatewayFor(
        storageType: String,
        classloader: ClassLoader
    ): (ConfigurationSection) -> ExpiringStorageGateway {
        val storageGatewayFactoryLoader = ServiceLoader.load(StorageGatewayFactory::class.java, classloader)
        return storageGatewayFactoryLoader.firstNotNullOfOrNull {
            it.storageGatewayFor(storageType)
        } ?: error("no impl")
    }
}
