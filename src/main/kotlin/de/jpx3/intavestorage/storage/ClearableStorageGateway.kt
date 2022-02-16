package de.jpx3.intavestorage.storage

import de.jpx3.intave.access.player.storage.StorageGateway
import java.util.concurrent.TimeUnit

interface ClearableStorageGateway : StorageGateway {
  fun clearEntriesOlderThan(value: Long, unit: TimeUnit)
}