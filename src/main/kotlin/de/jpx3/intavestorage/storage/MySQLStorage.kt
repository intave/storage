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
        connection.autoCommit = false
        connection.prepareStatement(
            "CREATE TABLE IF NOT EXISTS intave_storage (" +
                "    id VARCHAR(36) PRIMARY KEY NOT NULL," +
                "    bytes MEDIUMBLOB NOT NULL," +
                "    time LONG NOT NULL" +
                ")"
        )
        connection.commit()
    }

    override fun clearEntriesOlderThan(value: Long, unit: TimeUnit) {
        val preparedStatement =
            connection.prepareStatement(
                "DELETE FROM intave_storage WHERE " +
                    "time < ${System.currentTimeMillis() - unit.toMillis(value)}"
            )
        preparedStatement.execute()
    }

    override fun requestStorage(uuid: UUID, consumer: Consumer<ByteBuffer>) {
        // Query with the given uuid
        val preparedStatement =
            connection.prepareStatement("SELECT * FROM intave_storage WHERE id = $uuid")
        val resultSet = preparedStatement.executeQuery()
        if (!resultSet.next()) {
            // Accept with empty array as no entry could be found
            consumer.accept(ByteBuffer.wrap(ByteArray(0)))
            return
        }
        // Accept with the bytes of the entry
        consumer.accept(ByteBuffer.wrap(resultSet.getBytes("bytes")))
    }

    override fun saveStorage(uuid: UUID, buffer: ByteBuffer) {
        val blob = ByteArrayInputStream(buffer.array())
        val statement = connection.prepareStatement(
            "INSERT OR REPLACE INTO intave_storage VALUES(" +
                "    $uuid," +
                "    ?," +
                "    ${System.currentTimeMillis()}" +
                ")"
        )
        statement.setBlob(1, blob)
        statement.executeQuery()
    }
}
