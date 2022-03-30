package de.jpx3.intavestorage

import org.bukkit.configuration.ConfigurationSection

fun ConfigurationSection.string(path: String): String {
    return this.getString(path) ?: error("")
}
