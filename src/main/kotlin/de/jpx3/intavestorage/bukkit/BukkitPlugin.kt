package de.jpx3.intavestorage.bukkit

import de.jpx3.intave.access.player.storage.StorageGateway
import de.jpx3.intavestorage.IntaveStorage
import de.jpx3.intavestorage.storage.StorageOption
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class BukkitPlugin : JavaPlugin() {
  private var storage: IntaveStorage = IntaveStorage()

  override fun onEnable() {
    var storageOption = storageOption()
    storage.enable(storageOption)
  }

  private fun storageOption(): StorageGateway {
    createDataFolder()

    val configFile = File(dataFolder, "config.yml")
    if (!configFile.exists()) {
      saveResource("config.yml", false)
    }
    val configuration = YamlConfiguration()
    configuration.load(configFile)

    val storageOptionName = configuration.getString("storageOption", "NONE")
    val storageOption = StorageOption.valueOf(storageOptionName?.uppercase() ?: "NONE")
    val configurationSection = configuration.getConfigurationSection("${storageOptionName?.lowercase()}")!!

    return storageOption.storageGatewayFrom(configurationSection)
  }

  fun createDataFolder() {
    val dataFolder: File = dataFolder
    if (!(dataFolder.exists() || dataFolder.mkdirs())) {
      error("Failed to create datafolder")
    }
  }

  override fun onDisable() {
    storage.disable()
  }
}