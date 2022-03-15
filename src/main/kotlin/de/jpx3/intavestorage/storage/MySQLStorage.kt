package de.jpx3.intavestorage.storage

import java.io.ByteArrayInputStream
import java.nio.ByteBuffer
import java.sql.Connection
import java.util.UUID
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

@Suppress("SqlNoDataSourceInspection")
class MySQLStorage : CustomStorageGateway {
    private val connection: Connection = TODO()

    init {
        prepareTables()
    }

    private fun prepareTables() {
        connection.createStatement().execute(
            """
            CREATE TABLE IF NOT EXISTS intave_storage (
                id VARCHAR(36) PRIMARY KEY NOT NULL,
                data MEDIUMBLOB NOT NULL,
                last_used LONG NOT NULL
            )
            """
        )
    }

    override fun clearEntriesOlderThan(value: Long, unit: TimeUnit) {
        connection.prepareStatement(
            """
            DELETE FROM intave_storage
            WHERE last_used < ${System.currentTimeMillis() - unit.toMillis(value)}
            """
        ).execute()
    }

    override fun requestStorage(uuid: UUID, consumer: Consumer<ByteBuffer>) {
        connection.prepareStatement(
            """
            SELECT data
            FROM intave_storage
            WHERE id = ?
            """
        ).use {
            it.setString(1, uuid.toString())
            val result = it.executeQuery()
            val bytes = if (result.next()) {
                val blob = result.getBlob("data")
                blob.getBytes(1, blob.length().toInt())
            } else {
                ByteArray(0)
            }
            consumer.accept(ByteBuffer.wrap(bytes))
        }
    }

    override fun saveStorage(uuid: UUID, buffer: ByteBuffer) {
        val blob = ByteArrayInputStream(buffer.array())
        connection.prepareStatement(
            """
            INSERT INTO intave_storage
            VALUES (?, ?, ?) AS ins
            ON DUPLICATE KEY
                UPDATE
                    intave_storage.data = ins.data,
                    intave_storage.last_used = ins.last_used
            """
        ).use {
            it.setString(1, uuid.toString())
            it.setBlob(2, blob)
            it.setLong(3, System.currentTimeMillis())
            it.executeQuery()
        }
    }
}
