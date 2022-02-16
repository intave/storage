package de.jpx3.intavestorage.storage

import de.jpx3.intave.access.player.storage.EmptyStorageGateway
import de.jpx3.intave.access.player.storage.StorageGateway

enum class StorageOption(
  val gateway: StorageGateway
) {
  NONE(EmptyStorageGateway()),
  FILE(FileStorage())

}