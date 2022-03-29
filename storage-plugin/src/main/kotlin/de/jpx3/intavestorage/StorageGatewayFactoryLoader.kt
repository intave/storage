package de.jpx3.intavestorage

import java.util.ServiceLoader

/**
 * [StorageGatewayFactory] service loader.
 */
val storageGatewayFactoryLoader: ServiceLoader<StorageGatewayFactory> by lazy {
    ServiceLoader.load(StorageGatewayFactory::class.java)
}
