package de.jpx3.intavestorage.storage.database

import com.mysql.cj.jdbc.Driver
import org.bukkit.configuration.ConfigurationSection
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement

@Suppress("SqlNoDataSourceInspection")
class MySqlStorage(config: ConfigurationSection) : DatabaseStorage {
    private val connection: Connection = run {
        val url = "jdbc:mysql://${config.getString("host")}/${config.getString("database")}"
        val user = config.getString("user")
        val password = config.getString("password") as String
        DriverManager.registerDriver(Driver())
        DriverManager.getConnection(url, user, password)
    }

    init {
        prepareTable()
    }

    override fun prepareTable() {
        connection.createStatement().execute(
            """
            CREATE TABLE IF NOT EXISTS intave_storage (
                id CHAR(36) PRIMARY KEY NOT NULL,
                data MEDIUMBLOB NOT NULL,
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
            VALUES(?, ?, ?) as excluded
            ON DUPLICATE KEY UPDATE 
                data = excluded.data,
                last_used = excluded.last_used

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
