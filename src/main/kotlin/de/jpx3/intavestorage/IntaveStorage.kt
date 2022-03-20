package de.jpx3.intavestorage

import de.jpx3.intave.IntaveAccessor
import de.jpx3.intave.access.IntaveAccess
import de.jpx3.intave.access.player.storage.StorageGateway

class IntaveStorage {
    fun enable(storageGateway: StorageGateway) {
        intaveAccess().setStorageGateway(storageGateway)
    }

    private fun intaveAccess(): IntaveAccess {
        return IntaveAccessor.unsafeAccess()
    }
}
