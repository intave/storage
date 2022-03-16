package de.jpx3.intavestorage.storage

import org.bukkit.configuration.ConfigurationSection
import org.postgresql.Driver
import java.nio.ByteBuffer
import java.sql.Connection
import java.sql.DriverManager
import java.util.UUID
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

@Suppress("SqlNoDataSourceInspection")
class PostgreSqlStorage(config: ConfigurationSection) : CustomStorageGateway {
    private val connection: Connection = run {
        val url = "jdbc:postgresql://${config.getString("host")}/${config.getString("database")}"
        val user = config.getString("user")
        val password = config.getString("password") as String
        DriverManager.registerDriver(Driver())
        DriverManager.getConnection(url, user, password)
    }

    init {
        prepareSchema()
    }

    private fun prepareSchema() {
        connection.createStatement().execute(
            """
            CREATE TABLE IF NOT EXISTS intave_storage(
                id CHAR(36) PRIMARY KEY NOT NULL,
                data BYTEA NOT NULL,
                last_used BIGINT NOT NULL
            )
            """
        )
    }

    override fun clearEntriesOlderThan(value: Long, unit: TimeUnit) {
        connection.prepareStatement(
            """
            DELETE FROM intave_storage
            WHERE last_used < ?
            """
        ).use {
            it.setLong(1, System.currentTimeMillis() - unit.toMillis(value))
            it.execute()
        }
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
                result.getBytes("data")
            } else {
                ByteArray(0)
            }
            consumer.accept(ByteBuffer.wrap(bytes))
        }
    }

    override fun saveStorage(uuid: UUID, buffer: ByteBuffer) {
        connection.prepareStatement(
            """
            INSERT INTO intave_storage
            VALUES(?, ?, ?)
            ON CONFLICT(id) DO UPDATE SET
                data = EXCLUDED.data,
                last_used = EXCLUDED.last_used
            """
        ).use {
            it.setString(1, uuid.toString())
            it.setBytes(2, buffer.array())
            it.setLong(3, System.currentTimeMillis())
            it.execute()
        }
    }
}
