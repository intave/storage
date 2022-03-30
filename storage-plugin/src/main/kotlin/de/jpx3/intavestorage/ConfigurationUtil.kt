package de.jpx3.intavestorage

import org.bukkit.configuration.ConfigurationSection

/**
 * Gets the requested String by path.
 *
 * @param path Path of the String to get.
 * @return Requested String.
 */
fun ConfigurationSection.string(path: String): String {
    return this.getString(path) ?: error(
        "String property '$path' not found! Broken configuration file?"
    )
}
