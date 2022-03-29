package de.jpx3.intavestorage

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.plugin.java.JavaPlugin

class BukkitEntrypoint : JavaPlugin() {
    override fun onEnable() {
        saveDefaultConfig()
        val storageType = storageType()
        val config = storageGatewayConfigurationSection(storageType)
        IntaveStorage.enable(storageType, config, this.classLoader)
    }

    private fun storageType(): String {
        return config.getString("storage-type") ?: return "NONE"
    }

    private fun storageGatewayConfigurationSection(storageType: String): ConfigurationSection {
        return with(config) {
            val configSectionName = storageType.lowercase()
            getConfigurationSection(storageType.lowercase())
                ?: error("Storage configuration for $storageType not found! (section $configSectionName missing)")
        }
    }
}

// TODO: expire storage data
//            storageType.storageGatewayFrom(storageConfig).apply {
//                val expiration = getLong("expire")
//                if (expiration >= 1) {
//                    clearEntriesOlderThan(expiration, TimeUnit.DAYS)
//                }
//            }

// TODO: fix NONE (config-less impls)
