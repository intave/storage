package de.jpx3.intavestorage.storage.sql

import org.bukkit.configuration.ConfigurationSection
import org.postgresql.Driver
import java.sql.DriverManager
import java.sql.PreparedStatement

@Suppress("SqlNoDataSourceInspection")
class PostgreSqlStorage(config: ConfigurationSection) : JdbcBackedStorage {
    private val connection = run {
        val url = "jdbc:postgresql://${config.getString("host")}/${config.getString("database")}"
        val user = config.getString("user")
        val password = config.getString("password")
        DriverManager.registerDriver(Driver())
        DriverManager.getConnection(url, user, password)
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
}
