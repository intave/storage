package de.jpx3.intavestorage.bukkit

import de.jpx3.intave.access.player.storage.StorageGateway
import de.jpx3.intavestorage.IntaveStorage
import de.jpx3.intavestorage.storage.StorageType
import org.bukkit.plugin.java.JavaPlugin

class BukkitPlugin : JavaPlugin() {
    private var storage: IntaveStorage = IntaveStorage()

    override fun onEnable() {
        saveDefaultConfig()
        storage.enable(storageGateway())
    }

    private fun storageGateway(): StorageGateway {
        return with(config) {
            val storageTypeName = getString("storage-type") ?: StorageType.default().toString()
            val storageType = StorageType.valueOf(storageTypeName)
            val storageConfigName = storageTypeName.lowercase()
            val storageConfig = getConfigurationSection(storageConfigName)
                ?: error("Storage configuration for $storageTypeName not found! (section $storageConfigName missing)")
            storageType.storageGatewayFrom(storageConfig)
        }
    }

    override fun onDisable() {
        storage.disable()
    }
}
