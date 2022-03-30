package de.jpx3.intavestorage

import de.jpx3.intave.access.player.storage.StorageGateway

/**
 * [StorageGateway] that is able to additionally handle expired data.
 */
interface ExpiringStorageGateway : StorageGateway {
    /**
     * Clears expired data.
     */
    fun clearOldEntries()
}
