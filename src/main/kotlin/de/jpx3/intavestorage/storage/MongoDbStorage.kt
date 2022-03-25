package de.jpx3.intavestorage.storage

import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.client.MongoClients
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import org.bson.Document
import org.bson.UuidRepresentation
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.pojo.PojoCodecProvider
import org.bson.types.Binary
import org.bukkit.configuration.ConfigurationSection
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

class MongoDbStorage(config: ConfigurationSection) : ExpiringStorageGateway {
    private val pojoCodecRegistry = CodecRegistries.fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
    )
    private val connectionSettings = if (config.getBoolean("authorization")) {
        val user = config.getString("user")!!
        val password = config.getString("password")!!
        val defaultDatabase = config.getString("defaultdb")!!

        // Create the settings with credentials
        MongoClientSettings.builder()
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .codecRegistry(pojoCodecRegistry)
            .credential(
                MongoCredential.createCredential(
                    user,
                    defaultDatabase,
                    password.toCharArray()
                )
            )
            .build()
    } else {
        // Create the settings without credentials
        MongoClientSettings.builder()
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .codecRegistry(pojoCodecRegistry)
            .build()
    }
    private val connection = MongoClients.create(connectionSettings)
    private val database =
        connection.getDatabase(config.getString("database")!!).withCodecRegistry(pojoCodecRegistry)
    private val storageCollection = database.getCollection("storage").withCodecRegistry(pojoCodecRegistry)

    override fun clearEntriesOlderThan(value: Long, unit: TimeUnit) {
        storageCollection.deleteMany(
            Filters.lt(
                "last_used",
                System.currentTimeMillis() - unit.toMillis(value)
            )
        )
    }

    override fun requestStorage(uuid: UUID, consumer: Consumer<ByteBuffer>) {
        val emptyArray = ByteArray(0)
        // If no player is in the database return empty data
        val user = storageCollection.find(
            Filters.eq(
                "player",
                uuid
            )
        ).first() ?: run {
            consumer.accept(ByteBuffer.wrap(emptyArray))
            return
        }
        // Fetch the binary data of the player
        val binary = user.get(
            "data",
            Binary::class.java
        )
        consumer.accept(ByteBuffer.wrap(binary.data))
    }

    override fun saveStorage(uuid: UUID, buffer: ByteBuffer) {
        // If no player is in the database insert a new one
        val requiresInsert = storageCollection.find(
            Filters.eq(
                "player",
                uuid
            )
        ).first() == null
        if (requiresInsert) {
            storageCollection.insertOne(
                Document(
                    hashMapOf<String, Any>(
                        "player" to uuid,
                        "data" to buffer.array(),
                        "last_used" to System.currentTimeMillis()
                    )
                )
            )
        } else {
            storageCollection.updateOne(
                Filters.eq(
                    "player",
                    uuid
                ),
                Updates.set(
                    "data",
                    buffer.array()
                )
            )
        }
    }
}