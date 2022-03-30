package de.jpx3.intavestorage

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.plugin.java.JavaPlugin

class BukkitEntrypoint : JavaPlugin() {
    override fun onEnable() {
        saveDefaultConfig()
        enableIntaveStorage()
    }

    /**
     * Enables [IntaveStorage] after reading the plugin configuration.
     */
    private fun enableIntaveStorage() {
        val storageType = storageType()
        val config = configurationSectionFor(storageType)
        val intaveStorage = IntaveStorage(this.classLoader)
        if (config != null) {
            intaveStorage.enable(storageType, config)
        } else {
            intaveStorage.enable(storageType)
        }
    }

    /**
     * Resolves the currently activated storage type from the plugin
     * configuration.
     *
     * @return The storage type.
     */
    private fun storageType(): String {
        return config.getString("storage-type") ?: return "NONE"
    }

    /**
     * Resolves the configuration section for a storage type from the plugin
     * configuration.
     *
     * @param storageType The storage type.
     * @return The configuration section, if present.
     */
    private fun configurationSectionFor(
        storageType: String
    ): ConfigurationSection? {
        return config.getConfigurationSection(storageType.lowercase())
    }
}

// TODO: expire storage data
//            storageType.storageGatewayFrom(storageConfig).apply {
//                val expiration = getLong("expire")
//                if (expiration >= 1) {
//                    clearEntriesOlderThan(expiration, TimeUnit.DAYS)
//                }
//            }
