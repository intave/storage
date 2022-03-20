package de.jpx3.intavestorage.bukkit

import de.jpx3.intave.access.player.storage.StorageGateway
import de.jpx3.intavestorage.IntaveStorage
import de.jpx3.intavestorage.storage.ConfigurableStorageType
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.TimeUnit

class BukkitPlugin : JavaPlugin() {
    private var storage: IntaveStorage = IntaveStorage()

    override fun onEnable() {
        saveDefaultConfig()
        val storageGateway = storageGateway()
        storage.enable(storageGateway)
    }

    private fun storageGateway(): StorageGateway {
        return with(config) {
            val storageTypeName = getString("storage-type")
            if (storageTypeName == null || storageTypeName == "NONE") {
                return ConfigurableStorageType.fallback()
            }
            val storageType = ConfigurableStorageType.valueOf(storageTypeName)
            val storageConfigName = storageTypeName.lowercase()
            val storageConfig = getConfigurationSection(storageConfigName)
                ?: error("Storage configuration for $storageTypeName not found! (section $storageConfigName missing)")
            val storageGateway = storageType.storageGatewayFrom(storageConfig)
            storageGateway.also { it.clearEntriesOlderThan(getLong("expire"), TimeUnit.DAYS) }
        }
    }

    override fun onDisable() {
        storage.disable()
    }
}
