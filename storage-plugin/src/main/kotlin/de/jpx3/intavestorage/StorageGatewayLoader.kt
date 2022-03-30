package de.jpx3.intavestorage

import java.util.ServiceLoader

/**
 * Resolves the [ServiceLoader] for [StorageGatewayProvider]s.
 *
 * @param classLoader The [ClassLoader] the [ServiceLoader] should be
 * instantiated with. In the context of a Bukkit plugin, this should be the
 * Bukkit classloader.
 * @return The [ServiceLoader].
 */
fun storageGatewayProviderLoader(
    classLoader: ClassLoader
): ServiceLoader<StorageGatewayProvider> {
    return ServiceLoader.load(StorageGatewayProvider::class.java, classLoader)
}
