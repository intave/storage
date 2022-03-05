package de.jpx3.intavestorage

import de.jpx3.intave.IntaveAccessor
import de.jpx3.intave.access.IntaveAccess
import de.jpx3.intave.access.player.storage.StorageGateway
import de.jpx3.intavestorage.storage.StorageOption

class IntaveStorage {
  private var storage: StorageGateway? = null

  fun enable(storageGateway: StorageGateway) {
    intaveAccess().setStorageGateway(storage)
  }

  private fun intaveAccess(): IntaveAccess {
    return IntaveAccessor.unsafeAccess();
  }

  fun disable() {
    storage = null
  }
}