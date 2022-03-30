package de.jpx3.intavestorage.postgresql

import de.jpx3.intavestorage.JdbcBackedStorageGateway
import org.postgresql.Driver
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.util.concurrent.TimeUnit

/**
 * Storage gateway between Intave and a PostgreSQL database.
 *
 * @property config The [PostgreSqlConfiguration].
 */
class PostgreSqlStorageGateway(
    private val config: PostgreSqlConfiguration
) : JdbcBackedStorageGateway {
    private val connection = run {
        DriverManager.registerDriver(Driver())
        DriverManager.getConnection(config.uri, config.user, config.password)
    }

    init {
        prepareTable()
    }

    override fun prepareTable() {
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

    override fun requestStorageQuery(): PreparedStatement {
        return connection.prepareStatement(
            """
            SELECT data
            FROM intave_storage
            WHERE id = ?
            """
        )
    }

    override fun saveStorageQuery(): PreparedStatement {
        return connection.prepareStatement(
            """
            INSERT INTO intave_storage
            VALUES(?, ?, ?)
            ON CONFLICT(id) DO UPDATE SET
                data = EXCLUDED.data,
                last_used = EXCLUDED.last_used
            """
        )
    }

    override fun clearEntriesQuery(): PreparedStatement {
        return connection.prepareStatement(
            """
            DELETE FROM intave_storage
            WHERE last_used < ?
            """
        )
    }

    override fun expirationThreshold(): Long {
        return TimeUnit.DAYS.toMillis(config.expirationThreshold)
    }
}
