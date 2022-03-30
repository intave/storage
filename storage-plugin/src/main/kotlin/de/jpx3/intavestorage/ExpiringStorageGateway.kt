package de.jpx3.intavestorage

import de.jpx3.intave.access.player.storage.StorageGateway

interface ExpiringStorageGateway : StorageGateway {
    fun clearOldEntries()
}
