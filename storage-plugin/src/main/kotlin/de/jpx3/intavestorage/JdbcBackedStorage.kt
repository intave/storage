package de.jpx3.intavestorage

import java.nio.ByteBuffer
import java.sql.PreparedStatement
import java.util.UUID
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

interface JdbcBackedStorage : ExpiringStorageGateway {
    fun prepareTable()

    override fun requestStorage(id: UUID, lazyReturn: Consumer<ByteBuffer>) {
        requestStorageQuery().use {
            it.setString(1, id.toString())
            val result = it.executeQuery()
            val bytes = if (result.next()) {
                result.getBytes("data")
            } else {
                ByteArray(0)
            }
            lazyReturn.accept(ByteBuffer.wrap(bytes))
        }
    }

    fun requestStorageQuery(): PreparedStatement

    override fun saveStorage(id: UUID, storage: ByteBuffer) {
        saveStorageQuery().use {
            it.setString(1, id.toString())
            it.setBytes(2, storage.array())
            it.setLong(3, System.currentTimeMillis())
            it.execute()
        }
    }

    fun saveStorageQuery(): PreparedStatement

    override fun clearEntriesOlderThan(value: Long, unit: TimeUnit) {
        clearEntriesQuery().use {
            it.setLong(1, System.currentTimeMillis() - unit.toMillis(value))
            it.execute()
        }
    }

    fun clearEntriesQuery(): PreparedStatement
}
