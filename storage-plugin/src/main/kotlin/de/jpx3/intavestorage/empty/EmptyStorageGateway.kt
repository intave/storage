package de.jpx3.intavestorage.empty

import de.jpx3.intavestorage.ExpiringStorageGateway
import java.nio.ByteBuffer
import java.util.UUID
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

class EmptyStorageGateway : ExpiringStorageGateway {
    override fun clearEntriesOlderThan(value: Long, unit: TimeUnit) {}

    override fun requestStorage(p0: UUID?, p1: Consumer<ByteBuffer>?) {}

    override fun saveStorage(p0: UUID?, p1: ByteBuffer?) {}
}
