package de.jpx3.intavestorage

import java.nio.ByteBuffer
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException
import java.util.UUID
import java.util.function.Consumer

/**
 * [ExpiringStorageGateway] with some default implementations for JDBC storage
 * gateway implementations.
 */
interface JdbcBackedStorageGateway : ExpiringStorageGateway {
    fun reconnect()

    /**
     * Creates the table required by this plugin if it does not exist. Should be
     * called after establishing a connection with the database.
     */
    fun prepareTable()

    override fun requestStorage(id: UUID, lazyReturn: Consumer<ByteBuffer>) {
        try {
            requestStorageQuery()
        } catch (e: SQLException) {
            reconnect()
            requestStorageQuery()
        }.use {
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

    /**
     * Query to request storage data; used to implement [requestStorage].
     *
     * @return A [PreparedStatement] for the query.
     */
    fun requestStorageQuery(): PreparedStatement

    override fun saveStorage(id: UUID, storage: ByteBuffer) {
        try {
            saveStorageQuery()
        } catch (e: SQLException) {
            reconnect()
            saveStorageQuery()
        }.use {
            it.setString(1, id.toString())
            it.setBytes(2, storage.array())
            it.setLong(3, System.currentTimeMillis())
            it.execute()
        }
    }

    /**
     * Query to save storage data; used to implement [saveStorage].
     *
     * @return A [PreparedStatement] for the query.
     */
    fun saveStorageQuery(): PreparedStatement

    override fun clearOldEntries() {
        val expirationThreshold = expirationThreshold().takeIf {
            it >= 0
        } ?: return
        try {
            clearEntriesQuery()
        } catch (e: SQLException) {
            reconnect()
            clearEntriesQuery()
        }.use {
            it.setLong(1, System.currentTimeMillis() - expirationThreshold)
            it.execute()
        }
    }

    /**
     * After how many days the storage data expires.
     *
     * @return The expiration threshold in days.
     */
    fun expirationThreshold(): Long

    /**
     * Query to clear expired storage data; used to implement [clearOldEntries].
     *
     * @return A [PreparedStatement] for the query.
     */
    fun clearEntriesQuery(): PreparedStatement
}
